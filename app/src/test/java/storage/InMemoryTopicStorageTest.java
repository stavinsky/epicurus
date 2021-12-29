package storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

public class InMemoryTopicStorageTest {
    @Test void emptyBuffTest() {
        Storage storage = new Storage(new InMemoryTopicIndex());
        storage.add_topic("topic");
        var objects = storage.read_since("topic", 0);
        assertEquals(objects.size(), 0);
    }

    @Test void bigBufferTest() {
        Random random = new Random();
        ArrayList<byte[]> expected = new ArrayList<>();
        
        for (int i = 0; i < 10000; i++) {
            int size = random.nextInt(100);
            byte[] testPayload = new byte[size];
            random.nextBytes(testPayload);
            expected.add(testPayload);
        }
        Storage storage = new Storage(new InMemoryTopicIndex());
        storage.add_topic("topic");
        for (byte[] item : expected) {
            storage.add("topic", item);
        }
        ArrayList<StorageObject> receivedArray =  storage.read_since("topic", 0);
        assertEquals(receivedArray.size(), 10000);
        for (int i = 0; i < 10000; i++) {
            assertTrue(Arrays.equals(expected.get(i), receivedArray.get(i).payload));
        }
    }

    @Test void concurencyTest() throws Exception {
        final int elementsCount = 10000;
        Random random = new Random();
        Set<byte[]> expected = new TreeSet<byte[]>(Arrays::compare);
        
        for (int i = 0; i < elementsCount; i++) {
            int size = random.nextInt(100);
            byte[] testPayload = new byte[size];
            random.nextBytes(testPayload);
            expected.add(testPayload);
        }
        Storage storage = new Storage(new InMemoryTopicIndex());
        storage.add_topic("topic");
        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        for (byte[] item : expected) {
            pool.execute(() -> {
                storage.add("topic", item);
            });
        }
        pool.shutdown();
        pool.awaitTermination(60, TimeUnit.SECONDS);
        ArrayList<StorageObject> actual =  storage.read_since("topic", 0);
        assertTrue(expected.size() > 0);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            System.out.println(new String(actual.get(i).payload));
            assertTrue(expected.contains(actual.get(i).payload));
        }


    }    
}
