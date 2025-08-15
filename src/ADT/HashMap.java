package ADT;

public class HashMap<K, V> implements HashMapInterface<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    
    private LinkedList<Entry<K, V>>[] buckets;
    private int size;
    private int capacity;
    
    @SuppressWarnings("unchecked")
    public HashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.buckets = new LinkedList[capacity];
        this.size = 0;
        
        // Initialize all buckets
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new LinkedList<Entry<K, V>>();
        }
    }
    
    @SuppressWarnings("unchecked")
    public HashMap(int initialCapacity) {
        this.capacity = initialCapacity;
        this.buckets = new LinkedList[capacity];
        this.size = 0;
        
        // Initialize all buckets
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new LinkedList<Entry<K, V>>();
        }
    }
    
    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        
        // Check if resize is needed
        if ((double) size / capacity >= LOAD_FACTOR) {
            resize();
        }
        
        int bucketIndex = getBucketIndex(key);
        LinkedList<Entry<K, V>> bucket = buckets[bucketIndex];
        
        // Check if key already exists
        for (int i = 0; i < bucket.size(); i++) {
            Entry<K, V> entry = bucket.get(i);
            if (entry.key.equals(key)) {
                entry.value = value; // Update existing value
                return;
            }
        }
        
        // Add new entry
        bucket.add(new Entry<K, V>(key, value));
        size++;
    }
    
    @Override
    public V get(K key) {
        if (key == null) {
            return null;
        }
        
        int bucketIndex = getBucketIndex(key);
        LinkedList<Entry<K, V>> bucket = buckets[bucketIndex];
        
        for (int i = 0; i < bucket.size(); i++) {
            Entry<K, V> entry = bucket.get(i);
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        
        return null; // Key not found
    }
    
    @Override
    public V remove(K key) {
        if (key == null) {
            return null;
        }
        
        int bucketIndex = getBucketIndex(key);
        LinkedList<Entry<K, V>> bucket = buckets[bucketIndex];
        
        for (int i = 0; i < bucket.size(); i++) {
            Entry<K, V> entry = bucket.get(i);
            if (entry.key.equals(key)) {
                bucket.remove(i);
                size--;
                return entry.value;
            }
        }
        
        return null; // Key not found
    }
    
    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            return false;
        }
        
        int bucketIndex = getBucketIndex(key);
        LinkedList<Entry<K, V>> bucket = buckets[bucketIndex];
        
        for (int i = 0; i < bucket.size(); i++) {
            Entry<K, V> entry = bucket.get(i);
            if (entry.key.equals(key)) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public boolean containsValue(V value) {
        if (value == null) {
            return false;
        }
        
        for (int bi = 0; bi < capacity; bi++) {
            LinkedList<Entry<K, V>> bucket = buckets[bi];
            for (int i = 0; i < bucket.size(); i++) {
                Entry<K, V> entry = bucket.get(i);
                if (value.equals(entry.value)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    @Override
    public int size() {
        return size;
    }
    
    @Override
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            buckets[i].clear();
        }
        size = 0;
        System.out.println("HashMap cleared");
    }
    
    @Override
    public void display() {
        if (isEmpty()) {
            System.out.println("HashMap is empty");
            return;
        }
        
        System.out.println("HashMap contents:");
        for (int i = 0; i < capacity; i++) {
            LinkedList<Entry<K, V>> bucket = buckets[i];
            if (!bucket.isEmpty()) {
                System.out.print("Bucket " + i + ": ");
                for (int j = 0; j < bucket.size(); j++) {
                    Entry<K, V> entry = bucket.get(j);
                    System.out.print("[" + entry.key + "=" + entry.value + "] ");
                }
                System.out.println();
            }
        }
        System.out.println("Total entries: " + size);
    }
    
    // Iterate over all key-value pairs using a simple callback (no Java collections exposure)
    public void forEach(KVConsumer<K, V> consumer) {
        for (int i = 0; i < capacity; i++) {
            LinkedList<Entry<K, V>> bucket = buckets[i];
            for (int j = 0; j < bucket.size(); j++) {
                Entry<K, V> entry = bucket.get(j);
                consumer.accept(entry.key, entry.value);
            }
        }
    }
    
    // Helper method to get bucket index
    private int getBucketIndex(K key) {
        int hashCode = key.hashCode();
        return Math.abs(hashCode) % capacity;
    }
    
    // Helper method to resize the HashMap
    @SuppressWarnings("unchecked")
    private void resize() {
        int oldCapacity = capacity;
        capacity *= 2;
        
        LinkedList<Entry<K, V>>[] oldBuckets = buckets;
        buckets = new LinkedList[capacity];
        
        // Initialize new buckets
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new LinkedList<Entry<K, V>>();
        }
        
        // Reset size and rehash all entries
        size = 0;
        for (int bi = 0; bi < oldBuckets.length; bi++) {
            LinkedList<Entry<K, V>> bucket = oldBuckets[bi];
            for (int j = 0; j < bucket.size(); j++) {
                Entry<K, V> entry = bucket.get(j);
                put(entry.key, entry.value);
            }
        }
        
        System.out.println("HashMap resized from " + oldCapacity + " to " + capacity);
    }
    
    // Inner class to represent key-value pairs
    private static class Entry<K, V> {
        K key;
        V value;
        
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
    
    // Additional utility methods
    public int getCapacity() {
        return capacity;
    }
    
    public double getLoadFactor() {
        return (double) size / capacity;
    }
}
