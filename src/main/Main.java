package main;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.IOHandler;
import io.Line;
import solve.Constraint;
import solve.DomainCreator;
import solve.Solver;

public class Main {

	public static void main(String[] args) {
		
		ArrayList<Line> line = new ArrayList<Line>();
		
		int[] aziende	= {10,26,50,100,200,400};
		int[] rounds	= {4,5,8,10,12,16};
		int[] incontri	= {0,2};
		DomainCreator cc;
		Solver	 testSolver;
		
		IOHandler results = new IOHandler("resources/results.txt");
		

		int i = 2;
		int k = 0;
		
		Instant start;
		Instant finish;
		long timeElapsed;
		
		//cc = new DomainCreator(aziende[i],rounds[i],incontri[k],new IOHandler("resources/"+aziende[i]+"/"+incontri[k]+"/vincoli.csv"),new IOHandler("resources/"+aziende[i]+"/"+incontri[k]+"/preferenze.csv")); 
		//cc.writeConstraintList();
		//cc.writeObj();
		
		line.add(new Line("Elapsed Time (N = "+aziende[i]+" T = "+rounds[i]+" L = "+incontri[k]+")",0));

		
		testSolver = new Solver("resources/"+aziende[i]+"/"+incontri[k]+"/vincoli.csv",
				"resources/"+aziende[i]+"/"+incontri[k]+"/preferenze.csv",
				"resources/"+aziende[i]+"/"+incontri[k]+"/output.txt");
		
		line.add(new Line("",0));
		line.add(new Line("Inizio Caricamento Vincoli",0));
		start = Instant.now();
		
		testSolver.loadConstraints();
		
	    methodToTime();
		 
	    finish = Instant.now();
	 
	    timeElapsed = Duration.between(start, finish).getSeconds();  //in seconds
	    line.add(new Line("Fine Caricamento Vincoli - Tempo Fase: "+String.valueOf(timeElapsed)+ " seconds",0));
	    
	    line.add(new Line("",0));
	    
	    
	    
		line.add(new Line("Inizio Caricamento Funzione Obiettivo",0));
		start = Instant.now();
		
		testSolver.loadObjectiveCoefficient();
		
	    methodToTime();
		 
	    finish = Instant.now();
	 
	    timeElapsed = Duration.between(start, finish).getSeconds();  //in seconds
	    line.add(new Line("Fine Caricamento Funzione Obiettivo - Tempo Fase: "+String.valueOf(timeElapsed)+ " seconds",0));
	    
	    line.add(new Line("",0));
	    
		line.add(new Line("Inizio Caricamento Funzione Obiettivo",0));
		start = Instant.now();
		
		//testSolver.solve();
		
	    methodToTime();
		 
	    finish = Instant.now();
	 
	    timeElapsed = Duration.between(start, finish).getSeconds();  //in seconds
	    line.add(new Line("Fine Caricamento Funzione Obiettivo - Tempo Fase: "+String.valueOf(timeElapsed)+ " seconds",0));
	    
	    line.add(new Line("",0));
		
		line.add(new Line("Solver Result: "+ String.valueOf(testSolver.getSolverResult()),0));
		line.add(new Line("Obj Value:"+String.valueOf(testSolver.getObjRes()),0));
		line.add(new Line(String.valueOf(timeElapsed)+ " seconds",0));
		line.add(new Line("",0));

		
		results.appendContent(line);
		
		line.clear();
		
		
		
		 
	
		
		
		}

	
	private static void methodToTime() {
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
