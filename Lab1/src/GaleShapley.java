import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GaleShapley {

	private int[][] rankList;
	private int num_persons;
	private int[] proposal;
	private boolean[] taken;
	private String[] nameList;
	ArrayList<pair<Integer>> matchList;
	
	GaleShapley(String inFile) {		
		create_data(inFile);
		proposal = new int[num_persons];
		taken = new boolean[num_persons];		
	}
	
	private void create_data(String inFile) {
		String line;
		try {
			FileInputStream fileReader = new FileInputStream(inFile);
			InputStreamReader isr = new InputStreamReader(fileReader);
			BufferedReader bufferedReader = 
	                new BufferedReader(isr);
			while ((line = bufferedReader.readLine()) != null) {
				String pattern0 = "#.*";
				Pattern r0 = Pattern.compile(pattern0);
				Matcher m0 = r0.matcher(line);
				
				String pattern1 = "n=(\\d*)";
				Pattern r1 = Pattern.compile(pattern1);
				Matcher m1 = r1.matcher(line);
				
				String pattern2 = "^([0-9]*)\\s(.*)";
				Pattern r2 = Pattern.compile(pattern2);
				Matcher m2 = r2.matcher(line);
				
				String pattern3 = "^([0-9]*)\\:\\s(.*)";
				Pattern r3 = Pattern.compile(pattern3);
				Matcher m3 = r3.matcher(line);
				
				if (m0.find()) {
					//Do nothing
				} else if (m1.find()) {
					num_persons = Integer.parseInt(m1.group(1))*2;
					nameList = new String[num_persons];
					rankList = new int[num_persons][num_persons/2];
				} else if (m2.find()) {
					int idx = Integer.parseInt(m2.group(1)) - 1;
					nameList[idx] = m2.group(2);
				} else if (m3.find()) {
					int idx = Integer.parseInt(m3.group(1)) - 1;
					String[] prefIdx = m3.group(2).split(" ");
					for (int i = 0; i < prefIdx.length; i++) {						
						rankList[idx][i] = Integer.parseInt(prefIdx[i]) - 1;
					}
				} 
			}
			bufferedReader.close();
		}
		catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + inFile + "'");
		}
		catch(IOException ex) {
            System.out.println("Error reading file '" + inFile + "'");
        }
	}
	
	private boolean prefer_m(int w, int m, int pm) {
		for (int i = 0; i < num_persons/2; i++) {
			if (rankList[w][i] == m) return true;
			if (rankList[w][i] == pm) return false;
		}
		return false;
	}
	
	public void match() {
		matchList = new ArrayList<pair<Integer>>();
		int m;
		PriorityQueue<Integer> prioQueue = new PriorityQueue<Integer>();
		for (int i = 0; i < num_persons; i += 2) { prioQueue.add(i); }
		boolean breakpnt = false;
		
		while (!prioQueue.isEmpty()) {
			m = prioQueue.poll();
			if (proposal[m] < num_persons/2) {
				int w = rankList[m][proposal[m]];
				if (!taken[w]) {
					matchList.add(new pair<Integer>(m, w));
					taken[w] = true;
				} else {
					pair<Integer> poi = null; //pair of interest
					for (pair<Integer> p : matchList) {
						if (p.female == w) { 
							poi = p; 
							break;
						}
					}
					if (poi.male == 874 || breakpnt) {
						breakpnt = true;
						System.out.println("bananpotatis");
					}
					int pm = poi.male;
					if (prefer_m(w, m, pm)) {
						prioQueue.add(pm);
						proposal[pm]++;
						poi.male = m;						
					} else {
						proposal[m]++;
						prioQueue.add(m);
					}
				}
			}
		}
	}

	public void print(String out) {
		Collections.sort(matchList);
		PrintWriter writer = null;
		
		try {
			writer = new PrintWriter(out + ".out", "UTF-8");
		
			for (pair<Integer> p : matchList) {
				String male = nameList[p.male];
				String female = nameList[p.female];
				writer.println(male + " -- " + female);
				System.out.println(male + " -- " + female);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			writer.close();
		}
//		System.out.println();
//		for (int i = 0; i < proposal.length; i++) {
//			System.out.println(i + ". " + proposal[i]);
//		}
	}
	
	private class pair<T> implements Comparable<pair<T>>{
		private T male;
		private T female;
		
		pair(T male, T female) {
			this.male = male;
			this.female = female;
		}
		
		@Override
		public int compareTo(pair<T> p) {
			return ((Integer) male).compareTo((Integer) p.male);
		}
	}
	
	public static void main(String[] args) {
//		GaleShapley g = new GaleShapley(
//				"/Users/Andreas/downloads/EDAF05/sm-illiad.in");
		GaleShapley g = new GaleShapley(
				"/Users/Andreas/downloads/EDAF05/sm-worst-500.in");
//		GaleShapley g = new GaleShapley(
//				"/Users/Andreas/downloads/EDAF05/sm-random-50.in");
//		GaleShapley g = new GaleShapley(
//				"/Users/Andreas/downloads/EDAF05/sm-friends.txt");
		String filepath = "/Users/Andreas/downloads/EDAF05/sm-worst-500";
		g.match();
		g.print(filepath);
//		try {
//			GaleShapley g = new GaleShapley(args[0]);
//			g.match();
//			g.print();
//		} catch(ArrayIndexOutOfBoundsException e) {
//			System.out.println("No input args");
//		}
	}
}



