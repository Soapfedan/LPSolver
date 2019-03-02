package solve;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import io.IOHandler;
import io.Line;
import lpsolve.*;

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
	

	public Solver(String constraintsFilePath, String objCoefficientsFilePath, String outputFilePath) {
		
		this.objIO = new IOHandler(objCoefficientsFilePath);
		this.constraintsIO = new IOHandler(constraintsFilePath);
		this.outputIO = new IOHandler(outputFilePath);
		
	}
	
	public void loadConstraints() {
				
		try {			
			
			ArrayList<String[]> c = this.constraintsIO.readFile();
			
			if(c.size() > 1) {
				
				String[] domainData = c.remove(0);
								
				
				if(domainData.length == 3) {
					this.companiesNumber = Integer.parseInt(domainData[0]);
					this.roundsNumber = Integer.parseInt(domainData[1]);
					this.minIncontri = Integer.parseInt(domainData[2]);
				}
				
				this.constraints = new ArrayList<Constraint>();
				
				for (String[] constraintLine: c) {
					
					this.constraints.add(new Constraint(constraintLine));
					
				}
			}
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
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
	
	public void solve(){
		
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
			 
//			 solution.add(new Line("Dati Dominio",0));
//			 solution.add(new Line("",0));
//			 solution.add(new Line("N.Imprese = "+this.companiesNumber,0));
//			 solution.add(new Line("N.Round = "+this.roundsNumber,0));
//			 solution.add(new Line("N.Variabili = "+numSolution,0));
			 
//			 solution.add(new Line("",0));
//			 solution.add(new Line("",0));
			 
			 // set objective function
//			 solution.add(new Line("Matrice di preferenza:",0));
//			 
//			 for (int i = 0; i < this.objCoefficient.length; i++) {	
//				 
//				 solution.add(new Line("[",2));
//					
//					for (int j = 0; j < this.objCoefficient.length; j++) {
//						
//						solution.add(new Line(String.valueOf(this.objCoefficient[i][j]),2));
//					}
//					
//				  solution.add(new Line("]",0));
//			}
//		  
//		   solution.add(new Line("",0));
//		   solution.add(new Line("Funzione Obiettivo: ",0));
//		   solution.add(new Line("[" + this.calcObjFunNumbers() + "]",0));			   

		   //setto la funzione obiettivo
		   
		   //System.out.println(this.calcObjFunNumbers());
		   solv.strSetObjFn(this.calcObjFunNumbers());
		   			   
//		   solution.add(new Line("",0));
		 
		  // add constraints
		   
		  solution.add(new Line("Vincoli:",0));
		  
		  for(Constraint c : this.constraints) {
			  solv.strAddConstraint(c.getCoefficients(), c.getCompareSign(), c.getConstTerm());
//			  solution.add(new Line("[ "+c.getCoefficients()+" "+ Constraint.getSign(c.getCompareSign())+" "+c.getConstTerm()+" ]",0));
		  }
		  
		  //set binary variables
		  
		  for (int i = 1; i <= numSolution; i++) {
			solv.setBinary(i, true);
		  }

		  

		  // solve the problem
//		  System.out.println("IsOptimal: "+solv.solve());
		  this.solverResult = solv.solve();
		  
		  
		  // print solution 
//		  solution.add(new Line("",0));
//		  solution.add(new Line("",0));
		  
//		  solution.add(new Line("Risultato Funzione obiettivo:",2));
		  
		  this.objRes = solv.getObjective();
		  
//		  solution.add(new Line(String.valueOf(solv.getObjective()),0));
		  
//		  solution.add(new Line("",0));
		  
		  double[] var = solv.getPtrVariables();
		  for (int i = 0; i < var.length; i++) {
			  solution.add(new Line("x[" + i+1 + "] = " + var[i],0));
		  }
		  
//		  solution.add(new Line("",0));
//		  
//		  solution.add(new Line("Tempo impiegato: "));
//		  solution.add(new Line(String.valueOf(solv.timeElapsed()),0));
		  
		  this.timeElapsed = solv.timeElapsed();
		  

		  // delete the problem and free memory
		  solv.deleteLp();
			  
		  this.outputIO.appendContent(solution);
		  
		} catch (LpSolveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private String calcObjFunNumbers() {
		
		String ret = "";
		
		for (int i = 0; i < objCoefficient.length; i++) {
					
			for (int j = 0; j < objCoefficient.length; j++) {
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
