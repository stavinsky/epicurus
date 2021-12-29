package storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;


public class StorageBasicTest {
    @Test void store_and_read() {
        Storage storage = new Storage(new InMemoryTopicIndex());
        long now = System.currentTimeMillis(); 
        storage.add_topic("test_topic");
        storage.add("test_topic", "1234".getBytes());
        storage.add("test_topic", "12345".getBytes());
        storage.add("test_topic", "123456".getBytes());
        ArrayList<StorageObject> storageObjects = storage.read_since("test_topic", now);
        assertEquals(storageObjects.size(), 3);
        assertTrue(Arrays.equals("1234".getBytes(), storageObjects.get(0).payload));
        assertTrue(Arrays.equals("12345".getBytes(), storageObjects.get(1).payload));
        assertTrue(Arrays.equals("123456".getBytes(), storageObjects.get(2).payload));
    }    

    @Test
    void pubAndSubTest() throws Exception {
        Storage storage = new Storage(new InMemoryTopicIndex());
        storage.add_topic("test_topic");
        ArrayList<StorageObject> actual = new ArrayList<>();
        Thread receiverThread = new Thread(() -> {
            var queue = storage.subscribe_from_now("test_topic");
            for (int i = 1; i <= 3; i++) {
                StorageObject so;
                try {
                    so = queue.take();
                    actual.add(so);
                } catch (InterruptedException e) {
                    System.out.println(e);    
                }
            } 
        });
        storage.add("test_topic", "1234".getBytes());
        receiverThread.start();
        storage.add("test_topic", "12345".getBytes());
        storage.add("test_topic", "123456".getBytes());

        receiverThread.join();
        assertEquals(3, actual.size());
        System.out.println(new String(actual.get(0).payload));

    }

}
