package io;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class LinesWriter extends FileWriter{


	public LinesWriter(String filepath,boolean append) throws IOException {
		super(filepath,append);		
	}
	
	public void write(ArrayList<Line> c) {
		
		try {
			for (Line l: c) {
				super.write(l.getContent());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		
		try {
			super.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
}
