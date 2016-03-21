package ImgTrans;

public class FFT extends DFT
{
    /**二维快速傅里叶变换
     * @param grays 输入图像的灰度矩阵，宽和高必须是2的整数次幂，函数内部不再检查
     * @return 返回变换后的频谱，是一个二维复数矩阵
     */
    public static Complex[][] FFT2(int[][] grays)
    {
		int M=grays[0].length;
		int N=grays.length;
		Complex[][] fftData=new Complex[N][M];
		//实数信号转成复数信号
		for(int i=0;i<N;i++)
		{
			for(int j=0;j<M;j++)
			{
				fftData[i][j]=new Complex(grays[i][j],0);
			}
		}
        //在y方向上进行快速傅里叶变换
        for (int i = 0; i < N; i++)
        {
            //每一行做傅立叶变换
        	fftData[i] = FFT1(fftData[i],true);
        }

        //对x方向进行傅里叶变换
        for (int j = 0; j < M; j++)
        {
            //每一列做傅立叶变换
        	Complex[] temp=new Complex[N];
            for (int i = 0; i < N; i++)
                temp[i] = fftData[i][j];
            temp=FFT1(temp,true);
            for (int i = 0; i < N; i++)
            	fftData[i][j]=temp[i];
        }
        //乘以系数
        for(int i=0;i<N;i++)
        {
        	for(int j=0;j<M;j++)
        	{
        		fftData[i][j]=fftData[i][j].Mul(1.0/(Math.sqrt(N)*Math.sqrt(N)));
        	}
        }
        
        return fftData;
    }
    
    
    /**fft逆变换
     * @param fftData
     * @return
     */
    public static int[] IFFT2(Complex[][] fftData)
    {
		int M=fftData[0].length;
		int N=fftData.length;
		Complex[][] ifftData=new Complex[N][M];
        //在y方向上进行快速傅里叶变换
        for (int i = 0; i < N; i++)
        {            
            ifftData[i] = FFT1(fftData[i],false);
        }
        //对x方向进行傅里叶变换
        for (int j = 0; j < M; j++)
        {
            //每一列做傅立叶变换
        	Complex[] temp=new Complex[N];
            for (int i = 0; i < N; i++)
                temp[i] = ifftData[i][j];
            temp=FFT1(temp,false);
            for (int i = 0; i < N; i++)
            	ifftData[i][j]=temp[i];
        }
        //乘以系数
        for(int i=0;i<N;i++)
        {
        	for(int j=0;j<M;j++)
        	{
        		ifftData[i][j]=ifftData[i][j].Mul(1.0/(Math.sqrt(N)*Math.sqrt(N)));
        	}
        }
        
        return toPix(ifftData);
    }
    
    
    
    /**1d-fft
     * @param f
     * @param forward =true表示正变换，false表示逆变换
     * @return
     */
    private static Complex[] FFT1(Complex[] f,boolean forward)
    {
    	int N=f.length;
    	int power= (int) (Math.log10(N)/Math.log10(2));
    	Complex[] F=new Complex[N];
    	//按奇偶分组
    	for(int i=0;i<N;i++)
    	{
    		int p = 0;  
            for (int j = 0; j < power; j++)  
                if ((i & (1 << j)) != 0)  
                    p += 1 << (power - j - 1);  

            F[p] = f[i];
    	}
    	//蝶形因子
    	Complex[] wc = new Complex[N/ 2];
    	if(forward)
    	{
    		for (int i = 0; i < N / 2; i++)
            {
                double angle = -i * Math.PI * 2 / N;
                wc[i]=new Complex(Math.cos(angle),Math.sin(angle));
            }
    	}
    	else
    	{
    		for (int i = 0; i < N / 2; i++)
    		{
                double angle = -i * Math.PI * 2 / N;
                wc[i]=new Complex(Math.cos(angle),-Math.sin(angle));
            }
    	}
        
        
    	//蝶形运算
    	for(int L=1;L<=power;L++)//第L层数
    	{
    		int bf=1<<(power-L);//蝶形数
    		for(int n=0;n<bf;n++)//第n+1个蝶形
    		{
    			int bfsize=1<<L;//蝶形大小
    			for(int k=0;k<bfsize/2;k++)//第k+1个蝶形因子
    			{
    				Complex temp,X1,X2;;
    				int i=n*bfsize+k,
    					j=i+bfsize/2;
    				temp=wc[k*bf].Mul(F[j]);
    				X1=F[i].Add(temp);
    				X2=F[i].Sub(temp);
    				F[i]=X1;
    				F[j]=X2;
    			}    		
    		}
    	}
    	return F;
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
    			new Complex(4,0),
    			new Complex(6,0),
    			new Complex(7,0),
    			new Complex(0,0)};
    	Complex[] fftData=FFT1(data,true);
    	Complex[] ifftData=FFT1(fftData,false);
    	for(Complex c:ifftData)
    	{
    		System.out.println(c.getModulu());
    	}
    }

}
