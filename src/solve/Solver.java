package solve;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import lpsolve.LpSolve;
import lpsolve.LpSolveException;
import main.Utils;

public class Solver {
	

	private int[][] objCoefficient;
	private int companiesNumber;
	private int roundsNumber;
	private int minIncontri;
	private double objRes;
	private double timeElapsed;
	private int solverResult;
	
	
	public Solver(int companiesNumber, int roundsNumber, int minIncontri) {
		
		this.companiesNumber = companiesNumber;
		this.roundsNumber = roundsNumber;
		this.minIncontri = minIncontri;
		
	}
	
	
	

	public double[] solve() {
		
		 try {
			 
				int numSolution = (this.companiesNumber * (this.companiesNumber-1))/2 * this.roundsNumber;

				LpSolve solv = LpSolve.makeLp(0, numSolution);
				
				solv.setMaxim();				
			   
				solv.setAddRowmode(true);

			   solv.strSetObjFn(this.calcObjFunNumbers());

			   
				/**
				 * BLOCCO VINCOLO 1
				 */
			   for (int i = 1; i <= this.companiesNumber; i++)
			    {
			        for (int t = 1; t <= this.roundsNumber ; t++)
			        {
			        	ArrayList<Integer> c = new ArrayList<Integer>();

			    
			            for (int j1 = 1; j1 <= this.companiesNumber && i > j1; j1++)
			            {
			            	int absVar = Utils.getAbsoluteVar(i, j1, t,this.companiesNumber);
			            	c.add(absVar);

			            }
			    
			            for (int j2 = i+1; j2 <= this.companiesNumber && j2 > i; j2++)
			            {
			            	int absVar = Utils.getAbsoluteVar(j2, i, t,this.companiesNumber);
			            	c.add(absVar);
			            	
			            }
			            
			            int[] colno = new int[c.size()];
			            double[] sparserow = new double[c.size()];
			            
			            for (int j = 0; j < c.size(); j++) {
			            	colno[j] = c.get(j);
			            	sparserow[j] = 1.0;
						}

			            
			            solv.addConstraintex(c.size(),sparserow, colno,LpSolve.EQ,1.0);

			           
			          
			        }
			    }
			   
			   /**
			    * BLOCCO VINCOLI 2
			    */
			   
			   
				for (int i = 1; i <= companiesNumber*(companiesNumber-1)/2; i++) {	
					
					int k=i;
					
					ArrayList<Integer> c = new ArrayList<Integer>();
					
					for (int j = 1; j <= roundsNumber; j++) {
						
						
						c.add(k);
						k += companiesNumber*(companiesNumber-1)/2;

						
					}
					
		            int[] colno = new int[c.size()];
		            double[] sparserow = new double[c.size()];
		            
		            for (int j = 0; j < c.size(); j++) {
		            	colno[j] = c.get(j);
		            	sparserow[j] = 1.0;
					}	       
		            
		            solv.addConstraintex(c.size(),sparserow, colno,LpSolve.LE,1.0);
					
				}
				
				/**
				 * BLOCCO VINCOLI 3
				 */
			     
				for (int i = 1; i <= this.companiesNumber; i++)
			    {
					ArrayList<Integer> c = new ArrayList<Integer>();
					
					int nIncontri = 0;
					
			        for (int t = 1; t <= this.roundsNumber  ; t++)
			        {

			    
			            for (int j1 = 1; j1 <= this.companiesNumber && i > j1; j1++)
			            {
			            	int absVar = Utils.getAbsoluteVar(i, j1, t,this.companiesNumber);
		            		c.add(absVar);

			            }
			    
			            for (int j2 = i+1; j2 <= this.companiesNumber && j2 > i; j2++)
			            {
			            	int absVar = Utils.getAbsoluteVar(j2, i, t,this.companiesNumber);
			            	c.add(absVar);
			            	
			            }	             
			          
			        }
			        
			        int[] colno = new int[c.size()];
		            double[] sparserow = new double[c.size()];
		            
		            for (int j = 0; j < c.size(); j++) {
		            	colno[j] = c.get(j);
		            	sparserow[j] = 1.0;
					}	       
		            
		            solv.addConstraintex(c.size(),sparserow, colno,LpSolve.GE,this.minIncontri);
			        
			       
			    }
			   
			  
			  solv.setAddRowmode(false);
			  
			  
			  for (int i = 1; i <= numSolution; i++) {
				solv.setBinary(i, true);
			  }

			  solv.solve();
			  
			  System.out.println("Valore Funzione obiettivo "+solv.getObjective());
			  double[] var = solv.getPtrVariables();
			  
			  for (int i = 0; i < var.length; i++) {
				  int j = i+1;
				  System.out.println(("x[" + j + "] = " + var[i]));
			  }
			  
			  
		
			  
			  // delete the problem and free memory
			  solv.deleteLp();
				  
			  return var;
			  
			} catch (LpSolveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return null;
		
		 
	}
	
	
	private String calcObjFunNumbers() {
		
		String ret = "";
		
		for (int i = 0; i < companiesNumber; i++) {
					
			for (int j = 0; j < companiesNumber; j++) {
				if(i>j) {
					int sum = this.objCoefficient[i][j]*2;
					
					ret += sum + " ";
				}
			
			}
		}
		
		for (int i = 1; i < this.roundsNumber; i++) {
			ret += ret;
		}
		
		
		return ret;
	}


	public int[][] getObjCoefficient() {
		return objCoefficient;
	}

	public int getCompaniesNumber() {
		return companiesNumber;
	}

	public int getRoundsNumber() {
		return roundsNumber;
	}

	public double getObjRes() {
		return objRes;
	}
	
	public double getTimeElapsed() {
		return timeElapsed;
	}

	public int getSolverResult() {
		return solverResult;
	}

	public void setObjCoefficient(int[][] objCoefficient) {
		this.objCoefficient = objCoefficient;
	}	
	
	
	
	

	
	
	
	
	
	
	
}
