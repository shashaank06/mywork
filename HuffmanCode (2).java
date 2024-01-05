import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class HuffmanCode {

    public class Node {
        private int frequency_sum;
        private int depth;
        private int subtree_size;
        private int ascii_sum;
        private Node lc;
        private Node rc;

        public Node(int frequency_sum, int depth, int subtree_size, int ascii_sum) {
            this.frequency_sum = frequency_sum;
            this.depth = depth;
            this.subtree_size = subtree_size;
            this.ascii_sum = ascii_sum;
            this.lc = null;
            this.rc = null;
        }

        public Node(int frequency_sum, int depth, int subtree_size, int ascii_sum, Node lc, Node rc) {
            this.frequency_sum = frequency_sum;
            this.depth = depth;
            this.subtree_size = subtree_size;
            this.ascii_sum = ascii_sum;
            this.lc = lc;
            this.rc = rc;
        }

        public Node merge(Node right) {
            return new Node(this.frequency_sum + right.frequency_sum, Math.max(this.depth, right.depth) + 1,
                    this.subtree_size + right.subtree_size + 1,
                    this.ascii_sum + right.ascii_sum, this, right);
        }

        @Override
        public String toString() {
            return "f:" + frequency_sum + " d:" + depth + " sub:" + subtree_size + " asum:" + ascii_sum;
        }
    
    }

    public class NodeFrequencyComparator implements Comparator<Node> {
        @Override
        public int compare(Node a, Node b) {
            return a.frequency_sum - b.frequency_sum;
        }
    }

    public int compare(Node a, Node b) {            
        if(a.depth != b.depth) {
            return a.depth - b.depth;
        }
        if(a.subtree_size != b.subtree_size) {
            return a.subtree_size - b.subtree_size;
        }
        return a.ascii_sum - b.ascii_sum;
    }


    HashMap<Character, Integer> frequencies = new HashMap<Character, Integer>();
    HashMap<Character, String> codesMap = new HashMap<Character, String>();
    HashMap<String, Character> codesReverseMap = new HashMap<String, Character>();


    ArrayList<Character> chars = new ArrayList<>();
    ArrayList<String> codes = new ArrayList<>();

    private void dfs(Node cur, String code) {
        if(cur.depth == 1) {
            chars.add((char) cur.ascii_sum);
            codes.add(code);
            codesMap.putIfAbsent((char) cur.ascii_sum, code);
            codesReverseMap.putIfAbsent(code, (char) cur.ascii_sum);
            return;
        }
        dfs(cur.lc, code + "0");
        dfs(cur.rc, code + "1");
    }

    public HuffmanCode(char[] a, int[] f) {
        PriorityQueue<Node> pq = new PriorityQueue<Node>(new NodeFrequencyComparator());
        for(int i = 0; i < a.length; i++) {
            frequencies.putIfAbsent((Character) a[i], f[i]);
            pq.add(new Node(f[i], 1, 1, a[i]));
        }
        while(pq.size() > 1) {
            Node lc = pq.poll();
            Node rc = pq.poll();
            
            if(compare(lc, rc) > 0) {
                Node temp = lc;
                lc = rc;
                rc = temp;
            }

            Node merged = lc.merge(rc);
            pq.add(merged);

            System.out.println(lc.frequency_sum + " - " + rc.frequency_sum + " - " + merged.frequency_sum);
        }

        Node root = pq.poll();
        dfs(root, "");
    }

    public void printCodeWords() {
        for(int i = 0; i < chars.size(); i++) {
            System.out.println(chars.get(i) + "[" + (int)chars.get(i) + "]: " + codes.get(i) + " (" + frequencies.get(chars.get(i)) + ")");
        }
    }

    public String encode(String text) {
        String code = "";
        for(int i = 0; i < text.length(); i++) {
            code = code + codesMap.get(text.charAt(i));
        }
        return code;
    }

    public String decode(String codeString) {
        String text = "";
        String cur = "";

        for(int i = 0; i < codeString.length(); i++) {
            cur = cur + codeString.charAt(i);
            if(codesReverseMap.get(cur) != null) {
                text += codesReverseMap.get(cur);
                System.out.println(cur);
                cur = "";
            }
        }
        
        return text;
    }
};