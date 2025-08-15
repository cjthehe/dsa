package ADT;

public class ArrayList<T> implements ListInterface<T> {
    
    private static final int DEFAULT_CAPACITY = 10;
    private T[] elements;
    private int size;
    
    @SuppressWarnings("unchecked")
    public ArrayList() {
        this.elements = (T[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }
    
    @Override
    public void add(T element) {
        ensureCapacity(size + 1);
        elements[size++] = element;
    }
    
    @Override
    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        
        ensureCapacity(size + 1);
        
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        
        elements[index] = element;
        size++;
    }
    
    @Override
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        
        T removed = elements[index];
        
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        
        elements[--size] = null;
        return removed;
    }
    
    @Override
    public boolean remove(T element) {
        int index = indexOf(element);
        if (index != -1) {
            remove(index);
            return true;
        }
        return false;
    }
    
    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return elements[index];
    }
    
    @Override
    public T set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        
        T oldElement = elements[index];
        elements[index] = element;
        return oldElement;
    }
    
    @Override
    public boolean contains(T element) {
        return indexOf(element) != -1;
    }
    
    @Override
    public int size() {
        return size;
    }
    
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }
    
    @Override
    public int indexOf(T element) {
        for (int i = 0; i < size; i++) {
            if ((element == null && elements[i] == null) || 
                (element != null && element.equals(elements[i]))) {
                return i;
            }
        }
        return -1;
    }
    
    @SuppressWarnings("unchecked")
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = Math.max(elements.length * 2, minCapacity);
            T[] newElements = (T[]) new Object[newCapacity];
            
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            
            elements = newElements;
        }
    }
    
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
