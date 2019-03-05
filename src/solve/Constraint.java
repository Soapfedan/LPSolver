package solve;

import java.util.Arrays;

import lpsolve.*;

public class Constraint {
	
	private String coefficients;
	private String[] coeffs;
	private int compareSign;
	private int constTerm;
	
	public Constraint(String[] line) {
		
		this.coeffs = line;
		
		this.coefficients = "";
		String l = "";
		
		
		if(line.length > 2) {
			
			for (int i = 0; i < line.length-2; i++) {			
				l = line[i] + " ";
				this.coefficients += l;
			}
			
			switch (line[line.length-2]) {
			
				case ">=":
					this.compareSign = LpSolve.GE;
					break;
				
				case "<=":
					this.compareSign = LpSolve.LE;
					break;
					
				case "=":
					this.compareSign = LpSolve.EQ;
					break;
			}
			
			this.constTerm = Integer.parseInt(line[line.length-1]);
		}
		
		
		
	}
	
	public static String getSign(int sign) {
		
		String ret = "";
		
		switch (sign) {
		
		case LpSolve.GE:
			ret = ">=";
			break;
		
		case LpSolve.LE:
			ret = "<=";
			break;
			
		case LpSolve.EQ:
			ret = "=";
			break;
		}
		
		return ret;
	}

	public String getCoefficients() {
		return coefficients;
	}

	public int getCompareSign() {
		return compareSign;
	}

	public int getConstTerm() {
		return constTerm;
	}
	
	public String toString() {
		return this.coefficients + this.compareSign + " " + this.constTerm;		
	}
	
	
	
	

}
