import java.util.ArrayList;

/*
* BPARSER CLASS
* Purpose: Attempts to use a PDA to parse from a String, and generate quads based off of precedence rules
* Dependencies: java.util.ArrayList, bToken
* Author: Daniel Feuling
* */

public class bParser 
{
	public bParser() {};
	
	String[] handleStack = new String[100];
	bQuad[] quadStack = new bQuad[100];
	int handleTop = 0;
	int quadTop = 0;
	Integer tempCount = 8;
	//
	String recentVar = "";
	String handleOp = "";
	String handle = "";
	static int[][] precedenceTable = new int[][]
			{
				{0,0,-1,-1,-1,1,1},
				{0,0,-1,-1,-1,1,1},
				{1,1,0,0,-1,1,1,1},
				{1,1,0,0,-1,1,1,1}
			};
	
	public void parseHandle(ArrayList<bToken> tokenList)
	{
		for(int x = 0 ; x < tokenList.size() ; x++)
		{
			//if it's a variable, record what it is in case an assignment comes after
			if(tokenList.get(x).getTokenType() == bToken.bTokenType.VAR)
				this.recentVar = tokenList.get(x).getToken();
			
			//if the current token is an assign, and the previous is a variable, we need to do some work
			if(tokenList.get(x).getTokenType() == bToken.bTokenType.ASSIGN && tokenList.get(x-1).getTokenType() == bToken.bTokenType.VAR)
			{
				int z = x+1;
				//go until we hit a a comma or a semicolon
				while(tokenList.get(z).getTokenType() != bToken.bTokenType.SEMI && tokenList.get(z).getTokenType() != bToken.bTokenType.COMMA)
				{
					handle = handle + tokenList.get(z).getToken();
					z++;
				}
				//remove all spaces
				handle.trim();
				handle.replaceAll("\\s", "");
				System.out.println(recentVar + " is " + handle);
				//pass handle to quadGen
				quadGen(handle);
				handle = "";
			}
		}
	}
	
	//PDA
	public void quadGen(String handle)
	{
		
		handle = handle + ";";
		while(handle.length() > 1)
		{
		
			String currentOp = "";
			String nextOp = "";
			Boolean currentVic = false;
			Boolean nextVic = false;
			int hold = 0;
			int index = 0;
			
			
			while(!currentVic)
			{
				for(int x = 0 ; x < handle.length() ; x++)
				{
					//determine what operation is currently being considered
					switch(determineIncomingCharacter(handle.charAt(x)))
					{
					//+
					case 0:
						currentOp = "+";
						currentVic = true;
						index = x;
						hold = x;
						break;
					//-
					case 1:
						currentOp = "-";
						currentVic = true;
						index = x;
						hold = x;
						break;
					//*
					case 2:
						currentOp = "*";
						currentVic = true;
						index = x;
						hold = x;
						break;
					///
					case 3:
						currentOp = "/";
						currentVic = true;
						index = x;
						hold = x;
						break;
					//;
					case 4:
						currentOp = ";";
						currentVic = true;
						index = x;
						hold = x;
						break;
					default:
						System.out.println("NOPING OUT");
						break;
					}
				}
			}
				
				
			while(!nextVic)
			{
				//determine what the next operation is
				switch(determineIncomingCharacter(handle.charAt(index)))
				{
				//+
				case 0:
					nextOp = "+";
					nextVic = true;
					break;
				//-
				case 1:
					nextOp = "-";
					nextVic = true;
					break;
				//*
				case 2:
					nextOp = "*";
					nextVic = true;
					break;
				///
				case 3:
					nextOp = "/";
					nextVic = true;
					break;
				//;
				case 4:
					nextOp = ";";
					nextVic = true;
					break;
				default:
					index++;
					break;
				}
				
			}
				//determine precedence and action of two operators
				switch(precedenceTable[determineIncomingCharacter(currentOp.charAt(0))][determineIncomingCharacter(nextOp.charAt(0))])
				{
				case -1:
					System.out.println("Yielding");
					break;
				case 0:
					System.out.println("Equal precedence.");
					break;
				case 1:
					this.pushQuad(new bQuad(currentOp.charAt(0), this.handle.substring(hold-1, hold-1), this.handle.substring(hold+1, hold+1), "T".concat(this.tempCount.toString())));
					break;
				default:
					System.out.println("Error with precedence.");
					break;
				}
		
		}
		
	}
	
	public void pushHandle(String handle)
	{
		handleStack[this.handleTop] = handle;
		this.handleTop++;
	}
	
	public void pushQuad(bQuad quad)
	{
		quadStack[this.quadTop] = quad;
		this.quadTop++;
	}
	
	
	public String popHandle()
	{
		this.handleTop--;
		return this.handleStack[handleTop];
	}
	
	public bQuad popQuad()
	{
		this.quadTop--;
		return this.quadStack[quadTop];
	}
	
	public static int determineIncomingCharacter(char incomingChar)
	{
		int classificationToBeReturned = 66;
		
	
		//+
		if(incomingChar == 43)
			classificationToBeReturned = 0;
		
		//-
		if(incomingChar == 45)
			classificationToBeReturned = 1;
		
		//*
		if(incomingChar == 42)
			classificationToBeReturned = 2;
		
		///
		if(incomingChar == 47)
			classificationToBeReturned = 3;
		
		//;
		if(incomingChar == 59)
			classificationToBeReturned = 4;
		
		
		return classificationToBeReturned;
	}
	
}
