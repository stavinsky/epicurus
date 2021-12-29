package storage;

import java.util.Iterator;

public interface TopicStorage {
    void add(StorageObject so);

    int position_by_ts(long timestamp);

    Iterator<StorageObject> iter_from(int pos); 
    
}
