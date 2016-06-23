import java.util.ArrayList;

public class MergeSort {

	MergeSort() {
		
	}
	
	private ArrayList<Node> regSort(ArrayList<Node> A, String str) {
		ArrayList<Node> A2 = new ArrayList<Node>(A);
		Node testNode = null;
		double min;
		int minidx;
		if (str == "x") {
			for (int i = 0; i < A2.size(); i++) {
				min = A2.get(i).x;
				minidx = i;
				for (int j = i+1; j < A2.size(); j++) {
					if (A2.get(j).x.compareTo(min) < 0) {
						minidx = j;
						min = A2.get(j).x;
					}
				}
				testNode = A2.get(i);
				A2.set(i, A2.get(minidx));
				A2.set(minidx, testNode);
			}
		} else if (str == "y"){
			for (int i = 0; i < A2.size(); i++) {
				min = A2.get(i).y;
				minidx = i;
				for (int j = i+1; j < A2.size(); j++) {
					if (A2.get(j).y.compareTo(min) < 0) {
						minidx = j;
						min = A2.get(j).y;
					}
				}
				testNode = A2.get(i);
				A2.set(i, A2.get(minidx));
				A2.set(minidx, testNode);
			}
		}
		return A2;
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
