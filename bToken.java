
/*
 * BTOKEN CLASS
 * Purpose: Binds tokens and their token types together as one unit, makes reading / writing more simplistic and understandable for other package operations
 * Dependencies: None
 * Author: Daniel Feuling
 * 
 * BTOKENTYPE ENUM
 * Purpose: Constrain the possible token types to a certain range of standardized possibilities, aid in readability / writability / clarity
 * Dependencies: bToken class
 * Author: Daniel Feuling
*/


public class bToken 
{
	bToken(){};
	bToken(String Token, int Type)
	{
		token = Token;
		switch(Type)
		{
			case 2:
				this.tokenType = bTokenType.VAR;
				break;
			case 4:
				this.tokenType = bTokenType.LIT;
				break;
			case 6:
				this.tokenType = bTokenType.MOP;
				break;
			case 10:
				this.tokenType = bTokenType.MOP;
				break;
			case 12:
				this.tokenType = bTokenType.RELOP;
				break;
			case 13:
				this.tokenType = bTokenType.ASSIGN;
				break;
			case 15:
				this.tokenType = bTokenType.RELOP;
				break;
			case 16:
				this.tokenType = bTokenType.RELOP;
				break;
			case 18:
				this.tokenType = bTokenType.RELOP;
				break;
			case 19:
				this.tokenType = bTokenType.RELOP;
				break;
			case 21:
				this.tokenType = bTokenType.COMMA;
				break;
			case 23:
				this.tokenType = bTokenType.ADDOP;
				break;
			case 25:
				this.tokenType = bTokenType.ADDOP;
				break;
			case 26:
				this.tokenType = bTokenType.ERROR;
				break;
			case 27:
				this.tokenType = bTokenType.COMMENT;
				break;
			case 29:
				this.tokenType = bTokenType.$LB;
				break;
			case 31:
				this.tokenType = bTokenType.$RB;
				break;
			case 33:
				this.tokenType = bTokenType.$LP;
				break;
			case 35:
				this.tokenType = bTokenType.$RP;
			break;
			case 37:
				this.tokenType = bTokenType.SEMI;
			break;
			
			//SPECIAL CASES
			case 1001:
				this.tokenType = bTokenType.$IF;
			break;
			case 1002:
				this.tokenType = bTokenType.$WHILE;
			break;
			case 1003:
				this.tokenType = bTokenType.$CONST;
			break;
			case 1004:
				this.tokenType = bTokenType.$VAR;
			break;
			case 1005:
				this.tokenType = bTokenType.$VAR;
			break;
				
		}
		System.out.println("bToken created with token " + this.token + " and type " + this.tokenType);
	}
	
	enum bTokenType {VAR,LIT,MOP,RELOP,ASSIGN,COMMA,ADDOP,ERROR,COMMENT,$LB,$RB,$LP,$RP,SEMI,$CLASS,$PNAME,$IF,$WHILE,$CONST,$VAR;}
	public bTokenType tokenType;
	public String token = "";
	
	static int[][] tokenStateTable = new int[][]
		{
			{0,1,3,5,7,11,14,17,20,22,24,28,30,32,34,36}, //R0, START
			{2,1,1,2,2,2,2,2,2,2,2,2,2,2,2,2}, //R1, LETTERDIGIT
			{2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2}, //R2, FINAL STATE <VAR>
			{4,4,3,4,4,4,4,4,4,4,4,4,4,4,4,4}, //R3, DIGIT
			{4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4}, //R4, FINAL STATE <LIT>
			{6,6,6,26,26,26,26,26,26,26,6,26,26,6,6,26}, //R5, STAR
			{6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6}, //R6, FINAL STATE <MOP>
			{10,10,10,8,26,26,26,26,26,26,26,26,26,10,10,26}, //R7, SLASH
			{8,8,8,9,26,8,8,8,8,8,8,8,8,8,8,26}, //R8, /* COMMENT
			{9,9,9,9,27,9,9,9,9,9,9,9,9,9,9,26}, //R9, /* COMMENT *
			{10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10}, //R10, FINAL STATE <MOP>
			{13,13,13,26,26,12,26,26,26,26,13,26,26,13,13,26}, //R11, EQUAL
			{12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12}, //R12, FINAL STATE <RELOP>
			{13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13}, //R13, FINAL STATE <ASSIGN>
			{16,16,16,26,26,15,26,26,26,26,16,26,26,16,16,26}, //R14, LESS THAN
			{15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15}, //R15, FINAL STATE <RELOP>
			{16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16}, //R16, FINAL STATE <RELOP>
			{19,19,19,26,26,18,26,26,26,26,19,26,26,19,19,26}, //R17, GREATER THAN
			{18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18}, //R18, FINAL STATE <RELOP>
			{19,19,19,19,19,19,19,19,19,19,19,19,19,19,19,19}, //R19, FINAL STATE <RELOP>
			{21,21,21,26,26,26,26,26,26,26,26,26,26,26,26,26}, //R20, COMMA
			{21,21,21,21,21,21,21,21,21,21,21,21,21,21,21,21}, //R21, FINAL STATE <COMMA>
			{23,23,23,26,26,26,26,26,26,22,26,26,26,26,26,26}, //R22, ADD
			{23,23,23,23,23,23,23,23,23,23,23,23,23,23,23,23}, //R23, FINAL STATE <ADDOP>
			{25,25,25,26,26,26,26,26,26,26,26,26,26,26,26,26}, //R24, SUB
			{25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25}, //R25, FINAL STATE <ADDOP>
			{26,26,26,26,26,26,26,26,26,26,26,26,26,26,26,26}, //R26, FINAL STATE <ERROR>
			{27,27,27,27,27,27,27,27,27,27,27,27,27,27,27,27}, //R27, FINAL STATE <COMMENT>
			{29,29,29,26,26,26,26,26,26,26,26,26,26,26,26,26}, //R28, LB
			{29,29,29,29,29,29,29,29,29,29,29,29,29,29,29,29}, //R29, FINAL STATE <$LB>
			{31,31,31,26,26,26,26,26,26,26,26,26,26,26,26,26}, //R30, RB
			{31,31,31,31,31,31,31,31,31,31,31,31,31,31,31,31}, //R31, FINAL STATE <$RB>
			{33,33,33,26,26,26,26,26,26,26,33,26,26,33,26,26}, //R32, LP
			{33,33,33,33,33,33,33,33,33,33,33,33,33,33,33,33}, //R33, FINAL STATE <$LP>
			{35,35,35,35,35,26,26,26,35,35,35,26,35,26,35,35}, //R34, RP
			{35,35,35,35,35,35,35,35,35,35,35,35,35,35,35,35}, //R35, FINAL STATE <$RP>
			{36,37,3,26,26,26,26,26,26,26,26,28,37,37,37,26}, //R36, SEMI
			{37,37,37,37,37,37,37,37,37,37,37,37,37,37,37,37} //R37, FINAL STATE <SEMI>
		};
	
	public void setToken(String token){this.token = token;}
	public String getToken() {return this.token;}
	public void setTokenType(bTokenType tokenType) {this.tokenType = tokenType;}
	public bTokenType getTokenType() {return this.tokenType;}
	
	
	public void tokenToConsole()
	{
		System.out.print("Token: " + this.token);
		System.out.print("         ");
		System.out.print("Token type: " + this.tokenType.toString());
		System.out.println();
	}
}
