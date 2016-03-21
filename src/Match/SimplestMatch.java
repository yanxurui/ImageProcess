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
		//读取图片
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
		
		System.out.println("相似度："+similarity+"%");
	}
	
	public static int match(BufferedImage img1,BufferedImage img2)
	{
		String hashCode1=new PHash(img1).getPHash();
		String hashCode2=new PHash(img2).getPHash();
		//6.计算差异
		int difference = 0;
		double similarity=0.0;//以百分数表示的相似度
		System.out.println(hashCode1);
		System.out.println(hashCode2);
		for(int i=0;i<PHash.SIZE*PHash.SIZE;i++)
		{
			if(hashCode1.charAt(i)!=hashCode2.charAt(i))
				difference++;
		}
		similarity=100-100.0*difference/(PHash.SIZE*PHash.SIZE);
		System.out.println("差异值："+difference);
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
		//1.缩小尺寸
		 createThumbImage();  
		
		//2.简化色彩
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
		
		//3.计算平均值
		avgPixel = avgPixel /(width*height);  
		
		//4.比较像素的灰度
		int[][] comps=new int[SIZE][SIZE];//将height*width的矩阵转成SIZE*SIZE大小的方正
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

		//5.计算哈希值
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
		//调用画图类画缩小尺寸后的图  
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
	 * 灰度值计算  
	 * @param pixels 彩色RGB值(Red-Green-Blue 红绿蓝)  
	 * @return int 灰度值  
	 */  
	private static int rgbToGray(int pixels) {  
	       // int _alpha =(pixels >> 24) & 0xFF;  
	       int red = (pixels >> 16) & 0xFF;  
	       int green = (pixels >> 8) & 0xFF;  
	       int blue = (pixels) & 0xFF;  
	       return (int) ((0.3 * red + 0.59 * green + 0.11 * blue)/4);  
	}
}