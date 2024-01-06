import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;
import java.util.*;
public class topSort {
	// Representation of the Graph 
	static class Graph { 
		int V; // No. of vertices 
		List<Integer>[] adj; // Adjacency List 
		//Constructor 
		Graph(int V) 
		{ 
			this.V = V; 
			adj = new LinkedList[V]; 
			for (int i = 0; i < V; i++) 
				adj[i] = new LinkedList<>(); 
		} 
		//Method to add an edge in the graph

		void addEdge(int v, int w) 
		{ 
			adj[v].add(w); 
		} 

		// function to print the entire topological order of complete graph
		void topSort() 
		{ 
		
			// Create an integer array to store the indegrees for all vertices. Make initial initializations for all indegrees as 0. 
			int indeg[] = new int[V]; 

			// Explore adjacency lists to fill in the vertices' degrees. This action requires O(V+E) time.
			for (int i = 0; i < V; i++) { 
				for (int node : adj[i]) { 
					indeg[node]++; 
				} 
			} 
			// Create a queue and add all the vertices having indegree=0 to the queue

			Queue<Integer> q = new LinkedList<Integer>(); 
			for (int i = 0; i < V; i++) { 
				if (indeg[i] == 0) 
					q.add(i); 
			} 
			// Initially take count as 0
			int cnt = 0; 
			// Creating a vector for storing the result i.e for storing the topological order of the graph
			Vector<Integer> topOrder = new Vector<Integer>(); 
			while (!q.isEmpty()) { 
				// Remove the nodes from beginning of the queue and add it to the topological order
				int u = q.poll(); 
				topOrder.add(u); 
				// Perform iterations through all of its neighbouring nodes of the node on which dequeue is performed
                        // then Reduce the in-degree by 1
				for (int node : adj[u]) { 
					// If in-degree becomes zero, add it to queue 
					if (--indeg[node] == 0) 
						q.add(node); 
				} 
				cnt++; 
			} 

			//Check if there exists any cycle in the graph
			if (cnt != V) { 
				System.out.println("No in-degree 0 vertex; not an acyclic graph."); 
				return; 
			} 
            if(topOrder.isEmpty()){
                System.out.print("No more in-degree 0 vertex; not an acyclic graph.");
                return;   
            }
			// Print the topological order of the graph
			for (int i : topOrder) { 
				System.out.print(i + " "); 
			} 
		} 
	} 

    public static void main(String[] args) {
    	File inFile = null;
	if (0 < args.length) {
  	inFile = new File(args[0]);
	} else {
   	System.err.println("Invalid arguments count:" + args.length);
	}
        String line = "";
        String csvSplitBy = "\n";
	int c=0;
	int numberOfGraphs ;
	String graphNumber;
	Graph g;
	int verticesNumber ;
	
        try (BufferedReader br = new BufferedReader(new FileReader(inFile))) {
        	line = br.readLine();
        	String[] value = line.split(csvSplitBy);
            	String str1 = value[0];
            	String[] parts = str1.split(" ");
            	numberOfGraphs = Integer.parseInt(parts[0]);
            	//System.out.println(" No.of graphs = " +numberOfGraphs );
            	c++;
            	System.out.println("Topological Orders:");
            	
            	for(int i=0;i<numberOfGraphs;i++)
            	{
            		br.readLine();
            		line=br.readLine();
            		String[] values = line.split(csvSplitBy);
            		String str = values[0];
            		//System.out.println(str);
            		graphNumber=str.substring(3,str.indexOf(":"));
			String substr = str.substring(str.indexOf("{")+2,str.indexOf("}"));
			String[] count = substr.split(" ");    
			//finding the number of vertices
			verticesNumber = count.length;
			//Creating a new Graph 
			 g = new Graph(verticesNumber);
			 br.readLine();
			 while ((line = br.readLine()) != null  && !line.contains("-------"))
			 {
			 	String[] values1 = line.split(csvSplitBy);
            			String str2 = values1[0];
            			String substr2 = str2.substring(str2.indexOf("(")+1,str2.indexOf(")"));
  
	            		String[] S1 = substr2.split(","); 
        	    		int u = Integer.parseInt(S1[0].trim());
        	    		int v = Integer.parseInt(S1[1].trim());
				//Add edges to the graph;
				g.addEdge(u,v);
				
			 }
			 System.out.print(graphNumber+": ");
			 g.topSort();
			 System.out.print("\n");
            	}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

