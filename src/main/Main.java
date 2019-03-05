package main;


import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;

import io.IOHandler;
import io.Line;
import lpsolve.LpSolve;
import lpsolve.LpSolveException;
import solve.Constraint;
import solve.Creator;
import solve.DomainCreator;
import solve.Solver;

public class Main {

	public static void main(String[] args) throws LpSolveException {
		
		//System.out.println(Arrays.toString(args));
		solve(4,2,0);
//		create(4,2,0);
		
		//test(131312,1231232);
		
		
	}
	
	
	public static void test(int n, int t) throws LpSolveException {
		
		//LpSolve s = new LpSolve(0);
		
		int numVar = (n * (n-1))/2 * t;

		LpSolve solv = LpSolve.makeLp(4, 4);
		
		
		solv.readLp("resources/modello.lp", LpSolve.NORMAL, "test model");
		
		//Arrays.toString(solv.getConstraints());
		
		solv.solve();
		
		System.out.println(solv.getObjective());
		
		 double[] var = new double[4];
		 solv.getVariables(var);
		 for (int i = 0; i < var.length; i++) {
			 int j = i+1;
			  System.out.println("x[" + j + "] = " + var[i]);
		  }
		 System.out.println(Arrays.toString(var));
	}
	
	public static void create(int N, int T, int L) {
		
		
		Creator c = new Creator(N,T,L,"resources/"+N+"/"+L+"/modello.lp");
		c.createConstraint();
		
	}
	
	

	
	public static void solve(int N, int T, int L) {
		
		ArrayList<Line> line = new ArrayList<Line>();		
		DomainCreator cc;
		Solver	 testSolver;
		long start;
		long finish;
		long timeElapsed;

		
		IOHandler results = new IOHandler("resources/results.txt");
		
		
		/**
		 * Creazione del dominio
		 */
		
		line.add(new Line("Elapsed Time (N = "+N+" T = "+T+" L = "+L+")",0));
		cc = new DomainCreator(N,T,L,new IOHandler("resources/"+N+"/"+L+"/vincoli.csv"),new IOHandler("resources/"+N+"/"+L+"/preferenze.csv")); 
		
		
		
		
		/**
		 * Inizio scrittura dei vincoli sul file CSV
		 */
		
		
		System.out.println("Inizio Fase Scrittura Vincoli");
		
		start = Instant.now().toEpochMilli();
		line.add(new Line("Inizio Scrittura Vincoli",0));
		
		/**
		 * Scrittura dei vincoli
		 */
		//cc.writeConstraintList();
		
		finish = Instant.now().toEpochMilli();
		 

	    timeElapsed = finish - start;
	    line.add(new Line("Fine Scrittura Vincoli: "+String.valueOf(timeElapsed)+ " milliseconds",0));
	    
		/**
		 * Fine scrittura dei vincoli sul file CSV
		 */
	    
	    
	    
	    
	    
	    /**
	     * Inizio scrittura della funzione obiettivo sul file CSV
	     */
	    
	    System.out.println("Inizio Fase Scrittura Funzione Obiettivo");
	    
	    start = Instant.now().toEpochMilli();
		line.add(new Line("Inizio Scrittura Funzione obiettivo ",0));
		
		
		/**
		 * Scrittura della funzione obiettivo
		 */
		
		cc.writeObj();
		
		finish = Instant.now().toEpochMilli();
		 
	    timeElapsed = finish - start;
	    line.add(new Line("Fine Scrittura Funzione obiettivo: "+String.valueOf(timeElapsed)+ " milliseconds",0));

	    /**
	     * Fine scrittura della funzione obiettivo sul file CSV
	     */
	    
	    
	    
	    
	    
	    /**
	     * Inizio caricamento di tutti i vincoli in memoria
	     */
	    
	    
	    System.out.println("Inizio Fase Caricamento Vincoli");
	    
		start = Instant.now().toEpochMilli();
		
		testSolver = new Solver(N,T,L,"resources/"+N+"/"+L+"/preferenze.csv");
		
		line.add(new Line("",0));
		line.add(new Line("Inizio Caricamento Vincoli",0));
		
		
		/**
		 * Caricamento dei vincoli dal file CSV
		 */
		//testSolver.loadConstraints("resources/"+N+"/"+L+"/vincoli.csv");
		
	   
		 
	    finish = Instant.now().toEpochMilli();
	 

	    timeElapsed = finish - start;
	    line.add(new Line("Fine Caricamento Vincoli - Tempo Fase: "+String.valueOf(timeElapsed)+ " milliseconds",0));
	    
	    line.add(new Line("",0));
	    
	    /**
	     * Fine caricamento di tutti i vincoli in memoria
	     */
	    
	    
	    
	    
	    /**
	     * Inizio caricamento di tutti funzione obiettivo
	     */
	    

	    System.out.println("Inizio Fase Caricamento Funzione Obiettivo");
	    
	    start = Instant.now().toEpochMilli();
	    
		line.add(new Line("Inizio Caricamento Funzione Obiettivo",0));
		
		
		testSolver.loadObjectiveCoefficient();
		
	 
		 
	    finish = Instant.now().toEpochMilli();
	 
	    //timeElapsed = Duration.between(start, finish).getSeconds();  //in seconds
	    timeElapsed = finish - start;
	    line.add(new Line("Fine Caricamento Funzione Obiettivo - Tempo Fase: "+String.valueOf(timeElapsed)+ " milliseconds",0));
	    
	    line.add(new Line("",0));

	    /**
	     * Fine caricamento di tutti funzione obiettivo
	     */
	    
	    
	    
	    /**
	     * Inizio risoluzione del problema
	     */
	    
	    System.out.println("Inizio Fase Risoluzione Sistema");
	    
	    start = Instant.now().toEpochMilli();
		line.add(new Line("Inizio Elaborazione",0));
		
		/**
		 * 	Chiamata esecuzione risoluzione algoritmo
		 */
		testSolver.solve();
		

		 
	    finish = Instant.now().toEpochMilli();
	 
	    //timeElapsed = Duration.between(start, finish).getSeconds();  //in seconds
	    timeElapsed = finish - start;
	    line.add(new Line("Fine Elaborazione - Tempo Fase: "+String.valueOf(timeElapsed)+ " milliseconds",0));
	    
	    line.add(new Line("",0));
		
		line.add(new Line("Solver Result: "+ String.valueOf(testSolver.getSolverResult()),0));
		line.add(new Line("Obj Value:"+String.valueOf(testSolver.getObjRes()),0));
		//line.add(new Line(String.valueOf(timeElapsed)+ " seconds",0));
		line.add(new Line("",0));

		 /**
	     * Fine risoluzione del problema
	     */
		
		results.writeContent(line);
		
		line.clear();
		
		
		
	}

	

}
