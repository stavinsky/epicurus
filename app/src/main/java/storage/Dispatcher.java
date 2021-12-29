package storage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class Dispatcher {
    BlockingQueue<Event> incomingEventsQueue;
    ConcurrentMap<String, CopyOnWriteArrayList<BlockingQueue<StorageObject>>> consumersQueues;

    private class Event {
        public StorageObject so;
        public String topicName;

        Event(String topicName, StorageObject so) {
            this.topicName = topicName;
            this.so = so;
        }
    }

    private class SendToConsumers implements Runnable {
        private void fanOutEvent(Event event) {
            if (consumersQueues.containsKey(event.topicName) == false) {
                return;
            }
            var queueList = consumersQueues.get(event.topicName);
            for (var queue : queueList) {
                queue.add(event.so);
            }
        }

        @Override
        public void run() {
            while (true) {
                Event event;
                try {
                    event = incomingEventsQueue.take();
                    fanOutEvent(event);
                } catch (InterruptedException e) {
                    continue;
                }
            }
        }
    }

    Dispatcher() {
        consumersQueues = new ConcurrentHashMap<>();
        incomingEventsQueue = new LinkedBlockingQueue<>();
        Thread thread = new Thread(new SendToConsumers());
        thread.start();
    }

    void newEvent(String topicName, StorageObject so) {
        try {
            incomingEventsQueue.put(new Event(topicName, so));
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }

    void subscribe(String topicName, BlockingQueue<StorageObject> consumerQueue) {
        CopyOnWriteArrayList<BlockingQueue<StorageObject>> queues;
        if (consumersQueues.containsKey(topicName)) {
            queues = consumersQueues.get(topicName);
        } else {
            queues = new CopyOnWriteArrayList<>();
            consumersQueues.put(topicName, queues);
        }
        queues.add(consumerQueue);
    }
    
}
