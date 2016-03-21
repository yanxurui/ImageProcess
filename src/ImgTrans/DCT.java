package ImgTrans;

public class DCT 
{
	/**���ͼ��Ķ�ά��ɢ����Ƶ�ף���ͼ��Ŀ��������
	 * @param pix ����ͼ��ĻҶȾ���
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
			//�ȶ�ÿһ�н���һά��ɢ���ұ任
			for(int i=0;i<h;i++)
			{
				pix[i]=DCT1(pix[i]);
			}
			//�ٶ�ÿһ�н���һά��ɢ���ұ任
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
	/**һά��ɢ�������任
	 * @param x һά��ɢ�ź�
	 * @return ����ź�
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
	
	/**��ά��ɢ������任
	 * @param pix ��άƵ�ף��ǲ���Ƶ��ͼ��ĻҶȾ��󣬶��������ά��ɢ���ұ任�����
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
			//�ȶ�ÿһ�н���һά��ɢ������任
			for(int i=0;i<h;i++)
			{
				pix[i]=IDCT1(pix[i]);
			}
			//�ٶ�ÿһ�н���һά��ɢ������任
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
	/**һά��ɢ������任
	 * @param y ��ʾƵ�׵�һά��ɢ�ź�
	 * @return �ؽ���ԭʼ�ź�
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
	
	
	
	
    /**�þ������ʵ��������ͼ���DCT��ֻ�ܴ�������ȵ�ͼ��
     * @param img
     * @param type= 1Ϊ��DCT�任, type =-1Ϊ��任
     * @throws Exception
     */
    public  static void DCT2_2(double[][] pix,int type)
    {
    	int N=pix.length;
    	//DCTϵ������
    	double[][] dct_coef=coeff(N);
        double[][] dct_coeft= new double[N][N];//DCTת��ϵ������
        //����ת�þ���ϵ��
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                dct_coeft[i][j] = dct_coef[j][i];
        
        double [][] temp;
        //�������
        if(type==1)//���任
        {
        	temp=matrixMul(dct_coef,pix,N);
        	temp=matrixMul(temp,dct_coeft,N);
        	for(int i=0;i<N;i++)
        		pix[i]=temp[i];//ע�⣺pix�����ã����ܶ���ֱ�Ӹ�ֵ
        }
        else//��任
        {
        	temp=matrixMul(dct_coeft,pix,N);
        	temp=matrixMul(temp,dct_coef,N);
        	for(int i=0;i<N;i++)
        		pix[i]=temp[i];
        }
    }

    /**����������
     * @param N
     * @return
     */
    private static double[][] coeff(int N)
    {
    	double[][] dct_coef=new double[N][N];
        double C = 1/ Math.sqrt(N);

        for (int i = 0; i < N; i++)
            dct_coef[0][i] = C;

        //��ʼ��DCTϵ��
        for (int i = 1; i < N; i++)
            for (int j = 0; j < N; j++)
                dct_coef[i][j] = Math.sqrt(2)*C*Math.cos(i * Math.PI * (j + 0.5) / N);
        return dct_coef;
    }
    
    /**��СΪNXN����������������ˣ�������ڵ�һ������
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
