package ADT;

public interface ListInterface<T> {
    void add(T element);
    void add(int index, T element);
    T remove(int index);
    boolean remove(T element);
    T get(int index);
    T set(int index, T element);
    boolean contains(T element);
    int size();
    boolean isEmpty();
    void clear();
    int indexOf(T element);
}
