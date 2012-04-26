package pl.wroc.uni.ii.zmn;

import java.math.BigDecimal; 
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Hashtable;

public class Precise {
	public MathContext m_myMathContex = new MathContext(60);
	private BigDecimal m_pi = new BigDecimal("3.141592653589793238462643383279502884197169399375105820974944");
	private BigDecimal m_2pi = m_pi.multiply(BigDecimal.valueOf(2));
	private BigDecimal m_e = new BigDecimal("2.7182818284590452353602874713526624977572470936999595749669676277240766303535");
	private BigDecimal m_2_half = Divide(m_e,2);
	public BigDecimal m_exactly = Power(10,-40);
	public BigDecimal m_max = Power(10,3);
	public BigDecimal m_exactlySqrt = Power(10,-60);
	public Hashtable<String,BigInteger> m_FactorialArray = new Hashtable<String,BigInteger>(); 
	public Hashtable<String,BigDecimal> m_ExpArray = new Hashtable<String,BigDecimal>(); 
	public Hashtable<String,BigDecimal> m_SinusArray = new Hashtable<String,BigDecimal>(); 
	public Hashtable<String,BigDecimal> m_CosinusArray = new Hashtable<String,BigDecimal>(); 
	

	
	public BigDecimal Max(BigDecimal firstNumber, BigDecimal secondNumber)
	{
		if (firstNumber.compareTo(secondNumber) < 0) {
			return secondNumber;
		} else {
			return firstNumber;
		}
	}
	//for abs(argument) < 1
	
	
	
	public BigDecimal Add(BigDecimal firstNumber, BigDecimal secondNumber) {
		BigDecimal temp;
		try {
			temp = firstNumber.add(secondNumber,m_myMathContex);
			return temp;
		}
		catch(Exception ex)	{
			WriteDownToLog(new String[]{firstNumber.toString(),secondNumber.toString()});
			System.out.println("Add error occured;");
			return new BigDecimal(0);
		}	
	} 
		
	public BigDecimal Add(BigDecimal firstNumber, double secondNumber) {
		return this.Add(firstNumber, new BigDecimal(secondNumber));
	} 
	
	public BigDecimal Subtract(BigDecimal firstNumber, BigDecimal secondNumber) {
		BigDecimal temp;
		try {
			temp = firstNumber.subtract(secondNumber,m_myMathContex);
			return temp;
		}
		catch(Exception ex)	{
			WriteDownToLog(new String[]{firstNumber.toString(),secondNumber.toString()});
			System.out.println("Subtract error occured ;");
			return new BigDecimal(0);
		}			
	} 
	
	public BigDecimal Subtract(BigDecimal firstNumber, double secondNumber) {
		return this.Subtract(firstNumber,new BigDecimal(secondNumber));
	}
	
	public BigDecimal Subtract(double firstNumber, BigDecimal secondNumber) {
		return this.Subtract(BigDecimal.valueOf(firstNumber),secondNumber);
	} 
	
	public BigDecimal Multiply(BigDecimal firstNumber, BigDecimal secondNumber) {
		BigDecimal temp = firstNumber.multiply(secondNumber,m_myMathContex );
		return temp;
	}
	
	public BigDecimal Multiply(BigDecimal firstNumber, double secondNumber)
	{
		return this.Multiply(firstNumber, new BigDecimal(secondNumber));
	}
	
	public BigDecimal Multiply(double firstNumber, BigDecimal secondNumber)
	{
		return this.Multiply(new BigDecimal(firstNumber), secondNumber);
	}
	
	public BigInteger Multiply(BigInteger firstNumber, BigInteger secondNumber){
		return firstNumber.multiply(secondNumber);
	}
	
	public BigInteger Multiply(long firstNumber, BigInteger secondNumber){
		return Multiply(BigInteger.valueOf(firstNumber),secondNumber);
	}
	
	public BigInteger Multiply(BigInteger firstNumber, long secondNumber){
		return Multiply(firstNumber,BigInteger.valueOf(secondNumber));
	}

	public BigDecimal Divide(BigDecimal firstNumber, BigDecimal secondNumber) {
		BigDecimal temp;
		try {
			if (secondNumber.toString().equals("0")) {
				throw new Exception("ERR-Division by zero!!!");
			}
			temp = firstNumber.divide(secondNumber,m_myMathContex);
			return temp;
		}
		catch(Exception ex)	{
			WriteDownToLog(new String[]{firstNumber.toString(),secondNumber.toString()});
			System.out.println(ex + "\nDivide error occured ;");
			return new BigDecimal(0);
		}			
	}
	
	
	public BigDecimal Divide(double firstNumber, BigDecimal secondNumber) {
		return this.Divide(new BigDecimal(firstNumber), secondNumber);
	}
	
	
	public BigDecimal Divide(BigDecimal firstNumber, double secondNumber) {
		return this.Divide(firstNumber, new BigDecimal(secondNumber));
	}
	
	
	public int Compare(BigDecimal firstNumber, double secondNumber) {
		return this.Compare(firstNumber, new BigDecimal(secondNumber));
	}
	
	public int Compare(double firstNumber, BigDecimal secondNumber) {
		return this.Compare(new BigDecimal(firstNumber), secondNumber);
	}
	
	public int Compare(BigDecimal firstNumber, BigDecimal secondNumber) {
		return firstNumber.compareTo(secondNumber);
	}

	
	public BigDecimal Cosinus(BigDecimal argument) {
		BigDecimal tempCosinus;
		if (m_CosinusArray.get(argument.toString()) != null) 
		{
			return m_CosinusArray.get(argument.toString());
		} else {
			tempCosinus = RealCosinus(argument);
			m_CosinusArray.put(argument.toString(), tempCosinus);
		}
		return tempCosinus;
	}
	
	public BigDecimal RealCosinus(BigDecimal argument) {
		argument = GetSinusAgument(argument);
		BigDecimal temp = new BigDecimal(1);
		BigDecimal temp0 = new BigDecimal(0);
		BigDecimal temp2;
		int counter = 1;
		if (argument.toString().equals("0")) {
			return temp;
		}
		try {
			while (Subtract(temp,temp0).abs().compareTo(m_exactly) > 0) {
				temp0 = temp;
				temp2 = Divide(Power(argument, (2*counter)),Factorial(2*counter));
				temp2 = Multiply(Math.pow(-1, counter),temp2);
				temp = Add(temp,temp2);
				counter++;
			}
		}
		catch(Exception ex)	{
			WriteDownToLog(new String[]{argument.toString()});
			System.out.println("Cosinus error occured ;");
			return new BigDecimal(0);
		}	
		return temp;
	}
	
	public BigDecimal Sinus(BigDecimal argument) {
		BigDecimal tempSinus;
		if (m_SinusArray.get(argument.toString()) != null) 
		{
			return m_SinusArray.get(argument.toString());
		} else {
			tempSinus = RealSinus(argument);
			m_SinusArray.put(argument.toString(), tempSinus);
		}
		return tempSinus;
	}
	
	public BigDecimal RealSinus(BigDecimal argument) {
		argument = GetSinusAgument(argument);
		BigDecimal temp = argument;
		BigDecimal temp2;
		BigDecimal temp0 = new BigDecimal(0);
		int counter = 1;		
		if (argument.toString().equals("0")) {
			return new BigDecimal(0);
		}
		try {
			while (Subtract(temp,temp0).abs().compareTo(m_exactly) > 0) {
				temp0 = temp;
				temp2 = Divide(Power(argument, (2*counter+1)),Factorial(2*counter+1));
				temp = this.Add(temp,Multiply(Math.pow(-1,counter),temp2));
				counter++;//Factorial(2*counter+1).toString()
			}
		}
		catch(Exception ex)	{
			WriteDownToLog(new String[]{argument.toString()});
			System.out.println("Sinus error occured ;");
			return new BigDecimal(0);
		}	
		return temp;
	}
	
	public BigDecimal Tangens(BigDecimal argument) {
		argument = GetSinusAgument(argument);
		BigDecimal tangTemp;
		BigDecimal temp0;
		try {
			tangTemp = this.Sinus(argument);
			if (tangTemp.equals(0)) {
				return new BigDecimal(0); 
			}
			temp0 = this.Cosinus(argument);
			temp0 = this.Divide(tangTemp, temp0);
		}
		catch(Exception ex)	{
			WriteDownToLog(new String[]{argument.toString()});
			System.out.println("Tangens error occured ;");
			return new BigDecimal(0);
		}	
		return temp0;
	}
	
	
	
	public BigInteger RealFactorial(int argument){
		if (argument  < 2 ) { 
			return BigInteger.valueOf(1); 
		} 	else { 
			return this.Multiply(argument,RealFactorial(argument - 1));
		}
	}
	
	public BigDecimal Factorial(int argument) {
		BigInteger m_tempFactorial;
		if (m_FactorialArray.get(String.valueOf(argument)) != null) 
		{
			return new BigDecimal((BigInteger)m_FactorialArray.get((String.valueOf(argument))));
		} else {
			m_tempFactorial = RealFactorial(argument);
			m_FactorialArray.put(String.valueOf(argument), m_tempFactorial);
		}
		return new BigDecimal(m_tempFactorial);		
	}
	
	public BigDecimal Power(double argument, int value){
		BigDecimal tempPower = this.Power(new BigDecimal(argument), value);
		return tempPower;		
	}
		
	public BigDecimal Power(BigDecimal argument, int value) {
		BigDecimal powerTemp = new BigDecimal(1);
		if (value <  0 ) {
			argument = Divide(1,argument);
			for(int i = value; i < 0;i++) {
				powerTemp = this.Multiply(powerTemp, argument);	//argument.toString() 
			}
		} else {
			for(int i = 0; i < value; i++) {
				powerTemp = this.Multiply(powerTemp, argument);
			}
		}
		return powerTemp;
	}
	public BigDecimal Exp(BigDecimal argument) {
		BigDecimal tempExp;
		if (m_ExpArray.get(argument.toString()) != null) 
		{
			return m_ExpArray.get(argument.toString());
		} else {
			tempExp = RealExp(argument);
			m_ExpArray.put(argument.toString(), tempExp);
		}
		return tempExp;
	}
	
		
	public BigDecimal RealExp(BigDecimal argument) {
		BigDecimal expTemp = new BigDecimal(0);
		BigDecimal temp0 = new BigDecimal(-1);
		BigDecimal temp2;
		int counter = 0;
		
		if (argument.compareTo(BigDecimal.valueOf(-60)) < 0) {
			System.out.println("To tiny Exponent try");
			return BigDecimal.valueOf(0);
		}
		
		while (Subtract(temp0,expTemp).abs().compareTo(m_exactly) > 0) {
			temp0 = expTemp;
			temp2 = Divide(Power(argument,counter),Factorial(counter));
			expTemp = this.Add(expTemp, temp2);
			counter++;
		}		
		return expTemp;
	}
	
	
	public BigDecimal Sqrt(BigDecimal argument){
		if (argument.compareTo(BigDecimal.valueOf(0)) < 0) {
			System.out.println("Sqrt negative try");
			return null;
		}
		BigDecimal tempSqrt = new BigDecimal(1);
		BigDecimal temp0;
		temp0 = new BigDecimal(0);
		tempSqrt = new BigDecimal(1);

		while (Subtract(tempSqrt,temp0).abs().compareTo(m_exactlySqrt) > 0){
			temp0 = tempSqrt;
			tempSqrt = Multiply(0.5,Add(tempSqrt,Divide(argument,tempSqrt)));
		}		
		return tempSqrt;
	}
	
		
	/*public BigDecimal Logharitmus(BigDecimal argument) {
		if (argument.compareTo(BigDecimal.valueOf(0)) < 0) {
			System.out.println("Nagative logharitm try!");
			return BigDecimal.valueOf(-1);
		}
		
		BigDecimal tempLogh = new BigDecimal(1);
		if (argument.compareTo(m_2_half) >= 0 ) {
			tempLogh = this.Multiply(tempLogh, 2);
			tempLogh = this.Multiply(tempLogh, this.Logharitmus(this.Divide(argument,m_2_half)));
			tempLogh = this.Divide(tempLogh, 2);
		} else {
			tempLogh = this.RealLogharitmus(argument);	
		}
		return tempLogh;
	}	

	private BigDecimal RealLogharitmus(BigDecimal argument) {
		BigDecimal temp0 = new BigDecimal(1);
		BigDecimal temp = new BigDecimal(0);
		BigDecimal down = new BigDecimal(1);
		argument = Subtract(argument,1);
		int counter = 0;
		try {
			while (Subtract(temp,temp0).abs().compareTo(m_exactly) > 0) {
				temp0 = temp;
				temp = this.Power(-1, counter);
				temp = this.Multiply(temp,this.Power(argument,(counter + 1)));
				down = new BigDecimal(counter + 1);
				temp = Divide(temp,down);
				temp = Add(temp,temp0);
				System.out.println(temp.toString());
				counter++;
			}
		}
		catch(Exception ex)	{
			WriteDownToLog(new String[]{argument.toString()});
			System.out.println("RealLogharitmus error occured;");
			return new BigDecimal(0);
		}			
		return temp ;
	}*/
		
	
	
	public void WriteDownToLog(String[] values) {
		String output = "ERROR";
		for (String mes : values) {
			output += mes + "; ";
		}
		System.out.println(output);		
	}

	public BigDecimal GetSinusAgument(BigDecimal argument){
		if (argument.compareTo(Multiply(m_pi,10000)) > 0) {
			return GetSinusAgument(Subtract(argument,Multiply(10000,m_pi)));
		} else if (argument.compareTo(Multiply(m_pi,10000).negate()) < 0) {
			return GetSinusAgument(Add(argument,Multiply(10000,m_pi)));
		} else if (argument.compareTo(Multiply(m_pi,1000)) > 0) {
			return GetSinusAgument(Subtract(argument,Multiply(1000,m_pi)));
		} else if (argument.compareTo(Multiply(m_pi,1000).negate()) < 0) {
			return GetSinusAgument(Add(argument,Multiply(1000,m_pi)));
		} else if (argument.compareTo(Multiply(m_pi,100)) > 0) {
			return GetSinusAgument(Subtract(argument,Multiply(100,m_pi)));
		} else if (argument.compareTo(Multiply(m_pi,100).negate()) < 0) {
				return GetSinusAgument(Add(argument,Multiply(100,m_pi)));
		} else if (argument.compareTo(Multiply(m_pi,10)) > 0) {
			return GetSinusAgument(Subtract(argument,Multiply(10,m_pi)));
		} else if (argument.compareTo(Multiply(m_pi,10).negate()) < 0) {
			return GetSinusAgument(Add(argument,Multiply(10,m_pi)));
		} else if (argument.compareTo(m_2pi) > 0) {
			return GetSinusAgument(Subtract(argument,m_2pi));
		} else if (argument.compareTo(m_2pi.negate()) < 0 ) {
			return GetSinusAgument(Add(argument,Multiply(2,m_2pi)));
		} else {
			return argument;
		}
	}
}
