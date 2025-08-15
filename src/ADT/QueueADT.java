package ADT;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class QueueADT<T> implements QueueInterface<T>, Iterable<T> {
    private int capacity;
    private T[] arrayQueue;
    private int frontIndex;
    private int backIndex;
    private int size;
    
    public QueueADT(int capacity){
        this.capacity = capacity;
        arrayQueue = (T[])new Object[capacity];
        frontIndex = 0;
        backIndex = -1;
        size = 0;
    }
    
    @Override
    public void enqueue(T item){
        if(!isFull()){
            
            backIndex = (backIndex + 1) % capacity; 
            arrayQueue[backIndex] = item;
            size ++;
       }
        
    }
    
    @Override
    public T dequeue() {
        T front = arrayQueue[0];
        if (!isEmpty()) {
            for (int i = 0; i < size - 1; i++) {
                arrayQueue[i] = arrayQueue[i + 1];
            }
            arrayQueue[size - 1] = null; 
            size--;
        }
        return front;
    }

    @Override
    public boolean RemoveSpecificElement(T itemToRemove){
        int originalSize = size;
        boolean found = false;
        
        if(!isEmpty()){
            for(int i = 0;i < size;i++){
                T item = dequeue();
                if (!found && item.equals(itemToRemove)) {
                    found = true;  // skip enqueueing this one — effectively removing it
                    continue;
                }
                enqueue(item);
            }
        }
        return found;
    }
    
    @Override
    public boolean isFull(){
        return size == capacity;
    }
    
    @Override
    public boolean isEmpty(){
        return size == 0;
    }
    
    @Override
    public int size(){
        return size;
    }
    
    @Override
    public T getFront(){
        if(!isEmpty()){
            return arrayQueue[frontIndex];
        }
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int currentIndex = frontIndex;
            private int elementsSeen = 0;

        @Override
        public boolean hasNext() {
        return elementsSeen < size;
            }

        @Override
        public T next() {
            if (hasNext()) {
                T item = arrayQueue[currentIndex];
                currentIndex = (currentIndex + 1) % capacity;
                elementsSeen++;
                return item;
            }
                throw new NoSuchElementException();
            }
        };
    }
    
//    @Override
//    public void clear(){
//        for(int i = 0; i < size ; i++){
//            int count = (frontIndex + i) % capacity;
//            arrayQueue[count] = null;
//        }
//        
//        frontIndex = 0;
//        size = 0;
//        backIndex = -1;
//    }
    
    
}
