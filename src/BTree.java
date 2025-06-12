
import java.util.LinkedList;
import java.util.Queue;


/**
 * A simple implementation for B-Tree with Integer keys
 *
 * Implemented operations: insert, displayLevels
 *
 * Implement as exercises:
 *   public boolean contains(int key);
 *   public int height();
 *   public int level(int key);
 *   public int min();
 *   public int max();
 *   public int successor(int key);
 *   public int predecessor(int key);
 *   public void inorder(){;
 */

public class BTree {

    private int T; // the mindegree of the B-Tree

    class BTreeNode {
        int n;    // current number of keys contained in node
        Integer key[] = new Integer[2 * T - 1];   //maximum 2T-1 keys
        BTreeNode child[] = new BTreeNode[2 * T]; // maximum 2T children
        boolean leaf = true;

        public String toString(){
            StringBuilder sb =new StringBuilder();
            sb.append(" [ ");
            for (int i=0; i<n; i++)
                sb.append(" "+key[i]);
            sb.append(" ] ");
            return sb.toString();
        }
    }

    /**
     * Constructor of an empty B-Tree of mindegree T
     * @param t - degree of B tree
     */
    public BTree(int t) {
        T = t;
        root = new BTreeNode();
        root.n = 0;
        root.leaf = true;
    }

    private BTreeNode root; // root of tree

    /**
     * Insert a key into a B-Tree in a single pass down the tree
     * see [CLRS] algorithm
     * @param key - new key to be inserted
     */
    public void insert(Integer key) {
        BTreeNode r = root;
        if (r.n == 2 * T - 1) { // if the root node is already full
            BTreeNode s = new BTreeNode(); // preventively splits the root
            root = s;
            s.leaf = false;
            s.n = 0;
            s.child[0] = r;
            split(s, 0, r);
            insertNonfullStart(s, key);
        } else {
            insertNonfullStart(r, key);
        }
    }


    /**
     * Splits a node and introduces the new split as a child of the same parent
     * see [CLRS] algorithm
     * @param x - parent of node to split
     * @param pos - position in parent where to link new node
     * @param y - node to be split
     */
    private void split(BTreeNode x, int pos, BTreeNode y) {
        System.out.println("Split node "+y.toString());

        BTreeNode z = new BTreeNode();
        z.leaf = y.leaf; // new node z is leaf only if node y to be splitted was leaf
        z.n = T - 1;
        for (int j = 0; j < T - 1; j++) { //copy right half of y into new node
            z.key[j] = y.key[j + T];
        }
        if (!y.leaf) {
            for (int j = 0; j < T; j++) {
                z.child[j] = y.child[j + T];
            }
        }

        y.n = T - 1;
        for (int j = x.n; j >= pos + 1; j--) { //right shift children in parent node
            x.child[j + 1] = x.child[j];
        }
        x.child[pos + 1] = z; //insert new node z as child of parent

        for (int j = x.n - 1; j >= pos; j--) {
            x.key[j + 1] = x.key[j];
        }
        x.key[pos] = y.key[T - 1];
        x.n = x.n + 1;
    }


    /**
     * Inserts key k into node x which is assumed to be non-full when function is called.
     * This function recurses as necessary down the tree, at all times guaranteeing that
     * the node to which it recurses is not full by calling split as necessary
     * see [CLRS] algorithm
     * @param x - root (non-full node) of subtree where insertion is done
     * @param k - new key to be inserted
     */
    final private void insertNonfullStart(BTreeNode x, int k) {

        if (x.leaf) {
            // x is a non-full leaf node, insert key into it
            int i = 0;
            // shift existing keys right to make place for new k
            for (i = x.n - 1; i >= 0 && k < x.key[i]; i--) {
                x.key[i + 1] = x.key[i];
            }
            x.key[i + 1] = k;
            x.n = x.n + 1;
        } else { // x is not a leaf
            int i = 0;
            for (i = x.n - 1; i >= 0 && k < x.key[i]; i--) {
            } // search child to continue insertion
            ;
            i++;
            BTreeNode tmp = x.child[i];
            if (tmp.n == 2 * T - 1) {
                // if child is full, split it
                split(x, i, tmp);
                if (k > x.key[i]) { // determines in which split half we insert
                    i++;
                }
            }
            insertNonfullStart(x.child[i], k); // recursive insert
        }
    }
    public boolean contains(int key){
        return contains(root,key);
    }
    private boolean contains(BTreeNode root,int key){
       if(root==null) return false;
       int i=0;
       while(i<root.n){
           if(root.key[i]==key) return true;
           if(root.key[i]>key) return contains(root.child[i],key);
           i++;
       }
       return false;
    }

    public int height(){
        return height( root);
    }
    private int height(BTreeNode root){
        if(root.leaf) return 1;
        return 1+height(root.child[0]);
    }
    public void printByLevel(){
        printByLevel(root);
    }
    private void printByLevel(BTreeNode root){
        Queue<BTreeNode> q=new LinkedList<>();
        q.add(root);
        while(!q.isEmpty()){
            BTreeNode cur=q.poll();
            for(int i=0;i<cur.n;i++){
                System.out.print(cur.key[i]);
                if(root.child[i]!=null){
                    q.add(root.child[i]);
                }
            }
            if(root.child[cur.n]!=null){
                q.add(root.child[cur.n]);
            }
            System.out.println();
        }

    }
    public int successor(int key){
        return successor(root,key,0);
    }
    private int successor(BTreeNode root,int key,int ancestor){
        if(root==null) return ancestor;

        int i=0;
        while (i<root.n && root.key[i]<=key){
            ancestor=root.key[i];
            i++;
        }
        if(root.key[i]==key){
            if(root.child[i+1]!=null){
                root=root.child[i+1];
                while (root.child[0]!=null){
                    root=root.child[0];
                }
                return root.key[0];
            }
        }
        else if(i<root.n) return root.key[i];
        else return ancestor;
        return successor(root.child[i],key,ancestor);
    }
    public int predecessor(int key){
        return predecessor(root,key,0);
    }
    private int predecessor(BTreeNode root,int key,int ancestor){
        if(root==null) return ancestor;
        int i=0;
        while(i<root.n && root.key[i]<key){
            ancestor=root.key[i];
            i++;
        }

        if(root.key[i]==key){
            if(root.child[i]!=null){
                root=root.child[i];
                while (root.child[root.n]!=null){
                    root=root.child[root.n];
                }
                return root.key[root.n-1];
            }
        }
        else if(i>0) return root.key[i-1];
        else return ancestor;
        return predecessor(root.child[i],key,ancestor);
    }


}