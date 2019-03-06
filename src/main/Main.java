package main;


import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import io.IOHandler;
import io.Line;
import lpsolve.LpSolve;
import lpsolve.LpSolveException;
import solve.Constraint;
import solve.Creator;
import solve.DomainCreator;
import solve.Solver;

public class Main {
	
	private static int N_PREFERENZE = 3;


	public static void main(String[] args) throws LpSolveException {
		
		int N = 10;
		int T = 3;
		int L = 0;
		String inputFilePath = "resources/pref.txt";

		double[] variables = solve(N,T,L,inputFilePath);
		
		doTests(N,T,L,variables);
		
		
	}
	
	public static void doTests(int N, int T, int L, double[] variables) {
		
		boolean res1=false,res2=false;
		
		if(variables.length > 0) {
			
			TestSuite suite = new TestSuite(N, T, L);
			
			/**
			 * ogni azienda può avere al massimo un incontro per round
			 */
			
			res1 = suite.checkCompanyOneMatchPerRound(variables);
			
			System.out.println("");
			
			/**
			 * ogni azienda, fra tutti i round, può incontrarsi al massimo una volta con ogni azienda
			 */
			
			res2 = suite.checkCompaniesMatchAllRounds(variables);
			
		}
		
		
	}
	
	
	public static double[] solve(int N, int T, int L,String inputFilePath) {
			
		DomainCreator cc;
		Solver	 testSolver;
		long start;
		long finish;
		long timeElapsed;

		
		/**
		 * Creazione del dominio
		 */
		
		System.out.println("Elapsed Time (N = "+N+" T = "+T+" L = "+L+")");
		cc = new DomainCreator(N,T,L,new IOHandler(inputFilePath)); 
		testSolver = new Solver(N,T,L,inputFilePath);

		
	    /**
	     * Inizio scrittura della funzione obiettivo sul file
	     */
	    
	    start = Instant.now().toEpochMilli();
		System.out.println("Inizio Scrittura Funzione obiettivo ");
		
		
		/**
		 * Scrittura della funzione obiettivo
		 */
		
		//cc.writeObj(N_PREFERENZE);
		
		finish = Instant.now().toEpochMilli();
		 
	    timeElapsed = finish - start;
	    System.out.println("Fine Scrittura Funzione obiettivo: "+String.valueOf(timeElapsed)+ " milliseconds");

	    /**
	     * Fine scrittura della funzione obiettivo sul file
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

	    printListaIncontri(N,T,objVars);
	    printListaPreferenze(N, T, objVars);
		
	    return objVars;
		
		
	}

	public static void printListaIncontri(int N, int T, double[] variables) {
		
		System.out.println("");
		System.out.println("--------- Statistiche Lista Incontri Aziende--------");
		System.out.println("");
		
		
		for (int i = 1; i <= N; i++)
	    {
			ArrayList<String> c = new ArrayList<String>();
			
			int nIncontri = 0;
			
	        for (int t = 1; t <= T ; t++)
	        {

	    
	            for (int j1 = 1; j1 <= N && i > j1; j1++)
	            {
	            	int absVar = Utils.getAbsoluteVar(i, j1, t,N);
	            	
	            	if(variables[absVar-1] == 1.0) {
	            		nIncontri += 1;
	            		c.add("az"+j1);
	            	}
	            	

	            }
	    
	            for (int j2 = i+1; j2 <= N && j2 > i; j2++)
	            {
	            	int absVar = Utils.getAbsoluteVar(j2, i, t,N);
	            	
	            	if(variables[absVar-1] == 1.0) {
	            		nIncontri += 1;
	            		c.add("az"+j2);
	            	}
	            	
	            	
	            }	             
	          
	        }
	        
	        String entry = "Azienda "+i+", "+nIncontri+ " incontri, ["+String.join(",", c)+"]";
	        
	        System.out.println(entry);
	       
	    }
		

		System.out.println("");
	}
	
	public static void printListaPreferenze(int N, int T, double[] variables) {
		
		System.out.println("");
		System.out.println("--------- Statistiche Preferenze Aziende --------");
		System.out.println("");
		
		
		for (int i = 1; i <= N; i++)
	    {

			
			int nIncontri = 0;
			
	        for (int t = 1; t <= T ; t++)
	        {

	    
	            for (int j1 = 1; j1 <= N && i > j1; j1++)
	            {
	            	int absVar = Utils.getAbsoluteVar(i, j1, t,N);
	            	
	            	if(variables[absVar-1] == 1.0) {
	            		nIncontri += 1;

	            	}
	            	

	            }
	    
	            for (int j2 = i+1; j2 <= N && j2 > i; j2++)
	            {
	            	int absVar = Utils.getAbsoluteVar(j2, i, t,N);
	            	
	            	if(variables[absVar-1] == 1.0) {
	            		nIncontri += 1;

	            	}
	            	
	            	
	            }	             
	          
	        }
	        
	        String entry = "Azienda "+i+": "+nIncontri+ "/"+N_PREFERENZE;
	        
	        if(nIncontri < N_PREFERENZE) {
	        	entry += " -> WARNING";
	        }
	        
	        System.out.println(entry);
	       
	    }
		

		System.out.println("");
	}
	
}
