package storage;

import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTopicIndex implements TopicIndex {
    private ConcurrentHashMap<String, TopicStorage> topics;

    InMemoryTopicIndex() {
        topics = new ConcurrentHashMap<>();
    }

    @Override
    public void add(String topicName) {
        topics.put(topicName, new InMemoryTopicStorage());
    }

    @Override
    public TopicStorage get(String topicName) {
        return topics.get(topicName);
    }
    
}
