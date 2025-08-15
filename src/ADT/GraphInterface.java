package ADT;

public interface GraphInterface<T> {
    boolean addVertex(T vertex);
    boolean addEdge(T from, T to);

    boolean removeVertex(T vertex);
    boolean removeEdge(T from, T to);

    boolean hasVertex(T vertex);
    boolean hasEdge(T from, T to);

    int vertexCount();
    int edgeCount();

    ListInterface<T> neighborsOf(T vertex);

    ListInterface<T> bfs(T startVertex);
    ListInterface<T> dfs(T startVertex);

    void clear();
}


