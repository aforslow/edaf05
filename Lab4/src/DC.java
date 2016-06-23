import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DC {
	private ArrayList<Node> nameList;
	private double closest_dist;
	private String name;
	private int dimension;
	
	DC(String inFile) {
		nameList = new ArrayList<Node>();
		File file = new File(inFile);
		String line;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			while ((line = reader.readLine()) != null) {
				String pattern1 = "^\\s*?([A-Za-z\\d]*)\\s+"
						+ "([-\\d\\.eE+]*)\\s+([-\\d\\.eE+]*)$";
				Pattern r1 = Pattern.compile(pattern1);
				Matcher m1 = r1.matcher(line);
				
				String pattern2 = "^NAME\\s*\\:\\s*([A-Za-z\\d]*)";
				Pattern r2 = Pattern.compile(pattern2);
				Matcher m2 = r2.matcher(line);
				
				String pattern3 = "^DIMENSION\\s*\\:\\s*(\\d*)";
				Pattern r3 = Pattern.compile(pattern3);
				Matcher m3 = r3.matcher(line);
				
				if (m1.find()) {
					String name = m1.group(1);
					BigDecimal xB = new BigDecimal(m1.group(2));
					BigDecimal yB = new BigDecimal(m1.group(3));
					Double x = xB.doubleValue();
					Double y = yB.doubleValue();
					Node n = new Node(name, x, y);
					nameList.add(n);
				} else if (m2.find()) {
					this.name = m2.group(1);
				} else if (m3.find()) {
					dimension = Integer.parseInt(m3.group(1));
				}
			}
			reader.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getName() {
		return name;
	}
	
	public int getDimension() {
		return dimension;
	}
	
	public double getClosestDist() {
		return closest_dist;
	}
	
	public void closestPair() {
		ArrayList<Node> A1 = new ArrayList<Node>(nameList);
		ArrayList<Node> A2 = new ArrayList<Node>(nameList);
		ArrayList<Node> px = mergeSort(A1, "x"); //construct sorting alg
		ArrayList<Node> py = mergeSort(A2, "y"); //construct sorting alg
		
		ArrayList<Node> closestPair = closestPairRec(px, py); //construct it
		closest_dist = getDist(closestPair.get(0), closestPair.get(1));
		System.out.println("Closest pair: " + closestPair.get(0).name 
				+ " " + closestPair.get(1).name + " " + closest_dist);
		
	}
	
	private ArrayList<Node> 
		closestPairRec(ArrayList<Node> px, ArrayList<Node> py) {
		ArrayList<Node> Qpair = new ArrayList<Node>();
		ArrayList<Node> Rpair = new ArrayList<Node>();
		ArrayList<Node> Spair = new ArrayList<Node>();
		ArrayList<Node> Qx = new ArrayList<Node>();
		ArrayList<Node> Rx = new ArrayList<Node>();
		ArrayList<Node> Qy = new ArrayList<Node>();
		ArrayList<Node> Ry = new ArrayList<Node>();
		ArrayList<Node> Sy = new ArrayList<Node>();
		
		if (px.size() <= 3) {
			return sortDist(px); //return closest pair in P
		}
		
		//Construct Qx, Qy, Rx, Ry
		for (int i = 0; i < px.size()/2; i++) {
			Qx.add(px.get(i));
		}
		for (int i = px.size()/2; i < px.size(); i++) {
			Rx.add(px.get(i));
		}
		double x = Qx.get(Qx.size()-1).x; //Rightmost x of Q (middle x)
		for (Node n : py) {
			if (n.x <= x) {
				Qy.add(n);
			} else {
				Ry.add(n);
			}
		}
		
		Qpair = closestPairRec(Qx, Qy);
		Rpair = closestPairRec(Rx, Ry);
		
		double delta = minPairDist(Qpair, Rpair);
		
		for (Node n : py) {
			if (Math.abs(x - n.x) <= delta) {
				Sy.add(n);
			}
		}
		
		//Get min pair in Sy
		if (Sy.size() >= 2) {
			Spair = sortDist(Sy);
			if (getDist(Spair.get(0), Spair.get(1)) < delta) {
				return Spair;
			} else if (getDist(Qpair.get(0), Qpair.get(1)) < 
					getDist(Rpair.get(0), Rpair.get(1))) {
				return Qpair;
			} else {
				return Rpair;
			}
		} else if (getDist(Qpair.get(0), Qpair.get(1)) < 
				getDist(Rpair.get(0), Rpair.get(1))) {
			return Qpair;
		} else {
			return Rpair;
		}
	}
	
	private ArrayList<Node> sortDist(ArrayList<Node> list) {
		ArrayList<Node> minPair = new ArrayList<Node>();
		double min = getDist(list.get(0), list.get(1));
		minPair.add(list.get(0));
		minPair.add(list.get(1));
		
		double dist;
		for (int i = 0; i < list.size(); i++) {
			for (int j = i+1; (j < list.size() && (j < 16)); j++) {
				dist = getDist(list.get(i), list.get(j));
				if (dist < min) {
					min = dist;
					minPair.clear();
					minPair.add(list.get(i));
					minPair.add(list.get(j));
				}
			}
		}
		return minPair;
	}
	
	private double getDist(Node n1, Node n2) {
		Double dx = n1.x - n2.x;
		Double dy = n1.y - n2.y;
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	private double minPairDist(ArrayList<Node> p1, ArrayList<Node> p2) {
		double d1 = getDist(p1.get(0), p1.get(1));
		double d2 = getDist(p2.get(0), p2.get(1));
		return (d1 < d2) ? d1 : d2;
	}
	
	private ArrayList<Node> mergeSort(ArrayList<Node> whole, String str) {
		ArrayList<Node> left = new ArrayList<Node>();
		ArrayList<Node> right = new ArrayList<Node>();
		int center;
		
		if (whole.size() == 1) {
			return whole;
		} else {
			center = whole.size() / 2;
			for (int i = 0; i < center; i++) {
				left.add(whole.get(i));
			}
			
			for (int i = center; i < whole.size(); i++) {
				right.add(whole.get(i));
			}
			
			left = mergeSort(left, str);
			right = mergeSort(right, str);
			
			merge(left, right, whole, str);
		}
		return whole;
	}
	
	private void merge(ArrayList<Node> left, ArrayList<Node> right, 
			ArrayList<Node> whole, String str) {
		int leftidx = 0;
		int rightidx = 0;
		int wholeidx = 0;
		
		while(leftidx < left.size() && rightidx < right.size()) {
			if (str == "x") {
				if (left.get(leftidx).x.
						compareTo(right.get(rightidx).x) < 0) {
					whole.set(wholeidx, left.get(leftidx));
					leftidx++;
				} else {
					whole.set(wholeidx, right.get(rightidx));
					rightidx++;
				}
			} else if (str == "y") {
				if ((left.get(leftidx)).y.
						compareTo(right.get(rightidx).y) < 0) {
					whole.set(wholeidx, left.get(leftidx));
					leftidx++;
				} else {
					whole.set(wholeidx, right.get(rightidx));
					rightidx++;
				}
			}
			wholeidx++;
		}
		
		ArrayList<Node> rest;
		int restidx;
		if (leftidx >= left.size()) {
			rest = right;
			restidx = rightidx;
		} else {
			rest = left;
			restidx = leftidx;
		}
		
		for (int i = restidx; i < rest.size(); i++) {
			whole.set(wholeidx, rest.get(i));
			wholeidx++;
		}
	}
	
	public void printList() {
		for (Node n : nameList) {
			System.out.println(n.name + " " + n.x + " "
					+ n.y);
		}
	}
	
	private	class Node{
		private Double x;
		private Double y;
		private String name;
		
		Node(String name, Double x, Double y){
			this.name = name;
			this.x = x;
			this.y = y;
		}
	}
	
	public static void main(String[] args) {
		String inFile = "/Users/Andreas/downloads/lab4 2" 
				+ "/burma14.tsp";
		DC dc = new DC(inFile);
//		dc.printList();
		dc.closestPair();
	}
}
