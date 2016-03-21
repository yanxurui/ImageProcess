package TamperDetect;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Algorithm.Common;
import Match.ShowImage;

/**将一张图片在ps中打开，另存为一张新的图片后比较两张图片对应像素点的值
 * ***有一半以上的像素点值不一样***
 * @author YXR
 *
 */
public class Test2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedImage img1 = null,img2 = null;
		try {
			img1=ImageIO.read(new File("E:\\programming\\JAVA\\Workspaces\\Eclipse\\ImageProcess\\lenna.jpg"));
			img2=ImageIO.read(new File("E:\\programming\\JAVA\\Workspaces\\Eclipse\\ImageProcess\\lenna2.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int width=img1.getWidth();
		int height=img1.getHeight();
		int count=0;
		//法一
		int[] pixels1=Common.grabber(img1,width,height );
		int[] pixels2=Common.grabber(img2, width, height);
		for(int i=0;i<pixels1.length;i++)
		{
			if(pixels1[i]!=pixels2[i])
			{
//				System.out.println(i);
//				System.out.println(((pixels1[i]>>24)&0xff)+" "+((pixels1[i]>>16)&0xff)+" "+((pixels1[i]>>8)&0xff)+" "+(pixels1[i]&0xff));
//				System.out.println(((pixels2[i]>>24)&0xff)+" "+((pixels2[i]>>16)&0xff)+" "+((pixels2[i]>>8)&0xff)+" "+(pixels2[i]&0xff));

				count++;
//				if(count==100)
//					System.exit(0);
				pixels1[i]=pixels1[i]&0xffff0000;//涂红
			}
		}
		new ShowImage("变化的区域",Common.createImage(pixels1, width, height));

		//法二
//		for(int i=0;i<img1.getWidth();i++)
//		{
//			for(int j=0;j<img1.getHeight();j++)
//			{
//				if(img1.getRGB(i, j)!=img2.getRGB(i, j))
//					count++;
//			}
//		}

		System.out.println(count);
	}

}
