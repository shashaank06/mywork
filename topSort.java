
import java.io.*;
import java.util.*;

public class topSort {

	private static List<GraphData> getGraphData(String fileData,int count) {
		int totalGraphs=count;
		List<GraphData> listOfGraphData = new ArrayList<GraphData>();
		for (int i = 0; i < totalGraphs; i++) {

			// finding start and end index for graph data
			int endIndex = 0;
			if (i == (totalGraphs - 1)) {
				endIndex = fileData.length();
			} else {
				endIndex = fileData.indexOf("** G" + (i + 2) + ":");
			}

			// finding each graph data using start and end index
			String eachGraphData = fileData.substring(fileData.indexOf("** G" + (i + 1) + ":"), endIndex);

			// find nodes from graph content
			String nodeData = eachGraphData.substring(eachGraphData.indexOf("V = {") + 5, eachGraphData.indexOf("}"))
					.trim();
			String stringNodes[] = nodeData.split(" ");
			GraphData graphData = new GraphData();
			graphData.nodes = new Stack<Integer>();
			for (int j = 0; j < stringNodes.length; j++) {
				int node = Integer.parseInt(stringNodes[j]);
				graphData.nodes.add(node);
			}

			// find edges from graph content
			String edgeData = eachGraphData
					.substring(eachGraphData.indexOf("E = {") + 5, eachGraphData.lastIndexOf("}")).trim();
			edgeData = edgeData.replace(" ", "").replace(")", ") ");
			String stringEdges[] = edgeData.split(" ");
			graphData.edgeList = new ArrayList<Edge>();
			for (int k = 0; k < stringEdges.length; k++) {
				Edge edgeObj = new Edge();
				String edge = stringEdges[k];
				String ed[] = edge.replace("(", "").replace(")", "").split(",");
				edgeObj.source = Integer.parseInt(ed[0]);
				edgeObj.destination = Integer.parseInt(ed[1]);
				graphData.edgeList.add(edgeObj);
			}

			// adding data to list
			listOfGraphData.add(graphData);
		}

		return listOfGraphData;
	}

	public static void main(String[] args) throws IOException {

		if (args.length > 0) {
			File file = new File(args[0]);
			int count=0;
			Scanner scanner = new Scanner(file);
			StringBuilder sb = new StringBuilder();
			while (scanner.hasNextLine()) {
				String k=scanner.nextLine();
				sb.append(k);
				if(k.contains("V = {")){
					count++;
				}
				
			}
			String fileData = sb.toString().trim();
			List<GraphData> listOfGraphData = getGraphData(fileData,count);
			TopologicalSort(listOfGraphData);

		} else {
			System.out.println("Please Prvoide file path as an argument");
		}
	}

	public static void TopologicalSort(List<GraphData> listOfGraphData) {

		for (int i = 0; i < listOfGraphData.size(); i++) {
			GraphData graphData = listOfGraphData.get(i);
			Graph graph = new Graph(graphData.nodes.size());

			for (int j = 0; j < graphData.edgeList.size(); j++) {
				graph.addEdge(graphData.edgeList.get(j).source, listOfGraphData.get(i).edgeList.get(j).destination);
			}

			System.out.print("G"+(i+1)+":  ");
			graph.topologicalSort();
		}
	}
}	

class GraphData {
	Stack<Integer> nodes;
	List<Edge> edgeList;
}

class Edge {
	int source;
	int destination;
}

class Graph {
	private int vertices;
	private ArrayList<ArrayList<Integer>> adjList;

	Graph(int vertices) {
		this.vertices = vertices;
		adjList = new ArrayList<ArrayList<Integer>>(vertices);
		for (int i = 0; i < vertices; ++i) {
			adjList.add(new ArrayList<Integer>());
		}
	}

	// add edge to vertices
	public void addEdge(int i, int j) {
		adjList.get(i).add(j);
	}

	public void topologicalSort() {

		int indegree[] = new int[vertices];
		for (int k = 0; k < adjList.size(); k++) {
			List<Integer> list = adjList.get(k);
			for (int j = 0; j < list.size(); j++) {
				indegree[list.get(j)]++;

			}
		}
		Queue<Integer> q = new LinkedList<Integer>();
		for (int m = 0; m < vertices; m++) {
			if (indegree[m] == 0)
				q.add(m);
		}
		int cnt = 0;

		Vector<Integer> topOrder=new Vector<Integer>();
		while (!q.isEmpty()) {

			int u = q.poll();
			topOrder.add(u);

			for (int node : adjList.get(u)) {
				// If in-degree becomes zero,
				// add it to queue
				if (--indegree[node] == 0)
					q.add(node);
			}
			cnt++;
		}
		
		boolean isFound = false;
		for (int m: topOrder) {
			isFound = true;
			System.out.print(m + " ");
		}
		// Check if there was a cycle
		if (cnt != vertices) {
			if(isFound) {
				System.out.println("-> no more in-degree 0 vertex; not an acyclic graph.");
			}else {
				System.out.println("No in-degree 0 vertex; not an acyclic graph.");
			}
			return;
		}

		// Print topological order

		System.out.println();
	}
}

