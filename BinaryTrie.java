import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class BinaryTrie implements Serializable {
    private Node root;

    /**
     * Constructs a Huffman decoding trie according to the given frequency table which maps the characters to
     * their relative frequencies. The less frequent branch is on the '0' side of the trie, and the more
     * frequent branch is on the '1' side.
     * @param frequencyTable
     */
    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        if (frequencyTable == null || frequencyTable.size() == 0) {
            return;
        }

        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> e : frequencyTable.entrySet()) {
            if (e.getValue() > 0) {
                pq.offer(new Node(e.getKey(), e.getValue(), null, null));
            }
        }

        // special case: there is only one character with a nonzero frequency
        if (pq.size() == 1) {
            if (!frequencyTable.containsKey('\0')) {
                pq.offer(new Node('\0', 0, null, null));
            } else {
                pq.offer(new Node('\1', 0, null, null));
            }
        }

        // merge two smallest trees, less frequent branch is at the '0' side.
        while(pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pq.offer(parent);
        }

        this.root = pq.poll();
    }

    public Match longestPrefixMatch(BitSequence querySequence) {
        if (querySequence == null || root == null) {
            return null;
        }

        Node cur = root;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < querySequence.length(); i++) {
            if (cur.isLeaf()) {
                break;
            }
            if (querySequence.bitAt(i) == 0) {
                sb.append('0');
                cur = cur.left;
            } else {
                sb.append('1');
                cur = cur.right;
            }
        }
        return new Match(new BitSequence(sb.toString()), cur.ch);
    }

    public Map<Character, BitSequence> buildLookupTable() {
        Map<Character, BitSequence> lookupTable = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        lookupTableHelper(lookupTable, root, sb);
        return lookupTable;
    }

    private void lookupTableHelper(Map<Character, BitSequence> table, Node node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        if (node.isLeaf()) {
            table.put(node.ch, new BitSequence(sb.toString()));
            return;
        }

        lookupTableHelper(table, node.left, sb.append('0'));
        sb.deleteCharAt(sb.length() - 1);
        lookupTableHelper(table, node.right,sb.append('1'));
        sb.deleteCharAt(sb.length() - 1);
    }

    private static class Node implements Comparable<Node>, Serializable {
        private final char ch;
        private final int freq;
        private final Node left, right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        boolean isLeaf() {
            return (left == null) && (right == null);
        }

        @Override
        public int compareTo(Node o) {
            return this.freq - o.freq;
        }
    }

    public static void main(String[] args) {
        Map<Character, Integer> frequencyTable = new HashMap<Character, Integer>();
        frequencyTable.put('a', 1);
        frequencyTable.put('b', 2);
        frequencyTable.put('c', 4);
        frequencyTable.put('d', 5);
        frequencyTable.put('e', 6);
        BinaryTrie trie = new BinaryTrie(frequencyTable);
        BitSequence shouldBeA = new BitSequence("0001");
        Match m = trie.longestPrefixMatch(shouldBeA);
        System.out.println(m.getSymbol());
        System.out.println(m.getSequence());
    }
}
