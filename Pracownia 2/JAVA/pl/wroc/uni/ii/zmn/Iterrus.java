package pl.wroc.uni.ii.zmn;

import java.io.*;
import java.math.BigDecimal;
//import java.text.*;

/**
 * "Numerical logharitmus"
 * 
 * @author Krzysztof Parjaszewski (krzysztof.parjaszewski@gmail.com)
 * @version 1.0.0
 * @category 
 *  		 UNIWERSYTET WROCŁAWSKI, WYDZIAŁ MATEMATYKI I INFORMATYKI
 *  		 Kierunek Informatyka
 *  		 Analiza Numeryczna 
 *  		 Pracownia, semestr zimowy 2007 - lista nr 2, zadanie 9.
 * 			 kodowanie - utf-8
 *
 *  [TEX]
 *  Niech $\alpha$ bedzie rozwiązaniem równania nieliniowego $f(x) = 0$. Załózmy, ze dysponujemy metodami
    iteracyjnymi postaci: \\[6pt]
    \begin{displaymath}
        x_{n+1} := F(x_{n});\ \ \ \ \ x_{n+1} := G(x_{n}); \label{eq:proste}
    \end{displaymath} \\[6pt]
    gdzie $F$ i $G$ sa funkcjami spełniajacymi warunek
    $F(\alpha) = G(\alpha) = \alpha$ (np. w wypadku metody Newtona mamy $F(x) = x - f(x)/f^{'}(x))$. Załózmy, ze metody te
    sa rzedu $p$ i $q$, odpowiednio. Można wykazać, że metody postaci \\[6pt]
    \begin{displaymath}
        x_{n+1} := F(G(x_{n}));\ \ \ \ \ x_{n+1} := G(F(x_{n})); \label{eq:zlozone}
    \end{displaymath} \\[6pt]
    są rzędu $p\cdot q$.
    Wykorzystaj powyższą obserwację do zaproponowania metod iteracyjnych wysokiego rzędu rozwiązywania
    równań nieliniowych. Przeprowadź odpowiednie eksperymente numeryczne i wyciągnij wnioski.
* [/TEX]
 */



public class Iterrus extends Function  {
	
	public enum Method  {
		NEWTON { 
			BigDecimal Next(BigDecimal x, int equation) {
				return m_Function.Next_Newton(x,equation);
			}
			String Name (){
				return "Newton's Method";
			};
		} ,NEW_ONE{ 
			BigDecimal Next(BigDecimal x, int equation) {
				return m_Function.Next_New_One(x,equation);
			}
			String Name() {
				return "New Method";
			}
			
		} , OLVER{ 
			BigDecimal Next(BigDecimal x, int equation)	{
				return m_Function.Next_Olver(x,equation);
			}
			String Name() {
				return "Olver's Method";
			};
		} , HADRLEY{ 
			BigDecimal Next(BigDecimal x, int equation) {
				return m_Function.Next_Hardley(x,equation);
			}
			String Name() {
				return  "Hardley's Method";
			}
		} , TANGENT{ 
			BigDecimal Next(BigDecimal x, int equation) {
				if (m_Function.m_range.equals(0)) {
					m_Function.m_range = new BigDecimal(5);
				} 
				return m_Function.Next_Tangent(m_Function.Subtract(x,m_Function.m_range),m_Function.Add(x,m_Function.m_range), equation);
			}
			String Name() {
				return  "Tangents' Method";
			};
		};

		abstract BigDecimal Next(BigDecimal x, int equation);
		abstract String Name();
		private static Function m_Function = new Function();
		public BigDecimal F_Value(BigDecimal x, int equation) {
			return m_Function.F_Value(x, equation);
		}
	};
	
	public enum CompositeMethod {
		NEWTON_TANGENT {
			String Name() {
				return "Newston's with Tangents'";
			};
			BigDecimal Next_UP(BigDecimal x, int equation) {
				if (m_Function.m_range.equals(0)) {
					m_Function.m_range = new BigDecimal(5);
				} 
				BigDecimal temp = m_Function.Next_Tangent(m_Function.Subtract(x, m_Function.m_range),m_Function.Add( x , m_Function.m_range) , equation);
				return m_Function.Next_Newton(temp, equation);
			};
			
			BigDecimal Next_DOWN(BigDecimal x, int equation) {
				BigDecimal temp =  m_Function.Next_Newton(x, equation);
				if (m_Function.m_range.equals(0)) {
					m_Function.m_range = new BigDecimal(5);
				} 
				return m_Function.Next_Tangent(m_Function.Subtract(temp, m_Function.m_range),m_Function.Add(temp, m_Function.m_range ), equation);
			};
		}
		, OLVER_HARDLEY {
			String Name() {
				return "Olvers's with Hardley's";
			};
			BigDecimal Next_UP(BigDecimal x, int equation) {
				BigDecimal temp = m_Function.Next_Hardley(x, equation);
				return m_Function.Next_Olver(temp, equation);
			};
			BigDecimal Next_DOWN(BigDecimal x, int equation) {
				BigDecimal temp = m_Function.Next_Olver(x, equation);
				return m_Function.Next_Hardley(temp, equation);
			};
		}
		, OLVER_NEWTON {
			String Name() {
				return "Olvers's with Newtons's";
			};
			BigDecimal Next_UP(BigDecimal x, int equation) {
				BigDecimal temp = m_Function.Next_Newton(x, equation);
				return m_Function.Next_Olver(temp, equation);
			};
			BigDecimal Next_DOWN(BigDecimal x, int equation) {
				BigDecimal temp = m_Function.Next_Olver(x, equation);
				return m_Function.Next_Newton(temp, equation);
			};
		}
		, NEWTON_NEW {
			String Name() {
				return "Newton's with new";
			};
			BigDecimal Next_UP(BigDecimal x, int equation) {
				BigDecimal temp = m_Function.Next_New_One(x, equation);
				return m_Function.Next_Newton(temp, equation);
			};
			BigDecimal Next_DOWN(BigDecimal x, int equation) {
				BigDecimal temp =  m_Function.Next_Newton(x, equation);
				return m_Function.Next_New_One(temp, equation);
			};
		};
		abstract String Name();
		private static Function m_Function = new Function();
		abstract BigDecimal Next_UP(BigDecimal x, int equation);
		abstract BigDecimal Next_DOWN(BigDecimal x, int equation);
		
		public BigDecimal F_Value(BigDecimal x, int equation) {
			return m_Function.F_Value(x, equation);
		}
	};
		
	protected File m_log_file;
	protected PrintWriter m_output_log_stream;
	
	protected File m_result_file;
	protected PrintWriter m_output_result_stream; 
	
	protected File m_data_file;
	protected PrintWriter m_output_data_stream;
	
	protected Precise m_precise;
		
	public void go( String[] args )  throws IOException {
		System.out.println("Program STARTED");
		try{
			InitOutputFiles();
			for (int eq = 3; eq <= 3 /*EquationCount*/ ; eq++ )
			{

				/*LogWrite("Equation nr " + eq + " START");
				Method met = Method.NEWTON ;
				LogWrite(met.Name() + " before loop");
				ResultWrite(met.Name() + " <<<<<<<<<<<<");
				DataWrite(met.Name() + " >>>>>>>>>>>>>");
				Loop(met, eq);
				LogWrite(met.Name() + " after loop");*/
				/*for (Method met : Method.values()) {
					LogWrite(met.Name() + " before loop");
					ResultWrite(met.Name() + " <<<<<<<<<<<<");
					DataWrite(met.Name() + " >>>>>>>>>>>>>");
					Loop(met, eq);
					LogWrite(met.Name() + " after loop");
				}*/
				CompositeMethod met = CompositeMethod.OLVER_NEWTON ;
				LogWrite(met.Name() + " before loop UP");
				ResultWrite(met.Name() + " <<<<<<<<<<<< UP");
				DataWrite("\n\n" + met.Name() + " >>>>>>>>>>>>> UP");
				Composite_Loop(met, eq, true); //UP
				LogWrite(met.Name() + " after loop UP\n\n");
				/*for (CompositeMethod met : CompositeMethod.values()) {
					LogWrite(met.Name() + " before loop UP");
					ResultWrite(met.Name() + " <<<<<<<<<<<< UP");
					DataWrite("\n\n" + met.Name() + " >>>>>>>>>>>>> UP");
					Composite_Loop(met, eq, true); //UP
					LogWrite(met.Name() + " after loop UP\n\n");
					
					LogWrite(met.Name() + " before loop DOWN");
					ResultWrite(met.Name() + " <<<<<<<<<<<< DOWN");
					DataWrite("\n\n" + met.Name() + " >>>>>>>>>>>>> DOWN");
					Composite_Loop(met, eq, false); //DOWN
					LogWrite(met.Name() + " after loop DOWN\n\n");					
				}*/
				LogWrite("Equation nr " + eq + " END");
			}
			System.out.println("Program TERMINATED");
		}
		catch(Exception ex) {
			System.out.println("An error occured");
		}
		finally {
			CloseFiles();
			System.out.println("Files closed");
		}	
	} 
	
	private void Composite_Loop(CompositeMethod met,int eq,boolean up) throws IOException 
	{
		try {
			System.out.println("Loop started");
			BigDecimal i = new BigDecimal(-4.2);
			int z = -1;
			BigDecimal temp;
			for(;i.compareTo(new BigDecimal(-3.4)) < 0;i = Add(i,0.001))
			{
				/*if (i.abs().compareTo(new BigDecimal(3)) < 0) {
					temp = i;
					for(z=0; z < 40;z++)
					{
						Add(temp, 0.125);
						Inside_Composite_Loop(temp,met,eq,up);

					}
				} else if (i.abs().compareTo(new BigDecimal(10)) < 0) {
					temp = i;
					for(z=0; z < 10;z++)
					{
						Add(temp,0.3);
						Inside_Composite_Loop(temp,met,eq,up);
					}
				} else{
					Inside_Composite_Loop(i,met,eq,up);
				}*/
				Inside_Composite_Loop(i,met,eq,up);
			}
			i = new BigDecimal(3.8);
			
			for(;i.compareTo(new BigDecimal(4.2)) < 0;i = Add(i,0.001))
			{
				Inside_Composite_Loop(i,met,eq,up);
			}
			
			System.out.println("Loop ended");
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());	
		}
	}
		
	private void Loop(Method met,int eq) throws IOException {
		try {

			System.out.println("Loop started");
			BigDecimal i = new BigDecimal(-3.9);
			int z = -1;
			BigDecimal temp = new BigDecimal(-1);
			for(;i.compareTo(new BigDecimal(-3.26)) < 0;i = Add(i,0.001))
			{
				/*if (i.abs().compareTo(new BigDecimal(3)) < 0) {
					temp = i;
					for(z=0; z < 40;z++)
					{
						Add(temp, 0.125);
						Inside_Loop(temp,met,eq);
					}
				} else if (i.abs().compareTo(new BigDecimal(10)) < 0) {
					temp = i;
					for(z=0; z < 10;z++)
					{
						Add(temp,0.3);
						Inside_Loop(temp,met,eq);
					}
				} else{
					Inside_Loop(i,met,eq);
				}*/
				Inside_Loop(i,met,eq);
			}
			i = new BigDecimal(3.8);
			
			for(;i.compareTo(new BigDecimal(4.24)) < 0;i = Add(i,0.001))
			{
				Inside_Loop(i,met,eq);
			}
			System.out.println("Loop ended");
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());	
		}
	}
	
	private void Inside_Composite_Loop(BigDecimal i,CompositeMethod  met,int eq,boolean up) throws IOException 
	{
		int j = -1;
		boolean resultFound = false;
		BigDecimal temp = i;
		BigDecimal temp2= i;

		this.ResultWrite("We started with " + temp);
		resultFound = false;
		for(j=0;j<50;j++) {		
			if (up) {temp2 = met.Next_UP(temp,eq);}
			else {temp2 = met.Next_DOWN(temp,eq);}
			BigDecimal next = Subtract(temp2,temp).abs();
			ResultWrite(next.toString());
			if (next.compareTo(m_max) > 0) {
				ResultWrite("STOP! - to large decimal !");
				resultFound = false;
				break;
			} else	if (next.compareTo(m_exactly) < 0 ) {
			//this.ResultWrite("Step " + (j + 1) + ": " + temp2);
			//if (temp2 == temp) {
				this.ResultWrite("Loop stopped after " + j + " steps\t\tOK ");
				temp2 = met.F_Value(temp, eq);
				if (temp2.abs().compareTo(Power(10,-15)) < 0 ){
					this.DataWrite("j; " + j + "; i; " + i.toString() + "; temp; " + temp.toString() + "; OK (with error: " + temp2.toString()  + ")");	
				} else if(temp2.abs().compareTo(Power(10,-6)) < 0 ){
					this.DataWrite("j; " + j + "; i; " + i.toString() + "; temp; " + temp.toString() + "; OK2 (with error: " + temp2.toString()  + ")");	
				} else {
					this.DataWrite("j; " + j + "; i; " + i.toString() + "; temp; " + temp.toString() + "; FAIL (error:" + temp2.toString()  + ")");
				}					
				resultFound = true;
				break;
			} else {
				if ((met != CompositeMethod.NEWTON_TANGENT ) || (up != true)) {
					temp = Max(temp,temp2);
				} else {
					temp = temp2;	
				}
				
			}
		}
		this.ResultWrite();
		this.ResultWrite();
		if (resultFound ){
			this.ResultWrite("We got " + temp + ".\n==============\n==============\n");
		} else {
			temp2 = met.F_Value(temp, eq);
			this.DataWrite("j; " + j + "; i; " + i + "; temp; " + temp + "; !!!FAIL" + temp2.toString() );
			this.ResultWrite("Result still not found !!! (after "+ j + " iterations) !!! \n==============\n==============\n");
		}
		if ((met == CompositeMethod.NEWTON_TANGENT ) && (up)) {
			m_range = new BigDecimal(0);
		}
	}

	private void Inside_Loop(BigDecimal i,Method  met,int eq) throws IOException 
	{
		int j = -1;
		boolean resultFound = false;
		BigDecimal temp = i;
		BigDecimal temp2= i;

		this.ResultWrite("We started with " + temp);
		resultFound = false;
		for(j=0;j<80;j++) {		
			
			temp2 = ((Method) met).Next(temp,eq);
			//this.ResultWrite("Step " + (j + 1) + ": " + temp2);temp.toString()

			BigDecimal next = Subtract(temp2,temp).abs();
			ResultWrite(next.toString());
			if (next.compareTo(m_max) > 0) {
				ResultWrite("STOP! - to large decimal !");
				resultFound = false;
				break;
			} else	if (next.compareTo(m_exactly) < 0 ) {
			//	System.out.println("YEAH!    " + i.toString()); 
				this.ResultWrite("Loop stopped after " + j + " steps\t\tOK ");
				temp2 = ((Method) met).F_Value(temp, eq);
				if (next.abs().compareTo(Power(10,-15)) < 0 ){
					this.DataWrite("steps; " + j + "; i; " + i.toString() + "; temp; " + temp + "; OK (with error: " + temp2 + ")");	
				} else if(next.abs().compareTo(Power(10,-6)) < 0 ){
					this.DataWrite("steps; " + j + "; i; " + i.toString() + "; temp; " + temp + "; OK2 (with error: " + temp2 + ")");	
				} else {
					this.DataWrite("steps; " + j + "; i; " + i.toString() + "; temp; " + temp + "; FAIL (error:" + temp2 + ")");
				}				
				resultFound = true;
				break;
			} else {
				
				if (((Method) met) == Method.TANGENT ){
					temp = Max(temp,temp2);
				} else {
					temp = temp2;	
				}
			}
		}
		if (resultFound ){
			this.ResultWrite("We got " + temp + ".\n==============\n==============\n");
		} else {
			temp2 = ((Method) met).F_Value(temp, eq);
			this.DataWrite("j; " + j + "; i; " + i + "; temp; " + temp + "; !!!FAIL!! with error" + temp2);
			this.ResultWrite("Result still not found !!! (after " + j +  "  iterations) !!! \n==============\n==============\n");
		}
		//System.out.println("next!    " + i.toString()); 
		if (((Method) met) == Method.TANGENT) {
			m_range = new BigDecimal(0);
		}
	}
	
	private void ResultWrite() throws IOException {
		Write("",this.m_output_result_stream);
	}
	
	private void ResultWrite(String message) throws IOException
	{
		Write(message,this.m_output_result_stream);
	}
	
	private void DataWrite() throws IOException 
	{
		Write("",this.m_output_data_stream);	
	}
	
	private void DataWrite(String message) throws IOException
	{
		Write(message,this.m_output_data_stream);
	}
	
	private void LogWrite(String message) throws IOException {
		Write(message,this.m_output_log_stream);
		System.out.println(message);
	}
	
	private void LogWrite() throws IOException {
		Write("",this.m_output_log_stream);
	}
	
	private void Write(String message,PrintWriter writer) throws IOException
	{
		if (writer == null ) {
			System.out.println(message);
		} else {
			writer.write(message + "\n");
			writer.flush();
		//	writer.println(message);
		}		
	}
	
	private void InitOutputFiles() throws IOException {
		this.m_log_file  = new File ( "application.log" ); 
		this.m_log_file.createNewFile();
		this.m_output_log_stream = new PrintWriter(new FileWriter(m_log_file.getAbsolutePath()));
		
		this.m_data_file  = new File ( "data.fin" ); 
		this.m_data_file.createNewFile();
		this.m_output_data_stream = new PrintWriter(new FileWriter(m_data_file.getAbsolutePath()));
		
		this.m_result_file  = new File ( "result.fin" ); 
		this.m_result_file.createNewFile();
		this.m_output_result_stream = new PrintWriter(new FileWriter(m_result_file.getAbsolutePath()));

	}
	
	private void CloseFiles() throws IOException {
		if (this.m_output_data_stream != null ) {
			this.m_output_data_stream.close();
		} 
		if (this.m_output_result_stream != null ) {
			this.m_output_result_stream.close();
		}
		if (this.m_output_log_stream != null ) {
			this.m_output_log_stream.close();
		}
	}

}
