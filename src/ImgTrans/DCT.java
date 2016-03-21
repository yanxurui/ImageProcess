package ImgTrans;

public class DCT 
{
	/**输出图像的二维离散余弦频谱，对图像的宽高无限制
	 * @param pix 输入图像的灰度矩阵
	 */
	public static void DCT2(double[][] pix)
	{
		int w=pix[0].length;
		int h=pix.length;
		
		if(w==h)
		{
			DCT2_2(pix,1);
		}
		else
		{
			//先对每一行进行一维离散余弦变换
			for(int i=0;i<h;i++)
			{
				pix[i]=DCT1(pix[i]);
			}
			//再对每一列进行一维离散余弦变换
			double[] temp=new double[h];
			for(int j=0;j<w;j++)
			{
				for(int i=0;i<h;i++)
				{
					temp[i]=pix[i][j];
				}
				temp=DCT1(temp);
				for(int i=0;i<h;i++)
				{
					pix[i][j]=temp[i];
				}
			}
		}
	}
	/**一维离散余弦正变换
	 * @param x 一维离散信号
	 * @return 输出信号
	 */
	private static double[] DCT1(double[] x)
	{
		int N=x.length;
		double y[]=new double[N];
		for(int k=0;k<N;k++)
		{
			y[k]=0;
			double C=k==0?1.0/Math.sqrt(N):Math.sqrt(2.0/N);
			for(int n=0;n<N;n++)
			{
				double cos= Math.cos( (n + 0.5) *k* Math.PI/ N);
				y[k]+=C*x[n]*cos;
			}
		}

		return y;
	}
	
	/**二维离散余弦逆变换
	 * @param pix 二维频谱，是不是频谱图像的灰度矩阵，而是上面二维离散余弦变换的输出
	 */
	public static void IDCT2(double[][] pix)
	{
		int w=pix[0].length;
		int h=pix.length;
		if(w==h)
		{
			DCT2_2(pix,-1);
		}
		else
		{
			//先对每一行进行一维离散余弦逆变换
			for(int i=0;i<h;i++)
			{
				pix[i]=IDCT1(pix[i]);
			}
			//再对每一列进行一维离散余弦逆变换
			double[] temp=new double[h];
			for(int j=0;j<w;j++)
			{
				for(int i=0;i<h;i++)
				{
					temp[i]=pix[i][j];
				}
				temp=IDCT1(temp);
				for(int i=0;i<h;i++)
				{
					pix[i][j]=temp[i];
				}
			}	
		}

	}
	/**一维离散余弦逆变换
	 * @param y 表示频谱的一维离散信号
	 * @return 重建的原始信号
	 */
	private static double[] IDCT1(double[] y)
	{
		int N=y.length;
		double x[]=new double[N];
		for(int n=0;n<N;n++)
		{
			x[n]=0;
			for(int k=0;k<N;k++)
			{
				double C=k==0?1.0/Math.sqrt(N):Math.sqrt(2.0/N);
				double cos= Math.cos( (n + 0.5) *k* Math.PI/ N);
				x[n]+=C*y[k]*cos;
			}
		}

		return x;
	}
	
	
	
	
    /**用矩阵相乘实现正方形图像的DCT，只能处理宽高相等的图像
     * @param img
     * @param type= 1为正DCT变换, type =-1为逆变换
     * @throws Exception
     */
    public  static void DCT2_2(double[][] pix,int type)
    {
    	int N=pix.length;
    	//DCT系数矩阵
    	double[][] dct_coef=coeff(N);
        double[][] dct_coeft= new double[N][N];//DCT转置系数矩阵
        //定义转置矩阵系数
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                dct_coeft[i][j] = dct_coef[j][i];
        
        double [][] temp;
        //矩阵相乘
        if(type==1)//正变换
        {
        	temp=matrixMul(dct_coef,pix,N);
        	temp=matrixMul(temp,dct_coeft,N);
        	for(int i=0;i<N;i++)
        		pix[i]=temp[i];//注意：pix是引用，不能对他直接赋值
        }
        else//逆变换
        {
        	temp=matrixMul(dct_coeft,pix,N);
        	temp=matrixMul(temp,dct_coef,N);
        	for(int i=0;i<N;i++)
        		pix[i]=temp[i];
        }
    }

    /**基函数矩阵
     * @param N
     * @return
     */
    private static double[][] coeff(int N)
    {
    	double[][] dct_coef=new double[N][N];
        double C = 1/ Math.sqrt(N);

        for (int i = 0; i < N; i++)
            dct_coef[0][i] = C;

        //初始化DCT系数
        for (int i = 1; i < N; i++)
            for (int j = 0; j < N; j++)
                dct_coef[i][j] = Math.sqrt(2)*C*Math.cos(i * Math.PI * (j + 0.5) / N);
        return dct_coef;
    }
    
    /**大小为NXN的两个正方矩阵相乘，结果置于第一个矩阵
     * @param a
     * @param b
     * @param N
     */
    private static double [][] matrixMul(double[][] a,double[][] b,int N)
    {
    	double [][] result=new double[N][N]; 
        for (int i = 0; i < N; i++)
        {
        	
            for (int j = 0; j < N; j++)
            {
                for (int k = 0; k < N; k++)
                    result[i][j] += a[i][k] * b[k][j];
            }
        }
        return result;
    }
}
