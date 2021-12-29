package storage;

public interface TopicIndex {
    void add(String topicName);

    TopicStorage get(String topicName); 
}
