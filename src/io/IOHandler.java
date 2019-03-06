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
	
	public ArrayList<String[]> readFile(String signSplitBy){
		
		  BufferedReader br = null;
		  String line = "";
		  ArrayList<String[]> ret = new ArrayList<String[]>();
		
		  try {
		
		      br = new BufferedReader(new FileReader(this.filePath));
		      while ((line = br.readLine()) != null) {
		
		          // use comma as separator
		          String[] tokens = line.split(signSplitBy);	         	          
		          
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

	
	
	public void appendContent(ArrayList<String> content) {
		
		FileWriter wr;
		
		try {
			wr = new FileWriter(this.filePath,true);
			for (String l: content) {
				wr.write(l);
			}
			wr.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	public void writeContent(ArrayList<String> content) {
		
		FileWriter wr;
		
		try {
			wr = new FileWriter(this.filePath,false);
			for (String l: content) {
				wr.write(l);
			}
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
