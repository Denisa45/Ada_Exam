public class G_AVL<K extends Comparable<K>,V>{
    private class Node{
        K key;
        V value;
        Node left,right;
        int height;
    }
    private int balanceFactor(Node root) {
        return root.left.height-root.right.right.height;
    }

    private Node rotateL(Node x){
        Node y=x.right;
        x.right=y.left;
        y.left=x;

        x.height=1+Math.max(x.left.height, x.right.height);
        y.height=1+Math.max(y.left.height,y.right.height);
        return y;
    }
    private Node rotateR(Node x){
        Node y=x.left;
        x.left=y.right;

        y.right=x;
        x.height=1+Math.max(x.left.height, x.right.height);
        y.height=1+Math.max(y.left.height,y.right.height);
        return y;
    }
    private Node balance(Node root){
        if (balanceFactor(root)==-2){
            if(balanceFactor(root.left)==1){
                root.left=rotateL(root.left);
            }
            root=rotateR(root);
        }

        if(balanceFactor(root)==2){
            if(balanceFactor(root.right)==-1){
                root.right=rotateR(root.right);
            }
            root=rotateL(root);
        }
        return root;
    }
    private Node predecessor(Node root,K key){
        int cmp=0;
        Node ancestor=null;
        while(root!=null){
            cmp=root.key.compareTo(key);
            if(cmp==0){
                if(root.left!=null){
                    root=root.left;
                    while (root.right!=null){
                        root=root.right;
                    }
                    return root;
                }else return ancestor;
            }
            if(cmp<0){
                ancestor=root;
                root=root.right;
            }
            else root=root.left;
        }
        return null;
    }

    private Node successor(Node root,K key){
        Node ancestor=null;int cmp=0;
        while(root!=null){
            cmp=root.key.compareTo(key);
            if(cmp==0) {
                if(root.right!=null){
                    root=root.right;
                    while(root.left!=null){
                        root=root.left;
                    }
                    return root;
                }return ancestor;
            }

            if(cmp<0) {
                ancestor=root;
                root=root.right;
            }
            else root=root.left;
        }
        return null;
    }
}
