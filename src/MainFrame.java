import java.awt.EventQueue;
import java.awt.Image;
import java.awt.MediaTracker;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

import javax.swing.JTabbedPane;
import javax.swing.JRadioButton;
import javax.swing.UIManager;

import Algorithm.Elements;
import Algorithm.Common;
import ImgTrans.Complex;
import ImgTrans.DCT;
import ImgTrans.DFT;
import ImgTrans.FFT;
import TamperDetect.CopyDetect;
import UI.JImage;
import UI.MyPanel;
import UI.SettingDialog;

import java.awt.Font;

public class MainFrame implements ActionListener 
{
	private JFrame frame;
	private JMenuItem openItem,closeItem,grayItem,histItem,equalItem,linearItem;
	private JTabbedPane jtp;
	 
	private Image image=null;
	private int iw,ih;
    private int[] pixels;
    
    private Common common;
    private JRadioButton fitscreen;
    private JRadioButton originalsize;
    private JMenu freqTransMenu;
    private JMenuItem dftItem;
    private JMenuItem dctItem;
    private JMenuItem fftItem;
    private JMenu tamperDetectMenu;
    private JMenuItem copyItem;
   
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					Font font = new Font("宋体",Font.PLAIN,13);
			        UIManager.put("Button.font", font); 
			        UIManager.put("Menu.font", font);
			        UIManager.put("MenuItem.font", font);
			        UIManager.put("RadioButton.font", font);
			        
					MainFrame window = new MainFrame();
					window.frame.setVisible(true);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainFrame() 
	{
		initialize();
        common = new Common();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() 
	{
		frame = new JFrame();
		frame.setTitle("My Image Process");
		frame.setSize(1280, 720);
		//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);//最大化显示
		frame.setLocationRelativeTo(null);//屏幕居中显示
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu fileMenu = new JMenu("文件");
		menuBar.add(fileMenu);
		
		openItem= new JMenuItem("打开");
		openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		fileMenu.add(openItem);
		openItem.addActionListener(this);
		
		closeItem = new JMenuItem("关闭");
		closeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
		fileMenu.add(closeItem);
		closeItem.addActionListener(this);
		
		JMenu elementMenu = new JMenu("空域增强");
		menuBar.add(elementMenu);
		
		grayItem = new JMenuItem("灰度化");
		elementMenu.add(grayItem);
		grayItem.addActionListener(this);
		
		histItem = new JMenuItem("直方图");
		elementMenu.add(histItem);
		histItem.addActionListener(this);
		
		equalItem = new JMenuItem("均衡化");
		elementMenu.add(equalItem);
		equalItem.addActionListener(this);
		
		linearItem = new JMenuItem("线性变换");
		elementMenu.add(linearItem);
		linearItem.addActionListener(this);
		
		freqTransMenu = new JMenu("时频变换");
		menuBar.add(freqTransMenu);
		
		dftItem = new JMenuItem("DFT");
		freqTransMenu.add(dftItem);
		dftItem.addActionListener(this);
		
		fftItem = new JMenuItem("FFT");
		freqTransMenu.add(fftItem);
		fftItem.addActionListener(this);
		
		dctItem = new JMenuItem("DCT");
		freqTransMenu.add(dctItem);
		dctItem.addActionListener(this);
		
		tamperDetectMenu = new JMenu("篡改检测");
		menuBar.add(tamperDetectMenu);
		
		copyItem = new JMenuItem("拷贝移动");
		tamperDetectMenu.add(copyItem);
		copyItem.addActionListener(this);
		
		fitscreen = new JRadioButton("适应屏幕");
		menuBar.add(fitscreen);
		fitscreen.addActionListener(this);
		
		originalsize = new JRadioButton("原始尺寸");
		menuBar.add(originalsize);
		originalsize.addActionListener(this);
		
		ButtonGroup bg=new ButtonGroup();
		bg.add(fitscreen);
		bg.add(originalsize);
		fitscreen.setSelected(true);
		
		jtp = new JTabbedPane(JTabbedPane.TOP);
		frame.setContentPane(jtp);
	
	}

	@Override
	public void actionPerformed(ActionEvent evt) 
	{
		// TODO Auto-generated method stub
    	MediaTracker tracker = new MediaTracker(frame);
    	//打开一幅图像
        if (evt.getSource() == openItem) 
        {
        	//文件选择对话框
            JFileChooser chooser = new JFileChooser();
            //common.chooseFile(chooser, "C:\\Users\\YXR\\Desktop", 0);//设置默认目录,过滤文件
            
            common.chooseFile(chooser, 0);
            int r = chooser.showOpenDialog(null);                    
            
            if(r == JFileChooser.APPROVE_OPTION) 
            {
                String path = chooser.getSelectedFile().getAbsolutePath();
                String name=chooser.getSelectedFile().getName();
                //设置上次浏览的目录和选中的文件
                String directory=path.substring(0,path.lastIndexOf('\\'));
                Common.setCurrentDirectory(directory);
                Common.setSelectedFile(name);
                //装载图像
			    Image iImage = common.openImage(path, tracker);
			    MyPanel mp=new MyPanel();
			    mp.add(new JImage(iImage,name));
			    JScrollPane jsp=new JScrollPane(mp);
			    jtp.addTab(name,jsp);
			    jtp.setSelectedComponent(jsp);
            }
        }
        else if(evt.getSource()==closeItem)
        {
        	int curIndex=jtp.getSelectedIndex();
        	if(curIndex!=-1)
        		jtp.removeTabAt(curIndex);
        }
        else if (evt.getSource() == grayItem)
        {
        	getImage();
        	if(image!=null)
        	{
        		pixels = Common.grabber(image, iw, ih);
    			
    			//对图像进行灰度化
    			pixels = Elements.toGray(pixels);
    			
    			Image oImage=Common.createImage(pixels,iw,ih);
    			drawImage(new JImage(oImage,"灰度图"));
    		}
        }
        else if(evt.getSource()==histItem)
        {
        	getImage();
        	if(image!=null)
        	{
        	    pixels = Common.grabber(image, iw, ih);
				
				Image hist = Elements.getHistogram(pixels);
				
				drawImage(new JImage(hist,"直方图"));
			}
        }
        else if(evt.getSource()==equalItem)
        {
        	getImage();
        	if(image!=null)
        	{
        	    pixels = Common.grabber(image, iw, ih);
				
				pixels = Elements.equalization(pixels);
				
				Image hist = Elements.getHistogram(pixels);
				Image oImage=Common.createImage(pixels,iw,ih);
				
				drawImage(new JImage(oImage,"直方图均衡化后的效果"));
				drawImage(new JImage(hist,"均衡化后的直方图"));
			}
        }
        else if (evt.getSource() == linearItem)
        {
        	getImage();
        	if(image!=null)
        	{
        	    String[] paraName={"钭率","截距"};
        	    String[] initValue={"1", "0"};
        	    SettingDialog dialog=new SettingDialog(frame,"线性变换",paraName,initValue);
        	    if(dialog.getResult())
        	    {
        	    	float k = dialog.getPadx();//斜率
    	        	int   b = (int)dialog.getPady();//截距
    	               	       		
    				pixels = Common.grabber(image, iw, ih);
    				
    				//对图像进行进行线性拉伸
    				pixels = Elements.linetrans(pixels, k, b);
    				
    				Image oImage=Common.createImage(pixels,iw,ih);
    				drawImage(new JImage(oImage,"线性变换后的效果"));
        	    }
			}        	        	
        }
        else if (evt.getSource() == dftItem)
        {
        	getImage();
        	if(image!=null)
        	{
        		pixels = Common.grabber(image, iw, ih);
        		int[][] grays = new int[ih][iw];//灰度矩阵
        	    for (int i = 0; i < ih; i++)
        	    {
        	    	for(int j=0;j<iw;j++)
        	    	{
        	    		grays[i][j] = pixels[i*iw+j]&0xff;//默认输入图像是灰度图
		        	}
			    }

        		Complex[][] dftData=DFT.DFT2(grays);
        		pixels=DFT.visual(dftData);
        		Image oImage1=Common.createImage(pixels,iw,ih);
				drawImage(new JImage(oImage1,"DFT变换"));
        		
				pixels=DFT.IDFT2(dftData);
                Image oImage2=Common.createImage(pixels,iw,ih);
				drawImage(new JImage(oImage2,"DFT逆变换"));
        	}
        }
        else if (evt.getSource() == fftItem)
        {
        	getImage();
        	if(image!=null)
        	{
        		pixels = Common.grabber(image, iw, ih);
        		//如果宽和高都是2的整数次幂，则调用快速傅里叶变换
        		double hp=Math.log10(ih)/Math.log10(2);
        		double wp=Math.log10(iw)/Math.log10(2);
        		if((hp-(int)hp)==0&&(wp-(int)wp)==0)
        		{
        			int[][] grays = new int[ih][iw];//灰度矩阵
            	    for (int i = 0; i < ih; i++)
            	    	for(int j=0;j<iw;j++)
            	    		grays[i][j] = pixels[i*iw+j]&0xff;//默认输入图像是灰度图
            		Complex[][] fftData=FFT.FFT2(grays);
            		pixels=FFT.visual(fftData);
            		Image oImage1=Common.createImage(pixels,iw,ih);
    				drawImage(new JImage(oImage1,"FFT变换"));
    				
    				pixels=FFT.IFFT2(fftData);
                    Image oImage2=Common.createImage(pixels,iw,ih);
    				drawImage(new JImage(oImage2,"FFT逆变换"));
        		}
        		else
        			JOptionPane.showMessageDialog(frame, 
        					"fft只能处理宽高均为2的整数次幂的图像");
        	}
        }
        else if (evt.getSource() == dctItem)
        {
        	getImage();
        	if(image!=null)
        	{
				pixels = Common.grabber(image, iw, ih);
				double[][] pix = new double[ih][iw];//输入灰度矩阵
        	    for (int i = 0; i < ih; i++)
                    for (int j = 0; j < iw; j++)
                        pix[i][j] = pixels[i*iw+j]&0xff;
                        
                    
        	    
        	    DCT.DCT2(pix);
        	    pixels = common.toPixels(pix, iw, ih);
				Image oImage=Common.createImage(pixels,iw,ih);
				drawImage(new JImage(oImage,"DCT变换"));
				
				DCT.IDCT2(pix);
        	    pixels = common.toPixels(pix, iw, ih);
				Image oImage2=Common.createImage(pixels,iw,ih);
				drawImage(new JImage(oImage2,"DCT逆变换"));
			}
        }
        else if(evt.getSource()==copyItem)
        {
        	getImage();
        	if(image!=null)
        	{
        		CopyDetect cd=new CopyDetect(image);
    			Image oImage=cd.run();
				drawImage(new JImage(oImage,"篡改检测结果"));
        	}
        }
        else if(evt.getSource() == fitscreen)
        {
        	JImage.setDisplayMode(JImage.FITSCREEN);
        	//frame.validate();
        	frame.repaint();
        }
        else if(evt.getSource() == originalsize)
        {
        	JImage.setDisplayMode(JImage.ORIGINALSIZE);
        	//frame.validate();
        	frame.repaint();
        }
	}
	
	/**
	 * 获取处理的图像，默认为打开的原图
	 * 获取图像的宽高
	 */
	public void getImage()
	{
		image=null;
		JScrollPane jsp=(JScrollPane) jtp.getSelectedComponent(); 	
		if(jsp!=null)        	
    	{
			MyPanel mp=(MyPanel) jsp.getViewport().getView(); 
			image=mp.getImg();
    		iw=image.getWidth(null);
    		ih=image.getHeight(null);
    	}
	}
	/**
	 * 显示图片
	 * @param oImage
	 */
	public void drawImage(JImage oImage)
	{
		JScrollPane jsp=(JScrollPane) jtp.getSelectedComponent();
		MyPanel mp=(MyPanel) jsp.getViewport().getView();
		mp.add(oImage);
		//jp.repaint();
		//jsp.repaint();
		//不加这一句的话有些情况图片无法显示
		//！！！为什么都是调用这个方法，有的图片显示的好好的，而有的却要刷新后才能显示
		//!!!为什么调用上面的方法刷新却没有效果
		frame.repaint();
	}
}