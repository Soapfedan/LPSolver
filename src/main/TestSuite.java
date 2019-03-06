package main;

import java.util.ArrayList;
import java.util.Arrays;

import io.IOHandler;
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
	 */
	
	
	public boolean checkCompanyOneMatchPerRound(double[] vars){
		
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
	            	System.out.println("Errore: L'azienda " + i + " nel round "+ t + " in cui ha "+ sum + " incontri");
	            }

	           
	          
	        }
	        
	        
	    }
		
		if(ret) {
			System.out.println(" Test Vincolo 1 - OK");
		}else {
			System.out.println(" Test Vincolo 1 - ERROR");
		}
		
		return ret;
		
	}
	
	
	
	/**
	 * ogni azienda, fra tutti i round, può incontrarsi al massimo una volta con ogni azienda
	 */
	
	public boolean checkCompaniesMatchAllRounds(double[] vars) {
		
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
            	System.out.println("Errore: L'azienda " + i_j_t[0] + " e l'azienda "+ i_j_t[1] + " hanno più di un incontro");
            }

			
		}
		
		if(ret) {
			System.out.println(" Test Vincolo 2 - OK");
		}else {
			System.out.println(" Test Vincolo 2 - ERROR");
		}
		
		return ret;
	}
	
	
	public boolean checkMinIncontri(double[] variables) {
		
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
	        	System.out.println("Errore: L'azienda " + i_j_t[0] + ", "+nIncontri+" incontri ha meno di "+this.minIncontri+" incontri.");
	        }

	       
	    }
		
		if(ret) {
			System.out.println(" Test Vincolo 3 - OK");
		}else {
			System.out.println(" Test Vincolo 3 - ERROR");
		}
		
		
		
		
		return ret;
	}
	
	
}
