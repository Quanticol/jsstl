/**
 * 
 */
package eu.quanticol.jsstl2.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import eu.quanticol.jsstl2.SpatialModel;

/**
 * @author loreti
 *
 */
public class TraSpatialModelLoader implements SpatialModelLoader {
	
	private static final String LOCATION_TOLENS = "LOCATIONS";
	private int lineCounter = 0;
	private BufferedReader input;

	@Override
	public synchronized SpatialModel load(InputStream is) throws IOException {
		lineCounter = 0;
		input = new BufferedReader(new InputStreamReader(is));
		String line = nextLine();
		if (line == null) {
			throw new IOException("Missing location declaration!");
		}
		int locations = readLocations(line);
		SpatialModel sm = new SpatialModel(locations);		
		line = nextLine();
		while (line != null) {
			loadEdge(sm,line);
			line = nextLine();
		}
		sm.computeDistances();
		return sm;
	}

	private String nextLine() throws IOException {
		String line = "";
		do {
			line = input.readLine();
			lineCounter++;
		} while ((line != null)&&(line.trim().isEmpty())); 
		return line;
	}
	
	private void loadEdge(SpatialModel sm, String line) throws IOException {
		String[] tokens = line.split(" ");
		if (tokens.length == 3) {
			try {
				int src = Integer.parseInt(tokens[0]);
				int trg = Integer.parseInt(tokens[1]);
				double w = Double.parseDouble(tokens[2]);
				if ((src>=0)&&(src<sm.size())&&(trg>=0)&&(trg<sm.size())&&(w>=0.0)) {
					sm.set(src, trg, w);
					return ;
				}
			} catch (NumberFormatException e) {				
			}
		}
		throw new IOException("Syntax error at line "+lineCounter+"!\nExpected: \"<int> <int> <double>\"\nFound: "+line);	
	}

	private int readLocations(String line) throws IOException {
		String[] tokens = line.split(" ");
		if (tokens.length == 2) {
			if (tokens[0].startsWith(LOCATION_TOLENS)) {
				try {
					return Integer.parseInt(tokens[1]);
				} catch (NumberFormatException e) {
				}
			}
		}
		throw new IOException("Syntax error at line "+lineCounter+"!\nExpected: "+LOCATION_TOLENS+" <int>\nFound: "+line);	}

}
