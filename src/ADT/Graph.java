package ADT;

public class Graph<T> implements GraphInterface<T> {
    private final HashMap<T, ArrayList<T>> adjacency;
    private final ArrayList<T> vertices;
    private int edges;

    public Graph() {
        this.adjacency = new HashMap<>();
        this.vertices = new ArrayList<>();
        this.edges = 0;
    }

    @Override
    public boolean addVertex(T vertex) {
        if (vertex == null) return false;
        if (adjacency.containsKey(vertex)) return false;
        adjacency.put(vertex, new ArrayList<>());
        vertices.add(vertex);
        return true;
    }

    @Override
    public boolean addEdge(T from, T to) {
        if (from == null || to == null) return false;
        if (!adjacency.containsKey(from) || !adjacency.containsKey(to)) return false;
        ArrayList<T> list = adjacency.get(from);
        if (list.contains(to)) return false;
        list.add(to);
        edges++;
        return true;
    }

    @Override
    public boolean removeVertex(T vertex) {
        if (vertex == null) return false;
        if (!adjacency.containsKey(vertex)) return false;

        // Remove outgoing edges from vertex
        ArrayList<T> outgoing = adjacency.get(vertex);
        if (outgoing != null) {
            int outSize = outgoing.size();
            edges -= outSize;
        }

        // Remove the vertex from the graph structures
        adjacency.remove(vertex);

        int idx = vertices.indexOf(vertex);
        if (idx != -1) {
            vertices.remove(idx);
        }

        // Remove incoming edges to vertex (avoid double-counting self-loop already deducted)
        for (int i = 0; i < vertices.size(); i++) {
            T v = vertices.get(i);
            if (v != null && v.equals(vertex)) {
                continue;
            }
            ArrayList<T> list = adjacency.get(v);
            if (list == null) continue;
            while (list.remove(vertex)) {
                edges--;
            }
        }
        return true;
    }

    @Override
    public boolean removeEdge(T from, T to) {
        if (from == null || to == null) return false;
        if (!adjacency.containsKey(from) || !adjacency.containsKey(to)) return false;
        ArrayList<T> list = adjacency.get(from);
        if (list == null) return false;
        boolean removed = list.remove(to);
        if (removed) edges--;
        return removed;
    }

    @Override
    public boolean hasVertex(T vertex) {
        return vertex != null && adjacency.containsKey(vertex);
    }

    @Override
    public boolean hasEdge(T from, T to) {
        if (from == null || to == null) return false;
        if (!adjacency.containsKey(from)) return false;
        ArrayList<T> list = adjacency.get(from);
        return list != null && list.contains(to);
    }

    @Override
    public int vertexCount() {
        return vertices.size();
    }

    @Override
    public int edgeCount() {
        return edges;
    }

    @Override
    public ListInterface<T> neighborsOf(T vertex) {
        ArrayList<T> result = new ArrayList<>();
        if (vertex == null) return result;
        ArrayList<T> list = adjacency.get(vertex);
        if (list == null) return result;
        for (int i = 0; i < list.size(); i++) {
            result.add(list.get(i));
        }
        return result;
    }

    @Override
    public ListInterface<T> bfs(T startVertex) {
        ArrayList<T> order = new ArrayList<>();
        if (startVertex == null || !adjacency.containsKey(startVertex)) {
            return order;
        }

        HashMap<T, Boolean> visited = new HashMap<>();
        int capacity = Math.max(1, vertexCount());
        QueueADT<T> queue = new QueueADT<>(capacity);

        visited.put(startVertex, Boolean.TRUE);
        queue.enqueue(startVertex);

        while (!queue.isEmpty()) {
            T current = queue.dequeue();
            if (current == null) break;
            order.add(current);

            ArrayList<T> list = adjacency.get(current);
            if (list == null) continue;

            for (int i = 0; i < list.size(); i++) {
                T neighbor = list.get(i);
                if (!visited.containsKey(neighbor)) {
                    visited.put(neighbor, Boolean.TRUE);
                    queue.enqueue(neighbor);
                }
            }
        }
        return order;
    }

    @Override
    public ListInterface<T> dfs(T startVertex) {
        ArrayList<T> order = new ArrayList<>();
        if (startVertex == null || !adjacency.containsKey(startVertex)) {
            return order;
        }
        HashMap<T, Boolean> visited = new HashMap<>();
        dfsVisit(startVertex, visited, order);
        return order;
    }

    private void dfsVisit(T vertex, HashMap<T, Boolean> visited, ArrayList<T> order) {
        visited.put(vertex, Boolean.TRUE);
        order.add(vertex);
        ArrayList<T> list = adjacency.get(vertex);
        if (list == null) return;
        for (int i = 0; i < list.size(); i++) {
            T neighbor = list.get(i);
            if (!visited.containsKey(neighbor)) {
                dfsVisit(neighbor, visited, order);
            }
        }
    }

    @Override
    public ListInterface<T> getAllVertices() {
        ArrayList<T> result = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) {
            result.add(vertices.get(i));
        }
        return result;
    }

    @Override
    public void clear() {
        adjacency.clear();
        vertices.clear();
        edges = 0;
    }
}


