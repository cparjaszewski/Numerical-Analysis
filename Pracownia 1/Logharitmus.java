import java.io.*;
import java.text.*;
//import java.lang.*;
//import joie;
/**
 * "Numerical logharitmus"
 * 
 * @author Krzysztof Parjaszewski (krzysztof.parjaszewski@gmail.com)
 * @version 1.0.0
 * @category 
 *  		 UNIWERSYTET WROCŁAWSKI, WYDZIAŁ MATEMATYKI I INFORMATYKI
 *  		 Kierunek Informatyka
 *  		 Analiza Numeryczna 
 *  		 Pracownia, semestr zimowy 2007 - lista nr 1, zadanie 5.
 * kodowanie - utf-8
 *
 * Napisz podprogram obliczający wartość logarytmu naturalnego wg następujacej metody. 
 * Jeśli x = 1, to sprawa jest oczywista. W przeciwnym wypadku należy wyznaczyć takie: 
 * n zawarte w zbiorze liczb całkowitych,
 * r zawarte w [0.5 , 1), 
 * że x = r × 2^n. 
 * Nastepnie połóz 
 * u := (r − sqrt(2)/2)/(r + sqrt(2)/2) 
 * i oblicz przyblizoną wartość 
 * ln ( (1 + u)/( 1 - u) )
 * ze wzoru
 * ln( (1 + u)/(1 − u) ) ~=~  u ·(20790 − 21545.27u^2 + 4223.9187u^4 )/( 10395 − 14237.635u^2 + 4778.8377u^4 − 230.41913u^6 )
 * Wreszcie, przyjmij, ze ln x ~=~ (􀀀n − 1/2) ln_2 + ln((1 + u)/(1 − u)).
 * Porównaj wartości obliczone w ten sposób z podawanymi przez podprogram biblioteczny 
 * (funkcję standardową) dla np. 100 wartości argumentu. Jaki jest najwiekszy 
 * błąd względny? 
 * Skomentuj wyniki.
 */
public class Logharitmus {

	private double m_input;
	private double m_result = -1;
	
	private double m_max_rel_error = 0;
	
	protected BufferedReader m_br; 
	protected File m_result_file;
	protected BufferedWriter m_output_result_stream; 
	
	protected File m_data_file;
	protected BufferedWriter m_output_data_stream;
	
	private final double m_ln_2 = Math.log(2);
	private final double m_sqrt_2_div_2 = Math.sqrt(2)/2;
	
	public final String _VALIDATE_OK = "Ok";
	public final String _VALIDATE_ERR = "Unknown Error";
	public final String _VALIDATE_LOWER_THAN_ZERO = "Lower than zero!";
	
	public void New()
	{		
		m_br = new BufferedReader(new InputStreamReader(System.in));
	}
	
	public void go( String[] args ) throws IOException 
	{
		try {
			String validate;
			String temp_input;
			if (args.length == 0) {
				temp_input = m_br.readLine();       
			} else {
				temp_input = args[0];
			}
			
			validate = Validate( temp_input );
			while (!this.Validate( temp_input ).equals( this._VALIDATE_OK )) {
				System.out.println( validate +  "\n" + "Wrong data given!, please try again." );
				temp_input = m_br.readLine();  
	
				validate = Validate( temp_input , m_input );
			} 
			
			if ( m_input == 1 )	{
				m_result = 0;
			} else if ( m_input > 1.0  ) {
				m_result = CountMe(false);			
			} else 	{ /* so 1 > m_input > 0*/
				m_result = CountMe(true);
			}	 
			System.out.println("First try for " + this.m_input + "resulted: " + this.m_result);
			this.m_result_file  = new File ( "result.fin" ); 
			this.m_result_file.createNewFile();
			this.m_output_result_stream = new BufferedWriter(new FileWriter(m_result_file.getAbsolutePath()));
			this.m_data_file  = new File ( "data.fin" ); 
			this.m_data_file.createNewFile();
			this.m_output_data_stream = new BufferedWriter(new FileWriter(m_data_file.getAbsolutePath()));
			System.out.println( "File created." );	
			System.out.println( "Starting the loop with 100 arguments - results in  " + m_result_file.getAbsolutePath()	);
			Loop(100);
			this.m_output_result_stream.write("Maximal relational error :" +this.m_max_rel_error );
			System.out.println( "Application terminated succesfully!" );
			
		} catch (Exception ex) {
			System.out.println( "Unfortunatelly, an error occurs:\n" + ex.getMessage() );
		}
		finally {
			m_output_result_stream.close();
			m_output_data_stream.close();
			System.exit(0);
		}
	}
	
	private double CountMe(boolean lower_than_one) throws IOException
	{
		String string_output;
		int N_counter = 0;
		double R =  -1;
		double U =  -1;
		double compare = -1;
		
		double temp;
		double value;
		
		if ( this.m_input == 0 ) {
			throw new IOException( "Logathirm of zero is minus infinity!" );
		}
				
		if ( lower_than_one  ) {
			temp = 1 / this.m_input;
			while ( temp < 1) {	
				N_counter++;
				temp *=  2;			
			}
		} else {
			temp = this.m_input;
			while ( temp > 1) {
				N_counter++;
				temp = temp / 2;	
			}
		}
		
		if (lower_than_one)	{	
			N_counter = -N_counter;
			temp =  1 / ( 2 * this.m_input ) ;	
		} else {	
			temp = this.m_input;
		}
		R =  temp / java.lang.Math.pow(2, N_counter);
		U = (R - this.m_sqrt_2_div_2)/(R  + this.m_sqrt_2_div_2);
		value = Evaluate_U(U);		
		compare = CompareResults(value, N_counter, lower_than_one);
		if (lower_than_one) {
			temp = (1 / this.m_input);
		} else { 
			temp = this.m_input; 
		}
		string_output = "R: ," + R + ",U: ," + U + ",Input: ," + temp  + ",odwr, " + (1/temp) + "\n";
		if (this.m_output_data_stream == null) {		
			System.out.println(string_output);		
		} else {		
			this.m_output_data_stream.write(string_output);		
		}
		return compare;
	}
	
	private double Evaluate_U(double u)
	{
		double up;
		double bottom;
		up = 20790.0;
		up += - 21545.27*u*u;
		up += 4223.9187*u*u*u*u;
		bottom = 10395;
		bottom += - 14237.635*u*u;
		bottom += 4778.8377*u*u*u*u;
		bottom += - 230.41913*u*u*u*u*u*u;
		return u*(up/bottom);
	}

	private double CompareResults(double value,int n,boolean lower_than_one) throws IOException
	{ 
		double logaritm_x;
		double real_logaritm;
		double temp_input;
		String to_write_string;

		DecimalFormat format_double_value = new DecimalFormat("0.000000000000000000000000");
		DecimalFormat format_short_value = new DecimalFormat("0.##E0");
		DecimalFormat format_no_digits = new DecimalFormat("###");
		
		if (lower_than_one) {
			logaritm_x = (n + 0.5)* this.m_ln_2 ;
		}
		else {
			logaritm_x = (n - 0.5)* this.m_ln_2 ;
		}
		
		logaritm_x += value;
		if (lower_than_one)	{
			temp_input = 1 / (this.m_input);
			to_write_string = "Logharitm of " +  format_short_value.format(temp_input);
			to_write_string += "\t( 1/" + format_no_digits.format( this.m_input ) + " ) ";		
		} else	{
			temp_input = this.m_input;
			to_write_string = "Logharitm of " +  format_no_digits.format(temp_input);
		}
		
		to_write_string += "\n--------------------------\nEstimated value:\t\t" +  format_double_value.format( logaritm_x ) ;
		ResultWrite(to_write_string);
		real_logaritm = Math.log( temp_input );
		
		to_write_string = "\nReal value:\t\t\t" + format_double_value.format(real_logaritm); 
		to_write_string += "\nAbs error:\t\t\t" +  format_short_value.format(new Double(real_logaritm - logaritm_x)) ;
		to_write_string += "\nRel error:\t\t\t" +  format_short_value.format(new Double(CheckMaxError(real_logaritm, logaritm_x))) ;
		to_write_string += "\n";
		ResultWrite(to_write_string);
		return logaritm_x;
	}
	
	private void Loop(int repeatCount) throws IOException
	{
		System.out.println("Loop initialized");
		this.m_input = 1;
		
		for ( int i =1; i <= repeatCount; i++ ) {
			this.CountMe(false);
			this.CountMe(true);
			this.m_input += 2;
		}
		System.out.println("Loop ended");	
	}
	
	/* private void PrecisionTest () throws IOException
	{
		this.m_output_result_stream.write("SQRT of 2 is : " + new DecimalFormat("#########0.0000000000000000000000000000000000000").format(java.lang.Math.sqrt(2)));	
	}*/
	
	private double CheckMaxError(double real, double estimate) throws IOException
	{
		if (real == 0) { 
			DataWrite( "Error: ,Infinity," );
			return 0;
		}
		double temp_error = ( (real - estimate)/real );
		if (temp_error > this.m_max_rel_error) {
			this.m_max_rel_error  = temp_error;
			System.out.println("Max rel error changed: for real = " + real + " and now equals " + this.m_max_rel_error );
		}	
		DataWrite( "Error: ," + temp_error + ",");
		return temp_error;
	}
	
	private void ResultWrite(String message) throws IOException
	{
		Write(message,this.m_output_result_stream);
	}
	
	private void DataWrite(String message) throws IOException
	{
		Write(message,this.m_output_data_stream);
	}

	private void Write(String message,BufferedWriter writer) throws IOException
	{
		if (writer == null ) {
			System.out.println(message);
		} else {
			writer.write(message);
		}		
	}
	
	private String Validate( String toValidate, double toReplace  )
	{
		try {
			 this.m_input = Double.parseDouble(toValidate);
		}
		catch (Exception ex) {	
			System.out.println(ex.getMessage() + "Are you sure it is double input? Please try again.");
			return this._VALIDATE_ERR;
		}
		if (m_input < 0) {
			return this._VALIDATE_LOWER_THAN_ZERO;
		}
		return this._VALIDATE_OK;
	}
	
	private String Validate( String toValidate )
	{
		return this.Validate(toValidate, -1);
	}
	
}
