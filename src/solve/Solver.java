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
	

//	public Solver(String constraintsFilePath, String objCoefficientsFilePath, String outputFilePath) {
//		
//		this.objIO = new IOHandler(objCoefficientsFilePath);
//		this.constraintsIO = new IOHandler(constraintsFilePath);
//		this.outputIO = new IOHandler(outputFilePath);
//		
//	}
//	
	
	public Solver(int companiesNumber, int roundsNumber, int minIncontri,String objCoefficientsFilePath) {
		
		this.objIO = new IOHandler(objCoefficientsFilePath);
		this.companiesNumber = companiesNumber;
		this.roundsNumber = roundsNumber;
		this.minIncontri = minIncontri;
	}
	
	
//	public void loadConstraints() {
//				
//		try {			
//			
//			ArrayList<String[]> c = this.constraintsIO.readFile();
//			
//			if(c.size() > 1) {
//				
//				String[] domainData = c.remove(0);
//								
//				
//				if(domainData.length == 3) {
//					this.companiesNumber = Integer.parseInt(domainData[0]);
//					this.roundsNumber = Integer.parseInt(domainData[1]);
//					this.minIncontri = Integer.parseInt(domainData[2]);
//				}
//				
//				this.constraints = new ArrayList<Constraint>();
//				
//				for (String[] constraintLine: c) {
//					
//					this.constraints.add(new Constraint(constraintLine));
//					
//				}
//			}
//			
//			
//		} catch (Exception e) {
//			
//			e.printStackTrace();
//		}
//
//		
//	}
	
	public void loadConstraints(String filePath) {

        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";
        ArrayList<String[]> ret = new ArrayList<String[]>();

        this.constraints = new ArrayList<Constraint>();

        try {
        
              br = new BufferedReader(new FileReader(filePath));
              
              Integer count = 0;
            
              while ((line = br.readLine()) != null) {

                  String[] tokens = line.split(cvsSplitBy);    

                  if (count == 0) {
                      count++;
                    this.companiesNumber = Integer.parseInt(tokens[0]);
                    this.roundsNumber = Integer.parseInt(tokens[1]);
                    this.minIncontri = Integer.parseInt(tokens[2]);
                  }else {
                	  
                	  this.constraints.add(new Constraint(tokens));
                  }

                
                  
        
              }
        
          } catch (FileNotFoundException e) {
              e.printStackTrace();
          } catch (IOException e) {
              e.printStackTrace();
          } finally {
              if (br != null) {
                  try {
                      br.close();
                      
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              }
                        
          }
	}
	
	


	public void loadObjectiveCoefficient() {
		
		ArrayList<String[]> objs = this.objIO.readFile();
		
		
		if(this.companiesNumber > 1) {
			this.objCoefficient = new int[companiesNumber][companiesNumber];
			
			for (int i = 0; i < objs.size(); i++) {
				
				String[] line = objs.get(i);
				
				for (int j = 0; j < line.length; j++) {
					
					this.objCoefficient[i][j] = Integer.parseInt(line[j]);
				}
			}
		}
		
	}
	

	public void solve() {
		
		 try {
			 
				int numSolution = (this.companiesNumber * (this.companiesNumber-1))/2 * this.roundsNumber;

				LpSolve solv = LpSolve.makeLp(0, numSolution);
				
				//Dico che è un problema di massimo
				
				solv.setMaxim();
				
			   //setto la funzione obiettivo
			   
				solv.setAddRowmode(true);

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
//			            curr = [];
			    
			            for (int j1 = 1; j1 <= this.companiesNumber && i > j1; j1++)
			            {
			            	int absVar = this.absoluteVar(i, j1, t);
			            	c.add(absVar);
	
			            	//line.add(new Line("x"+String.valueOf(absVar)+ " + "));
			            	
			            	//curr.push(index_handler(i,j1,t));
			            }
			    
			            for (int j2 = i+1; j2 <= this.companiesNumber && j2 > i; j2++)
			            {
			            	int absVar = this.absoluteVar(j2, i, t);
			            	c.add(absVar);
			            	
			            	//line.add(new Line("x"+String.valueOf(absVar)+ " + "));
			            }
			            
			            int[] colno = new int[c.size()];
			            double[] sparserow = new double[c.size()];
			            
			            for (int j = 0; j < c.size(); j++) {
			            	colno[j] = c.get(j);
			            	sparserow[j] = 1.0;
						}
			            System.out.println(Arrays.toString(colno));
			            
			            solv.addConstraintex(c.size(),sparserow, colno,LpSolve.EQ,1.0);
			            
//			            String out = String.join(" + ", c);
//			            
//			            out = out + " = " + "1";
			            
			            
			           
			          
			        }
			    }
			   
			   /**
			    * BLOCCO VINCOLI 2
			    */
			   
//			   for (int i = 0; i < this.companiesNumber; i++) {
//				   for (int j = 0; j < this.roundsNumber; j++) {
//					   ArrayList<Integer> c = new ArrayList<Integer>();
//					   c.add(e)
//				   }
//			   }
			   
				for (int i = 1; i <= companiesNumber*(companiesNumber-1)/2; i++) {
					//scrivo la riga			
					
					int k=i;
					
					ArrayList<Integer> c = new ArrayList<Integer>();
					
					for (int j = 1; j <= companiesNumber*(companiesNumber-1)/2*roundsNumber; j++) {
						
						if(j == k) {
																		
							c.add(k);
							k += companiesNumber*(companiesNumber-1)/2;
							
						}
						
					}
					
		            int[] colno = new int[c.size()];
		            double[] sparserow = new double[c.size()];
		            
		            for (int j = 0; j < c.size(); j++) {
		            	colno[j] = c.get(j);
		            	sparserow[j] = 1.0;
					}
		            
		            System.out.println(Arrays.toString(colno));
		            
		            solv.addConstraintex(c.size(),sparserow, colno,LpSolve.LE,1.0);
					
				}
			     
			   
			  
//			  for(Constraint c : this.constraints) {
//				  solv.strAddConstraint(c.getCoefficients(), c.getCompareSign(), c.getConstTerm());
//
//			  }
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
				  
			  
			  
			} catch (LpSolveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	
	private String calcObjFunNumbers() {
		
		String ret = "";
		
		for (int i = 0; i < companiesNumber; i++) {
					
			for (int j = 0; j < companiesNumber; j++) {
				if(i>j) {
					int sum = this.objCoefficient[i][j]*this.objCoefficient[j][i];
					
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
	
	
	
	public void solvePrint(){
		
		 ArrayList<Line> solution = new ArrayList<Line>();
		 
		 solution.add(new Line(""));
		 
		 this.outputIO.writeContent(solution);
		
		 try {
			 
			int numSolution = (this.companiesNumber * (this.companiesNumber-1))/2 * this.roundsNumber;
			//System.out.println(numSolution);
			LpSolve solv = LpSolve.makeLp(0, numSolution);
			
			//Dico che è un problema di massimo
			
			solv.setMaxim();
			
			//Mostro i dati del dominio applicativo
			 
			 solution.add(new Line("Dati Dominio",0));
			 solution.add(new Line("",0));
			 solution.add(new Line("N.Imprese = "+this.companiesNumber,0));
			 solution.add(new Line("N.Round = "+this.roundsNumber,0));
			 solution.add(new Line("N.Variabili = "+numSolution,0));
			 
			 solution.add(new Line("",0));
			 solution.add(new Line("",0));
			 
			 // set objective function
			 solution.add(new Line("Matrice di preferenza:",0));
			 
			 for (int i = 0; i < this.objCoefficient.length; i++) {	
				 
				 solution.add(new Line("[",2));
					
					for (int j = 0; j < this.objCoefficient.length; j++) {
						
						solution.add(new Line(String.valueOf(this.objCoefficient[i][j]),2));
					}
					
				  solution.add(new Line("]",0));
			}
		  
		   solution.add(new Line("",0));
		   solution.add(new Line("Funzione Obiettivo: ",0));
		   solution.add(new Line("[" + this.calcObjFunNumbers() + "]",0));			   

		   //setto la funzione obiettivo
		   
		   solv.setAddRowmode(true);
		   
		   //System.out.println(this.calcObjFunNumbers());
		   solv.strSetObjFn(this.calcObjFunNumbers());
		   			   
		   solution.add(new Line("",0));
		 
		  // add constraints
		   
		  solution.add(new Line("Vincoli:",0));
		  
		  for(Constraint c : this.constraints) {
			  solv.strAddConstraint(c.getCoefficients(), c.getCompareSign(), c.getConstTerm());
			  solution.add(new Line("[ "+c.getCoefficients()+" "+ Constraint.getSign(c.getCompareSign())+" "+c.getConstTerm()+" ]",0));
		  }
		  
		  //set binary variables
		  
		  for (int i = 1; i <= numSolution; i++) {
			solv.setBinary(i, true);
		  }

		  //solv.setBreakAtFirst(true);
		  
		  solv.setAddRowmode(false);

		  // solve the problem
		  System.out.println("IsOptimal: "+solv.solve());
		  this.solverResult = solv.solve();
		  
		  
		  // print solution 
		  solution.add(new Line("",0));
		  solution.add(new Line("",0));
		  
		  solution.add(new Line("Risultato Funzione obiettivo:",2));
		  
		  this.objRes = solv.getObjective();
		  
		  solution.add(new Line(String.valueOf(solv.getObjective()),0));
		  
		  solution.add(new Line("",0));
		  
		  double[] var = solv.getPtrVariables();
		  for (int i = 0; i < var.length; i++) {
			  solution.add(new Line("x[" + i + "] = " + var[i],0));
		  }
		  
		  solution.add(new Line("",0));
		  
		  solution.add(new Line("Tempo impiegato: "));
		  solution.add(new Line(String.valueOf(solv.timeElapsed()),0));
		  
		  this.timeElapsed = solv.timeElapsed();
		  

		  // delete the problem and free memory
		  solv.deleteLp();
			  
		  this.outputIO.appendContent(solution);
		  
		} catch (LpSolveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private int absoluteVar(int i, int j,int t) {
		return ( (((i-1)*(i-1)+(i-1)) / 2) - (i-1-j) + ((this.companiesNumber*this.companiesNumber-this.companiesNumber) / 2) * (t-1) );
	}
	
	
	
	
	
	
	
}
