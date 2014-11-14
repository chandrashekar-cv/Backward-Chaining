package com;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BackwardChaining {
	
	int numOfStatements;
	Query question = null;
	Map<String,List<Query>> KB = new HashMap<String,List<Query>>();
	

	Query parseQuery(String queryString)
	{	
		String[] data = queryString.split("&|=>");
		Query input = createPremise(data[data.length-1]);
		
		for (int i =0;i<data.length-1;i++) {
			System.out.println(data[i]);
			input.premises.add(createPremise(data[i]));
		}
	
		return input;
	}
	
	Query createPremise(String premise)
	{
		Query pObj = new Query();
		String name = premise.substring(0, premise.indexOf("("));
		pObj.name = name;
		
		String[] variables= premise.substring(premise.indexOf("("),premise.lastIndexOf(")")).split(",");
		
		for (String var : variables) {
			String value=null;
			
			if(((int)var.charAt(0))>96)
				value="";
			else 
				value=var;
			
			pObj.variable.put(var,value);
		}
		
		return pObj;
	}
	
}
