package storage;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class StorageObject {
    public LocalDateTime storedAt;
    public byte[] payload; 

    StorageObject(LocalDateTime storedAt, byte[] payload) {
        this.storedAt = storedAt;
        this.payload = payload;
    }

    StorageObject(long timestamp, byte[] payload) {
        this.storedAt = Instant.ofEpochMilli(timestamp)
            .atZone(ZoneId.systemDefault()).toLocalDateTime(); 
        this.payload = payload;
    }

    StorageObject(byte[] payload) {
        this.storedAt = LocalDateTime.now();
        this.payload = payload;
    }    

    public long stored_at_ts() {
        long ts = storedAt.atZone(ZoneId.systemDefault())
            .toInstant().toEpochMilli();
        return ts;
    }
}
