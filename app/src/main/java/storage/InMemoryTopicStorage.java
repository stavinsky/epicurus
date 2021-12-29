package storage;

import static io.netty.buffer.Unpooled.buffer;

import io.netty.buffer.ByteBuf;
import java.util.Iterator;



public class InMemoryTopicStorage  implements TopicStorage, Iterable<StorageObject> {
    private ByteBuf buff;

    InMemoryTopicStorage() {
        buff = buffer(1000);
    }

    public synchronized void add(StorageObject so) {
        buff.writeLong(so.stored_at_ts());
        buff.writeInt(so.payload.length);
        buff.writeBytes(so.payload);
    }

    @Override
    public int position_by_ts(long timestamp) {
        int pos = 0;
        while (buff.readableBytes() > 0) {
            buff.readerIndex(pos);
            long currentTs = buff.readLong();
            int size = buff.readInt();
            if (currentTs >= timestamp) {
                return pos;
            }
            pos = pos + size + 4 + 8;
        }
        return pos;
    }

    class Iter implements Iterator<StorageObject> {
        int pos;

        Iter() {
            pos = 0;
        }

        Iter(int pos) {
            this.pos = pos;
        }

        @Override
        public boolean hasNext() {
            return pos < buff.writerIndex();
        }

        @Override
        public StorageObject next() {
            buff.readerIndex(pos);
            long ts = buff.readLong();
            int size = buff.readInt();
            byte[] payload = new byte[size];
            buff.readBytes(payload);
            StorageObject result = new StorageObject(ts, payload);
            pos = buff.readerIndex();
            return result;
        }
        
    }

    @Override
    public Iterator<StorageObject> iterator() {
        return new Iter();
    }

    public Iterator<StorageObject> iter_from(int pos) {
        return new Iter(pos);

    }
}
