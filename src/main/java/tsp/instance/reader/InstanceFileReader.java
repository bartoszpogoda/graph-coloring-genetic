package tsp.instance.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tsp.instance.AbstractInstance;
import tsp.instance.AdjacencyMatrixInstance;
import tsp.instance.EdgeListInstance;

/***
 * Reader for instances from https://mat.tepper.cmu.edu/COLOR/instances.html#XXDSJ
 * 
 * @author Student225988
 */
public class InstanceFileReader {
	
	private Pattern instanceInfoPattern = Pattern.compile("p edge (.*?) (.*?)");
	private Pattern edgeInfoPattern = Pattern.compile("e (.*?) (.*?)");
   

	public AbstractInstance read(String fileName) throws IOException {
		File instnaceFile = new File(fileName);

		return convert(fileName, instnaceFile);
	}

	private AbstractInstance convert(String instanceName, File instnaceFile) throws IOException {
		try(BufferedReader bufferedReader = new BufferedReader(new FileReader(instnaceFile))) {
			AbstractInstance instance = new EdgeListInstance(5);
			
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
	            Matcher matcher = null;
	            if((matcher = instanceInfoPattern.matcher(line)).matches()) {
	            	instance = new AdjacencyMatrixInstance(toInt(matcher.group(1)));
	            } else if((matcher = edgeInfoPattern.matcher(line)).matches()) {
	            	instance.setConnected(toInt(matcher.group(1)) - 1, toInt(matcher.group(2)) - 1, true);
	            }
	            
	        }
			
			return instance;
		}
	}

	private int toInt(String str) {
		return Integer.valueOf(str);
	}
	
}
