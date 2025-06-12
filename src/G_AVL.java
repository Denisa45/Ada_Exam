import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class G_AVL<K extends Comparable<K>, V> {
    private class Node {
        K key;
        V value;
        Node left, right;
        int height;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.height = 0;
        }

        public String toString() {
            return "(" + key + ", " + value + ")";
        }
    }

    private Node root;

    private int height(Node node) {
        return node == null ? -1 : node.height;
    }

    private int balanceFactor(Node node) {
        return height(node.left) - height(node.right);
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;

        x.height = 1 + Math.max(height(x.left), height(x.right));
        y.height = 1 + Math.max(height(y.left), height(y.right));
        return y;
    }

    private Node rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        y.right = x;

        x.height = 1 + Math.max(height(x.left), height(x.right));
        y.height = 1 + Math.max(height(y.left), height(y.right));
        return y;
    }

    // Balances the tree by performing rotations
    private Node balance(Node root) {
        if (root == null) return null;

        root.height = 1 + Math.max(height(root.left), height(root.right));
        int bf = balanceFactor(root);

        // Left heavy
        if (bf > 1) {
            if (balanceFactor(root.left) < 0) {
                root.left = rotateLeft(root.left);  // Left-Right case
            }
            root = rotateRight(root);  // Left-Left case
        }
        // Right heavy
        else if (bf < -1) {
            if (balanceFactor(root.right) > 0) {
                root.right = rotateRight(root.right);  // Right-Left case
            }
            root = rotateLeft(root);  // Right-Right case
        }

        return root;
    }

    // Put (insert or update)
    public void put(K key, V value) {
        root = put(root, key, value);
    }

    private Node put(Node root, K key, V value) {
        if (root == null) return new Node(key, value);
        int cmp = key.compareTo(root.key);

        if (cmp < 0) {
            root.left = put(root.left, key, value);
        } else if (cmp > 0) {
            root.right = put(root.right, key, value);
        } else {
            root.value = value;
            return root;
        }

        return balance(root);
    }

    // Get
    public V get(K key) {
        return get(root, key);
    }

    private V get(Node root, K key) {
        if (root == null) return null;
        int cmp = key.compareTo(root.key);
        if (cmp < 0) return get(root.left, key);
        else if (cmp > 0) return get(root.right, key);
        else return root.value;
    }

    // Remove
    public void remove(K key) {
        root = remove(root, key);
    }

    private Node remove(Node root, K key) {
        if (root == null) return null;
        int cmp = key.compareTo(root.key);

        if (cmp < 0) {
            root.left = remove(root.left, key);
        } else if (cmp > 0) {
            root.right = remove(root.right, key);
        } else {
            if (root.left == null) return root.right;
            if (root.right == null) return root.left;

            Node pred = findMax(root.left);  // In-order predecessor
            root.key = pred.key;
            root.value = pred.value;
            root.left = remove(root.left, pred.key);
        }

        return balance(root);
    }

    private Node findMax(Node root) {
        while (root.right != null) root = root.right;
        return root;
    }

    private Node findMin(Node root) {
        while (root.left != null) root = root.left;
        return root;
    }

    // Get all entries in sorted order
    public Iterable<Node> getEntries() {
        List<Node> result = new ArrayList<>();
        inOrder(root, result);
        return result;
    }

    private void inOrder(Node root, List<Node> list) {
        if (root == null) return;
        inOrder(root.left, list);
        list.add(root);
        inOrder(root.right, list);
    }

    // Optional: display tree level by level (BFS)
    public void displayLevels() {
        if (root == null) return;
        Queue<Node> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()) {
            Node cur = q.poll();
            System.out.print(cur + " ");
            if (cur.left != null) q.add(cur.left);
            if (cur.right != null) q.add(cur.right);
        }
        System.out.println();
    }
    public static void main(String[] args) {
        G_AVL<Integer, String> avl = new G_AVL<>();

        // Insert entries
        avl.put(50, "A");
        avl.put(30, "B");
        avl.put(70, "C");
        avl.put(20, "D");
        avl.put(40, "E");
        avl.put(60, "F");
        avl.put(80, "G");

        System.out.println("In-order traversal (sorted key-value pairs):");
        for (Object entry : avl.getEntries()) {
            System.out.println(entry);
        }

        System.out.println("\nLevel-order (structure):");
        avl.displayLevels();

        // Get values
        System.out.println("\nGet value for key 40: " + avl.get(40));
        System.out.println("Get value for key 100 (not present): " + avl.get(100));

        // Remove a node with two children
        System.out.println("\nRemoving key 50 (root with two children):");
        avl.remove(50);

        System.out.println("\nIn-order traversal after deletion:");
        for (Object entry : avl.getEntries()) {
            System.out.println(entry);
        }

        System.out.println("\nLevel-order after deletion:");
        avl.displayLevels();
    }
}
