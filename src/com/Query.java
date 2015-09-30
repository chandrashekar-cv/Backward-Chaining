package com;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class Query{
	
	//premise of the goal
	String name=null;

	//variables present in goal premise.
	Map<String,String> variable = new LinkedHashMap<String, String>();
	
	//List of premises which are supposed to be true for the goal to be true.
	List<Query> premises = new ArrayList<Query>();
	
	//Has the goal been achieved.
	boolean conclusion = false;
	
	//What is the 
//	Query conclusion=null;

}
