package TamperDetect;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Algorithm.Common;
import Match.ShowImage;

/**��һ��ͼƬ��ps�д򿪣����Ϊһ���µ�ͼƬ��Ƚ�����ͼƬ��Ӧ���ص��ֵ
 * ***��һ�����ϵ����ص�ֵ��һ��***
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
		//��һ
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
				pixels1[i]=pixels1[i]&0xffff0000;//Ϳ��
			}
		}
		new ShowImage("�仯������",Common.createImage(pixels1, width, height));

		//����
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
