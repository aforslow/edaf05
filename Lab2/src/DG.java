import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DG {
	private HashMap<String, Set<Integer>> hashmap;
	private ArrayList<String> words;
	private ArrayList<String> sorted_keys;
	private Node<Integer> root; 
	private ArrayList<ArrayList<Node<Integer>>> L;
	
	DG(String inFile) {
		words = new ArrayList<String>();
		sorted_keys = new ArrayList<String>();
		hashmap = new HashMap<String, Set<Integer>>();
		BufferedReader reader = null;
		
		try {
			File file = new File(inFile);
			reader = new BufferedReader(new FileReader(file));
			String line;
			int idx = 0;
			
			while ((line = reader.readLine()) != null) {
				words.add(line);
				String key = sort_word(line.substring(1, 5));
				sorted_keys.add(key);
				insert_key(idx, key);
				idx++;
			}
			insert_matches();			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void insert_key(int idx, String key) {
		if (!hashmap.containsKey(key)) {
			Set<Integer> matchSet = new HashSet<Integer>();
			matchSet.add(idx);
			hashmap.put(key, matchSet);
		}
	}
	
	private void insert_matches() {
		int idx = 0;
		for (String s : words) {
			String sorted_word = sort_word(s);
			for (int i = 0; i < sorted_word.length(); i++) {
				StringBuilder sb = new StringBuilder(sorted_word);
				sb.deleteCharAt(i);
				String possible_key = sb.toString();
				if (hashmap.containsKey(possible_key)) {
					Set<Integer> matchSet = hashmap.get(possible_key);
					matchSet.add(idx);
				}
			}
			idx++;
		}
	}
	
	public void bfs_print(String inFile, String out) {
		BufferedReader reader = null;
		PrintWriter writer = null;
		
		try {
			File file = new File(inFile);
			reader = new BufferedReader(new FileReader(file));
			writer = new PrintWriter(out + ".out", "UTF-8");
			String line;
			
			while ((line = reader.readLine()) != null) {
				int idx = 0;
				String[] pair = line.split(" ");
				String key = pair[0];
				for (String s : words) {
					if (s.equals(key)) {
						int dist = bfs(idx, pair[1]);
						writer.println(dist);
						System.out.println(dist);
						break;
					}
					idx++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String sort_word(String word) {
		char[] char_key = word.toCharArray();
		Arrays.sort(char_key);
		String key = new String(char_key);
		return key;
	}
	
	
	
	public void print_path(String s, String t) {
		int idx = 0;
		for (String str : words) {
			if (str.equals(s)) {
				bfs(idx, t);
			}
			idx++;
		}
		//for (Node<Integer> n : root)
	}
	private int bfs(int s, String str) {
		boolean[] discovered = new boolean[words.size()];
		discovered[s] = true;
		L =	new ArrayList<ArrayList<Node<Integer>>>();
		ArrayList<Node<Integer>> iList = new ArrayList<Node<Integer>>();
		root = new Node<Integer>();
		root.data = s;
		iList.add(root);
		L.add(iList);
		int i = 0, dist = -1;
		
		while (!L.get(i).isEmpty()) {
			ArrayList<Node<Integer>> i1List = new ArrayList<Node<Integer>>();
			for (Node<Integer> u : L.get(i)) {
				if (words.get(u.data).equals(str)) { 
					dist = i; 
				}
				Set<Integer> edges = hashmap.get(sorted_keys.get(u.data));
				for (Integer v : edges) {
					if (!discovered[v]) {
						discovered[v] = true;
						Node<Integer> vNode = new Node<Integer>();
						vNode.data = v;
						//Not mandatory in this lab; bfs tree structure
						if (u.children == null) {
							u.children = new ArrayList<Node<Integer>>();
						}
						u.children.add(vNode);
						i1List.add(vNode);
					}
				}
			}
			L.add(i1List);
			i++;
		}
		return dist;
	}
	
	public void print_hashmap() {
		System.out.println(hashmap.toString());
		System.out.println();
		System.out.println(sorted_keys.toString());
		System.out.println(words.toString());
	}
	
	public class Node<T> {
		private T data;
		private ArrayList<Node<T>> children;
	}	
	
	public static void main(String[] args) {
		String inFile = "/Users/Andreas/downloads/EDAF05/words-";
		String[] appendix = new String[3];
		appendix[0] = "10";
		appendix[1] = "250";
		appendix[2] = "5757";
		for (String s : appendix) {
			DG dg = new DG(inFile + s + ".dat");
			dg.bfs_print(inFile + s + "-test.in", "Output" + s);
			System.out.println();
			
		}
	}
}
