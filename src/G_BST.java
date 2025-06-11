import java.util.ArrayList;
import java.util.List;

public class G_BST<K extends Comparable<K>, V> {
    private class Node {
        K key;
        V value;
        Node left, right;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public String toString() {
            return key + " " + value;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }
    }

    private Node root;

    // Inorder traversal
    public void inorder() {
        inorder(root);
        System.out.println();
    }

    private void inorder(Node root) {
        if (root == null) return;
        inorder(root.left);
        System.out.print(root + " ");
        inorder(root.right);
    }

    // Contains method
    public boolean contains(K key) {
        return contains(root, key);
    }

    private boolean contains(Node root, K key) {
        if (root == null) return false;
        int cmp = root.key.compareTo(key);
        if (cmp == 0) return true;
        else if (cmp < 0) return contains(root.right, key);  // go right if root.key < key
        else return contains(root.left, key);                // go left if root.key > key
    }

    // Put method
    public void put(K key, V value) {
        root = put(root, key, value);
    }

    private Node put(Node root, K key, V value) {
        if (root == null) return new Node(key, value);
        int cmp = root.key.compareTo(key);
        if (cmp == 0) {
            root.value = value;
        } else if (cmp < 0) {
            root.right = put(root.right, key, value);  // go right
        } else {
            root.left = put(root.left, key, value);    // go left
        }
        return root;
    }

    // Find minimum key
    public K min() {
        if (root == null) return null;
        Node curr = root;
        while (curr.left != null) curr = curr.left;
        return curr.key;
    }

    // Find maximum key
    public K max() {
        if (root == null) return null;
        Node curr = root;
        while (curr.right != null) curr = curr.right;
        return curr.key;
    }

    // Successor: find next bigger key after given key
    private Node predecessor(Node root, K key) {
        Node curr = root;
        Node predecessor = null;

        while (curr != null) {
            int cmp = curr.key.compareTo(key);
            if (cmp < 0) {
                predecessor = curr;
                curr = curr.right;
            } else {
                curr = curr.left;
            }
        }
        return predecessor;
    }

    private Node successor(Node root, K key) {
        Node curr = root;
        Node successor = null;

        while (curr != null) {
            int cmp = curr.key.compareTo(key);
            if (cmp > 0) {
                successor = curr;
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        return successor;
    }

    // Delete method
    public void delete(K key) {
        root = delete(root, key);
    }


    private Node delete(Node root, K key) {
        if (root == null) return null;

        int cmp = root.key.compareTo(key);

        if (cmp > 0) {
            root.left = delete(root.left, key);
        } else if (cmp < 0) {
            root.right = delete(root.right, key);
        } else {
            // Found node to delete
            if (root.left == null) return root.right;
            if (root.right == null) return root.left;

            // Node with two children: replace with predecessor
            Node predecessor = predecessor(root,root.key);

            root.key = predecessor.key;
            root.value=predecessor.value;
            
            root.left = delete(root.left, predecessor.key);
        }
        return root;
    }
    public static void main(String[] args) {
        G_BST<Integer, String> bst = new G_BST<>();

        // Insert key-value pairs
        bst.put(5, "five");
        bst.put(3, "three");
        bst.put(7, "seven");
        bst.put(2, "two");
        bst.put(4, "four");
        bst.put(6, "six");
        bst.put(8, "eight");

        // Inorder traversal
        System.out.print("Inorder traversal: ");
        bst.inorder();

        // Contains test
        System.out.println("Contains 4? " + bst.contains(4));
        System.out.println("Contains 10? " + bst.contains(10));

        // Min and Max keys
        System.out.println("Min key: " + bst.min());
        System.out.println("Max key: " + bst.max());

        // Successor and Predecessor (using keys)
        G_BST<Integer, String>.Node succNode = bst.successor(bst.root, 5);
        System.out.println("Successor of 5: " + (succNode == null ? "null" : succNode.key + " " + succNode.value));

        G_BST<Integer, String>.Node predNode = bst.predecessor(bst.root, 5);
        System.out.println("Predecessor of 5: " + (predNode == null ? "null" : predNode.key + " " + predNode.value));

        succNode = bst.successor(bst.root, 8);
        System.out.println("Successor of 8: " + (succNode == null ? "null" : succNode.key + " " + succNode.value));

        predNode = bst.predecessor(bst.root, 2);
        System.out.println("Predecessor of 2: " + (predNode == null ? "null" : predNode.key + " " + predNode.value));

        // Delete nodes
        System.out.println("Deleting 3");
        bst.delete(3);
        System.out.print("Inorder traversal after deleting 3: ");
        bst.inorder();

        System.out.println("Deleting 5");
        bst.delete(5);
        System.out.print("Inorder traversal after deleting 5: ");
        bst.inorder();
    }

    public int height(){
        return height(root);
    }
    private int height(Node root){
        if(root==null) return 0;
        return 1+Math.max(height(root.left),height(root.right));
    }

    public boolean isBST(){
        return isBST(root,null,null);
    }
    private boolean isBST(Node root,K min,K max){
        if(root==null) return true;

        if(( min!=null && root.key.compareTo(min)<=0  )||
            (max!=null && root.key.compareTo(max)>0)){
            return false;
        }
        return isBST(root.left,min,root.key) && isBST(root.right,root.key,max);
    }

    public boolean isPerfectlyBalanced(){
        return isPerfectlyBalanced(root);
    }
    private int balanceFactor(Node x){
        return height(x.left)-height(x.right);
    }
    private boolean isPerfectlyBalanced(Node root){
        if(root==null) return true;
        if(Math.abs(balanceFactor(root))>1) return false;
        return isPerfectlyBalanced(root.left) && isPerfectlyBalanced(root.right);
    }
    public Node searchClosest(K key){
        return searchClosest(root,key);
    }
    private Node searchClosest(Node root,K key){
        int cmp=0;
        Node closest=null;
        while(root!=null){
            cmp=root.key.compareTo(key);
            if(cmp==0) return root;
            else if(cmp<0){
                closest=root;
                root=root.right;
            }
            else root=root.left;

        }
        return closest;
    }
    private void inorderKeys(Node root, List<Integer> keys) {
        if (root == null) return;
        inorderKeys(root.left, keys);
        keys.add((Integer) root.key);  // Cast safely assuming keys are Integer
        inorderKeys(root.right, keys);
    }
    public boolean checkExistTwoNodesWithSum(int s) {
        List<Integer> keys=new ArrayList<>();
        inorderKeys(root,keys);
        int left=0,right=keys.size()-1;

        while(left<right){
            int sum=keys.get(left)+keys.get(right);
            if(sum==s) return true;
            else if (sum<s) left++;
            else right--;
        }
        return false;
    }



}
