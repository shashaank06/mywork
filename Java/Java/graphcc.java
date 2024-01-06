import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class graphcc {

    int numOfNodes;
    boolean[][] adjacentMatrix;
    boolean[] isVisited;

    ArrayList<ArrayList<Integer>> getConnectedComponentsByDfs() {
        isVisited = new boolean[numOfNodes];
        for(int node = 0; node < numOfNodes; node++) {
            isVisited[node] = false;
        }
        ArrayList<ArrayList<Integer>> connectedComponents = new ArrayList<>();
        for(int node = 0; node < numOfNodes; node++) {
            if(isVisited[node] == false) {
                ArrayList<Integer> currentComponent = new ArrayList<>();
                dfs(node, currentComponent);
                connectedComponents.add(currentComponent);
            }
        }
        return connectedComponents;
    }

    void dfs(int node, ArrayList<Integer> component) {
        isVisited[node] = true;
        component.add(node);
        for(int adj = 0; adj < numOfNodes; adj++) {
            if(adjacentMatrix[node][adj] == true && isVisited[adj] == false) {
                dfs(adj, component);
            }
        }
    }

    ArrayList<ArrayList<Integer>> getConnectedComponentsByBfs() {
        isVisited = new boolean[numOfNodes];
        for(int node = 0; node < numOfNodes; node++) {
            isVisited[node] = false;
        }
        ArrayList<ArrayList<Integer>> connectedComponents = new ArrayList<>();
        for(int node = 0; node < numOfNodes; node++) {
            if(isVisited[node] == false) {
                ArrayList<Integer> currentComponent = new ArrayList<>();
                Queue<Integer> queue = new ArrayDeque<Integer>();
                queue.add(node);
                isVisited[node] = true;
                currentComponent.add(node);;
                while(queue.size() > 0) {
                    int currentNode = queue.poll();
                    for(int adj = 0; adj < numOfNodes; adj++) {
                        if(isVisited[adj] == false && adjacentMatrix[currentNode][adj] == true) {
                            isVisited[adj] = true;
                            queue.add(adj);
                            currentComponent.add(adj);
                        }
                    }
                }
                connectedComponents.add(currentComponent);
            }
        }
        return connectedComponents;
    }

    void solve(String filePath) throws Exception {
        File file = new File(filePath);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;

        int graphId = 0;

        while ((line = buffer.readLine()) != null) {
            if (line.startsWith("** G")) {
                graphId++;
                numOfNodes = parseNumberOfNodes(line);

                adjacentMatrix = new boolean[numOfNodes][numOfNodes];
                for (int from = 0; from < numOfNodes; from++) {
                    for (int to = 0; to < numOfNodes; to++) {
                        adjacentMatrix[from][to] = from == to ? true : false;
                    }
                }

                line = buffer.readLine();
                while ((line = buffer.readLine()) != null) {
                    if (line.startsWith("-")) {
                        break;
                    }
                    int from = parseFrom(line);
                    int to = parseTo(line);

                    adjacentMatrix[from][to] = true;
                    adjacentMatrix[to][from] = true;
                }

                System.out.println("** G" + graphId + "'s connected components:");
                
                System.out.println("     Breadth First Search:");
                outputConnectedComponent(getConnectedComponentsByBfs());

                System.out.println("     Depth First Search:");
                outputConnectedComponent(getConnectedComponentsByDfs());

                System.out.println();
            }
        }

        buffer.close();
    }

    public static void main(String[] args) throws Exception {
        String filepath = args[0];
        new graphcc().solve(filepath);
    }

    private void outputConnectedComponent(ArrayList<ArrayList<Integer>> connectedComponents) {
        for(ArrayList<Integer> component : connectedComponents) {
            System.out.print("         ");
            for(int node : component) {
                System.out.print(node + " ");
            }
            System.out.println();
        }
    }

    private static int parseNumberOfNodes(String line) {
        String clean = line.replaceAll("[^0-9]", " ").trim();
        String[] splitted = clean.split("\\s+");
        return Integer.parseInt(splitted[splitted.length - 1]) + 1;
    }

    private static int parseFrom(String line) {
        String clean = line.replaceAll("[^0-9.]", " ").trim();
        return Integer.parseInt(clean.split("\\s+")[0]);
    }

    private static int parseTo(String line) {
        String clean = line.replaceAll("[^0-9.]", " ").trim();
        return Integer.parseInt(clean.split("\\s+")[1]);
    }
}
