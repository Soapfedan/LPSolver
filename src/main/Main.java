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
		
		int N = 4;
		int T = 2;
		int L = 0;

		double[] variables = solve(N,T,L);
		
		doTests(N,T,L,variables);
		

		
		
	}
	
	public static void doTests(int N, int T, int L, double[] variables) {
		
		
		if(variables.length > 0) {
			
			TestSuite suite = new TestSuite(N, T, L);
			
			/**
			 * ogni azienda può avere al massimo un incontro per round
			 */
			
			suite.checkCompanyOneMatchPerRound(variables);
			
			
			/**
			 * ogni azienda, fra tutti i round, può incontrarsi al massimo una volta con ogni azienda
			 */
			
			suite.checkCompaniesMatchAllRounds(variables);
			
		}
		
		
	}
	
	
	public static double[] solve(int N, int T, int L) {
			
		DomainCreator cc;
		Solver	 testSolver;
		long start;
		long finish;
		long timeElapsed;

		
		/**
		 * Creazione del dominio
		 */
		
		System.out.println("Elapsed Time (N = "+N+" T = "+T+" L = "+L+")");
		cc = new DomainCreator(N,T,L,new IOHandler("resources/"+N+"/"+L+"/preferenze.csv")); 
		
		
		
		
		/**
		 * Inizio scrittura dei vincoli sul file CSV
		 */
		
		
		System.out.println("Inizio Fase Scrittura Vincoli");
		
		start = Instant.now().toEpochMilli();
		
		/**
		 * Scrittura dei vincoli
		 */
	
		finish = Instant.now().toEpochMilli();
		 

	    timeElapsed = finish - start;
	    System.out.println("Fine Scrittura Vincoli: "+String.valueOf(timeElapsed)+ " milliseconds");
	    
		/**
		 * Fine scrittura dei vincoli sul file CSV
		 */
	    
	    
	    
	    
	    
	    /**
	     * Inizio scrittura della funzione obiettivo sul file CSV
	     */

	    
	    start = Instant.now().toEpochMilli();
		System.out.println("Inizio Scrittura Funzione obiettivo ");
		
		
		/**
		 * Scrittura della funzione obiettivo
		 */
		
		cc.writeObj();
		
		finish = Instant.now().toEpochMilli();
		 
	    timeElapsed = finish - start;
	    System.out.println("Fine Scrittura Funzione obiettivo: "+String.valueOf(timeElapsed)+ " milliseconds");

	    /**
	     * Fine scrittura della funzione obiettivo sul file CSV
	     */
	    
	    
	    
	    
	    
	    /**
	     * Inizio caricamento di tutti i vincoli in memoria
	     */
	    
	    
	    System.out.println("Inizio Fase Caricamento Vincoli");
	    
		start = Instant.now().toEpochMilli();
		
		testSolver = new Solver(N,T,L,"resources/"+N+"/"+L+"/preferenze.csv");
		
	
		
		/**
		 * Caricamento dei vincoli dal file CSV
		 */
		//testSolver.loadConstraints("resources/"+N+"/"+L+"/vincoli.csv");
		
	   
		 
	    finish = Instant.now().toEpochMilli();
	 

	    timeElapsed = finish - start;
	    System.out.println("Fine Caricamento Vincoli - Tempo Fase: "+String.valueOf(timeElapsed)+ " milliseconds");
	    
	
	    /**
	     * Fine caricamento di tutti i vincoli in memoria
	     */
	    
	    
	    
	    
	    /**
	     * Inizio caricamento di tutti funzione obiettivo
	     */
	    

	    System.out.println("Inizio Fase Caricamento Funzione Obiettivo");
	    
	    start = Instant.now().toEpochMilli();
	    		
		testSolver.loadObjectiveCoefficient();
		
	 
		 
	    finish = Instant.now().toEpochMilli();
	 
	    //timeElapsed = Duration.between(start, finish).getSeconds();  //in seconds
	    timeElapsed = finish - start;
	    System.out.println("Fine Caricamento Funzione Obiettivo - Tempo Fase: "+String.valueOf(timeElapsed)+ " milliseconds");

	    /**
	     * Fine caricamento di tutti funzione obiettivo
	     */
	    
	    
	    
	    /**
	     * Inizio risoluzione del problema
	     */
	    
	    System.out.println("Inizio Fase Risoluzione Sistema");
	    
	    start = Instant.now().toEpochMilli();

		
		/**
		 * 	Chiamata esecuzione risoluzione algoritmo
		 */
	    
	    double[] objVars = new double[(N*N-N)/2*T];
		objVars = testSolver.solve();
		

		 
	    finish = Instant.now().toEpochMilli();

	    timeElapsed = finish - start;
	    System.out.println("Fine Elaborazione - Tempo Fase: "+String.valueOf(timeElapsed)+ " milliseconds");
	    

		 /**
	     * Fine risoluzione del problema
	     */

		
	    return objVars;
		
		
	}

	

}
