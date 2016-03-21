package ImgTrans;

public class DFT 
{
	/**2d-dft
	 * @param pix ����ͼ��ĻҶȾ���
	 * @return ��ά����Ҷ�任��ĸ�������
	 */
	public static Complex[][] DFT2(int[][] grays)
	{
		int M=grays[0].length;
		int N=grays.length;
		
		Complex[][] dftData=new Complex[N][M];
		//ʵ���ź�ת�ɸ����ź�
		for(int i=0;i<N;i++)
		{
			for(int j=0;j<M;j++)
			{
				dftData[i][j]=new Complex(grays[i][j],0);
			}
		}
		//�ȶ�������ִ��һά��ɢ����Ҷ�任
		for(int i=0;i<N;i++)
		{
			dftData[i]=DFT1(dftData[i],true);
		}
		//�ٶ�������ִ��һά��ɢ����Ҷ�任
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
	 * @param dftData ����Ҷ�任��ĸ�������
	 * @return ���任�ָ������ĻҶȸ�������
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
	
	/**1d-dft ����������Ǹ�����ʽ
	 * @param f �����һά��ɢ�ź�
	 * @param forward =true ��ʾ���任��false��ʾ��任
	 * @return ���ر任����ź�
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
	
	
	
    /**���ݿ��ӻ���
     * Ƶ��Ϊ������,ֱ��������������
     * @param dftData
     * @return �Ҷ�ͼ���һά��������
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
     * ����������ת��Ϊ�Ҷ�ͼ��
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
     * ����
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
