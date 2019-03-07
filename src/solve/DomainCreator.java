package solve;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import main.Main;


public class DomainCreator {

	private int companiesNumber;
	private int roundsNumber;
	private int numVariables;
	private int minTables;
	private String prefFilePath;
	
	public DomainCreator(int companiesNumber, int roundsNumber, int minTables, String outputIO) throws IOException {
		this.companiesNumber = companiesNumber;
		this.roundsNumber = roundsNumber;
		this.minTables = minTables;
		this.prefFilePath = outputIO;

		File file = new File(this.prefFilePath);
		file.createNewFile();

		this.numVariables = this.companiesNumber * (this.companiesNumber-1)/2 * this.roundsNumber;
		
		
		
	}
	
	
	public void writeObj(int N_PREFERENZE) throws IOException {
		
		
		int count = 0;
		 ArrayList<String> line = new ArrayList<String>();
		 
		 FileWriter wr = new FileWriter(this.prefFilePath,false);
		 wr.write("");
		 wr.close();
		
		for (int i = 1; i <= companiesNumber; i++) {										

			for (int j = 1; j <= companiesNumber; j++) {
											
			   String l = "";
			   if(count < N_PREFERENZE && i>j) {
				   int el = (int) (Math.random() * companiesNumber % i);
				   if(el == 0) el = j;
				   l = i+","+el+","+10;
				   count++;
				   line.add(l+"\n");					
			   }
		
			}
			count = 0;
			
			wr = new FileWriter(this.prefFilePath,true);
			for (String l: line) {
				wr.write(l);
			}
			wr.close();
				
				
			
			line.clear();
		}
		
	}
	
	
	
	
}
