package ImgTrans;
//复数类
public class Complex 
{	
	private double re;
	private double im;
	
	public Complex()
	{
		this.re = 0;
		this.im = 0;
	}
	
	public Complex(double re,double im)
	{
		this.re = re;
		this.im = im;
	}
	
	public void setRE(double re)
	{
		this.re=re;
	}
	
	public void setIM(double im)
	{
		this.im=im;
	}
	
	public double getRE()
	{
		return this.re;
	}
	
	public double getIM()
	{
		return this.im;
	}
	
	//加法
	public Complex Add(Complex c)
	{
		Complex result=new Complex();
		result.re=this.re+c.re;
		result.im=this.im+c.im;
		return result;
	}
	
	//减法
	public Complex Sub(Complex c)
	{
		Complex result=new Complex();
		result.re=this.re-c.re;
		result.im=this.im-c.im;
		return result;
	}
	
	//乘法
	public Complex Mul(Complex c)
	{
		Complex result = new Complex();
		result.re = this.re*c.re-this.im*c.im;
		result.im = this.re*c.im+c.re*this.im;
		return result;
	}
	
	//数乘
	public Complex Mul(double c)
	{
		Complex result = new Complex(0,0);
		result.re=this.re*c;
		result.im=this.im*=c;
		return result;
	}
	/**
	 * 求复数的模
	 * @return
	 */
	public double getModulu()
	{
		return Math.sqrt(re*re+im*im);
	}
	public String toString()
	{
		return re+","+im;
	}
}