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
	public static int TO_PRINT_STATS;
	private static String INPUT_FILE_PATH;
	private static String OUTPUT_FILE_PATH;
	public static String STATS_FILE_PATH;
	private static Solver SOLVER_SYSTEM;


	public static void main(String[] args) {
		
			if(args.length >= 4) {
				
				try {
				
				Main.getJavaArguments(args);
				
				Main.loadObjectiveCoefficient();
				
				double[] variables = Main.solve(Main.N_COMPANIES, Main.N_ROUNDS, Main.N_MIN_BLOCK, Main.INPUT_FILE_PATH);
				
				Main.doConstraintsTest(Main.N_COMPANIES, Main.N_ROUNDS, Main.N_MIN_BLOCK, variables);
				
				Main.writeObjToFile(variables);
				
				} catch (IOException e) {
					e.printStackTrace();
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
		
		if(args.length == 5) {
			Main.TO_PRINT_STATS = Integer.parseInt(args[4]);
		}
		
		
		Main.OUTPUT_FILE_PATH = Main.INPUT_FILE_PATH + ".out";
		Main.SOLVER_SYSTEM = new Solver(Main.N_COMPANIES,Main.N_ROUNDS,Main.N_MIN_BLOCK);
		
		if(Main.TO_PRINT_STATS == 1) {
			
			Main.STATS_FILE_PATH = Main.INPUT_FILE_PATH + ".stats";
			Main.printEnviromentInfo();
		}
			
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
	    
	    
	    
	    if(Main.TO_PRINT_STATS == 1) {
			
			File statsFile = new File(Main.STATS_FILE_PATH);
			statsFile.createNewFile();
			FileWriter wr = new FileWriter (Main.STATS_FILE_PATH,true);
			
			long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() /1024*1024;
			wr.write("Tempo impiegato per caricare le preferenze: "+String.valueOf(timeElapsed)+ " milliseconds \n");
		    wr.write("Memoria utilizzata per caricare le preferenze: "+String.valueOf(usedMemory)+ " MB \n");
	
		    wr.close();
		}
		
	}
	
	
	public static double[] solve(int N, int T, int L,String inputFilePath) throws IOException {
		
		FileWriter wr = null;
		
		if(Main.TO_PRINT_STATS == 1) {
			
			File statsFile = new File(Main.STATS_FILE_PATH);
			statsFile.createNewFile();
			wr = new FileWriter (Main.STATS_FILE_PATH,true);
			
			wr.write("----------- Statistiche Programma ----------------- \n");
		}

		long start;
		long finish;
		long timeElapsed;

		
		/**
		 * Creazione del dominio
		 */
		
		System.out.println("Elapsed Time (N = "+N+" T = "+T+" L = "+L+")"); 
	
		if(Main.TO_PRINT_STATS == 1) {
			wr.write("\nDati Dominio \nN Aziende = "+N+" \nN Round = "+T+" \nMin Incontri = "+L+""+"\n \n");
		}

	    
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

	    
	    if(Main.TO_PRINT_STATS == 1) {
	    	
	    	wr.write("Valore Funzione Obiettivo "+Main.SOLVER_SYSTEM.getObjRes()+"\n \n");
	    
	    	long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() /1024*1024;
		    wr.write("Tempo impiegato per la risoluzione del problema: "+String.valueOf(timeElapsed)+ " milliseconds \n");
		    wr.write("Memoria utilizzata per la risoluzione del problema: "+String.valueOf(usedMemory)+ " MB \n\n");
	
		    wr.close();
			
	    }
	    
	    
	    
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
	
	private static void doConstraintsTest(int N, int T, int L, double[] variables) throws IOException {
		
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
	
	
	private static void printEnviromentInfo() throws IOException {
		
		
		File statsFile = new File(Main.STATS_FILE_PATH);
		statsFile.createNewFile();
		FileWriter wr = new FileWriter (Main.STATS_FILE_PATH,false);
		
		wr.write("----------- Elenco Informazioni Macchina ----------------- \n");
		
		String version = System.getProperty("java.version");
		String os_arch = System.getProperty("os.arch");
		String os_name = System.getProperty("os.name");
		String os_version = System.getProperty("os.version");
		
		long maxMemory = Runtime.getRuntime().maxMemory() /1024*1024;
		long allocatedMemory = Runtime.getRuntime().totalMemory() /1024*1024;
		//long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		
		wr.write("Java Version \t \t \t"+ version +"\n");
		wr.write("Java Allocated Memory \t"+ allocatedMemory +" MB \n");
		wr.write("Java -Xmx \t \t \t \t"+ maxMemory +" MB \n");
		wr.write("Os Architecture Version "+ os_arch +"\n");
		wr.write("Os System Name \t \t \t"+ os_name +"\n");
		wr.write("Os System Version \t \t"+ os_version +"\n \n \n");
		
		wr.close();
		
		
		
	}
	
	
	
	private static void printListaIncontri(int N, int T, double[] variables) throws IOException {
		
		
		
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
	            	
	            	if(variables[absVar-1] == 1.0 && Main.SOLVER_SYSTEM.getObjCoefficient()[i-1][j1-1] > 1) {
	            		nIncontri += 1;
	            		c.add("az"+j1);
	            	}
	            	

	            }
	    
	            for (int j2 = i+1; j2 <= N && j2 > i; j2++)
	            {
	            	int absVar = Utils.getAbsoluteVar(j2, i, t,N);
	            	
	            	if(variables[absVar-1] == 1.0 && Main.SOLVER_SYSTEM.getObjCoefficient()[j2-1][i-1] > 1) {
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
		
		
		 if(Main.TO_PRINT_STATS == 1) {
				
				File statsFile = new File(Main.STATS_FILE_PATH);
				statsFile.createNewFile();
				FileWriter wr = new FileWriter (Main.STATS_FILE_PATH,true);
				

				wr.write("\n--------- Statistiche Lista Incontri Aziende-------- \n \n");
				
				for(int i = T; i>0;i--) {
					if(map.get(i) != null)
					wr.write(map.get(i) + "\n");
				}
				
			    wr.close();
		}
	}
	
	private static void printListaPreferenze(int N, int T, double[] variables) throws IOException {
		
		int[] numPreferenze = new int[N];
		
		System.out.println("");
		System.out.println("--------- Statistiche Preferenze Aziende --------");
		System.out.println("");
		
		for (int i = 0; i < N; i++)
	    {

			int nIncontri = 0;
			    
	            for (int j1 = 0; j1 < N && i > j1; j1++)
	            {
	            	
	            	if(Main.SOLVER_SYSTEM.getObjCoefficient()[i][j1] > 1) {
	            		numPreferenze[i] += 1;
	            	}
	            	

	            }
	    
	            for (int j2 = i+1; j2 < N && j2 > i; j2++)
	            {
	            	
	            	if(Main.SOLVER_SYSTEM.getObjCoefficient()[j2][i] > 1) {
	            		numPreferenze[i] += 1;

	            	}
	            	
	            	
	            }	

	      //int is = i+1;  
	      //System.out.println("Azienda "+is+" n prefs "+numPreferenze[i]);
	       
	    }
		
		
		
		
	
		
		FileWriter wr = null;
		
		if(Main.TO_PRINT_STATS == 1) {
			
			File statsFile = new File(Main.STATS_FILE_PATH);
			statsFile.createNewFile();
			wr = new FileWriter (Main.STATS_FILE_PATH,true);
			

			wr.write("--------- Statistiche Preferenze Aziende -------- \n \n");
			
		    
		}
		
		
		for (int i = 0; i < N; i++)
	    {

			
			int nIncontri = 0;
			
	        for (int t = 0; t < T ; t++)
	        {

	    
	            for (int j1 = 0; j1 < N && i > j1; j1++)
	            {
	            	int absVar = Utils.getAbsoluteVar(i+1, j1+1, t+1,N);
	            	
	            	if(variables[absVar-1] == 1.0 && Main.SOLVER_SYSTEM.getObjCoefficient()[i][j1] > 1) {
	            		nIncontri += 1;

	            	}
	            	

	            }
	    
	            for (int j2 = i+1; j2 < N && j2 > i; j2++)
	            {
	            	int absVar = Utils.getAbsoluteVar(j2+1, i+1, t+1,N);
	            	
	            	if(variables[absVar-1] == 1.0 && Main.SOLVER_SYSTEM.getObjCoefficient()[j2][i] > 1) {
	            		nIncontri += 1;

	            	}
	            	
	            	
	            }	             
	          
	        }
	        
	        int is = i+1;
	        String entry = "Azienda "+is+": "+nIncontri+ "/"+numPreferenze[i];
	        
	        if(nIncontri < numPreferenze[i]) {
	        	entry += " -> WARNING";
	        }
	        

	        
	        System.out.println(entry);
	        
	        if(Main.TO_PRINT_STATS == 1 && wr != null) {
				

				wr.write(entry + " \n");
				
			    
			}
	       
	    }
		

		if(wr != null) {
			wr.close();
		}
		
	}
	
	
}
