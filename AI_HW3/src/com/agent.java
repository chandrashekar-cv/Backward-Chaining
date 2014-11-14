package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

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
							
							if(bc.KB.containsKey(input.name))
								bc.KB.get(input.name).add(input);
							else
								bc.KB.put(input.name,(List<Query>) input);
							
							line= reader.readLine();
							
						}
						break;
				
				}
				
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
