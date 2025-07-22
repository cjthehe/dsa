package ADT;

public interface ArrayInterface<T>{
    void enqueue(T items);
    T dequeue();
    T peek();
    boolean isEmpty();
    boolean isFull();
    int size();
    void clear();
    
}
