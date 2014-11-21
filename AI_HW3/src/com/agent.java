package com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class agent {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		BufferedReader reader =null;
		BackwardChaining bc= new BackwardChaining();
		
		
		try {
			reader = new BufferedReader(new FileReader(new File("input.txt")));
			String line=null;
			int counter=0;
			while((line=reader.readLine())!=null)
			{
				counter++;
				switch(counter)
				{
					case 1:
						Query question = bc.parseQuery(line);
						bc.question=question;
						break;
					case 2:
						bc.numOfStatements = Integer.parseInt(line);
						break;
					case 3:
						for(int i=0;i<bc.numOfStatements;i++)
						{
							Query input = bc.parseQuery(line);
							
							bc.insertIntoKB(input);
							
							line= reader.readLine();
							
						}
						break;
				
				}
				
			}
			
			reader.close();
			
			//invoke backward chaining
			bc.startBackwardChain(bc.question);
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("output.txt")));
			if(bc.question.conclusion)
			{
				writer.write("TRUE");
				System.out.println("TRUE");
			}
			else 
			{
				writer.write("FALSE");
				System.out.println("FALSE");
			}
			writer.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("Please check the path specified.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Parsing Exception - Please check the data format in input.txt");
			e.printStackTrace();
		}
		
		
		
	}

}
