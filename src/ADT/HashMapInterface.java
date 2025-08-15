package ADT;

public interface HashMapInterface<K, V> {
    void put(K key, V value);
    V get(K key);
    V remove(K key);
    boolean containsKey(K key);
    boolean containsValue(V value);
    boolean isEmpty();
    int size();
    void clear();
    void display();
}
