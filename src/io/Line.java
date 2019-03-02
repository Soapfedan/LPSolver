package io;

public class Line {

	private String content;
		
	public Line(String text) {
		this.content = text;
	}

	/**
	 * 
	 * @param text	- data to write
	 * @param manipulate - Indicate which string manipulation is needed
	 *        0 = add new line
	 * 	      1 = add right whitespace
	 * 	      2 = add left whitespace
	 *        3 = add both whitespace
	 *         
	 */
	public Line(String text, int manipulate) {
		
		switch (manipulate) {
			
			case 0:
				text += "\n";
				break;
				
			case 1:
				text += " ";
				break;
				
			case 2:
				text = " " + text;
				break;
				
			case 3:
				text = " " + text + " ";
				break;
				
		}
		
		this.content = text;
	}

	public String getContent() {
		return content;
	}
	
	
	
}
