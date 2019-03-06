package main;

public class Utils {

	public static int[] getOriginalThreeIndexes(int ai,int N, int T) {
		
		int[] i_j_t = new int[3];
		
		
		for (int i = 1; i <= N; i++)
	    {
	        for (int t = 1; t <= T ; t++)
	        {
	    
	            for (int j1 = 1; j1 <= N && i > j1; j1++)
	            {
	            	int absVar = Utils.getAbsoluteVar(i, j1, t, N);
	      
	            	if(absVar == ai) {
	            		
	            		i_j_t[0] = i;
        				i_j_t[1] = j1;
						i_j_t[2] = t;
						
						return i_j_t;
	            	}

	            }
	    
	        
	           
	          
	        }
	    }
		
		
		return i_j_t;
	}
	
	public static int getAbsoluteVar(int i, int j,int t, int N) {
		return ( (((i-1)*(i-1)+(i-1)) / 2) - (i-1-j) + ((N*N-N) / 2) * (t-1) );
	}
}
