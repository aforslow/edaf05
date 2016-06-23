import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MST {
	private ArrayList<String> path;
	private HashMap<String, Node<String>> hashmap;
	private ArrayList<Edge> edges;
	private int totalWeight;
	
	MST(String inFile) {
		File file = new File(inFile);
		hashmap = new HashMap<String, Node<String>>();
		edges = new ArrayList<Edge>();
		path = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			int i = 0;
			
			while ((line = reader.readLine()) != null) {
				String pattern1 = "^(\"?[^\"]*\"?)\\s$";
				Pattern r1 = Pattern.compile(pattern1);
				Matcher m1 = r1.matcher(line);
				
				String pattern2 = "(\"?[^\"]*\"?)";
				Pattern r2 = Pattern.compile(pattern2);
				Matcher m2 = r2.matcher(line);
				
				String pattern3 = "^" + pattern2 + "-{2}" + pattern2
						+ "\\s\\[(\\d*)\\]$";
				Pattern r3 = Pattern.compile(pattern3);
				Matcher m3 = r3.matcher(line);
				
				if (!m3.find()) {
					if (m1.find()) {
						String c = m1.group(1);
						Node<String> city = new Node<String>(c);
						hashmap.put(c, city);
						i++;
					}
				} else {
					String c1 = m3.group(1);
					String c2 = m3.group(2);
					int dist = Integer.parseInt(m3.group(3));
					Node<String> cityN1 = hashmap.get(c1);
					Node<String> cityN2 = hashmap.get(c2);
					Edge edge = new Edge(cityN1, cityN2, dist);
					if (edge != null) {
						edges.add(edge);
					}
				}
			}
			Collections.sort(edges);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sort_edges() {
		for (Edge e : edges) {
			if (!e.u.mother.contains(e.v)) {
				HashSet<Node<String>> vMother = e.v.mother;
				for (Node<String> n : vMother) {
					e.u.mother.add(n);
					n.mother = e.u.mother;
				}
				totalWeight += e.dist;
			}
		}
		for (Node<String> n : edges.get(0).u.mother) {
			path.add(n.city);
		}
	}
	
	public void print_path() {
		for (String s : path) {
			System.out.println(s);
		}
		System.out.println("Total distance: " + totalWeight);
	}
	
	public void print_edges() {
		for (Edge e : edges) {
			System.out.println(e.u.city + " -- "
					+ e.v.city + " "
					+ e.dist + " ");
		}
		System.out.println(edges.get(0).dist);
	}
	
	private class Node<T> {
		private T city;
		private HashSet<Node<T>> mother;
		
		Node(T city) {
			this.city = city;
			mother = new HashSet<Node<T>>();
			mother.add(this);
		}
	}
	
	public class Edge implements Comparable<Edge>{
		private Node<String> u;
		private Node<String> v;
		private Integer dist;
		
		Edge(Node<String> u, Node<String> v, Integer dist) {
			this.u = u;
			this.v = v;
			this.dist = dist;
		}
		
		@Override
		public int compareTo(Edge e) {
			return dist.compareTo(e.dist);
		}
	}
	
	public static void main(String[] args) {
		MST mst = new MST("/Users/Andreas/downloads/edaf05/Lab3"
				+ "/tinyEWG-alpha.txt");
//		mst.print_edges();
		mst.sort_edges();
		mst.print_path();
	}
}
