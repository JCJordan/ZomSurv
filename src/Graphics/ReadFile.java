package Graphics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile {

	private String path;
	
	public ReadFile(String filePath) {
		this.path = filePath;
	}
	
	public String[] OpenFile() throws IOException {
		
		FileReader fileRead = new FileReader(path);
		BufferedReader textReader = new BufferedReader(fileRead);
		
		int numberOfLines = readLines();
		String[] textData = new String[numberOfLines];
		
		int i;
		
		for (i=0; i < numberOfLines; i++) {
			textData[i] = textReader.readLine();
		}
				
		textReader.close();
		return textData;
		
	}
	
	int readLines() throws IOException {
		
		FileReader dataFile = new FileReader(path);
		BufferedReader textReader = new BufferedReader(dataFile);
		
		String line;
		int numberOfLines = 0;
		
		while ((line = textReader.readLine()) != null) {
			numberOfLines++;
		}
		
		textReader.close();
		
		return numberOfLines;
		
	}
	
	
	
}
