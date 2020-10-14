/*
 * BSYMBOL CLASS
 * Purpose: Represents a Symbol in a Symbol Table, also manages it's own FSA
 * Dependencies: None
 * Author: Daniel Feuling
*/

public class bSymbol 
{
	public bSymbol(String to, bToken.bTokenType ty, String value, int seg)
	{
		this.token = to;
		this.type = ty;
		this.value = value;
		this.segment = seg;
		
		if(seg == 0)
		{
			this.address = c_address;
			c_address++;
			c_address++;
		}
		
		if(seg == 1)
		{
			this.address = d_address;
			d_address++;
			d_address++;
		}
		
	}
	
	String token; 
	bToken.bTokenType type;
	String value;
	static int d_address = 0;
	static int c_address = 0;
	int segment; //code segment or data segment -- code represented by 0, data by 1
	int address;
	
	static int[][] symbolStateTable = new int[][]
		{
			{0,0,0,0,0,0,0,1,0,0,0,0,0}, //0
			{0,0,0,0,0,0,0,2,0,0,0,0,1}, //1
			{0,0,0,0,3,0,0,0,0,0,0,0,2}, //2
			{8,4,10,10,0,0,0,10,0,0,0,0,3}, //3
			{0,0,0,0,0,0,0,5,0,0,0,0,4}, //4
			{0,0,0,0,0,0,0,0,0,0,6,0,5}, //5
			{0,0,0,0,0,0,0,0,0,7,0,0,6}, //6
			{0,0,0,0,0,0,3,0,4,0,0,0,7}, //7
			{0,0,0,0,0,0,0,9,0,0,0,0,8}, //8
			{0,0,0,0,0,0,3,0,8,0,0,0,9}, //9 
			{0,0,10,10,10,10,10,10,10,11,10,0,10}, //10
			{10,10,10,10,10,10,10,10,10,10,10,12,11}, //11
			{12,12,12,12,12,12,12,12,12,12,12,12,12}, //12
			{0,0,0,0,0,0,0,0,0,0,0,0,13}, //13
		};
		
}
