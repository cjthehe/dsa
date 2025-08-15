package ADT;

public interface QueueInterface<T>{
    public void enqueue(T item);
    public T dequeue();
    public boolean RemoveSpecificElement(T item);
    public T getFront();
    public boolean isEmpty();
    public boolean isFull();
    public int size();
//    public void clear();
    
}
