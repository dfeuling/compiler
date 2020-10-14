import java.io.File;
import java.util.Scanner;
import java.io.PrintWriter;
import java.util.ArrayList;

/*
 * BSCANNER CLASS
 * Purpose: Read input from a file, process the input to determine tokens based on grammar, create a token list based on those tokens, construct a symbol table based on the token list
 * Dependencies: java.util.Scanner, a native Java class responsible for reading system input | java.io.File, a native Java class responsible for File operations
 * Author: Daniel Feuling
 * Last Modified: 3/20/20
 * 
 * MEMBER DATA
 * constant int TOKENSTATEROWS: Number of rows in state table to use in scanning for tokens; rows represent possible states
 * constant int TOKENSTATECOLUMNS: Number of columns in state table to use in scanning for tokens; columns represent possible inputs
 * int tokenState: Holds the current index to determine the state of the token table
 * int lineCount: Holds the current number of lines that have been scanned, assists in error reporting while scanning
 * int charCount: Holds the current number of characters scanned in a line, assists in error reporting while scanning
 * Scanner read: Responsible for taking input from the system, reading from a file in this instance is preferred -- console usage is possible not not recommended
 * File input: Contains input data for program to read from
 * File output: 
 * 
 * METHODS
 * 
 * public void init()
 * Purpose: Initialize arrays appropriately, set-up files and their inputs / outputs in an isolated environment that allows for error handling
 * Dependencies: java.io.File, java.io.Scanner, java.io.PrintWriter
 * Author: Daniel Feuling
 * 
 * 
 * public int determineIncomingCharacter(char incomingChar)
 * Purpose: Takes a character as input, and returns an integer that represents what group that character belongs to based on grammar, explicit classifications follow, '|' delimiting:
 * 0: Space | 1: Letter | 2: Digit | 3: * | 4: / | 5: = | 6: < | 7: > | 8: , | 9: + | 10: = | 11: { | 12: } | 13: )( | 14: ) | In the case of an invalid character, 66 is returned.
 * Dependencies: None
 * Author: Daniel Feuling
 * 
 * 
 * public void tokenize()
 * Purpose: Token List creation and population
 * Dependencies: java.util.ArrayList, java.util.Scanner
 * Author: Daniel Feuling
 * 
 * 
 * public int determineIncomingSymbol(bToken token)
 * Purpose: Takes a taken as a parameter, and returns an integer representing the symbol
 * Dependencies: bToken
 * Author: Daniel Feuling
 * 
 * 
 * public void symbolize(ArrayList<bToken> tokenlist)
 * Purpose: Symbol list creation and population, uses the tokenlist that has been made
 * Dependencies: java.util.ArrayList, bToken, bSymbol
 * Author: Daniel Feuling
 * 
 * public void showTokens()
 * Purpose: Tool function, displays a token list to the console
 * Dependencies: None
 * Author: Daniel Feuling
 * 
 * public void showSymbolTable()
 * Purpose: Tool function, displays the token list to the console
 * Dependencies: None
 * Author: Daniel Feuling
 * 
 * public void scan
 * Purpose: Tool function, responsible for running comprehensive scanning operations
 * Dependencies: None
 * Author: Daniel Feuling
*/

public class bScanner
{
	bScanner()
	{};
	
	final int TOKENSTATEROWS = 36;
	final int TOKENSTATECOLUMNS = 15;
	int csIndex = 0;
	int dsIndex = 0;
	File input;
	File output;
	File errors;
	Scanner read;
	PrintWriter write;
	PrintWriter writeError;
	ArrayList<bToken> tokenList = new ArrayList<bToken>();
	ArrayList<bSymbol> symbolTable = new ArrayList<bSymbol>();
	
	public void init() 
	{
		this.input = new File("inputs.txt");
		this.output = new File("outputs.txt");
		this.errors = new File("errors.txt");
		
		if(!this.output.exists())
		{
			try {this.output.createNewFile();}
			catch(Exception e) {System.out.println("Error creating output file."); System.exit(1);}
			System.exit(1);
		}
		
		if(!this.errors.exists())
		{
			try {this.errors.createNewFile();}
			catch(Exception e) {System.out.println("Error creating errors file (Ironic, yes?)."); System.exit(1);}
		}
		
		try
		{this.read = new Scanner(input);}
		catch(Exception e) {System.out.println("Error in initialization of input file! File not found."); System.exit(1);};
		
		try
		{this.write = new PrintWriter(output);}
		catch(Exception e) {System.out.println("Error in initialization of output file! File not found."); System.exit(1);};
		
		try
		{this.writeError = new PrintWriter(errors);}
		catch(Exception e) {System.out.println("Error in initialization of error file! File not found."); System.exit(1);};
	
	}
	
	public int determineIncomingCharacter(char incomingChar)
	{
		int classificationToBeReturned = 66;
		
		//space
		if(incomingChar == ' ') 
			classificationToBeReturned = 0;
		
		//letter
		if((incomingChar >= 65 && incomingChar <= 90) || (incomingChar >= 97 && incomingChar <= 122))
			classificationToBeReturned = 1;
		
		//digit
		if(incomingChar >= 48 && incomingChar <= 57)
			classificationToBeReturned = 2;
		
		//*
		if(incomingChar == 42)
			classificationToBeReturned = 3;
		
		///
		if(incomingChar == 47)
			classificationToBeReturned = 4;
		
		//=
		if(incomingChar == 61)
			classificationToBeReturned = 5;
		
		//<
		if(incomingChar == 60)
			classificationToBeReturned = 6;
		
		//>
		if(incomingChar == 62)
			classificationToBeReturned = 7;
		
		//,
		if(incomingChar == 44)
			classificationToBeReturned = 8;
		
		//+
		if(incomingChar == 43)
			classificationToBeReturned = 9;
		
		//-
		if(incomingChar == 45)
			classificationToBeReturned = 10;
		
		//{
		if(incomingChar == 123)
			classificationToBeReturned = 11;
		
		//}
		if(incomingChar == 125)
			classificationToBeReturned = 12;
		
		//(
		if(incomingChar == 40)
			classificationToBeReturned = 13;
		
		//)
		if(incomingChar == 41)
			classificationToBeReturned = 14;

		//;
		if(incomingChar == 59)
			classificationToBeReturned = 15;
		
		return classificationToBeReturned;
	}

	//token list creation and population
	public void tokenize()
	{
		boolean victory = false;
		int tokenState = 0;
		int charCount = 0;
		int lineCount = 0;
		Character nextToken = ' ';
		String tokenLine = "";
		String tokenSoFar = "";
		
		//as long as there's another line
			//as long as a final state hasn't been reached
				//read the string character by character, when a final state is reached, take the string so far and the type and output
		while(read.hasNextLine())
		{
			
			tokenLine = read.nextLine();
			tokenLine = tokenLine.trim();
			tokenLine = tokenLine + " ";
			lineCount++;
			charCount = 0;
			while(charCount < tokenLine.length()-1)
			{	
				
				if(!victory)
				{		
				nextToken = (Character)tokenLine.charAt(charCount);
				charCount++;
				}
				victory = false;
				
				
				switch(bToken.tokenStateTable[tokenState][determineIncomingCharacter(nextToken)])
				{
					case 0: 
						tokenState = bToken.tokenStateTable[tokenState][determineIncomingCharacter(nextToken)];
						tokenSoFar = tokenSoFar.concat(nextToken.toString());
						break;
					case 1:
						tokenState = bToken.tokenStateTable[tokenState][determineIncomingCharacter(nextToken)];
						tokenSoFar = tokenSoFar.concat(nextToken.toString());
						break;
					case 2: 
						//VAR
						victory = true;
						tokenSoFar = tokenSoFar.trim();
						
						//account for special cases of words that are if and while statements, const and var declarations
						//this is done because the grammar dictates that a mix of numbers and letters is a var, so reserved words start as type var
						if(tokenSoFar.equalsIgnoreCase("if"))
							tokenList.add(new bToken(tokenSoFar,1001));
						else if(tokenSoFar.equalsIgnoreCase("while"))
							tokenList.add(new bToken(tokenSoFar,1002));
						else if(tokenSoFar.equalsIgnoreCase("const"))
							tokenList.add(new bToken(tokenSoFar,1003));
						else if(tokenSoFar.equalsIgnoreCase("var"))
							tokenList.add(new bToken(tokenSoFar,1004));
						else
						tokenList.add(new bToken(tokenSoFar,2));
						
						tokenSoFar = "";
						tokenState = 0;
						break;
					case 3:
						tokenState = bToken.tokenStateTable[tokenState][determineIncomingCharacter(nextToken)];
						tokenSoFar = tokenSoFar.concat(nextToken.toString());
						break;
					case 4: 
						//LIT
						victory = true;
						tokenSoFar = tokenSoFar.trim();
						tokenList.add(new bToken(tokenSoFar,4));
						tokenSoFar = "";
						tokenState = 0;
						break;
					case 5:
						tokenState = bToken.tokenStateTable[tokenState][determineIncomingCharacter(nextToken)];
						tokenSoFar = tokenSoFar.concat(nextToken.toString());
						break;
					case 6:
						//MOP
						victory = true;
						tokenSoFar = tokenSoFar.trim();
						tokenList.add(new bToken(tokenSoFar,6));
						tokenSoFar = "";
						tokenState = 0;
						break;
					case 7:
						tokenState = bToken.tokenStateTable[tokenState][determineIncomingCharacter(nextToken)];
						tokenSoFar = tokenSoFar.concat(nextToken.toString());
						break;
					case 8:
						tokenState = bToken.tokenStateTable[tokenState][determineIncomingCharacter(nextToken)];
						tokenSoFar = tokenSoFar.concat(nextToken.toString());
						break;
					case 9:
						tokenState = bToken.tokenStateTable[tokenState][determineIncomingCharacter(nextToken)];
						tokenSoFar = tokenSoFar.concat(nextToken.toString());
						break;
					case 10:
						//MOP
						victory = true;
						tokenSoFar = tokenSoFar.trim();
						tokenList.add(new bToken(tokenSoFar,10));
						tokenSoFar = "";
						tokenState = 0;
						break;
					case 11:
						tokenState = bToken.tokenStateTable[tokenState][determineIncomingCharacter(nextToken)];
						tokenSoFar = tokenSoFar.concat(nextToken.toString());
						break;
					case 12:
						//RELOP
						tokenSoFar = tokenSoFar.trim();
						tokenList.add(new bToken(tokenSoFar + nextToken,12));
						tokenSoFar = "";
						tokenState = 0;
						break;
					case 13:
						//ASSIGN
						victory = true;
						tokenSoFar = tokenSoFar.trim();
						tokenList.add(new bToken(tokenSoFar,13));
						tokenSoFar = "";
						tokenState = 0;
						break;
					case 14:
						tokenState = bToken.tokenStateTable[tokenState][determineIncomingCharacter(nextToken)];
						tokenSoFar = tokenSoFar.concat(nextToken.toString());
						break;
					case 15:
						//RELOP
						tokenSoFar = tokenSoFar.trim();
						tokenList.add(new bToken(tokenSoFar + nextToken,15));
						tokenSoFar = "";
						tokenState = 0;
						charCount++;
						break;
					case 16:
						//RELOP
						victory = true;
						tokenSoFar = tokenSoFar.trim();
						tokenList.add(new bToken(tokenSoFar,16));
						tokenSoFar = "";
						tokenState = 0;
						break;
					case 17:
						tokenState = bToken.tokenStateTable[tokenState][determineIncomingCharacter(nextToken)];
						tokenSoFar = tokenSoFar.concat(nextToken.toString());
						break;
					case 18:
						//RELOP
						tokenSoFar = tokenSoFar.trim();
						tokenList.add(new bToken(tokenSoFar + nextToken,18));
						tokenSoFar = "";
						tokenState = 0;
						charCount++;
						break;
					case 19:
						//RELOP
						victory = true;
						tokenSoFar = tokenSoFar.trim();
						tokenList.add(new bToken(tokenSoFar,19));
						tokenSoFar = "";
						tokenState = 0;
						break;
					case 20:
						tokenState = bToken.tokenStateTable[tokenState][determineIncomingCharacter(nextToken)];
						tokenSoFar = tokenSoFar.concat(nextToken.toString());
						break;
					case 21:
						//COMMA
						victory = true;
						tokenSoFar = tokenSoFar.trim();
						tokenList.add(new bToken(tokenSoFar,21));
						tokenSoFar = "";
						tokenState = 0;
						break;
					case 22:
						tokenState = bToken.tokenStateTable[tokenState][determineIncomingCharacter(nextToken)];
						tokenSoFar = tokenSoFar.concat(nextToken.toString());
						break;
					case 23:
						//ADDOP
						victory = true;
						tokenSoFar = tokenSoFar.trim();
						tokenList.add(new bToken(tokenSoFar,23));
						tokenSoFar = "";
						tokenState = 0;
						break;
					case 24:
						tokenState = bToken.tokenStateTable[tokenState][determineIncomingCharacter(nextToken)];
						tokenSoFar = tokenSoFar.concat(nextToken.toString());
						break;
					case 25:
						//ADDOP
						victory = true;
						tokenSoFar = tokenSoFar.trim();
						tokenList.add(new bToken(tokenSoFar,25));
						tokenSoFar = "";
						tokenState = 0;
						break;
					case 26:
						//ERROR
						victory = true;
						tokenSoFar = tokenSoFar.trim();
						tokenList.add(new bToken(tokenSoFar,26));
						tokenSoFar = "";
						tokenState = 0;
						break;
					case 27:
						//COMMENT
						tokenSoFar = tokenSoFar.trim();
						tokenList.add(new bToken(tokenSoFar,27));
						tokenSoFar = "";
						tokenState = 0;
						break;
					case 28:
						tokenState = bToken.tokenStateTable[tokenState][determineIncomingCharacter(nextToken)];
						tokenSoFar = tokenSoFar.concat(nextToken.toString());
						break;
					case 29:
						//$LB
						victory = true;
						tokenSoFar = tokenSoFar.trim();
						tokenList.add(new bToken(tokenSoFar,29));
						tokenSoFar = "";
						tokenState = 0;
						break;
					case 30:
						tokenState = bToken.tokenStateTable[tokenState][determineIncomingCharacter(nextToken)];
						tokenSoFar = tokenSoFar.concat(nextToken.toString());
						break;
					case 31:
						//$RB
						victory = true;
						tokenSoFar = tokenSoFar.trim();
						tokenList.add(new bToken(tokenSoFar,31));
						tokenSoFar = "";
						tokenState = 0;
						break;
					case 32:
						tokenState = bToken.tokenStateTable[tokenState][determineIncomingCharacter(nextToken)];
						tokenSoFar = tokenSoFar.concat(nextToken.toString());
						break;
					case 33:
						//$LP
						victory = true;
						tokenSoFar = tokenSoFar.trim();
						tokenList.add(new bToken(tokenSoFar,33));
						tokenSoFar = "";
						tokenState = 0;
						break;
					case 34:
						tokenState = bToken.tokenStateTable[tokenState][determineIncomingCharacter(nextToken)];
						tokenSoFar = tokenSoFar.concat(nextToken.toString());
						break;
					case 35:
						//$RP
						victory = true;
						tokenSoFar = tokenSoFar.trim();
						tokenList.add(new bToken(tokenSoFar,35));
						tokenSoFar = "";
						tokenState = 0;
						break;
					case 36:
						tokenState = bToken.tokenStateTable[tokenState][determineIncomingCharacter(nextToken)];
						tokenSoFar = tokenSoFar.concat(nextToken.toString());
						break;
					case 37:
						//SEMI
						victory = true;
						tokenSoFar = tokenSoFar.trim();
						tokenList.add(new bToken(tokenSoFar,37));
						tokenSoFar = "";
						tokenState = 0;
						break;
					default:
						writeError.println("Error at line number " + lineCount + ", character number " + charCount + ". Error character is " + nextToken);

				}
				
			}
		}

		
	}

	public int determineIncomingSymbol(bToken token)
	{
		int incomingSymbol = 1000;
		
		if(token.getTokenType() == bToken.bTokenType.$VAR)
			incomingSymbol = 0;
		else if(token.getTokenType() == bToken.bTokenType.$CONST)
			incomingSymbol = 1;
		else if(token.getTokenType() == bToken.bTokenType.$WHILE)
			incomingSymbol = 2;
		else if(token.getTokenType() == bToken.bTokenType.$IF)
			incomingSymbol = 3;
		else if(token.getTokenType() == bToken.bTokenType.$LB)
			incomingSymbol = 4;
		else if(token.getTokenType() == bToken.bTokenType.$RB)
			incomingSymbol = 5;
		else if(token.getTokenType() == bToken.bTokenType.SEMI)
			incomingSymbol = 6;
		else if(token.getTokenType() == bToken.bTokenType.VAR)
			incomingSymbol = 7;
		else if(token.getTokenType() == bToken.bTokenType.COMMA)
			incomingSymbol = 8;
		else if(token.getTokenType() == bToken.bTokenType.LIT)
			incomingSymbol = 9;
		else if(token.getTokenType() == bToken.bTokenType.ASSIGN)
			incomingSymbol = 10;
		else
			incomingSymbol = 11;
		
		return incomingSymbol;
	}
	
	//symbol list creation and population
	public void symbolize(ArrayList<bToken> tokenList)
	{
		int symbolState = 0;
		int z = 0;
		int incomingSymbol = 0;
		String thisVar = "";
		String thisLit = "";
		
		while(z < tokenList.size())
		{
			incomingSymbol = determineIncomingSymbol(tokenList.get(z));
			switch(bSymbol.symbolStateTable[symbolState][incomingSymbol])
			{
			case 1:
				tokenList.get(z).setTokenType(bToken.bTokenType.$CLASS);
				symbolState = bSymbol.symbolStateTable[symbolState][incomingSymbol];
				break;
			case 2:
				thisVar = tokenList.get(z).getToken();
				symbolTable.add(new bSymbol(thisVar,bToken.bTokenType.$PNAME,"",0));
				tokenList.get(z).setTokenType(bToken.bTokenType.$PNAME);
				symbolState = bSymbol.symbolStateTable[symbolState][incomingSymbol];
				thisVar = "";
				break;
			case 3:
				symbolState = bSymbol.symbolStateTable[symbolState][incomingSymbol];
				break;
			case 4:
				symbolState = bSymbol.symbolStateTable[symbolState][incomingSymbol];
				break;
			case 5:
				thisVar = tokenList.get(z).getToken();
				symbolState = bSymbol.symbolStateTable[symbolState][incomingSymbol];
				break;
			case 6:
				symbolState = bSymbol.symbolStateTable[symbolState][incomingSymbol];
				break;
			case 7:
				thisLit = String.valueOf(tokenList.get(z).getToken());
				symbolTable.add(new bSymbol(thisVar,bToken.bTokenType.$CONST,thisLit,1));
				symbolState = bSymbol.symbolStateTable[symbolState][incomingSymbol];
				thisVar = "";
				thisLit = "";
				break;
			case 8:
				
				symbolState = bSymbol.symbolStateTable[symbolState][incomingSymbol];
				break;
			case 9:
				thisVar = tokenList.get(z).getToken();
				symbolTable.add(new bSymbol(thisVar,bToken.bTokenType.$VAR,thisLit,1));
				symbolState = bSymbol.symbolStateTable[symbolState][incomingSymbol];
				thisVar = "";
				break;
			case 10:
				if(tokenList.size() == z)
				{
					symbolState = 12;
				}
				else
				{
					symbolState = bSymbol.symbolStateTable[symbolState][incomingSymbol];
				}
				break;
			case 11:
				if(tokenList.size() == z)
				{
					symbolState = 12;
				}
				else
				{
					symbolTable.add(new bSymbol(tokenList.get(z).getToken(),bToken.bTokenType.LIT,tokenList.get(z).getToken(),1));
					symbolState = bSymbol.symbolStateTable[symbolState][incomingSymbol];
				}
				break;
			case 12:
				//final state
				break;
			case 13:
				System.out.println("Error generating Symbol table.");
				System.exit(1);
				break;
			}
			z++;
		}
	}
	
	public void showTokens()
	{
		for(int x = 0 ; x < this.tokenList.size() ; x++)
		this.tokenList.get(x).tokenToConsole();
	}

	public void showSymbolTable()
	{
		System.out.println("----------- SYMBOL TABLE ---------");
		for(int x = 0 ; x < this.symbolTable.size() ; x++)
		{
			System.out.println(this.symbolTable.get(x).token + ", " + this.symbolTable.get(x).type + ", " + this.symbolTable.get(x).value + ", " + this.symbolTable.get(x).address + ", " + this.symbolTable.get(x).segment + ".");
		}
	}
	
	public void scan()
	{
		this.init();
		this.tokenize();
		this.symbolize(this.tokenList);
		this.showTokens();
		this.showSymbolTable();
	}
	
}
