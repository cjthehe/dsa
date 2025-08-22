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
    public boolean RemoveSpecificElement(T itemToRemove) {
        if (isEmpty()) {
            return false;
        }

        boolean found = false;
        int originalSize = size;

        Iterator<T> iter = iterator();
        while (iter.hasNext()) {
            T item = iter.next();
            if (!found && item.equals(itemToRemove)) {
                found = true;
                iter.remove(); 
                break;
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
            private int lastReturnedIndex = -1;
                
        @Override
        public boolean hasNext() {
        return elementsSeen < size;
        }

        @Override
        public T next() {
            if (hasNext()) {
                lastReturnedIndex = currentIndex;
                T item = arrayQueue[currentIndex];
                currentIndex = (currentIndex + 1) % capacity;
                elementsSeen++;
                return item;
            }
                throw new NoSuchElementException();
            }
        
        public void remove(){
            if (lastReturnedIndex < 0) {
                throw new IllegalStateException("next() not called before remove()");
            }

            for (int i = lastReturnedIndex; i < size - 1; i++) {
                arrayQueue[i] = arrayQueue[(i + 1) % capacity];
            }
            arrayQueue[size - 1] = null; // clear last slot
            size--;

            currentIndex = lastReturnedIndex;
            lastReturnedIndex = -1;
            elementsSeen--;
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
