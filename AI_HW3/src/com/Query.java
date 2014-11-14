package com;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Query{
	
	//premise of the goal
	String name=null;

	//variables present in goal premise.
	HashMap<String,String> variable = new HashMap<String, String>();
	
	//List of premises which are supposed to be true for the goal to be true.
	List<Query> premises = new ArrayList<Query>();
	
	//Has the goal been achieved.
	boolean conclusion = false;
	
	//What is the 
//	Query conclusion=null;

}
