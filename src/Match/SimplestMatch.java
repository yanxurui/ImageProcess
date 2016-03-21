package Match;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SimplestMatch 
{
	
	public static void main(String[] args)
	{
		//��ȡͼƬ
		BufferedImage img1 = null,img2 = null;
		try {
			img1=ImageIO.read(new File("lena.jpg"));
			img2=ImageIO.read(new File("lenna.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		int similarity=match(img1,img2);
		
		System.out.println("���ƶȣ�"+similarity+"%");
	}
	
	public static int match(BufferedImage img1,BufferedImage img2)
	{
		String hashCode1=new PHash(img1).getPHash();
		String hashCode2=new PHash(img2).getPHash();
		//6.�������
		int difference = 0;
		double similarity=0.0;//�԰ٷ�����ʾ�����ƶ�
		System.out.println(hashCode1);
		System.out.println(hashCode2);
		for(int i=0;i<PHash.SIZE*PHash.SIZE;i++)
		{
			if(hashCode1.charAt(i)!=hashCode2.charAt(i))
				difference++;
		}
		similarity=100-100.0*difference/(PHash.SIZE*PHash.SIZE);
		System.out.println("����ֵ��"+difference);
		return (int) similarity;
	}	
}

class PHash
{
	public static int SIZE=8;
	private BufferedImage img,thumbImg;
	int width,height;
	public PHash(BufferedImage img)
	{
		this.img=img;
		width=img.getWidth();
		height=img.getHeight();
	}
	public String getPHash()
	{
		//1.��С�ߴ�
		 createThumbImage();  
		
		//2.��ɫ��
		int[][] pixels = new int[height][width];
		int avgPixel= 0; 
		for (int y = 0; y <height; y++) 
		{
			for(int x = 0; x < width; x++) 
		    {
				pixels[y][x] = rgbToGray(thumbImg.getRGB(x, y));
				avgPixel +=pixels[y][x];
		    }
		}
		
		//3.����ƽ��ֵ
		avgPixel = avgPixel /(width*height);  
		
		//4.�Ƚ����صĻҶ�
		int[][] comps=new int[SIZE][SIZE];//��height*width�ľ���ת��SIZE*SIZE��С�ķ���
		int startX=(SIZE-width)/2,startY=(SIZE-height)/2;
		for (int y = 0; y <height; y++) 
		{
			for(int x = 0; x < width; x++) 
		    {
				if(pixels[y][x] >= avgPixel) {  
					comps[y+startY][x+startX]=2;
			    }else {  
			    	comps[y+startY][x+startX]=1;
			    }
		    }
		} 

		//5.�����ϣֵ
		StringBuffer hashCode = new StringBuffer();
		for(int y=0;y<SIZE;y++)
		{
			for(int x=0;x<SIZE;x++)
			{
				hashCode.append(comps[y][x]);
			}
		}

		return hashCode.toString();
	}
	
	private void createThumbImage()
	{
		if(width < height) 
		{
			width=width*SIZE/height;
			height=SIZE;
	    }else 
	    {  
	    	height=height*SIZE/width;
	    	width=SIZE;
	    }
		
		thumbImg= new BufferedImage(width, height,img.getType());  
		//���û�ͼ�໭��С�ߴ���ͼ  
		Graphics2D g = thumbImg.createGraphics();  
		g.drawImage(img,0 ,0 , width, height, null);
//		try {
//			ImageIO.write(thumbImg, "jpg", new File("output.jpg"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(width);System.out.println(height);
//		new ShowImage("thumb",thumbImg);		
	}
	/**  
	 * �Ҷ�ֵ����  
	 * @param pixels ��ɫRGBֵ(Red-Green-Blue ������)  
	 * @return int �Ҷ�ֵ  
	 */  
	private static int rgbToGray(int pixels) {  
	       // int _alpha =(pixels >> 24) & 0xFF;  
	       int red = (pixels >> 16) & 0xFF;  
	       int green = (pixels >> 8) & 0xFF;  
	       int blue = (pixels) & 0xFF;  
	       return (int) ((0.3 * red + 0.59 * green + 0.11 * blue)/4);  
	}
}