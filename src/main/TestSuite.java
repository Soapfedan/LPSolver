package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import lpsolve.LpSolve;

public class TestSuite {
	
	
	private int companiesNumber;
	private int roundsNumber;

	private int minIncontri;
	
	public TestSuite(int companiesNumber, int roundsNumber, int minTables) {
		
		this.companiesNumber = companiesNumber;
		this.roundsNumber = roundsNumber;
		this.minIncontri = minTables;

	}
	

	/**
	 * ogni azienda può avere al massimo un incontro per round
	 * @throws IOException 
	 */
	
	
	public boolean checkCompanyOneMatchPerRound(double[] vars) throws IOException{
		
		FileWriter wr = null;
		
		if(Main.TO_PRINT_STATS == 1) {
			
			File statsFile = new File(Main.STATS_FILE_PATH);
			statsFile.createNewFile();
			wr = new FileWriter (Main.STATS_FILE_PATH,true);
			

			wr.write("---------------- Inizio Test Vincolo 1 -------------------- \n\n");
			
		    
		}
		
		boolean ret = true;
		
		System.out.println("---------------- Inizio Test Vincolo 1 --------------------");
	
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
	            
	            int sum = 0;
	            
	            for (int j = 0; j < c.size(); j++) {
	            	
	            	//il -1 serve perché vars è un indice che va da 0 a NumVariabili -1
	            	sum += vars[c.get(j)-1];
	            	
				}

	            
	            if(sum != 1) {
	            	ret = false;
	            	if(wr != null && Main.TO_PRINT_STATS == 1) {
	            		wr.write("Errore: L'azienda " + i + " nel round "+ t + " in cui ha "+ sum + " incontri\n");
	            	}
	            	System.out.println("Errore: L'azienda " + i + " nel round "+ t + " in cui ha "+ sum + " incontri");
	            }else {
	            	if(wr != null && Main.TO_PRINT_STATS == 1) {
	            		wr.write("OK: L'azienda " + i + " nel round "+ t + " ha "+ sum + " incontri\n");
	            	}
	            	System.out.println("OK: L'azienda " + i + " nel round "+ t + " ha "+ sum + " incontri");
	            }

	           
	          
	        }
	        
	        
	    }
		
		if(ret) {
			System.out.println(" Test Vincolo 1 - OK");
			
			if(wr != null && Main.TO_PRINT_STATS == 1) {
        		wr.write("Test Vincolo 1 - OK\n");
        	}
			
		}else {
			System.out.println(" Test Vincolo 1 - ERROR");
			
			if(wr != null && Main.TO_PRINT_STATS == 1) {
        		wr.write("Test Vincolo 1 - ERROR\n");
        	}
		}
		
		if(wr !=null) {
			wr.close();
		}
		
		return ret;
		
	}
	
	
	
	/**
	 * ogni azienda, fra tutti i round, può incontrarsi al massimo una volta con ogni azienda
	 * @throws IOException 
	 */
	
	public boolean checkCompaniesMatchAllRounds(double[] vars) throws IOException {
		
		FileWriter wr = null;
		
		if(Main.TO_PRINT_STATS == 1) {
			
			File statsFile = new File(Main.STATS_FILE_PATH);
			statsFile.createNewFile();
			wr = new FileWriter (Main.STATS_FILE_PATH,true);
			

			wr.write("---------------- Inizio Test Vincolo 2 -------------------- \n\n");
			
		    
		}
		
		int round = 0;
		
		
		boolean ret = true;
		
		System.out.println("---------------- Inizio Test Vincolo 2 --------------------");
		
		for (int i = 1; i <= companiesNumber*(companiesNumber-1)/2; i++) {
			//scrivo la riga			
			
			int k=i;
			
			ArrayList<Integer> c = new ArrayList<Integer>();
			
			for (int j = 1; j <= roundsNumber; j++) {
				
												
				c.add(k);
				k += companiesNumber*(companiesNumber-1)/2;

				
			}
            		       
            
            int sum = 0;
            
            for (int j = 0; j < c.size(); j++) {
            	
            	//il -1 serve perché vars è un indice che va da 0 a NumVariabili -1
            	sum += vars[c.get(j)-1];
            	
			}

            int[] i_j_t = new int[3];
            i_j_t = Utils.getOriginalThreeIndexes(i,this.companiesNumber,this.roundsNumber);
            
            if(sum > 1) {
            	ret = false;
            	if(wr != null && Main.TO_PRINT_STATS == 1) {
            		wr.write("Errore: L'azienda " + i_j_t[0] + " e l'azienda "+ i_j_t[1] + " hanno più di un incontro\n");
            	}
            	System.out.println("Errore: L'azienda " + i_j_t[0] + " e l'azienda "+ i_j_t[1] + " hanno più di un incontro");
            }else {
            	if(wr != null && Main.TO_PRINT_STATS == 1) {
            		wr.write("OK: L'azienda " + i_j_t[0] + " e l'azienda "+ i_j_t[1] + " si incontrano al massimo 1 volta\n");
            	}
            	System.out.println("OK: L'azienda " + i_j_t[0] + " e l'azienda "+ i_j_t[1] + " si incontrano al massimo 1 volta");
            }

			
		}
		
		if(ret) {
			System.out.println(" Test Vincolo 2 - OK");
			
			if(wr != null && Main.TO_PRINT_STATS == 1) {
        		wr.write("Test Vincolo 2 - OK\n");
        	}
			
		}else {
			System.out.println(" Test Vincolo 2 - ERROR");
			
			if(wr != null && Main.TO_PRINT_STATS == 1) {
        		wr.write("Test Vincolo 2 - ERROR\n");
        	}
		}
		
		if(wr !=null) {
			wr.close();
		}
		
		return ret;
	}
	
	
	public boolean checkMinIncontri(double[] variables) throws IOException {
		
		FileWriter wr = null;
		
		if(Main.TO_PRINT_STATS == 1) {
			
			File statsFile = new File(Main.STATS_FILE_PATH);
			statsFile.createNewFile();
			wr = new FileWriter (Main.STATS_FILE_PATH,true);
			

			wr.write("---------------- Inizio Test Vincolo 3 -------------------- \n\n");
			
		    
		}
		
		boolean ret = true;
		
		System.out.println("---------------- Inizio Test Vincolo 2 --------------------");
		
		for (int i = 1; i <= this.companiesNumber; i++)
	    {

			
			int nIncontri = 0;
			
	        for (int t = 1; t <= this.roundsNumber ; t++)
	        {

	    
	            for (int j1 = 1; j1 <= this.companiesNumber && i > j1; j1++)
	            {
	            	int absVar = Utils.getAbsoluteVar(i, j1, t,this.companiesNumber);
	            	
	            	if(variables[absVar-1] == 1.0) {
	            		nIncontri += 1;

	            	}
	            	

	            }
	    
	            for (int j2 = i+1; j2 <= this.companiesNumber && j2 > i; j2++)
	            {
	            	int absVar = Utils.getAbsoluteVar(j2, i, t,this.companiesNumber);
	            	
	            	if(variables[absVar-1] == 1.0) {
	            		nIncontri += 1;

	            	}
	            	
	            	
	            }	             
	          
	        }
	        
	        int[] i_j_t = new int[3];
            i_j_t = Utils.getOriginalThreeIndexes(i,this.companiesNumber,this.roundsNumber);
	        
	        
	        if(nIncontri < this.minIncontri) {
	        	ret = false;
	        	System.out.println("Errore: L'azienda " + i_j_t[0] + ", "+nIncontri+" incontri minore di "+this.minIncontri+" incontri.");
	        	
	        	if(wr != null && Main.TO_PRINT_STATS == 1) {
            		wr.write("Errore: L'azienda " + i_j_t[0] + ", "+nIncontri+" incontri minore di "+this.minIncontri+" incontri.\n");
            	}
	        	
	        }else {
        		System.out.println("OK: L'azienda " + i_j_t[0] + ", "+nIncontri+" incontri maggiore di "+this.minIncontri+" incontri.");
	        	
	        	if(wr != null && Main.TO_PRINT_STATS == 1) {
            		wr.write("OK: L'azienda " + i_j_t[0] + ", "+nIncontri+" incontri maggiore di "+this.minIncontri+" incontri.\n");
            	}
	        }

	       
	    }
		
		if(ret) {
			System.out.println(" Test Vincolo 3 - OK");
			
			if(wr != null && Main.TO_PRINT_STATS == 1) {
        		wr.write("Test Vincolo 3 - OK\n");
        	}
			
		}else {
			System.out.println(" Test Vincolo 3 - ERROR");
			
			if(wr != null && Main.TO_PRINT_STATS == 1) {
        		wr.write("Test Vincolo 3 - ERROR\n");
        	}
		}
		
		if(wr !=null) {
			wr.close();
		}
		
		
		
		
		return ret;
	}
	
	
}
