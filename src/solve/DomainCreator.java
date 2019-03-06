package solve;

import java.util.ArrayList;

import io.IOHandler;
import io.Line;

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
		 ArrayList<Line> line = new ArrayList<Line>();
		 
		 outputIO.writeContent(line);
		
		for (int i = 1; i <= companiesNumber; i++) {
			//scrivo la riga											

			for (int j = 1; j <= companiesNumber; j++) {
											
			   String l = "";
			   if(count < N_PREFERENZE && i>j) {
				   int el = j;
//				   if(i % 2 == 0) {
//					   
//					   el = j + this.companiesNumber/N_PREFERENZE*count;
//					   if(i <= el) {
//						   el = j;
//					   }
//				   }
				   
				   l = i+"_"+el+":"+20;
				   count++;
				   line.add(new Line(l,0));					
			   }

		
			}
			
			count = 0;
			outputIO.appendContent(line);			
			
			line.clear();
		}
		
	}
	
	
	
	
}
