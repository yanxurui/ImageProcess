package Algorithm;
/**
 * @Elements.java
 * @Version 1.0 2010.02.09
 * @Author Xie-Hua Sun 
 */


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.*;

public class Elements
{
	/**
	 * ��ȡֱ��ͼ
	 * @param pix һά�����ʾ��RGBͼ�������
	 * @return ֱ��ͼ
	 */
	public static BufferedImage getHistogram(int[] pix) 
	{
        int[] grayValue=getGrayValue(pix);
        int[] intens=getIntensity(grayValue);
        return drawHistogram(intens);
	}
	/**
	 * ͳ�Ƹ����Ҷȼ���Ƶ��
	 * @param grayPix �Ҷ�ͼ�����������
	 * @return �����Ҷȼ���Ƶ��
	 * @throws Exception 
	 */
    private static int[] getIntensity(int[] grayValue)
	{	
		int[] intensity = new int[256];		
		for(int i = 0; i < grayValue.length; i++) 
		{
			if(grayValue[i]<0||grayValue[i]>255)
				System.out.println("Illegal gray value");
			intensity[grayValue[i]]++;
		}
		return intensity;
	}
    /**
     * ��ֱ��ͼ
     * @param intens �Ҷ�Ƶ��
     * @return �Ҷ�ֱ��ͼ
     */
	private static BufferedImage drawHistogram(int[] intens)
	{
		final int size=280,margin=5;
		BufferedImage histImage = new BufferedImage(size,size, BufferedImage.TYPE_4BYTE_ABGR);
      
		// draw XY Axis lines
        Graphics2D g2d = histImage.createGraphics();
        g2d.setPaint(Color.BLACK);
        g2d.drawLine(margin, size-margin, size-margin, size-margin);
        g2d.drawLine(size-margin, size-margin, size-margin-5, size-margin-5);
        g2d.drawLine(size-margin, size-margin, size-margin-5, size-margin+5);
        
        g2d.drawLine(margin, size-margin, margin, margin);
        g2d.drawLine(margin, margin, margin-5, margin+5);
        g2d.drawLine(margin, margin, margin+5, margin+5);
        
        g2d.setPaint(Color.GREEN);
        float rate = (float) ((size-2.0*margin)/Common.maximum(intens));
        for(int i=0; i<intens.length; i++) 
        {
        	int frequency = (int)(intens[i] * rate);
        	g2d.drawLine(margin+ i, size-margin-1, margin + i, size-margin-1-frequency);
        }
		return histImage;	
	}

	/**
	 * �ҶȻ�
	 * @param pix RGBͼ������أ�������һά������
	 * @return ��Ӧ�Ҷ�ͼ�������
	 */
    public static int[] toGray(int[] pix)
    {
    	int[] outPix=getGrayValue(pix);
    	for(int i=0;i<outPix.length;i++)
    	{
    		outPix[i] = 255 << 24|outPix[i] << 16|outPix[i] << 8|outPix[i];
		}		
		return outPix;
	}	
    
    
    /**���ػҶ�ֵ��һά����
     * @param pix
     * @return
     */
    private static int[] getGrayValue(int[] pix)
    {
    	ColorModel cm = ColorModel.getRGBdefault();
		int r, g, b, gray;
		int[] grayValue=new int[pix.length];
		
		for(int i = 0; i < pix.length; i++)	
		{			
			r = cm.getRed(pix[i]);
			g = cm.getGreen(pix[i]);
			b = cm.getBlue(pix[i]);	
            gray = (int)(0.299 * r + 0.587 *g + 0.114 *b);//������ֵ����Ҷ�
            grayValue[i]=gray;
		}		
		return grayValue;
    }
    

    /**���ػҶ�ֵ�Ķ�ά����
     * @param pix
     * @param width
     * @param height
     * @return
     */
    public static int[][] getGrayValue(int[] pix,int width,int height)
    {
    	int[][] result=new int[height][width];
    	int[] temp=getGrayValue(pix);
    	for(int i=0;i<height;i++)
    		for(int j=0;j<width;j++)
    			result[i][j]=temp[i*width+j];
    	return result;
    }
    
    
	/**���⻯
	 * @param pixels
	 * @return
	 */
	public static int[] equalization(int[] pixels) 
	{
		int[] grayValue=getGrayValue(pixels);
		int[] intens=getIntensity(grayValue);
		int[] map=new int[256];
		int[] outPix=new int[pixels.length];
		//ȷ��ӳ���ϵ
		int sum=0;
		for(int i=0;i<256;i++)
		{
			sum+=intens[i];
			map[i]=(int) (1.0*sum/pixels.length*255+0.5);//��������
		}
		
		//�任
		int gray;
		for(int i=0;i<grayValue.length;i++)
		{
			gray= map[grayValue[i]];
			outPix[i] = 255 << 24|gray << 16|gray << 8|gray;
		}
		return outPix;
	}
	
	/**
	 * ���Ա任
	 * @param pix ԭͼ������
	 * @param k �任��б��
	 * @param a �任�Ľؾ�
	 * @return �任���ͼ������
	 */
    public static int[] linetrans(int[] pix, float k, int a)
    {
		ColorModel cm = ColorModel.getRGBdefault();
		int r, g, b;
		int[] outPix=new int[pix.length];
		
		for(int i = 0; i < pix.length; i++) 
		{
			r = cm.getRed(pix[i]);
			g = cm.getGreen(pix[i]);
			b = cm.getBlue(pix[i]);
			
			//����ͼ������
			r  = (int)(k * r + a);
			g  = (int)(k * g + a);
			b  = (int)(k * b + a);
			
			if(r > 255)   r = 255;
			if(g > 255)   g = 255;
			if(b > 255)   b = 255;
			
			if(r <0)   r = 0;
			if(g <0)   g = 0;
			if(b <0)   b = 0;
			
			outPix[i] = 255 << 24|r << 16|g << 8|b;
		}
		return outPix;
	}

}
