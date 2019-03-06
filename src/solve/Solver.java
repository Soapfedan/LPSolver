package solve;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import io.IOHandler;
import io.Line;
import lpsolve.LpSolve;
import lpsolve.LpSolveException;
import main.Utils;

public class Solver {
	

	private IOHandler objIO;
	private IOHandler constraintsIO;
	private IOHandler outputIO;
	private ArrayList<Constraint> constraints;
	private int[][] objCoefficient;
	private int companiesNumber;
	private int roundsNumber;
	private int minIncontri;
	private double objRes;
	private double timeElapsed;
	private int solverResult;
	
	
	public Solver(int companiesNumber, int roundsNumber, int minIncontri,String objCoefficientsFilePath) {
		
		this.objIO = new IOHandler(objCoefficientsFilePath);
		this.companiesNumber = companiesNumber;
		this.roundsNumber = roundsNumber;
		this.minIncontri = minIncontri;
	}
	
	private void initializePrefsMatr() {
		
		this.objCoefficient = new int[companiesNumber][companiesNumber];
		
		for (int i = 0; i < companiesNumber; i++) {
			for (int j = 0; j < companiesNumber; j++) {
				if(i != j) {
					this.objCoefficient[i][j] = 1;
				}
			}
		}
	}
	


	private void loadObjectiveCoefficient() {

		
		
		if(this.companiesNumber > 1) {
		
			
		//Devo inizializzare tutta la matrice con tutti 1 tranne che sulla diagonale
		this.initializePrefsMatr();
		
		
		
		ArrayList<String[]> objs = this.objIO.readFile(" ");
			
			for (int i = 0; i < objs.size(); i++) {
				
				String[] line = objs.get(i);
				
				for (int j = 0; j < line.length; j++) {
					
					String[] a1 = new String[2];
					String[] a2 = new String[2];
					
					//Con a1 estraggo la coppia degli indici e il valore di preferenza es: 4_2:10 => ["4_2","10"]
					a1 = line[j].split(":");

					//Con a2 estraggo i due indici es: 4_2 => ["4","2"]
					a2 = a1[0].split("_");
					
					int iIndex = Integer.parseInt(a2[0]);
					int jIndex = Integer.parseInt(a2[1]);
					int pref = Integer.parseInt(a1[1]);
					
					this.objCoefficient[iIndex-1][jIndex-1] = pref;
					
					
				}
			}
		}
		
		for (int i = 0; i < companiesNumber; i++) {
			for (int j = 0; j < companiesNumber; j++) {
					System.out.print(this.objCoefficient[i][j]+ " ");
			}
			System.out.println("");
		}
		
	}
	

	public double[] solve() {
		
		 try {
			 
				int numSolution = (this.companiesNumber * (this.companiesNumber-1))/2 * this.roundsNumber;

				LpSolve solv = LpSolve.makeLp(0, numSolution);
				
				//Dico che è un problema di massimo
				
				solv.setMaxim();
				
			   //setto la funzione obiettivo
			   
				solv.setAddRowmode(true);
				
				//Carico i coefficienti in memoria
				this.loadObjectiveCoefficient();

				//Mi estraggo tutti i parametri
			   solv.strSetObjFn(this.calcObjFunNumbers());

			 
			  // add constraints
			   
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
					//scrivo la riga			
					
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
//		            System.out.println(Arrays.toString(colno));		       
		            
		            solv.addConstraintex(c.size(),sparserow, colno,LpSolve.LE,1.0);
					
				}
			     
			   
			  
			  solv.setAddRowmode(false);
			  
			  //set binary variables
			  
			  for (int i = 1; i <= numSolution; i++) {
				solv.setBinary(i, true);
			  }

			  //solv.setBreakAtFirst(true);
			  
			  // solve the problem

			  solv.solve();
			  
			  System.out.println(solv.getObjective());
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

	public ArrayList<Constraint> getConstraints() {
		return constraints;
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
	
	

	
	
	
	
	
	
	
}
