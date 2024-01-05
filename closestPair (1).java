import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Comparator;

public class closestPair {
    private int MAX = 200000;

    Point[] Points = new Point[MAX];
    Point[] temp = new Point[MAX];

    // Runs O(n^2) algorithm to find the closest pairs.
    Point[] brute(int l, int r) {
        Point[] result = new Point[2];
        Double mn = 1e100;
        for (int i = l; i <= r; i++) {
            for (int j = i + 1; j <= r; j++) {
                Double d = Points[i].distance(Points[j]);
                if (d < mn) {
                    mn = d;
                    result[0] = Points[i];
                    result[1] = Points[j];
                }
            }
        }
        return result;
    }

    // Returns Point[2] of length 2, representing the closest pair.
    Point[] findClosestPair(int l, int r) {
        if (r - l + 1 <= 3) {
            return brute(l, r);
        }
        int mid = (l + r) / 2;

        Point[] leftPair = findClosestPair(l, mid);
        double leftDistance = leftPair[0].distance(leftPair[1]);

        Point[] rightPair = findClosestPair(mid + 1, r);
        double rightDistance = rightPair[0].distance(rightPair[1]);

        // Merges the results from left and right half.
        double distance = Math.min(leftDistance, rightDistance);
        Point[] result = leftDistance < rightDistance ? leftPair : rightPair;

        // Stores possible points for the closest pair in temp.
        int at = 0; 
        for(int i = l; i <= r; i++) {
            if(Math.abs(Points[i].x - Points[mid].x) < distance) {
                temp[at++] = Points[i];
            }
        }
        if(at == 0) return result;

        // Sort the possible points in Y order.
        Arrays.sort(temp, 0, at, sortByY);
        for(int i = 0; i < at; i++) {
            for(int j = i + 1; j < at; j++) {
                double yD = Math.abs(temp[i].y - temp[j].y);
                // Break whenever the Y-distance is more than the distance. As they are sorted by Y,
                // it is guranteed that the other points can't be closer than distance, so we can
                // break early.
                if(yD >= distance) {
                    break;
                }
                double d = temp[i].distance(temp[j]);
                if(d < distance) {
                    distance = d;
                    result[0] = temp[i];
                    result[1] = temp[j];
                }
            }
        }
        Arrays.sort(result, 0, 2);
        return result;
    }

    void solve(String filePath) throws Exception {
        File file = new File(filePath);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;

        while ((line = buffer.readLine()) != null) {
            if (line.startsWith("**")) {
                String header = line;
                int n = 0;
                while ((line = buffer.readLine()) != null) {
                    if (line.startsWith("-")) {
                        break;
                    }
                    Points[n++] = Point.fromString(line);
                }
                long timer = System.currentTimeMillis();
                Arrays.sort(Points, 0, n);

                Point[] result = findClosestPair(0, n - 1);
                long ms = System.currentTimeMillis() - timer;

                System.out.println(header.replace("** ", ""));
                System.out.println("    " + result[0].distance(result[1]));
                System.out.println(result[0] + " - " + result[1]);
                System.out.println("    " + ms + " ms on " + n + " points");
            }
        }

        buffer.close();

    }

    public static void main(String[] args) throws Exception {
        String filepath = args[0];
        new closestPair().solve(filepath);
    }

    // Comparator to sort Points based on Y first, then X.
    private static Comparator<Point> sortByY = new Comparator<Point>() {
        @Override
        public int compare(Point p1, Point p2) {
            if (p1.y != p2.y) {
                return p1.y > p2.y ? +1 : -1;
            }
            if (p1.x  != p2.x) {
                return p1.x > p2.x ? +1 : -1;
            }
            return 0;
        }
    };

}
