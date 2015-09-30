package com;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class BackwardChaining {
	
	int numOfStatements;
	Query question = null;
	Map<String,List<Query>> KB = new HashMap<String,List<Query>>();
	

	void startBackwardChain(Query goal)
	{
		String name= goal.name;
		boolean valid= false;
		
		//List of sentences in KB with the same name as the goal which can be potentially used to prove the truth value of the goal. 
		List<Query> possibleGoals = KB.get(name);
		
		//iterating through possiblities
		for(int i=0;possibleGoals!=null && i<possibleGoals.size();i++)
		{
			Query eachPossbileGoal = possibleGoals.get(i);
			
			if(unify(eachPossbileGoal,goal))
			{	
				if(eachPossbileGoal.variable.containsKey("x") && eachPossbileGoal.variable.get("x")!=null)
					standardizeVariables(eachPossbileGoal, goal);
				
				if(!FOL_BC_AND(eachPossbileGoal,goal))
					valid=false;
				else
				{
					valid=true;
					break;
				}
			}
		}	
		
		if(valid)
		{
			goal.conclusion = valid;
			if(!checkIfInKB(goal))
				insertIntoKB(goal);
		}
	}
	
	boolean FOL_BC_AND(Query premise,Query goal)
	{
		
		if(premise.conclusion)
			return premise.conclusion;
		
		if(premise.premises.size()==0 && !premise.variable.containsKey("x"))
		{
			if(goal.variable.get("x")==null || goal.variable.get("x").equals("") )
				premise.conclusion = true;
			else
				premise.conclusion=	checkForGoal(premise, goal);
			
			return premise.conclusion;
		}
		
		List<Query> LHS = premise.premises;
		boolean allTrue = true;
		for(int j=0;j<LHS.size();j++)
		{
			Query prem = LHS.get(j);
			startBackwardChain(prem);
			if(allTrue && !prem.conclusion )
				allTrue = false;
			
		}
		
		premise.conclusion = allTrue;
			
		return premise.conclusion;
	}
	
	
	boolean unify(Query possibleGoal, Query goal)
	{
		if(possibleGoal.variable.size() != goal.variable.size())
			return false;
		
		Set<String> pgKeySet = possibleGoal.variable.keySet();
		Set<String>gSet = goal.variable.keySet();
		
		Iterator<String> itr1 = pgKeySet.iterator();
		Iterator<String> itr2 = gSet.iterator();
		while(itr1.hasNext() && itr2.hasNext())
		{
			String pgKey = itr1.next();
			String goalKey = itr2.next();
			String pgValue = possibleGoal.variable.get(pgKey);
			String goalValue = goal.variable.get(goalKey);
			
			
			if((goalKey.equals("x") && !pgKey.equals("x")))
			{
				if((goalValue==null || goalValue.equals("")) || goalValue.equals(pgValue))
					continue;
				else
					return false;
			}		
			else if(!goalKey.equals("x") && !pgKey.equals("x"))
			{
				if(goalValue.equals(pgValue))
					continue;
				else 
					return false;
			}
			else if(goalKey.equals("x") && pgKey.equals("x"))
			{
				if(goalValue==null || pgValue==null || goalValue.equals("") || pgValue.equals(""))
					continue;
				else
				{
					if(goalValue.equals(pgValue))
						continue;
					else
						return false;
				}
			}
			else if(!goalKey.equals("x") && pgKey.equals("x"))
				continue;
			else
				return false;
			
		}
		
		return true;
	}
	
	void standardizeVariables(Query premise, Query goal)
	{
		
		Set<String> pKey = premise.variable.keySet();
		Set<String> gKey = goal.variable.keySet();
		
		Iterator<String> premitr1 = pKey.iterator();
		Iterator<String> goalitr2 = gKey.iterator();
		while(premitr1.hasNext() && goalitr2.hasNext())
		{
			String t1 = premitr1.next();
			String t2 = goal.variable.get(goalitr2.next());
			
			if(t1.equals("x"))
				premise.variable.put(t1,t2);
			
		}
		
		String xValue = premise.variable.get("x");
		
		for(int i=0;i<premise.premises.size();i++)
		{
			if(premise.premises.get(i).variable.containsKey("x"))
			{
				premise.premises.get(i).variable.put("x",xValue);
			}
		}
		
	}
	
	void insertIntoKB(Query input)
	{
		if(KB.containsKey(input.name))
			KB.get(input.name).add(input);
		else
		{
			List<Query> list = new ArrayList<Query>();
			list.add(input);
			KB.put(input.name,list);
		}
	}
	
	boolean checkIfInKB(Query goal)
	{
		boolean present = false;
		List<Query> ListinKB = KB.get(goal.name);
		for(int i=0;i<ListinKB.size();i++)
		{
			Query temp = ListinKB.get(i);
			if(unify(temp, goal) && temp.conclusion)
			{
				present = true;
				break;
			}
				
		}
		
		return present;
	}
	
	boolean checkForGoal(Query premise, Query goal)
	{
		Set<String> pKey = premise.variable.keySet();
		Set<String> gKey = goal.variable.keySet();
		
		Iterator<String> premitr1 = pKey.iterator();
		Iterator<String> goalitr2 = gKey.iterator();
		while(premitr1.hasNext() && goalitr2.hasNext())
		{
			String t1 = premise.variable.get(premitr1.next());
			String t2 = goal.variable.get(goalitr2.next());
			
			if(!t1.equals(t2))
				return false;
			
		}
		return true;
	}
	
	
	Query parseQuery(String queryString)
	{	
		String[] data = queryString.split("&|=>");
		Query input = createPremise(data[data.length-1]);
		
		for (int i =0;i<data.length-1;i++) {
			
			input.premises.add(createPremise(data[i]));
		}
	
		return input;
	}
	
	Query createPremise(String premise)
	{
		Query pObj = new Query();
		String name = premise.substring(0, premise.indexOf("("));
		pObj.name = name;
		
		String[] variables= premise.substring(premise.indexOf("(")+1,premise.lastIndexOf(")")).split(",");
		
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
