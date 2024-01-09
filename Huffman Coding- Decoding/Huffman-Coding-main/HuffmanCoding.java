import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HuffmanCoding {
    // Element used to spilt between values in the table(header)
    public static final String SPILT_ELEMENT = "`,";
    public static final String TABLE_END_ELEMENT = SPILT_ELEMENT.repeat(2);
    private String lastOperationHeader;

    /**
     * convert the input string to its comperessed form
     * 
     * @param input input string ex.{"abc"}
     * @return compression result ex.{"1000101101"}
     */
    public String compress(String input) {
        // creating a map of each character in the input and calculating its frequency
        HashMap<Character, Integer> asciiMap = new HashMap<>();
        for (Character c : input.toCharArray()) {
            if (asciiMap.containsKey(c)) {
                int frequency = asciiMap.get(c);
                asciiMap.replace(c, frequency + 1);
            } else {
                asciiMap.put(c, 1);
            }
        }

        // converting ascii map to intial huffman tree
        ArrayList<HuffmanTreeNode> initalHuffmanTree = new ArrayList<>();
        for (Map.Entry<Character, Integer> e : asciiMap.entrySet()) {
            AsciiElement element = new AsciiElement(e.getKey().toString(), e.getValue());
            initalHuffmanTree.add(new HuffmanTreeNode(element));
        }
        // build huffman tree using intial huffman tree
        HuffmanTreeNode huffmanTreeRoot = buildHuffmanTree(initalHuffmanTree);
        // create codes map form huffman tree
        HashMap<Character, String> newCodesMap = generateCodesMap(asciiMap.keySet(), huffmanTreeRoot);
        // create header string from codes map
        String header = headerFromCodesMap(newCodesMap);
        // intialize the last operation header global variable with the new header
        setLastOperationHeader(header);
        // write the input in its compressed form
        StringBuilder output = new StringBuilder();
        for (Character c : input.toCharArray()) {
            output.append(newCodesMap.get(c));
        }
        return output.toString();
    }

    /**
     * converts the codes map to header string
     * 
     * @param codesMap hash map of each chracter as a key an its code as a value
     *                 ex.{a:010,b:1,c:011}
     * @return header string ex.{"a`,010`,b`,1`,c`,011`,`,"}
     */
    private String headerFromCodesMap(HashMap<Character, String> codesMap) {
        StringBuilder header = new StringBuilder();
        for (Map.Entry<Character, String> e : codesMap.entrySet()) {
            header.append(e.getKey()).append(SPILT_ELEMENT);
            header.append(e.getValue()).append(SPILT_ELEMENT);
        }
        header.append(SPILT_ELEMENT);
        return header.toString();
    }

    /**
     * converts the codes map to header string
     * 
     * @param header string ex.{"a`,010`,b`,1`,c`,011`,`,"}
     * @return hash map of each chracter as a key an its code as a value
     *         ex.{a:010,b:1,c:011}
     */
    private HashMap<Character, String> codesMapFromHeader(String header) {
        String[] splitValues = header.split(SPILT_ELEMENT);
        HashMap<Character, String> codesMap = new HashMap<>();
        for (int i = 0; i < splitValues.length; i += 2) {
            codesMap.put(splitValues[i].charAt(0), splitValues[i + 1]);
        }
        return codesMap;
    }

    /**
     * create codes map from huffman tree by traversing it to find each character
     * code
     * 
     * @param characterSet    set of characters to find thier codes
     * @param huffmanTreeRoot the root node of huffman tree
     * @return hash map of each chracter as a key an its code as a value
     *         ex.{a:010,b:1,c:011}
     */
    private HashMap<Character, String> generateCodesMap(Set<Character> characterSet, HuffmanTreeNode huffmanTreeRoot) {
        HashMap<Character, String> newCodesMap = new HashMap<>();
        for (Character c : characterSet) {
            if (characterSet.size() == 1) {
                newCodesMap.put(c, "1");
                break;
            }
            StringBuilder newCode = new StringBuilder();
            HuffmanTreeNode node = huffmanTreeRoot;
            while (!node.isLeaf()) {
                if (node.getLeft().getData().getName().contains(c.toString())) {
                    newCode.append("0");
                    node = node.getLeft();
                } else {
                    newCode.append("1");
                    node = node.getRight();
                }
            }
            newCodesMap.put(c, newCode.toString());
        }
        return newCodesMap;
    }

    /**
     * build huffman tree using huffman algorithm
     * 
     * @param huffmanTree intial list of huffman tree nodes. each node's data =
     *                    ex.{AsciiElement({'a',11})}, and it's left and right
     *                    childs will be null
     * @return new huffman tree root
     */
    private HuffmanTreeNode buildHuffmanTree(ArrayList<HuffmanTreeNode> huffmanTree) {
        // sorting the input to make sure that the first node is the smallest one
        // and the second node is the second smallest one
        sortHuffmanTree(huffmanTree);
        // Stoping condition:
        // if there is only one node this means that the algorithm is finished
        // and this node is the root node so return it
        if (huffmanTree.size() == 1) {
            return huffmanTree.get(0);
        }
        // create a new node that will be the sum of the smallest two nodes
        // remove the two smallest nodes from the tree
        // add the new node to the tree
        HuffmanTreeNode nodeSum = new HuffmanTreeNode();
        nodeSum.createByTwoChilds(huffmanTree.get(0), huffmanTree.get(1));
        huffmanTree.remove(0);
        // the second smallest node took index 0 after removing the first node
        huffmanTree.remove(0);
        huffmanTree.add(nodeSum);
        return buildHuffmanTree(huffmanTree);
    }

    /**
     * sorts an arraylist of huffman tree node
     * 
     * @param huffmanTree arraylist of huffman tree node
     */
    private void sortHuffmanTree(ArrayList<HuffmanTreeNode> huffmanTree) {
        Collections.sort(huffmanTree, new Comparator<HuffmanTreeNode>() {
            @Override
            public int compare(HuffmanTreeNode node_1, HuffmanTreeNode node_2) {
                AsciiElement element_1 = node_1.getData();
                AsciiElement element_2 = node_2.getData();
                return Integer.compare(element_1.getFrequency(), element_2.getFrequency());
            }
        });
    }

    /**
     * convert the comperessed string to its original form
     * 
     * @param header a string that will be converted to codes map
     * @param data   comperessed string
     * @return original string
     */
    public String decompress(String header, String data) {
        StringBuilder output = new StringBuilder();
        HashMap<Character, String> codesMap = codesMapFromHeader(header);
        StringBuilder codeBuilder = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            codeBuilder.append(data.charAt(i));
            for (Map.Entry<Character, String> e : codesMap.entrySet()) {
                if (e.getValue().equals(codeBuilder.toString())) {
                    output.append(e.getKey());
                    codeBuilder.delete(0, codeBuilder.length());
                }
            }
        }
        return output.toString();
    }

    /**
     * setter for lastOperationHeader
     * 
     * @param lastOperationHeader
     */
    public void setLastOperationHeader(String lastOperationHeader) {
        this.lastOperationHeader = lastOperationHeader;
    }

    /**
     * getter for lastOperationHeader
     * 
     * @return
     */
    public String getLastOperationHeader() {
        return lastOperationHeader;
    }
}
