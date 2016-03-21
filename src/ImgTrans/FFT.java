package ImgTrans;

public class FFT extends DFT
{
    /**��ά���ٸ���Ҷ�任
     * @param grays ����ͼ��ĻҶȾ��󣬿�͸߱�����2���������ݣ������ڲ����ټ��
     * @return ���ر任���Ƶ�ף���һ����ά��������
     */
    public static Complex[][] FFT2(int[][] grays)
    {
		int M=grays[0].length;
		int N=grays.length;
		Complex[][] fftData=new Complex[N][M];
		//ʵ���ź�ת�ɸ����ź�
		for(int i=0;i<N;i++)
		{
			for(int j=0;j<M;j++)
			{
				fftData[i][j]=new Complex(grays[i][j],0);
			}
		}
        //��y�����Ͻ��п��ٸ���Ҷ�任
        for (int i = 0; i < N; i++)
        {
            //ÿһ��������Ҷ�任
        	fftData[i] = FFT1(fftData[i],true);
        }

        //��x������и���Ҷ�任
        for (int j = 0; j < M; j++)
        {
            //ÿһ��������Ҷ�任
        	Complex[] temp=new Complex[N];
            for (int i = 0; i < N; i++)
                temp[i] = fftData[i][j];
            temp=FFT1(temp,true);
            for (int i = 0; i < N; i++)
            	fftData[i][j]=temp[i];
        }
        //����ϵ��
        for(int i=0;i<N;i++)
        {
        	for(int j=0;j<M;j++)
        	{
        		fftData[i][j]=fftData[i][j].Mul(1.0/(Math.sqrt(N)*Math.sqrt(N)));
        	}
        }
        
        return fftData;
    }
    
    
    /**fft��任
     * @param fftData
     * @return
     */
    public static int[] IFFT2(Complex[][] fftData)
    {
		int M=fftData[0].length;
		int N=fftData.length;
		Complex[][] ifftData=new Complex[N][M];
        //��y�����Ͻ��п��ٸ���Ҷ�任
        for (int i = 0; i < N; i++)
        {            
            ifftData[i] = FFT1(fftData[i],false);
        }
        //��x������и���Ҷ�任
        for (int j = 0; j < M; j++)
        {
            //ÿһ��������Ҷ�任
        	Complex[] temp=new Complex[N];
            for (int i = 0; i < N; i++)
                temp[i] = ifftData[i][j];
            temp=FFT1(temp,false);
            for (int i = 0; i < N; i++)
            	ifftData[i][j]=temp[i];
        }
        //����ϵ��
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
     * @param forward =true��ʾ���任��false��ʾ��任
     * @return
     */
    private static Complex[] FFT1(Complex[] f,boolean forward)
    {
    	int N=f.length;
    	int power= (int) (Math.log10(N)/Math.log10(2));
    	Complex[] F=new Complex[N];
    	//����ż����
    	for(int i=0;i<N;i++)
    	{
    		int p = 0;  
            for (int j = 0; j < power; j++)  
                if ((i & (1 << j)) != 0)  
                    p += 1 << (power - j - 1);  

            F[p] = f[i];
    	}
    	//��������
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
        
        
    	//��������
    	for(int L=1;L<=power;L++)//��L����
    	{
    		int bf=1<<(power-L);//������
    		for(int n=0;n<bf;n++)//��n+1������
    		{
    			int bfsize=1<<L;//���δ�С
    			for(int k=0;k<bfsize/2;k++)//��k+1����������
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
     * ����
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
