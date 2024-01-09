
public class HuffmanTreeNode {
    private AsciiElement data;
    private HuffmanTreeNode left;
    private HuffmanTreeNode right;

    public HuffmanTreeNode(AsciiElement data) {
        this.data = data;
    }

    public HuffmanTreeNode() {
    }

    /**
     * Intialize a node by using two other nodes these nodes will be the left and
     * the right childs the sum of these nodes will the data of that node example :
     * {a,5},{b,3} => {ab,8}
     * 
     * @param left  a HuffmanTreeNode will be the left child
     * @param right a HuffmanTreeNode will be the right child
     */
    public void createByTwoChilds(HuffmanTreeNode left, HuffmanTreeNode right) {
        AsciiElement sumNode = new AsciiElement();
        sumNode.setName(left.getData().getName().concat(right.getData().getName()));
        sumNode.setFrequency(left.getData().getFrequency() + right.getData().getFrequency());
        this.data = sumNode;
        this.left = left;
        this.right = right;
    }

    public boolean isLeaf() {
        return (this.left == null && this.right == null);
    }

    public void setData(AsciiElement data) {
        this.data = data;
    }

    public AsciiElement getData() {
        return data;
    }

    public void setLeft(HuffmanTreeNode left) {
        this.left = left;
    }

    public HuffmanTreeNode getLeft() {
        return left;
    }

    public void setRight(HuffmanTreeNode right) {
        this.right = right;
    }

    public HuffmanTreeNode getRight() {
        return right;
    }

}
