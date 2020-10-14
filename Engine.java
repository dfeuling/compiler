
/*
 * ENGINE CLASS
 * Purpose: Driver for the Compiler project, program entry and run point
 * Dependencies: bScanner
 * Author: Daniel Feuling
 */

public class Engine 
{

	public static void main(String[] args) 
	{
		bScanner bScanner = new bScanner();
		System.out.println("--------- INITIAL SCAN AND TOKEN LIST --------");
		bScanner.scan();
		System.out.println("------------ REVISED TOKEN LIST ------------");
		bScanner.showTokens();
		bParser parser = new bParser();
		System.out.println("----------- PARSER -----------");
	}

}
