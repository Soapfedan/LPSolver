package main;

import java.util.ArrayList;
import java.util.Arrays;

import io.IOHandler;
import lpsolve.LpSolve;

public class TestSuite {
	
	
	private int companiesNumber;
	private int roundsNumber;

	private int minTables;
	
	public TestSuite(int companiesNumber, int roundsNumber, int minTables) {
		
		this.companiesNumber = companiesNumber;
		this.roundsNumber = roundsNumber;
		this.minTables = minTables;

	}
	

	/**
	 * ogni azienda può avere al massimo un incontro per round
	 */
	
	
	public void checkCompanyOneMatchPerRound(double[] vars){
		
		System.out.println("---------------- Lista Errori Vincolo 1 --------------------");
	
		for (int i = 1; i <= this.companiesNumber; i++)
	    {
	        for (int t = 1; t <= this.roundsNumber ; t++)
	        {
	        	ArrayList<Integer> c = new ArrayList<Integer>();

	    
	            for (int j1 = 1; j1 <= this.companiesNumber && i > j1; j1++)
	            {
	            	int absVar = this.absoluteVar(i, j1, t);
	            	c.add(absVar);

	            }
	    
	            for (int j2 = i+1; j2 <= this.companiesNumber && j2 > i; j2++)
	            {
	            	int absVar = this.absoluteVar(j2, i, t);
	            	c.add(absVar);
	            	
	            }
	            
	            int sum = 0;
	            
	            for (int j = 0; j < c.size(); j++) {
	            	
	            	//il -1 serve perché vars è un indice che va da 0 a NumVariabili -1
	            	sum += vars[c.get(j)-1];
	            	
				}

	            
	            if(sum != 1) {
	            	System.out.println("Errore: L'azienda " + i + " nel round "+ t + " in cui ha "+ sum + " incontri");
	            }

	           
	          
	        }
	    }
		
		System.out.println("---------------- Fine Lista Errori Vincolo 1 --------------------");
		
	}
	
	
	
	/**
	 * ogni azienda, fra tutti i round, può incontrarsi al massimo una volta con ogni azienda
	 */
	
	public void checkCompaniesMatchAllRounds(double[] vars) {
		
		System.out.println("---------------- Inizio Lista Errori Vincolo 2 --------------------");
		
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
            i_j_t = this.getRevertSolution(i);
            
            if(sum > 1) {
            	System.out.println("Errore: L'azienda " + i_j_t[0] + " e l'azienda "+ i_j_t[1] + " hanno più di un incontro");
            }

			
		}
		
		System.out.println("---------------- Fine Lista Errori Vincolo 2 --------------------");
		

	}
	
	
	
	
	
	private int absoluteVar(int i, int j,int t) {
		return ( (((i-1)*(i-1)+(i-1)) / 2) - (i-1-j) + ((this.companiesNumber*this.companiesNumber-this.companiesNumber) / 2) * (t-1) );
	}
	
	/**
	 * Metodo che trasforma da indice assoluto alla tripletta di indici
	 * @param ai è l'indice assoluto della variabile da ritrasformare
	 * @return un array con le 3 variabili
	 */
	
	private int[] getRevertSolution(int ai) {
		
		int[] i_j_t = new int[3];
		
		
		for (int i = 1; i <= this.companiesNumber; i++)
	    {
	        for (int t = 1; t <= this.roundsNumber ; t++)
	        {
	    
	            for (int j1 = 1; j1 <= this.companiesNumber && i > j1; j1++)
	            {
	            	int absVar = this.absoluteVar(i, j1, t);
	      
	            	if(absVar == ai) {
	            		
	            		i_j_t[0] = i;
        				i_j_t[1] = j1;
						i_j_t[2] = t;
						
						return i_j_t;
	            	}

	            }
	    
	            for (int j2 = i+1; j2 <= this.companiesNumber && j2 > i; j2++)
	            {
	            	int absVar = this.absoluteVar(j2, i, t);

	            	if(absVar == ai) {
	            		
	            		i_j_t[0] = j2;
        				i_j_t[1] = i;
						i_j_t[2] = t;
						
						return i_j_t;
	            	}
	            	
	            }	            
	           
	          
	        }
	    }
		
		
		return i_j_t;
	}
	
	
}
