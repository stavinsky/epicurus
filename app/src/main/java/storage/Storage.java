package storage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;


public class Storage {
    TopicIndex topics;
    Dispatcher dispatcher;

    Storage(TopicIndex topics) {
        this.topics = topics;
        dispatcher = new Dispatcher();    
    }

    public void add(String topicName, byte[] payload) {
        StorageObject so = new StorageObject(payload);
        TopicStorage topic = topics.get(topicName);
        topic.add(so);
        dispatcher.newEvent(topicName, so);
    }

    public LinkedBlockingQueue<StorageObject> subscribe_from_now(String topicName) {
        LinkedBlockingQueue<StorageObject> queue = new LinkedBlockingQueue<>();
        dispatcher.subscribe(topicName, queue);
        return queue;
    }

    public void subscribe_since(String topic, LocalDateTime since) {
    }

    public ArrayList<StorageObject> read_since(String topicName, long timestamp) {
        TopicStorage topic = topics.get(topicName);
        ArrayList<StorageObject> result = new ArrayList<>();
        int pos = topic.position_by_ts(timestamp);
        Iterator<StorageObject> iter = topic.iter_from(pos);

        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }
    
    public void add_topic(String topic) {
        topics.add(topic);
    }
    
}
