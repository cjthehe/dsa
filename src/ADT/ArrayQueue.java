package ADT;

public class ArrayQueue<T> implements ArrayInterface<T> {
    private int capacity;
    private T[] arrayQueue;
    private int front;
    private int rear;
    private int size;
    
    public ArrayQueue(int capacity){
        this.capacity = capacity;
        arrayQueue = (T[])new Object[capacity];
        front = 0;
        rear = 0;
        size = 0;
    }
    
    @Override
    public void enqueue(T item){
        if(isFull()){
            System.out.println("The Queue is full. Please wait");
            return; // stop if full 
       }
        arrayQueue[rear] = item;
        rear = (rear + 1) % capacity; // increment for rear
        size ++;
    }
    
    @Override
    public T dequeue(){
        if(isEmpty()){
            System.out.println("Queue is Empty. Cannot Dequeue");
            return null;
        }
        
        T item = arrayQueue[front];
        arrayQueue[front] = null;
        front = (front + 1) % capacity;
        size--;
        return item;
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
    // peek is view the item infrot but no remove
    public T peek(){
        if(!isEmpty()){
            return arrayQueue[front];
        }
        System.out.print("No item to be selected");
        return null;
    }
    
    public void display(){
        if(isEmpty()){
            System.out.print("Queue is empty.");
            return;
        }
        for(int i = 0; i < size ; i++){
            int count = (front + i) % capacity;
            System.out.println((i+1) + "." + arrayQueue[count] );
        }
    }
    
    @Override
    public void clear(){
        for(int i = 0; i < size ; i++){
            int count = (front + i) % capacity;
            arrayQueue[count] = null;
        }
        
        front = 0;
        size = 0;
        rear = 0;
        
        System.out.println("Queue is cleared");
    }
    
//    @Override
//    public  void expandQueue(){
//        
//    }
}
