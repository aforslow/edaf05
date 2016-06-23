import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileTester {
	private ArrayList<String> fileList;
	private String fileStart;
	
	FileTester() {
		fileStart = "/Users/Andreas/downloads/lab4 2";
		String inFile = fileStart + "/closest-pair.out";
		fileList = new ArrayList<String>();
		
		try {
			File file = new File(inFile);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			
			while ((line = reader.readLine()) != null) {
				String pattern = "^\\.\\.\\/data([\\/A-Za-z\\.\\d]*)\\:";
				Pattern r1 = Pattern.compile(pattern);
				Matcher m1 = r1.matcher(line);
				
				if (m1.find()) {
					fileList.add(m1.group(1));
				}
			}
			
			reader.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void printTest() {
		try {
			PrintWriter writer = 
					new PrintWriter(fileStart + "/closest-pairs-1.out", 
							"UTF-8");
			for (String s : fileList) {
				DC dc = new DC(fileStart + s);
				dc.closestPair();
				double closest_dist = dc.getClosestDist();
				int dimension = dc.getDimension();
				writer.println(".." + s + ": " + dimension + " "
						+ closest_dist);
			}
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		FileTester ft = new FileTester();
		ft.printTest();
	}
}
