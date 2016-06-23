import java.util.ArrayList;

public class ClosestPair {
	private ArrayList<Node> nameList;
	private double closest_dist;
	
	ClosestPair(ArrayList<Node> nameList) {
		this.nameList = nameList;
	}
	
	public double getClosestDist() {
		return closest_dist;
	}
	
	public void closestPair() {
		ArrayList<Node> px = new ArrayList<Node>(nameList);
		ArrayList<Node> py = new ArrayList<Node>(nameList);
		px = mergeSort(px, "x");
		py = mergeSort(py, "y");
		
		ArrayList<Node> closestPair = closestPairRec(px, py); //construct it
		closest_dist = getDist(closestPair.get(0), closestPair.get(1));
		System.out.println("Closest pair: " + closestPair.get(0).name 
				+ " " + closestPair.get(1).name + " " + closest_dist);
	}
	
	private ArrayList<Node> 
		closestPairRec(ArrayList<Node> px, ArrayList<Node> py) {
		ArrayList<Node> Qpair 	= new ArrayList<Node>();
		ArrayList<Node> Rpair 	= new ArrayList<Node>();
		ArrayList<Node> Spair 	= new ArrayList<Node>();
		ArrayList<Node> Qx 		= new ArrayList<Node>();
		ArrayList<Node> Rx 		= new ArrayList<Node>();
		ArrayList<Node> Qy 		= new ArrayList<Node>();
		ArrayList<Node> Ry 		= new ArrayList<Node>();
		ArrayList<Node> Sy 		= new ArrayList<Node>();
		
		if (px.size() <= 3) {
			return minPair(px); //return closest pair in P
		}
		
		//Construct Qx, Qy, Rx, Ry
		for (int i = 0; i < px.size()/2; i++) {
			Qx.add(px.get(i));
		}
		for (int i = px.size()/2; i < px.size(); i++) {
			Rx.add(px.get(i));
		}
		double x = Qx.get(Qx.size()-1).x; // x = x*
		for (Node n : py) {
			if (n.x <= x) {
				Qy.add(n);
			} else {
				Ry.add(n);
			}
		}
		
		Qpair = closestPairRec(Qx, Qy);
		Rpair = closestPairRec(Rx, Ry);
		
		double delta = min(Qpair, Rpair); //min distance of pairs
		
		for (Node n : py) {
			if (Math.abs(x - n.x) <= delta) {
				Sy.add(n);
			}
		}
		
		if (Sy.size() >= 2) {
			Spair = minPair(Sy);
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
	
	/*Return closest pair in list*/
	private ArrayList<Node> minPair(ArrayList<Node> list) {
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
	
	/*Get distance between two nodes*/
	private double getDist(Node n1, Node n2) {
		Double dx = n1.x - n2.x;
		Double dy = n1.y - n2.y;
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	/*Get min distance between two pairs of nodes*/
	private double min(ArrayList<Node> p1, ArrayList<Node> p2) {
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
				if ((left.get(leftidx)).x.
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
	}
	
	private class Node {
		private Double x;
		private Double y;
		private String name;
		
		Node(String name, Double x, Double y) {
			this.name = name;
			this.x = x;
			this.y = y;
		}
	}
	
}
