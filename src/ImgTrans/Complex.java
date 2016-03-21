package ImgTrans;
//������
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
	
	//�ӷ�
	public Complex Add(Complex c)
	{
		Complex result=new Complex();
		result.re=this.re+c.re;
		result.im=this.im+c.im;
		return result;
	}
	
	//����
	public Complex Sub(Complex c)
	{
		Complex result=new Complex();
		result.re=this.re-c.re;
		result.im=this.im-c.im;
		return result;
	}
	
	//�˷�
	public Complex Mul(Complex c)
	{
		Complex result = new Complex();
		result.re = this.re*c.re-this.im*c.im;
		result.im = this.re*c.im+c.re*this.im;
		return result;
	}
	
	//����
	public Complex Mul(double c)
	{
		Complex result = new Complex(0,0);
		result.re=this.re*c;
		result.im=this.im*=c;
		return result;
	}
	/**
	 * ������ģ
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