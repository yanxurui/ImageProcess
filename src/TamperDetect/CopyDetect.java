package TamperDetect;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import Algorithm.Common;
import Algorithm.Elements;

public class CopyDetect 
{
	private static final int SIZE=8;
	private static final Color[] colors={Color.RED,Color.GREEN,Color.BLUE,Color.WHITE,Color.BLACK,Color.YELLOW};
	private int Index=-1;
	private Image srcImg;
	private int width,height;
	private int[][] gray;
	private HashMap<Criteria,List<Piece>> hashMap;
	
	public CopyDetect(Image image) 
	{
		srcImg=image;
		width=srcImg.getWidth(null);
		height=srcImg.getHeight(null);
		int[] pixels = Common.grabber(image, width, height);
		//�۸�
		//����ճ��ĳ������
//		for(int i=0;i<SIZE*2;i++)
//		{
//			for(int j=0;j<SIZE*3;j++)
//			{
//				pixels[(225+i)*width+315+j]=pixels[i*width+j];
//			}
//		}
//		srcImg=Common.createImage(pixels, width, height);
		
		gray=Elements.getGrayValue(pixels,width,height);
		
		hashMap=new HashMap<Criteria,List<Piece>>();
	}

	private void detect() 
	{		
		Set<Piece> ts=new TreeSet<Piece>();
		int count=0;
		//���ԭͼ�ߴ粻��8�ı������ܾͻ�����Һ��±�Ե
		for(int i=0;i<=height-SIZE;i+=1)
		{
			for(int j=0;j<=width-SIZE;j+=1)
			{
				Piece p=new Piece(i,j);
				if(!ts.add(p))//�ҵ��ظ��Ŀ�
				{
					count++;
				} 
			}
		}
		System.out.println("�ҵ��ظ�������:"+count+"��");
	}
	
	private  BufferedImage display()
	{
		BufferedImage bi=new BufferedImage(width,height,BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g=bi.createGraphics();
		g.drawImage(srcImg, 0, 0, null);
		
		System.out.println("�������ظ��������У�"+hashMap.size());
		
		Set<Entry<Criteria, List<Piece>>> set=hashMap.entrySet();
		List<Piece> temp;
		int count=0;
		for(Entry<Criteria, List<Piece>> entry:set)
		{
			//���
			//����һЩ���������
			if(entry.getKey().distance<10)
				continue;
			temp=entry.getValue();
			//������ֵ�����Ǹ���ճ������ 
			if(temp.size()>=30)
			{
				if(temp.get(0).x==0&&temp.get(0).y==0)
					continue;
				System.out.println(entry.getKey());
				System.out.println("�ظ��Ŀ�����"+temp.size());
				drawBox(g,temp);
				count++;
			}
		}
		if(count>0)
			System.out.println("����ճ����������"+count);
		else System.out.println("û�д۸�");
		return bi;
	}
	
	private void drawBox(Graphics2D g,List<Piece> repeatPiece)
	{		
		//����
		int x1,x2,y1,y2;
		int x3,y3;
		Piece temp=repeatPiece.get(0);
		x1=x2=temp.x;x3=temp.x2;
		y1=y2=temp.y;y3=temp.y2;
		for(Piece p:repeatPiece)
		{
			if(p.x<x1)
			{
				x1=p.x;
				x3=p.x2;
			}
			else if(p.x>x2)
			{
				x2=p.x;
			}
			
			if(p.y<y1)
			{
				y1=p.y;
				y3=p.y2;
			}
			else if(p.y>y2)
			{
				y2=p.y;
			}
		}
		x2+=SIZE;y2+=SIZE;
		System.out.println("���Ͻǣ�"+x1+" "+y1);
		System.out.println("���½ǣ�"+x2+" "+y2);
		
		g.setColor(colors[++Index%colors.length]);
		g.drawRect(y1, x1, y2-y1, x2-x1);//��һ�����α������ճ������
		g.drawRect(y3, x3, y2-y1, x2-x1);//����ͬ����ɫ����ظ�������
	}

	public Image run() 
	{
		detect();
		return display();
	}
	
	
	
	class Piece implements Comparable<Piece>
	{
		public int x,y,x2,y2;
		/**
		 * @param x
		 * @param y
		 * @param matrix 8X8�ķ���
		 */
		public Piece(int x,int y)
		{
			this.x=x;
			this.y=y;
		}
		
		public int get(int i,int j)
		{
			return gray[x+i][y+j];
		}
		
		public int getDistance()
		{
			return x-x2+y-y2;
		}
		private float getSlope()
		{
			return ((float)(x-x2))/(y-y2);
		}
		@Override
		public int compareTo(Piece o) {
			// TODO Auto-generated method stub
			
			int diff;
			for(int i=0;i<SIZE;i++)
			{
				for(int j=0;j<SIZE;j++)
				{
					diff=this.get(i, j)-o.get(i, j);
					//if(diff<-2||diff>2)
					if(diff<-1||diff>1)
					//if(diff!=0)
						return diff;
				}
			}
			
			//������ƥ��
			x2=o.x;y2=o.y;
			Criteria k=new Criteria(getSlope(),getDistance());
			List<Piece> l=hashMap.get(k);
			if(l!=null)
			{
				l.add(this);
			}
			else
			{
				l=new ArrayList<Piece>();
				l.add(this);
				hashMap.put(k, l);
			}
			
			return 0;
		}
		
		@Override
		public String toString()
		{
			StringBuffer sbf=new StringBuffer();
			for(int i=0;i<SIZE;i++)
			{
				for(int j=0;j<SIZE;j++)
				{
					sbf.append(this.get(i, j)+" ");
				}
				sbf.append('\n');
			}
			return sbf.toString();
		}

	}
}

class Criteria
{
	float slope;
	int distance;
	public Criteria(float s,int d)
	{
		slope=s;
		distance=d;
	}
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return slope+","+distance;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return (int) (slope+distance);
	}

	@Override
	public boolean equals(Object o) 
	{
		//ͬһ������
		if (this == o) 
			return true;
		//��ͬ����
        if (o == null || getClass() != o.getClass()) 
        	return false;
        
        Criteria c = (Criteria) o;
        //��б�ʺ;���ͬʱ��Ϊ�жϱ�׼
        if (c.slope==this.slope&&c.distance==this.distance)
        	return true;
        
        return false;
	}
	
}

