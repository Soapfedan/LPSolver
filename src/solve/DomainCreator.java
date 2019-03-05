package solve;

import java.util.ArrayList;

import io.IOHandler;
import io.Line;

public class DomainCreator {

	private int companiesNumber;
	private int roundsNumber;
	private int numVariables;
	private int minTables;
	private IOHandler constraintsIO;
	private IOHandler outputIO;
	
	public DomainCreator(int companiesNumber, int roundsNumber, int minTables, IOHandler constraintsIO, IOHandler outputIO) {
		this.companiesNumber = companiesNumber;
		this.roundsNumber = roundsNumber;
		this.minTables = minTables;
		this.constraintsIO = constraintsIO;
		this.outputIO = outputIO;
		
		
		this.numVariables = this.companiesNumber * (this.companiesNumber-1)/2 * this.roundsNumber;
		
		ArrayList<Line> domainData = new ArrayList<Line>();
		
		domainData.add(new Line(String.valueOf(companiesNumber+";")));
		domainData.add(new Line(String.valueOf(roundsNumber+";")));
		domainData.add(new Line(String.valueOf(minTables)));
		domainData.add(new Line("",0));
		
		
		this.constraintsIO.writeContent(domainData);
		
		
	}
	
	
	public void writeConstraintList() {
				
	    ArrayList<Line> line = new ArrayList<Line>();
	    
	    /**
	     * INIZIO BLOCCO VINCOLI 1
	     */
	    
	    int[][] mappaturaVariabili = new int[companiesNumber][companiesNumber];
	    int[][] vincoloValori = new int [companiesNumber*roundsNumber][numVariables];
	    
	    int counter = 1;
	    
	    for (int t = 0; t < roundsNumber; t++) {
			
	    	for (int i = 0; i < companiesNumber; i++) {
	    		for (int j = 0; j < companiesNumber; j++) {
	    			if(i>j) {
	    				mappaturaVariabili[i][j] = counter;
	    				mappaturaVariabili[j][i] = counter;
	    				counter ++;
	    			}
	    		}
	    	}
	    	
	    	for (int i = 0; i < companiesNumber; i++) {
	    		//System.out.print(i+ " ");
	    		for (int j = 0; j < companiesNumber; j++) {
	    			if(mappaturaVariabili[i][j] != 0) {
	    				int el = mappaturaVariabili[i][j] - 1;
	    				//System.out.print(el + " ");
	    				vincoloValori[i+(t*companiesNumber)][el] = 1;
	    			}
				}
				//System.out.println("");    		
	    	}
		}
	    	
	    	
		
	    
	    for (int i = 0; i < companiesNumber*roundsNumber; i++) {
			for (int j = 0; j < numVariables; j++) {
				//System.out.print(vincoloValori[i][j]);
				line.add(new Line(String.valueOf(vincoloValori[i][j])+";"));
			}
			line.add(new Line("=;1",0));

			constraintsIO.appendContent(line);
			
			line.clear();
		}
	    	    
		
		/**
	     * FINE BLOCCO VINCOLI 1
	     */
		
		/**
	     * INIZIO BLOCCO VINCOLI 2
	     */
		
		for (int i = 0; i < companiesNumber*(companiesNumber-1)/2; i++) {
			//scrivo la riga			
			
			int k=i;
			
			for (int j = 0; j < numVariables; j++) {
				
				if(j == k) {
					
					line.add(new Line("1;"));					
					k += companiesNumber*(companiesNumber-1)/2;
					
				}else {
					
					line.add(new Line("0;"));
					
				}
				
			}
			
			line.add(new Line("<=;1",0));
			
			
			constraintsIO.appendContent(line);
			
			line.clear();
		}
		
		
		
		/**
	     * FINE BLOCCO VINCOLI 2
	     */
		
		/**
	     * INIZIO BLOCCO VINCOLI 3
	     */
		
		int[][] vincoloValori3 = new int [companiesNumber][numVariables];
	    
	    counter = 1;
	    
	    for (int t = 0; t < roundsNumber; t++) {
			
	    	for (int i = 0; i < companiesNumber; i++) {
	    		for (int j = 0; j < companiesNumber; j++) {
	    			if(i>j) {
	    				mappaturaVariabili[i][j] = counter;
	    				mappaturaVariabili[j][i] = counter;
	    				counter ++;
	    			}
	    		}
	    	}
	    	
	    	for (int i = 0; i < companiesNumber; i++) {
	    		//System.out.print(i+ " ");
	    		for (int j = 0; j < companiesNumber; j++) {
	    			if(mappaturaVariabili[i][j] != 0) {
	    				int el = mappaturaVariabili[i][j] - 1;
	    				vincoloValori3[i][el] = 1;
	    			}
				}	
	    	}
		}
	    
//	    for (int i = 0; i < companiesNumber; i++) {
//			for (int j = 0; j < numVariables; j++) {
//				System.out.print(vincoloValori3[i][j]+ " ");
//			}
//			System.out.println("");
//		}
//	    
	    	
		if(minTables > 0) {
			
			for (int i = 0; i < companiesNumber; i++) {
				for (int j = 0; j < numVariables; j++) {
					line.add(new Line(String.valueOf(vincoloValori3[i][j])+";"));
				}
				line.add(new Line(">=;"+minTables,0));
				
				constraintsIO.appendContent(line);
				
				line.clear();
			}
		}
	    
		
		/**
	     * FINE BLOCCO VINCOLI 3
	     */
		
		
	}
	
	
	public void writeObj() {
		
		
		int count = 0;
		 ArrayList<Line> line = new ArrayList<Line>();
		 
		 outputIO.writeContent(line);
		
		for (int i = 0; i < companiesNumber; i++) {
			//scrivo la riga											

			for (int j = 0; j < companiesNumber; j++) {
											
			   if(i != j) {
					
				   	//int num = (int) (Math.random() * 100);
				   int num;
				   if(count < 4 && i>j) {
					   num = 1;
					   count++;
				   }else {
					   num = 1;
				   }
				   
				   
					line.add(new Line(num+";"));					
				}else {
					
					line.add(new Line("0;"));
				}
		
			}
			
			line.add(new Line("",0));
		
			if(i == 0) {

				outputIO.writeContent(line);
			}else {
				
				outputIO.appendContent(line);
			}
			
			line.clear();
		}
		
	}
	
	
	
	
}
