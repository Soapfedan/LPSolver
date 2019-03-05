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

public class Main_old {

	public static void main(String[] args) {
		
		//solvewithPrint(4,0);
		//solve();
		
		
	}
	
//	public static void solve(int i, int k) {
//		
//		int[] aziende	= {10,26,50,100,200,400};
//		int[] rounds	= {4,5,8,10,12,16};
//		//int[] rounds	= {4,5,2,2,2,2};
//		int[] incontri	= {0,2};
//		DomainCreator cc;
//		Solver	 testSolver;
//		
//		IOHandler results = new IOHandler("resources/results.txt");
//		
//
////		int i = 0;
////		int k = 0;
//		
//		Instant start;
//		Instant finish;
//		long timeElapsed;
//
//		cc = new DomainCreator(aziende[i],rounds[i],incontri[k],new IOHandler("resources/"+aziende[i]+"/"+incontri[k]+"/vincoli.csv"),new IOHandler("resources/"+aziende[i]+"/"+incontri[k]+"/preferenze.csv")); 
//		cc.writeConstraintList();
//		cc.writeObj();
//		
////		testSolver = new Solver("resources/"+aziende[i]+"/"+incontri[k]+"/vincoli.csv",
////				"resources/"+aziende[i]+"/"+incontri[k]+"/preferenze.csv",
////				"resources/"+aziende[i]+"/"+incontri[k]+"/output.txt");
////		
//		testSolver.loadConstraints("resources/"+aziende[i]+"/"+incontri[k]+"/vincoli.csv");
//		testSolver.loadObjectiveCoefficient();
//		testSolver.solve();
//		
//	}
//	
//	public static void solvewithPrint(int i, int k) {
//		ArrayList<Line> line = new ArrayList<Line>();
//		
//		int[] aziende	= {4,10,26,50,70,100,200,400};
//		int[] rounds	= {2,4,8,8,8,10,12,16};
//		//int[] rounds	= {4,5,2,2,2,2};
//		int[] incontri	= {0,2};
//		DomainCreator cc;
//		Solver	 testSolver;
//		
//		IOHandler results = new IOHandler("resources/results.txt");
//		
//
////		int i = 0;
////		int k = 0;
//		
//		long start;
//		long finish;
//		long timeElapsed;
//		
//		line.add(new Line("Elapsed Time (N = "+aziende[i]+" T = "+rounds[i]+" L = "+incontri[k]+")",0));
//		
//		
//		cc = new DomainCreator(aziende[i],rounds[i],incontri[k],new IOHandler("resources/"+aziende[i]+"/"+incontri[k]+"/vincoli.csv"),new IOHandler("resources/"+aziende[i]+"/"+incontri[k]+"/preferenze.csv")); 
//		System.out.println("Inizio Fase Scrittura Vincoli");
//		start = Instant.now().toEpochMilli();
//		line.add(new Line("Inizio Scrittura Vincoli",0));
//		
//		cc.writeConstraintList();
//		
//		finish = Instant.now().toEpochMilli();
//		 
//	    //timeElapsed = Duration.between(start, finish).getSeconds();  //in seconds
//	    timeElapsed = finish - start;
//	    line.add(new Line("Fine Scrittura Vincoli: "+String.valueOf(timeElapsed)+ " milliseconds",0));
//	    
//	    
//	    
//	    System.out.println("Inizio Fase Scrittura Funzione Obiettivo");
//	    
//	    start = Instant.now().toEpochMilli();
//		line.add(new Line("Inizio Scrittura Funzione obiettivo ",0));
//		
//		cc.writeObj();
//		
//		finish = Instant.now().toEpochMilli();
//		 
//	    //timeElapsed = Duration.between(start, finish).getSeconds();  //in seconds
//	    timeElapsed = finish - start;
//	    line.add(new Line("Fine Scrittura Funzione obiettivo: "+String.valueOf(timeElapsed)+ " milliseconds",0));
//
//	    
//	    
//	    System.out.println("Inizio Fase Caricamento Vincoli");
//	    
//		start = Instant.now().toEpochMilli();
//		
//		testSolver = new Solver("resources/"+aziende[i]+"/"+incontri[k]+"/vincoli.csv",
//				"resources/"+aziende[i]+"/"+incontri[k]+"/preferenze.csv",
//				"resources/"+aziende[i]+"/"+incontri[k]+"/output.txt");
//		
//		line.add(new Line("",0));
//		line.add(new Line("Inizio Caricamento Vincoli",0));
//		
//		testSolver.loadConstraints("resources/"+aziende[i]+"/"+incontri[k]+"/vincoli.csv");
//		
//	   
//		 
//	    finish = Instant.now().toEpochMilli();
//	 
//	    //timeElapsed = Duration.between(start, finish).getSeconds();  //in seconds
//	    timeElapsed = finish - start;
//	    line.add(new Line("Fine Caricamento Vincoli - Tempo Fase: "+String.valueOf(timeElapsed)+ " milliseconds",0));
//	    
//	    line.add(new Line("",0));
//	    
//
//	    System.out.println("Inizio Fase Caricamento Funzione Obiettivo");
//	    
//	    start = Instant.now().toEpochMilli();
//	    
//		line.add(new Line("Inizio Caricamento Funzione Obiettivo",0));
//		
//		
//		testSolver.loadObjectiveCoefficient();
//		
//	    methodToTime();
//		 
//	    finish = Instant.now().toEpochMilli();
//	 
//	    //timeElapsed = Duration.between(start, finish).getSeconds();  //in seconds
//	    timeElapsed = finish - start;
//	    line.add(new Line("Fine Caricamento Funzione Obiettivo - Tempo Fase: "+String.valueOf(timeElapsed)+ " milliseconds",0));
//	    
//	    line.add(new Line("",0));
//
//	    
//	    System.out.println("Inizio Fase Risoluzione Sistema");
//	    
//	    start = Instant.now().toEpochMilli();
//		line.add(new Line("Inizio Elaborazione",0));
//		
//		testSolver.solve();
//		
//	    methodToTime();
//		 
//	    finish = Instant.now().toEpochMilli();
//	 
//	    //timeElapsed = Duration.between(start, finish).getSeconds();  //in seconds
//	    timeElapsed = finish - start;
//	    line.add(new Line("Fine Elaborazione - Tempo Fase: "+String.valueOf(timeElapsed)+ " milliseconds",0));
//	    
//	    line.add(new Line("",0));
//		
//		line.add(new Line("Solver Result: "+ String.valueOf(testSolver.getSolverResult()),0));
//		line.add(new Line("Obj Value:"+String.valueOf(testSolver.getObjRes()),0));
//		//line.add(new Line(String.valueOf(timeElapsed)+ " seconds",0));
//		line.add(new Line("",0));
//
//		
//		results.appendContent(line);
//		
//		line.clear();
//		
//		
//		
//	}

	
	private static void methodToTime() {
//		try {
//			//TimeUnit.SECONDS.sleep(3);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}

}
