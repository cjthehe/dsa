package ADT;


public interface AVLTreeInterface<K extends Comparable<K>,V> {
    public void insert(K key, V value);
    public V search(K keyValue);
    public void delete(K keyValue);
    public boolean contains(K keyValue);
    public boolean isEmpty();
    public int size();
    public K findMin();
    public K findMax();
    public int height();
}
