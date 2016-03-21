package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

public class JImage extends JComponent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int FITSCREEN=0;
	public static final int ORIGINALSIZE=1;
	
    private Image image;
    private String title;
    private Rectangle rect;
    private static int displayMode=FITSCREEN;
    
    public JImage(Image image,String title)
    {
        super();
        this.image = image;
        this.title=title;
    }

	public Image getImage()
    {
    	return image;
    }
    public static void setDisplayMode(int mode)
    {
    	displayMode=mode;
    }
    
    /**
     * �������
     * ���ͼ��ȴ��ڴ�����С����ʾ������ԭ����������ʾ
     * @see javax.swing.JComponent#paintComponent(Graphics)
     */
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        int compoWidth =this.getBounds().width;
        int compoHeight =this.getBounds().height;
        int imageWidth = image.getWidth(this);
        int imageHeight = image.getHeight(this);
        
        FontMetrics fm=g.getFontMetrics();
        int titleWidth=fm.stringWidth(title);
        int titleHeight=fm.getAscent()+fm.getDescent();
        compoHeight-=titleHeight;

        if(displayMode==ORIGINALSIZE)
        {
        	if(!this.isPreferredSizeSet())
        	{
        		this.setPreferredSize(new Dimension(imageWidth,imageHeight+titleHeight));
        		this.revalidate();//û����һ���������Ĵ���
        	}
        }
        else
        {
        	if(this.isPreferredSizeSet())
        	{
        		this.setPreferredSize(null); 
        		this.revalidate();
        	}    
        }
        
        int x=0,y=0,width,height;
        if(compoWidth>=imageWidth&&compoHeight>=imageHeight)
        {
        	x = (compoWidth - imageWidth) /2;
        	y = (compoHeight - imageHeight) /2;
        	width=imageWidth;
        	height=imageHeight;
            g.drawImage(image, x, y,width,height, this);
        }
        else
        {
        	float scale=(float)compoWidth/compoHeight;
        	float imageScale=(float)imageWidth/imageHeight;
        	if(imageScale>=scale)
        	{
        		width=compoWidth;//��С��Ŀ��
        		height=imageHeight*compoWidth/imageWidth;//��С���ͼ��߶�
        		x=0;
        		y=(compoHeight-height)/2;
        	}
        	else
        	{
        		height=compoHeight;
        		width=imageWidth*compoHeight/imageHeight;
        		y=0;
        		x=(compoWidth-width)/2;
        	}
        	g.drawImage(image, x, y,width,height, this);
        }
        
        //���������ʾ
        g.drawString(title, (compoWidth-titleWidth)/2, compoHeight+fm.getAscent());
        
        rect=new Rectangle(x,y,width,height);
    }

	public boolean contains(Point p)
	{
		double x=p.getX()-this.getLocation().getX();
		double y=p.getY()-this.getLocation().getY();
		Point p2=new Point();
		p2.setLocation(x,y);
		if(rect.contains(p2))
			return true;
		return false;
	}
	
	public Rectangle getArea()
	{
		return rect;
	}
	
	
	public void saveAs()
	{
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();
		g.drawImage(image,0,0,null);
		
		//FileDialog�����Զ���ס�ϴη��ʵ�Ŀ¼������Ļ�ϵ�λ�ã���һ�δ򿪣�Ĭ�ϣ�λ������
		FileDialog fd=new FileDialog(new JFrame(),"�����ļ�",FileDialog.SAVE);
		if(title.endsWith(".jpg"))
			fd.setFile(title);
		else
			fd.setFile(title+".jpg");
		fd.setVisible(true);
		String directory=fd.getDirectory();
		String file=fd.getFile();
//		System.out.println(directory);
//		System.out.println(file);

		//���ȡ����drectory��fileͬʱΪnull
		if(directory!=null&&file!=null)
		{
			File newFile = new File(directory+file);

			try 
			{
				ImageIO.write(bi, "jpg", newFile);
			} 
			catch (IOException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	public void setSelected(boolean flag)
	{
        if(flag)
        	setBorder(new LineBorder(Color.RED));
        else
        	setBorder(null);
	}
	
    public static void main(String[] args)
    {
        JFrame frame =new JFrame("JImagePane Test");
        Image iamge = null;
		try {
			iamge = new ImageIcon(new URL("file:/C:/Users/YXR/Desktop/lenna.jpg")).getImage();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        JImage imagePane =new JImage(iamge,"ʾ��");
        JScrollPane jsp=new JScrollPane(imagePane);
        frame.getContentPane().add(jsp, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
