package solve;

import java.io.File;
import java.util.ArrayList;

import io.IOHandler;
import io.Line;

public class Creator {

	private int companiesNumber;
	private int roundsNumber;
	private int numVariables;
	private int minTables;
	private IOHandler hand;
	
	public Creator(int companiesNumber, int roundsNumber, int minTables, String filePath) {
		
		
		this.companiesNumber = companiesNumber;
		this.roundsNumber = roundsNumber;
		this.minTables = minTables;
		
		this.numVariables = this.companiesNumber * (this.companiesNumber-1)/2 * this.roundsNumber;
		
		this.hand = new IOHandler(filePath);
		
	}
	
	public void createConstraint() {
		
		ArrayList<Line> line = new ArrayList<Line>();
		
//		for (int t = 0; t < roundsNumber; t++) {
//			for (int i = 0; i < companiesNumber; i++) {
//				for (int j = 0; j < companiesNumber && i>j; j++) {
//					
//					int absVar = this.absoluteVar(i, j, t);
//					
//					line.add(new Line("x"+String.valueOf(absVar)+ " + "));
//	
//				}
//	for (int j = 0; j < companiesNumber && i>j; j++) {
//					
//					int absVar = this.absoluteVar(i, j, t);
//					
//					line.add(new Line("x"+String.valueOf(absVar)+ " + "));
//	
//				}
//			
//			}
//		}
		
		 hand.writeContent(line);
		  
		  for (int i = 1; i <= this.companiesNumber; i++)
		    {
		        for (int t = 1; t <= this.roundsNumber ; t++)
		        {
		        	ArrayList<String> c = new ArrayList<String>();
//		            curr = [];
		    
		            for (int j1 = 1; j1 <= this.companiesNumber && i > j1; j1++)
		            {
		            	int absVar = this.absoluteVar(i, j1, t);
		            	c.add("x"+String.valueOf(absVar));
		            	//line.add(new Line("x"+String.valueOf(absVar)+ " + "));
		            	
		            	//curr.push(index_handler(i,j1,t));
		            }
		    
		            for (int j2 = i+1; j2 <= this.companiesNumber && j2 > i; j2++)
		            {
		            	int absVar = this.absoluteVar(i, j2, t);
		            	c.add("x"+String.valueOf(absVar));
		            	//line.add(new Line("x"+String.valueOf(absVar)+ " + "));
		            }
		            
		            String out = String.join(" + ", c);
		            
		            out = out + " = " + "1";
		            
		            line.add(new Line(out,0));
		            //assign1[`${i}_${t}`] = curr;
		            hand.appendContent(line);
		            line.clear();
		        }
		    }
		  
		    
		
		
		
		
	}
	

	
	
	private int absoluteVar(int i, int j,int t) {
		return ( (((i-1)*(i-1)+(i-1)) / 2) - (i-1-j) + ((this.companiesNumber*this.companiesNumber-this.companiesNumber) / 2) * (t-1) );
	}
	
	
	
}
