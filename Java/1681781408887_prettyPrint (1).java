
//	name - Shashaank Reddy Gurrala
// 	student uid- 806546014
// 	A pledge of honesty  that I did not copy/modify from other's codes
// 	Declaration of copyright that no one else should copy/modify the code

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class prettyPrint {

    void solveParagraph(ArrayList<String> paragraph, int width) {
        int n = paragraph.size();
        if (n == 0) {
            return;
        }
        // dp[i] = The minimum raggedness for the words in paragraph[i, n - 1].
        int[] dp = new int[n + 1];
        // next[i] = Optimal j, such that paragraph[i, j] is in the first line.
        int[] next = new int[n + 1];

        // Initialization.
        dp[n] = 0;
        next[n] = n;

        for (int i = n - 1; i >= 0; i--) {
            int len = 0;
            dp[i] = 1000000000;
            next[i] = -1;
            for (int j = i; j < n; j++) {
                // word length.
                len += paragraph.get(j).length();
                // ending space.
                int space = width - (j - i + len);
                if (space >= 0) {
                    int curValue = j == n - 1 ? 0 : space * space + dp[j + 1];
                    if (curValue < dp[i]) {
                        dp[i] = curValue;
                        next[i] = j;
                    }
                } else {
                    break;
                }
            }
        }

        // Retrieving the answer from the next table.
        int at = 0;
        while (at < n) {
            int till = next[at];
            for (int i = at; i <= till; i++) {
                System.out.print(paragraph.get(i));
                if (i != till) {
                    System.out.print(" ");
                }
            }
            System.out.println();
            at = till + 1;
        }

        System.out.println("===[" + dp[0] + "]");
        System.out.println("");
    }

    // this method takes file path as input and parse the data.
    void printParagraphs(String filePath, int width) throws Exception {
        File file = new File(filePath);
        ArrayList<String> p = new ArrayList<>();

        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;
        while ((line = buffer.readLine()) != null) {
            boolean ends = true;
            String[] ws = line.trim().split(" ");
            for (String w : ws) {
                if (w.length() != 0) {
                    p.add(w);
                    ends = false;
                }
            }
            if (ends) {
                solveParagraph(p, width);
                p.clear();
            }
        }
        solveParagraph(p, width);
        buffer.close();
        System.out.println("Asg 7 by <your name>");
    }

    public static void main(String[] args) throws Exception {
        new prettyPrint().printParagraphs(args[0], Integer.parseInt(args[1]));
    }
}
