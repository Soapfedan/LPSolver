package solve;

import java.util.ArrayList;
import io.IOHandler;


public class DomainCreator {

	private int companiesNumber;
	private int roundsNumber;
	private int numVariables;
	private int minTables;
	private IOHandler outputIO;
	
	public DomainCreator(int companiesNumber, int roundsNumber, int minTables, IOHandler outputIO) {
		this.companiesNumber = companiesNumber;
		this.roundsNumber = roundsNumber;
		this.minTables = minTables;

		this.outputIO = outputIO;
		
		
		this.numVariables = this.companiesNumber * (this.companiesNumber-1)/2 * this.roundsNumber;
		
		
		
	}
	
	
	public void writeObj(int N_PREFERENZE) {
		
		
		int count = 0;
		 ArrayList<String> line = new ArrayList<String>();
		 
		 outputIO.writeContent(line);
		
		for (int i = 1; i <= companiesNumber; i++) {										

			for (int j = 1; j <= companiesNumber; j++) {
											
			   String l = "";
			   if(count < N_PREFERENZE && i>j) {
				   int el = j;
				   
				   l = i+","+el+","+10;
				   count++;
				   line.add(l+"\n");					
			   }

		
			}
			
			count = 0;
			outputIO.appendContent(line);			
			
			line.clear();
		}
		
	}
	
	
	
	
}
