import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;

public class dijkstra {

    private static double INF = 1e18;

    int numOfNodes;
    double[][] edgeWeight;

    ArrayList<Integer> getShortestPath(int startNode, int endNode) {

        boolean[] isVisited = new boolean[numOfNodes];
        double[] distance = new double[numOfNodes];
        int[] parentNode = new int[numOfNodes];

        for (int node = 0; node < numOfNodes; node++) {
            isVisited[node] = false;
            distance[node] = INF;
            parentNode[node] = -1;
        }

        distance[startNode] = 0;

        while (true) {
            // Finds the closest node that is yet to be visited.
            int bestNode = -1;
            for (int node = 0; node < numOfNodes; node++) {
                if (isVisited[node]) {
                    continue;
                }
                if (bestNode == -1 || distance[bestNode] > distance[node]) {
                    bestNode = node;
                }
            }
            if (bestNode == -1 || distance[bestNode] == INF) {
                break;
            }
            // Marks this node visited and relaxes all its adjacent nodes.
            isVisited[bestNode] = true;
            for (int nextNode = 0; nextNode < numOfNodes; nextNode++) {
                if (distance[bestNode] + edgeWeight[bestNode][nextNode] < distance[nextNode]) {
                    distance[nextNode] = distance[bestNode] + edgeWeight[bestNode][nextNode];
                    parentNode[nextNode] = bestNode;

                }
            }
        }

        if (!isVisited[endNode]) {
            return new ArrayList<>();
        }

        // Finds the shortest path from parent array.
        ArrayList<Integer> pathNodes = new ArrayList<>();
        int currentNode = endNode;
        while (currentNode != -1) {
            pathNodes.add(currentNode);
            currentNode = parentNode[currentNode];
        }
        Collections.reverse(pathNodes);
        return pathNodes;
    }

    void solve(String filePath) throws Exception {
        File file = new File(filePath);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;

        int graphId = 0;

        while ((line = buffer.readLine()) != null) {
            if (line.startsWith("**")) {
                graphId++;
                numOfNodes = parseNumberOfNodes(line);

                edgeWeight = new double[numOfNodes][numOfNodes];
                for (int from = 0; from < numOfNodes; from++) {
                    for (int to = 0; to < numOfNodes; to++) {
                        edgeWeight[from][to] = from == to ? 0 : INF;
                    }
                }

                line = buffer.readLine();
                while ((line = buffer.readLine()) != null) {
                    if (line.startsWith("-")) {
                        break;
                    }
                    int from = parseFrom(line);
                    int to = parseTo(line);
                    double weight = parseWeight(line);

                    edgeWeight[from][to] = Math.min(edgeWeight[from][to], weight);
                }

                ArrayList<Integer> path = getShortestPath(0, numOfNodes - 1);
                outputShortestPath(graphId, path);
            }
        }

        buffer.close();
    }

    public static void main(String[] args) throws Exception {
        String filepath = args[0];
        new dijkstra().solve(filepath);
    }

    private void outputShortestPath(int graphId, ArrayList<Integer> path) {

        System.out.println("G" + graphId + "'s shortest path from 0 to " + (numOfNodes - 1) + ":");

        if (path.size() == 0) {
            System.out.println("      *** There is no path.");
        } else {
            double totalDistance = 0;
            for (int i = 0; i + 1 < path.size(); i++) {
                int u = path.get(i);
                int v = path.get(i + 1);
                double weight = edgeWeight[u][v];
                totalDistance += weight;
                System.out.print("      (" + u + ", " + v + ", " + String.format("%.3f", weight) + ")");
                System.out.println(" -->    " + String.format("%.3f", totalDistance));
            }
        }
    }

    private static int parseNumberOfNodes(String line) {
        String[] tokens = line.replace(".", " ").split(" ");
        return Integer.parseInt(tokens[tokens.length - 1].split("}")[0]) + 1;
    }

    private static int parseFrom(String line) {
        String clean = line.replaceAll("[^0-9.]", " ").trim();
        return Integer.parseInt(clean.split("\\s+")[0]);
    }

    private static int parseTo(String line) {
        String clean = line.replaceAll("[^0-9.]", " ").trim();
        return Integer.parseInt(clean.split("\\s+")[1]);
    }

    private static double parseWeight(String line) {
        String clean = line.replaceAll("[^0-9.]", " ").trim();
        return Double.parseDouble(clean.split("\\s+")[2]);
    }
}
