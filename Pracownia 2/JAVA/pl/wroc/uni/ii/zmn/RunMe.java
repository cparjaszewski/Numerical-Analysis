package pl.wroc.uni.ii.zmn;

import java.io.IOException;
import java.math.MathContext;


public class RunMe {
	public static void main(String[] args) throws IOException
	{
		Iterrus count = new Iterrus();
		count.m_myMathContex = new MathContext(40);
		count.go(args);
		
	}
}
