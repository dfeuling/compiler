/*
 * BQUAD CLASS
 * Purpose: Represents a structure for use in translation to assembly.
 * Dependencies: None
 * Author: Daniel Feuling
 *
*/
public class bQuad 
{
	public bQuad() {};
	public bQuad(char opCode, String op1, String op2, String op3)
	{
		switch(opCode)
		{
		case '+':
			this.operator = "+";
			this.operand1 = op1;
			this.operand2 = op2;
			this.operand3 = op3;
			break;
		case '-':
			this.operator = "-";
			this.operand1 = op1;
			this.operand2 = op2;
			this.operand3 = op3;
			break;
		case '*':
			this.operator = "*";
			this.operand1 = op1;
			this.operand2 = op2;
			this.operand3 = op3;
			break;
		case '/':
			this.operator = "/";
			this.operand1 = op1;
			this.operand2 = op2;
			this.operand3 = op3;
			break;
		case '=':
			this.operator = "=";
			this.operand1 = op1;
			this.operand2 = op2;
			this.operand3 = op3;
			break;
		case 'i':
			this.operator = "If";
			this.operand1 = op1;
			this.operand2 = op2;
			this.operand3 = op3;
			break;
		case 'w':
			this.operator = "While";
			this.operand1 = op1;
			this.operand2 = op2;
			this.operand3 = op3;
			break;
		}
	}
	
	private String operator;
	private String operand1;
	private String operand2;
	private String operand3;
	
	public void multOp(String op1, String op2, String op3)
	{
		this.operand1 = op1;
		this.operand2 = op2;
		this.operand3 = op3;
	}
	
	public String getOp(){return this.operator;}
	public String getOp1(){return this.operand1;}
	public String getOp2(){return this.operand2;}
	public String getOp3(){return this.operand3;}
	public void setOp1(String op){this.operand1 = op;}
	public void setOp2(String op){this.operand2 = op;}
	public void setOp3(String op){this.operand3 = op;}
	public void setOp(String op){this.operator = op;}
}
