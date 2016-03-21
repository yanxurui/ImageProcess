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
		//篡改
		//复制粘贴某个区域
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
		//如果原图尺寸不是8的倍数可能就会忽略右和下边缘
		for(int i=0;i<=height-SIZE;i+=1)
		{
			for(int j=0;j<=width-SIZE;j+=1)
			{
				Piece p=new Piece(i,j);
				if(!ts.add(p))//找到重复的块
				{
					count++;
				} 
			}
		}
		System.out.println("找到重复的区域:"+count+"块");
	}
	
	private  BufferedImage display()
	{
		BufferedImage bi=new BufferedImage(width,height,BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g=bi.createGraphics();
		g.drawImage(srcImg, 0, 0, null);
		
		System.out.println("可能是重复的区域有："+hashMap.size());
		
		Set<Entry<Criteria, List<Piece>>> set=hashMap.entrySet();
		List<Piece> temp;
		int count=0;
		for(Entry<Criteria, List<Piece>> entry:set)
		{
			//针对
			//过滤一些特殊的区域
			if(entry.getKey().distance<10)
				continue;
			temp=entry.getValue();
			//超过阈值断言是复制粘贴区域 
			if(temp.size()>=30)
			{
				if(temp.get(0).x==0&&temp.get(0).y==0)
					continue;
				System.out.println(entry.getKey());
				System.out.println("重复的块数："+temp.size());
				drawBox(g,temp);
				count++;
			}
		}
		if(count>0)
			System.out.println("复制粘贴区域数："+count);
		else System.out.println("没有篡改");
		return bi;
	}
	
	private void drawBox(Graphics2D g,List<Piece> repeatPiece)
	{		
		//定界
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
		System.out.println("左上角："+x1+" "+y1);
		System.out.println("右下角："+x2+" "+y2);
		
		g.setColor(colors[++Index%colors.length]);
		g.drawRect(y1, x1, y2-y1, x2-x1);//画一个矩形标出复制粘贴区域
		g.drawRect(y3, x3, y2-y1, x2-x1);//用相同的颜色标出重复的区域
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
		 * @param matrix 8X8的方阵
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
			
			//两个块匹配
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
		//同一个对象
		if (this == o) 
			return true;
		//不同类型
        if (o == null || getClass() != o.getClass()) 
        	return false;
        
        Criteria c = (Criteria) o;
        //将斜率和距离同时作为判断标准
        if (c.slope==this.slope&&c.distance==this.distance)
        	return true;
        
        return false;
	}
	
}

