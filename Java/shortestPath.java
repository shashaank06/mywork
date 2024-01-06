
// Name: Shashaank Reddy Gurrala 
//UID:806546014
//A pledge of honesty  that I did not copy/modify from other's codes
//Declaration of copyright that no one else should copy/modify the code
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

public class shortestPath {

    private static double INF = 1e6;

    int numOfNodes;
    double[][] edgeWeight;
    boolean anyNegative;

    ArrayList<Integer> doDijkstra(int startNode, int endNode) {
        boolean[] isVisited = new boolean[numOfNodes];
        double[] distance = new double[numOfNodes];
        int[] parentNode = new int[numOfNodes];

        for (int node = 0; node < numOfNodes; node++) {
            isVisited[node] = false;
            distance[node] = INF;
            parentNode[node] = -1;
        }

        distance[startNode] = 0;

        PriorityQueue<AbstractMap.SimpleEntry<Integer, Double>> pq = new PriorityQueue<>(
                (node1, node2) -> node1.getValue() < node2.getValue() ? -1 : +1);
        pq.add(new AbstractMap.SimpleEntry<>(startNode, distance[startNode]));

        while (pq.size() > 0) {
            // Finds the closest node that is yet to be visited.
            AbstractMap.SimpleEntry<Integer, Double> best = pq.poll();
            if (isVisited[best.getKey()])
                continue;
            int bestNode = best.getKey();
            // Marks this node visited and relaxes all its adjacent nodes.
            isVisited[bestNode] = true;
            for (int nextNode = 0; nextNode < numOfNodes; nextNode++) {
                if (distance[bestNode] + edgeWeight[bestNode][nextNode] < distance[nextNode]) {
                    distance[nextNode] = distance[bestNode] + edgeWeight[bestNode][nextNode];
                    parentNode[nextNode] = bestNode;
                    pq.add(new AbstractMap.SimpleEntry<>(nextNode, distance[nextNode]));
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

    ArrayList<Integer> doBellmanFord(int startNode, int endNode) {
        double[] distance = new double[numOfNodes];
        int[] parentNode = new int[numOfNodes];

        for (int node = 0; node < numOfNodes; node++) {
            distance[node] = INF;
            parentNode[node] = -1;
        }

        distance[startNode] = 0;

        for (int itr = 0; itr < numOfNodes - 1; itr++) {
            for (int v = 0; v < numOfNodes; v++) {
                for (int w = 0; w < numOfNodes; w++) {
                    if (v == w)
                        continue;
                    if (edgeWeight[v][w] == INF) {
                        continue;
                    }
                    if (distance[w] > distance[v] + edgeWeight[v][w]) {
                        distance[w] = Math.max(-INF, distance[v] + edgeWeight[v][w]);
                        parentNode[w] = v;
                    }
                }
            }
        }
        if (distance[endNode] == INF) {
            return new ArrayList<>();
        }
        int cycleStart = -1;
        for (int u = 0; u < numOfNodes; u++) {
            for (int v = 0; v < numOfNodes; v++) {
                if (u == v)
                    continue;
                if (edgeWeight[u][v] == INF) {
                    continue;
                }
                if (distance[u] == INF || distance[v] == INF)
                    continue;
                if (distance[u] + edgeWeight[u][v] < distance[v]) {
                    cycleStart = u;
                    break;
                }
            }
        }
        if (cycleStart == -1) {
            ArrayList<Integer> pathNodes = new ArrayList<>();
            int currentNode = endNode;
            while (currentNode != -1) {
                pathNodes.add(currentNode);
                currentNode = parentNode[currentNode];
            }
            Collections.reverse(pathNodes);
            return pathNodes;
        } else {
            for (int itr = 0; itr < numOfNodes; itr++) {
                cycleStart = parentNode[cycleStart];
            }
            int cur = cycleStart;
            ArrayList<Integer> cycle = new ArrayList<>();
            while (cur != cycleStart || cycle.size() == 0) {
                cycle.add(cur);
                cur = parentNode[cur];
            }
            cycle.add(cur);
            Collections.reverse(cycle);
            return cycle;
        }
    }

    ArrayList<Integer> getShortestPath(int startNode, int endNode) {
        anyNegative = false;
        for (int i = 0; i < numOfNodes; i++) {
            for (int j = 0; j < numOfNodes; j++) {
                if (edgeWeight[i][j] < 0) {
                    anyNegative = true;
                }
            }
        }
        if (anyNegative) {
            return doBellmanFord(startNode, endNode);
        }
        return doDijkstra(startNode, endNode);
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
        System.out.println("Shortest path from vertex 0 to vertex n-1 in " + args[0] + ", |V|=n");
        new shortestPath().solve(filepath);

    }

    private void outputShortestPath(int graphId, ArrayList<Integer> path) {

        System.out.println("G" + graphId + "'s shortest path from 0 to " + (numOfNodes - 1) + ":");
        if (anyNegative)
            System.out.println("      Dynamic Programming");
        else
            System.out.println("      Dijkstra Algorithm");

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
            if (path.get(0) == path.get(path.size() - 1)) {
                System.out.println("      ** " + path.get(path.size() - 2) + " ==> " + path.get(path.size() - 1)
                        + " Enter a negative cycle.");
            }
        }
    }

    private static int parseNumberOfNodes(String line) {
        String[] tokens = line.replace(".", " ").split(" ");
        return Integer.parseInt(tokens[tokens.length - 1].split("}")[0]) + 1;
    }

    private static int parseFrom(String line) {
        String clean = line.replaceAll("[^0-9.-]", " ").trim();
        return Integer.parseInt(clean.split("\\s+")[0]);
    }

    private static int parseTo(String line) {
        String clean = line.replaceAll("[^0-9.-]", " ").trim();
        return Integer.parseInt(clean.split("\\s+")[1]);
    }

    private static double parseWeight(String line) {
        String clean = line.replaceAll("[^0-9.-]", " ").trim();
        return Double.parseDouble(clean.split("\\s+")[2]);
    }
}
