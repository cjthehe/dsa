package ADT;

public class AVLTree<K extends Comparable<K>, V> implements AVLTreeInterface<K, V> {

    private class AVLNode {
        K key;
        V value;
        AVLNode left, right;
        int height;

        AVLNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private AVLNode root;

    // Get height 
    private int height(AVLNode node) {
        if(node == null){
            return 0;
        }else{
            return node.height; 
        }
    }

    // Get balance factor
    private int getBalance(AVLNode node) {
        if(node == null){
            return 0;
        }else{
            return height(node.left) - height(node.right);
        }
    }

    // Right rotation
    private AVLNode rotateRight(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    // Left rotation
    private AVLNode rotateLeft(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    // Insert key-value pair
    private AVLNode insert(AVLNode node, K key, V value) {
        if (node == null)
            return new AVLNode(key, value);

        if (key.compareTo(node.key) < 0)
            node.left = insert(node.left, key, value);
        else if (key.compareTo(node.key) > 0)
            node.right = insert(node.right, key, value);
        else {
            node.value = value; // overwrite value
            return node;
        }

        // Update height
        node.height = 1 + Math.max(height(node.left), height(node.right));

        // Rebalance
        int balance = getBalance(node);

        // Left Left Case
        if (balance > 1 && key.compareTo(node.left.key) < 0)
            return rotateRight(node);

        // Right Right Case
        if (balance < -1 && key.compareTo(node.right.key) > 0)
            return rotateLeft(node);

        // Left Right Case
        if (balance > 1 && key.compareTo(node.left.key) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Right Left Case
        if (balance < -1 && key.compareTo(node.right.key) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    // Public insert method
    @Override
    public void insert(K key, V value) {
        root = insert(root, key, value);
    }

    // Search for key
    private V search(AVLNode node, K key) {
        if (node == null)
            return null;

        if (key.compareTo(node.key) < 0)
            return search(node.left, key);
        else if (key.compareTo(node.key) > 0)
            return search(node.right, key);
        else
            return node.value;
    }

    @Override
    public V search(K key) {
        return search(root, key);
    }

    @Override
    public boolean contains(K key) {
        return search(key) != null;
    }

    // Find minimum node
    private AVLNode findMin(AVLNode node) {
        while (node.left != null)
            node = node.left;
        return node;
    }

    // Delete node
    private AVLNode delete(AVLNode node, K key) {
        if (node == null)
            return null;

        if (key.compareTo(node.key) < 0)
            node.left = delete(node.left, key);
        else if (key.compareTo(node.key) > 0)
            node.right = delete(node.right, key);
        else {
            // One or no child
            if (node.left == null)
                return node.right;
            else if (node.right == null)
                return node.left;

            // Node with two children: Get inorder successor (min in right)
            AVLNode minNode = findMin(node.right);
            node.key = minNode.key;
            node.value = minNode.value;
            node.right = delete(node.right, minNode.key);
        }

        // Update height
        node.height = 1 + Math.max(height(node.left), height(node.right));

        // Rebalance
        int balance = getBalance(node);

        // Left-Left Case
        if (balance > 1 && getBalance(node.left) >= 0)
            return rotateRight(node);

        // Left-Right Case
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Right-Right Case
        if (balance < -1 && getBalance(node.right) <= 0)
            return rotateLeft(node);

        // Right-Left Case
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    @Override
    public void delete(K key) {
        root = delete(root, key);
    }
}
