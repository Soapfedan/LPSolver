package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class IOHandler {

	private String filePath;
	
	
	
	public IOHandler(String filepath) {
				
		this.filePath = filepath;		
		File file = new File(this.filePath);
		
	}
	
	public ArrayList<String[]> readFile(){
		
		  BufferedReader br = null;
		  String line = "";
		  String cvsSplitBy = ";";
		  ArrayList<String[]> ret = new ArrayList<String[]>();
		
		  try {
		
		      br = new BufferedReader(new FileReader(this.filePath));
		      while ((line = br.readLine()) != null) {
		
		          // use comma as separator
		          String[] tokens = line.split(cvsSplitBy);	         	          
		          
		          ret.add(tokens);
		
		      }
		
		  } catch (FileNotFoundException e) {
		      e.printStackTrace();
		  } catch (IOException e) {
		      e.printStackTrace();
		  } finally {
		      if (br != null) {
		          try {
		              br.close();
		              
		          } catch (IOException e) {
		              e.printStackTrace();
		          }
		      }
		                
		  }
		  return ret;
	}
	
	
	public ArrayList<String[]> readFile(boolean debug){
		
		  BufferedReader br = null;
		  String line = "";
		  String cvsSplitBy = ";";
		  ArrayList<String[]> ret = new ArrayList<String[]>();
		
		  try {
		
		      br = new BufferedReader(new FileReader(this.filePath));
		      while ((line = br.readLine()) != null) {
		
		          // use comma as separator
		          String[] tokens = line.split(cvsSplitBy);
		          
		          if(debug) {
		        	  for (int i = 0; i < tokens.length; i++) {
			        	  System.out.print(tokens[i]+" ");
			          }
		        	  System.out.println("");
		          }
		          
		          
		          ret.add(tokens);
		
		      }
		
		  } catch (FileNotFoundException e) {
		      e.printStackTrace();
		  } catch (IOException e) {
		      e.printStackTrace();
		  } finally {
		      if (br != null) {
		          try {
		              br.close();
		              
		          } catch (IOException e) {
		              e.printStackTrace();
		          }
		      }
		                
		  }
		  return ret;
	}
	
//	public ArrayList<ArrayList<Integer>> readIntegerFile() {
//			
//	  ArrayList<ArrayList<Integer>> a = new ArrayList<ArrayList<Integer>>();
//	  Scanner input;
//	  int i = 0;
//		input = new Scanner(this.filePath);
//		 while(input.hasNextLine())
//		  {
//		      Scanner colReader = new Scanner(input.nextLine());
//		      ArrayList col = new ArrayList();
//		      while(colReader.hasNextInt())
//		      {
//		    	  Integer number = colReader.nextInt();
//		    	  //System.out.println(number);
//		          col.add(number);
//		      }
//		      a.add(col);
//		      
//		  }
//		 
//		 return a;
//			 
//    }
	  	
	
	public void appendContent(ArrayList<Line> content) {
		
		LinesWriter wr;
		
		try {
			wr = new LinesWriter(this.filePath,true);
			wr.write(content);
			wr.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	public void writeContent(ArrayList<Line> content) {
		
		LinesWriter wr;
		
		try {
			wr = new LinesWriter(this.filePath,false);
			wr.write(content);
			wr.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public String getFilePath() {
		return filePath;
	}
	
	
}
