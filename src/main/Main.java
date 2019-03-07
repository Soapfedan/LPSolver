package main;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import lpsolve.LpSolveException;
import solve.DomainCreator;
import solve.Solver;

public class Main {
	
	private static int N_PREFERENZE = 3;
	private static int N_COMPANIES;
	private static int N_ROUNDS;
	private static int N_MIN_BLOCK;
	private static String INPUT_FILE_PATH;
	private static String OUTPUT_FILE_PATH;
	private static Solver SOLVER_SYSTEM;


	public static void main(String[] args) {
		
			if(args.length == 4) {
				
				try {
				
				Main.getJavaArguments(args);
				
				Main.loadObjectiveCoefficient();
				
				double[] variables = Main.solve(Main.N_COMPANIES, Main.N_ROUNDS, Main.N_MIN_BLOCK, Main.INPUT_FILE_PATH);
				
				Main.doConstraintsTest(Main.N_COMPANIES, Main.N_ROUNDS, Main.N_MIN_BLOCK, variables);
				
				Main.writeObjToFile(variables);
				
				} catch (IOException e) {
					System.out.println(Arrays.toString(e.getStackTrace()));
				}
				
			}else {
				
				System.out.println("Attenzione errore nel caricamento dei parametri del problema");
			}
		
		
		
	}
	
	public static void getJavaArguments(String[] args) throws IOException {
			
		Main.N_COMPANIES = Integer.parseInt(args[0]);
		Main.N_ROUNDS = Integer.parseInt(args[1]);
		Main.N_MIN_BLOCK = Integer.parseInt(args[2]);
		Main.INPUT_FILE_PATH = args[3];
		Main.OUTPUT_FILE_PATH = Main.INPUT_FILE_PATH + ".out";
		Main.SOLVER_SYSTEM = new Solver(Main.N_COMPANIES,Main.N_ROUNDS,Main.N_MIN_BLOCK);
		
		DomainCreator dc = new DomainCreator(Main.N_COMPANIES, Main.N_ROUNDS, Main.N_MIN_BLOCK, Main.INPUT_FILE_PATH);
		
		//dc.writeObj(N_PREFERENZE);
		
	}


	public static void loadObjectiveCoefficient() throws IOException {

		
		long start;
		long finish;
		long timeElapsed;
		
		 System.out.println("Inizio Lettura Coefficienti Fun Obiettivo");
		    
	    start = Instant.now().toEpochMilli();

		
		int[][] objCoefMatrix = Main.initializePrefsMatr();				
		
		if(Main.N_COMPANIES > 1) {
		
			  BufferedReader br = null;
			  String line = "";
			
		      br = new BufferedReader(new FileReader(Main.INPUT_FILE_PATH));
		      
		      while ((line = br.readLine()) != null) {
		
		          String[] parameters = line.split(",");	         	          
		          
		          if(parameters.length == 3) {
		        	  
		        	  
		        	  
		        	  int iIndex = Integer.parseInt(parameters[0]);
		        	  int jIndex = Integer.parseInt(parameters[1]);
		        	  int pref = Integer.parseInt(parameters[2]);
		        	  
		        	  if(iIndex <= Main.N_COMPANIES && jIndex <= Main.N_COMPANIES) {
		        		  
		        		  objCoefMatrix[iIndex-1][jIndex-1] = pref;
		        	  }
		        	  
		          }
		
		      }
		

		      if (br != null) {
		         
		              br.close();
		              
		         
		      }
			                
			  
		
		}
		
		for (int i = 0; i < N_COMPANIES; i++) {
			for (int j = 0; j < N_COMPANIES; j++) {
					System.out.print(objCoefMatrix[i][j]+ " ");
			}
			System.out.println("");
		}
		
		Main.SOLVER_SYSTEM.setObjCoefficient(objCoefMatrix);
		
	   finish = Instant.now().toEpochMilli();

	    timeElapsed = finish - start;
	    System.out.println("Fine Lettura Coefficienti Fun Obiettivo - Tempo Fase: "+String.valueOf(timeElapsed)+ " milliseconds");
		
	}
	
	
	public static double[] solve(int N, int T, int L,String inputFilePath) {
		

		long start;
		long finish;
		long timeElapsed;

		
		/**
		 * Creazione del dominio
		 */
		
		System.out.println("Elapsed Time (N = "+N+" T = "+T+" L = "+L+")"); 
	
  

	    
	    System.out.println("Inizio Fase Risoluzione Sistema");
	    
	    start = Instant.now().toEpochMilli();

		
		/**
		 * 	Chiamata esecuzione risoluzione algoritmo
		 */
	    
	    double[] objVars = new double[(N*N-N)/2*T];
		objVars = Main.SOLVER_SYSTEM.solve();
		

		 
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

	public static void writeObjToFile(double[] variables) throws IOException {
		
		int[][][] results = new int[Main.N_COMPANIES][Main.N_COMPANIES][Main.N_ROUNDS];
		
		for (int i = 1; i <= Main.N_COMPANIES; i++)
	    {

			
	        for (int t = 1; t <= Main.N_ROUNDS ; t++)
	        {

	    
	            for (int j1 = 1; j1 <= Main.N_COMPANIES && i > j1; j1++)
	            {
	            	int absVar = Utils.getAbsoluteVar(i, j1, t,Main.N_COMPANIES);
	            	
	            	if(variables[absVar-1] == 1.0) {
	            		results[i-1][j1-1][t-1] = 1;

	            	}
	            	

	            }
	    
	            for (int j2 = i+1; j2 <= Main.N_COMPANIES && j2 > i; j2++)
	            {
	            	int absVar = Utils.getAbsoluteVar(j2, i, t,Main.N_COMPANIES);
	            	
	            	if(variables[absVar-1] == 1.0) {
	            		results[j2-1][i-1][t-1] = 1;

	            	}
	            	
	            	
	            }	             
	          
	        }
		
	    }
		
		File outputFile = new File(Main.OUTPUT_FILE_PATH);
		outputFile.createNewFile();
		FileWriter wr = new FileWriter(Main.OUTPUT_FILE_PATH,false);
	
		for (int t = 1; t <= N_ROUNDS; t++) {
			for (int i = 1; i <= Main.N_COMPANIES; i++) {
				for (int j = 1; j <= Main.N_COMPANIES; j++) {
					if(results[i-1][j-1][t-1] == 1) {
						wr.write(i+","+j+","+t+"\n");
					}
				}
			}
		}
			
		
		wr.close();
			
		
	}
	
	
	
	private static int[][] initializePrefsMatr() {
		
		int [][] objCoefficient = new int[Main.N_COMPANIES][Main.N_COMPANIES];
		
		for (int i = 0; i < Main.N_COMPANIES; i++) {
			for (int j = 0; j < Main.N_COMPANIES; j++) {
				if(i != j) {
					objCoefficient[i][j] = 1;
				}
			}
		}
		
		return objCoefficient;
		
		
	}
	
	private static void doConstraintsTest(int N, int T, int L, double[] variables) {
		
		boolean res1=false,res2=false,res3=false;
		
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
			
			/**
			 * ogni azienda deve avere un numero minimo di incontri
			 */
			
			res3 = suite.checkMinIncontri(variables);
		}
		
		
	}
	
	
	
	private static void printListaIncontri(int N, int T, double[] variables) {
		
		HashMap<Integer, String> map = new HashMap<>();
		
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
	        
	        if(!map.containsKey(nIncontri)) {
	        	
	        	map.put(nIncontri, entry + " \n");
	        }else {
	        	map.put(nIncontri, map.get(nIncontri) + entry + "\n");
	        }
	       
	    }
		

		for(int i = T; i>0;i--) {
			if(map.get(i) != null)
			System.out.println(map.get(i));
		}
	}
	
	private static void printListaPreferenze(int N, int T, double[] variables) {
		
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
	            	
	            	if(variables[absVar-1] == 1.0 && Main.SOLVER_SYSTEM.getObjCoefficient()[i][j1] > 1) {
	            		nIncontri += 1;

	            	}
	            	

	            }
	    
	            for (int j2 = i+1; j2 <= N && j2 > i; j2++)
	            {
	            	int absVar = Utils.getAbsoluteVar(j2, i, t,N);
	            	
	            	if(variables[absVar-1] == 1.0 && Main.SOLVER_SYSTEM.getObjCoefficient()[j2][i] > 1) {
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
		


		
	}
	
	
}
