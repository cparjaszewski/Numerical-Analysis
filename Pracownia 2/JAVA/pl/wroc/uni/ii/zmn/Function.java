package pl.wroc.uni.ii.zmn;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;

public class Function extends Precise {
	
	private BigDecimal m_temp;
	private BigDecimal m_F,m_F_prim,m_F_bis;
	private BigDecimal m_tangent_up,m_tangent_down;
	public BigDecimal m_range = new BigDecimal(0);
	public int EquationCount = 3;
	private BigDecimal m_f1exp;
	private BigDecimal m_f1sin;
	private BigDecimal m_f1cos;
	
	/*private BigDecimal m_g1_exp_x;
	private BigDecimal m_g1_sin_x;
	private BigDecimal m_g1_cos_x;
	private BigDecimal m_g_1_minus_0_2_exp_x;
	private BigDecimal m_g_1_minus_0_2_exp_negate_x;
	private BigDecimal m_g1_exp_negate_x;
	*/
	public void New() {
		this.m_myMathContex = new MathContext(40);
	}
	public void Prepare_F(BigDecimal x)  {
		if (x.compareTo(new BigDecimal("100"))> 0 ) {
			System.out.println("too big exp for argument:  " + x.toString());
			return;
		} else {
			System.out.println("normal x :  " + x.toString());
		}
		m_f1exp = Exp(x);
		m_f1sin = Sinus(x);
		m_f1cos = Cosinus(x);
		m_F = Subtract(m_f1exp,m_f1sin);
		m_F_prim = Subtract(m_f1exp,m_f1cos);;
		m_F_bis = Add(m_f1exp, m_f1sin);
	}
	
	public void Prepare_G(BigDecimal x) {
		/*
		m_g1_exp_x = Exp(x);
		m_g1_cos_x = Cosinus(x);
		m_g1_sin_x = Sinus(x);
		m_g1_exp_negate_x = Exp(x.negate());
		m_g_1_minus_0_2_exp_x = Subtract(1,Multiply(0.2,m_g1_exp_x));
		m_g_1_minus_0_2_exp_negate_x = Subtract(0.2,Exp(x.negate()));

		m_F = Divide(m_g1_sin_x,m_g_1_minus_0_2_exp_negate_x);
		m_F_prim = Divide(Multiply(m_g1_exp_x,,Power(Subtract(1,Multiply(0.2,m_g1_exp_x )),2))

		m_F_bis = 
		
		m_g1 = Add(m_g1_x_log_1_div_x,m_g1_tg_x_2);		
*/		
	}
	/*private BigDecimal G1(BigDecimal x) {
	//	BigDecimal temp = (Math.pow(Math.tan(x),2) + Math.log((1/x))*x);
		//return temp;	
		BigDecimal temp;
		if (x.equals(0) ){
			temp = new BigDecimal(0);	
		} else {			
			 temp = Logharitmus(Divide(1, x));
		}
		temp = Multiply(temp, x);
		temp = Add(temp,Power(Tangens(x),2));
		return temp;
	}
	*/
	/*private BigDecimal G1_prim(BigDecimal x) {
		BigDecimal temp = Divide(1,Power(m_g1_cos_x ,2));
		temp = Multiply(temp, Multiply(m_g1_tg_x ,2));
		temp = Subtract(temp, 1);
		return temp;		
	}
	
	private BigDecimal G1_bis(BigDecimal x) {
		BigDecimal temp = Divide(1,Power(m_g1_cos_x ,2));
		temp = Multiply(temp,4);
		temp = Multiply(temp,m_g1_tg_x_2);
		temp = Add(temp,Multiply(Power(Divide(1,m_g1_cos_x),4),2));
		temp = Subtract(temp,Divide(1,x));
		return temp;		
	} */
	
	public BigDecimal H1(BigDecimal x) {
		BigDecimal temp = Multiply(Power(x,4),0.25);
		temp = Subtract(temp,Multiply(Power(x,3),1/90));
		temp = Subtract(temp,Multiply(Power(x,2),20));
		temp = Add(temp,Add(Multiply(x,3),2));
		return temp;		
	}
	
	private BigDecimal H1_prim(BigDecimal x) {
		BigDecimal temp = Multiply(Power(x,3),2);
		temp = Add(temp,Multiply(Power(x,2),0.06));
		temp = Subtract(temp,Multiply(40,x));
		temp = Add(temp, 3);
		return temp;		
	}
	
	private BigDecimal H1_bis(BigDecimal x) {
		BigDecimal temp = Multiply(Power(x,2),6);
		temp = Add(temp,Multiply(0.12,x));
		temp = Subtract(temp,40);
		return temp;		
	} 
	
	private boolean getValues(BigDecimal x, int equation) {
		if (equation == 1) {
			Prepare_F(x);
			return true;
		/*} else if (equation == 2) {
			Prepare_G(x);
			m_F = ;m_F_bis.toString()
			m_F_prim = G1_prim(x);
			m_F_bis = G1_bis(x);
			return true;*/
		}  else if (equation == 3) {
			m_F = H1(x);
			m_F_prim = H1_prim(x);
			m_F_bis = H1_bis(x);
			return true;
		} else {
			return false;
		}
	}
	
	private boolean getTangentValues(BigDecimal a, BigDecimal b, int equation) {
		BigDecimal temp;
		if (equation == 1) {
			temp = Divide(Add(a,b),2);
			Prepare_F(temp);
			if (Compare(m_F,0) < 0 ){
				m_tangent_up = b;
				m_tangent_down = temp;
			} else {
				m_tangent_up = temp;
				m_tangent_down = a;
			}		
			System.out.println(m_tangent_up.toString());
			return true;			
		/*} else if (equation == 2) {
			temp = Divide(Add(a,b),2);
			m_g1_x_log_1_div_x = Multiply(temp,Logharitmus(Divide(1,temp)));
			m_g1_tg_x_2 = Power(Tangens(temp),2);
			m_g1 = Add(m_g1_x_log_1_div_x,m_g1_tg_x_2);			
			if (Compare(m_g1,0) < 0 ){
				m_tangent_up = b;
				m_tangent_down = temp;
			} else {
				m_tangent_up = temp;
				m_tangent_down = a;
			}			
			return true;
		*/} else if (equation == 3) {
			temp = Divide(Add(a,b),2);
			if  (Compare(H1(temp),0) < 0 ){
				m_tangent_up = b;
				m_tangent_down = temp;
			} else {
				m_tangent_up = temp;
				m_tangent_down = a;
			}			
			return true;
		}else {
			return false;
		}
	}
	
	public BigDecimal Next_Newton(BigDecimal x, int equation) {
		if (getValues(x,equation) == false) {
			return new BigDecimal(-1);
		}
		m_temp = Subtract(x,Divide(m_F,m_F_prim));
		return m_temp;
	}
	
	public BigDecimal Next_New_One(BigDecimal x, int equation) {
		if (getValues(x,equation) == false) {
			return new BigDecimal(-1);
		}
		//return new BigDecimal(0);
		m_temp = Subtract(x,Divide(m_F,Sqrt(Subtract(Power(m_F_prim,2),Multiply(m_F,m_F_bis)))));
		return m_temp;
	}	

	public BigDecimal Next_Olver(BigDecimal x, int equation) {
		if (getValues(x,equation) == false) {
			return new BigDecimal(-1);
		}
		m_temp = Subtract(Subtract(x,Divide(m_F,m_F_prim)),Multiply(Multiply(0.5,Power(Divide(m_F,m_F_prim),2)),Divide(m_F_bis,m_F_prim)));
		return m_temp;
	}	
	
	public BigDecimal Next_Hardley(BigDecimal x, int equation) {
		if (getValues(x,equation) == false) {
			return new BigDecimal(-1);
		}
		BigDecimal m_down = Subtract(Divide(m_F_prim,m_F),Multiply(0.5,Divide(m_F_bis,m_F_prim)));
		m_temp = Subtract(x,Divide(1,m_down));
		return m_temp;
	}
	
	
	public BigDecimal Next_Tangent(BigDecimal a,BigDecimal b, int equation) {
		if (getTangentValues(a,b,equation) == false) {
			return new BigDecimal(-1);
		}
		m_temp = Divide(Add(m_tangent_up ,m_tangent_down ),2);
		m_range = Divide(m_range,2) ;
		return m_temp;
	}
	
	public BigDecimal F_Value(BigDecimal x, int equation) {
		if (equation == 1) {
			Prepare_F(x);
			return m_F;	
		/*}	else if (equation == 2) {
			Prepare_G(x);
			return m_g1 ;
		*/} else if (equation == 3) {
			return H1(x);
		} else {
			System.out.println("ERR: Wrong equation nr!");
			return new BigDecimal(-1);
		}
	}
	

}
