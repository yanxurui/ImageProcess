package ImgTrans;

public class DFT 
{
	/**2d-dft
	 * @param pix 输入图像的灰度矩阵
	 * @return 二维傅里叶变换后的复数矩阵
	 */
	public static Complex[][] DFT2(int[][] grays)
	{
		int M=grays[0].length;
		int N=grays.length;
		
		Complex[][] dftData=new Complex[N][M];
		//实数信号转成复数信号
		for(int i=0;i<N;i++)
		{
			for(int j=0;j<M;j++)
			{
				dftData[i][j]=new Complex(grays[i][j],0);
			}
		}
		//先对行向量执行一维离散傅里叶变换
		for(int i=0;i<N;i++)
		{
			dftData[i]=DFT1(dftData[i],true);
		}
		//再对列向量执行一维离散傅里叶变换
		for(int j=0;j<M;j++)
		{
			Complex[] temp=new Complex[N];
			for(int i=0;i<N;i++)
			{
				temp[i]=dftData[i][j];
			}
			temp=DFT1(temp,true);
			for(int i=0;i<N;i++)
			{
				dftData[i][j]=temp[i];
			}
		}
		
		return dftData;
	}
	
	/**2d-idft
	 * @param dftData 傅里叶变换后的复数矩阵
	 * @return 反变换恢复出来的灰度复数矩阵
	 */
	public static int[] IDFT2(Complex[][] dftData)
	{
		int M=dftData[0].length;
		int N=dftData.length;
		Complex[][] idftData=new Complex[N][M];
		
		for(int i=0;i<N;i++)
		{
			idftData[i]=DFT1(dftData[i],false);
		}
		for(int j=0;j<M;j++)
		{
			Complex[] temp=new Complex[N];
			for(int i=0;i<N;i++)
			{
				temp[i]=idftData[i][j];
			}
			temp=DFT1(temp,false);
			for(int i=0;i<N;i++)
			{
				idftData[i][j]=temp[i];
			}
		}
		
		return toPix(idftData);
	}
	
	/**1d-dft 输入输出都是复数形式
	 * @param f 输入的一维离散信号
	 * @param forward =true 表示正变换，false表示逆变换
	 * @return 返回变换后的信号
	 */
	private static Complex[] DFT1(Complex[] f,boolean forward)
	{
		int N=f.length;
		double s=1/Math.sqrt(N);
		Complex[] result=new Complex[N];
		for(int i=0;i<N;i++)
		{
			result[i]=new Complex();
		}
		for(int k=0;k<N;k++)
		{
			double phim=2*Math.PI*k/N;
			for(int n=0;n<N;n++)
			{
				double cosw=Math.cos(phim*n);
				double sinw=Math.sin(phim*n);
				if(forward)
					sinw=-sinw;
				result[k]=result[k].Add(f[n].Mul(new Complex(cosw,sinw)));
			}
			result[k]=result[k].Mul(s);
		}
		return result;
	}
	
	
	
    /**数据可视化：
     * 频谱为幅度谱,直流分量移至中心
     * @param dftData
     * @return 灰度图像的一维像素数组
     */
    public static int[] visual(Complex[][] dftData)
    {
    	int w=dftData[0].length;
    	int h=dftData.length;
        int[] pix = new int[w*h];
	    
        int u,v,temp;
        pix=toPix(dftData);
        for (int i = 0; i < h/2; i++)
        {
            for (int j = 0; j < w; j++)
            {                
                u = i + h / 2;
                if (j <w/2)
                	v = j + w / 2;
                else 
                	v = j - w / 2;
            	
            	temp=pix[u*w+v];
            	pix[u*w+v]=pix[i*w+j];
            	pix[i*w+j]=temp;
            	
            }
        }
        return pix; 
    }
    
    /**
     * 将复数矩阵转换为灰度图像
     * @param data
     * @return
     */
    protected static int[] toPix(Complex[][] data)
    {
		int M=data[0].length;
		int N=data.length;
        int r;
        int[] pix = new int[M * N];
        
        for (int i= 0; i< N; i++)
        {
            for (int j = 0; j < M; j++)
            {
                r = (int)(data[i][j].getModulu()+0.5);
                if(r>255)
                	r=255;
                pix[i *M+j] = 255 << 24|r << 16|r << 8|r;             
            }
        }
		return pix;
    }
    
    /*
     * 测试
     */
    public static void main(String[] args)
    {
    	Complex[] data={
    			new Complex(1,0),
    			new Complex(3,0),
    			new Complex(5,0),
    			new Complex(2,0),
    			new Complex(4,0)};
    	Complex[] dftData=DFT1(data,true);
    	Complex[] idftData=DFT1(dftData,false);
    	for(Complex c:idftData)
    	{
    		System.out.println(c.getModulu());
    	}
    }
}
