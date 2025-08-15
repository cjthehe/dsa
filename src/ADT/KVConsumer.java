package ADT;

public interface KVConsumer<K, V> {
    void accept(K key, V value);
}


