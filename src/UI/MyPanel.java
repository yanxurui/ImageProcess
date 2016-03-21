package UI;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;


public class MyPanel extends JPanel implements MouseListener, ActionListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPopupMenu jpm;
    private JMenuItem saveItem;
    private JMenuItem removeItem;
    private Component currComponent;
    
    private JImage selectedImg=null;
    
	public MyPanel()
	{
		super();
		init();
	}
    private void init() 
    {
        jpm=new JPopupMenu();
        saveItem=new JMenuItem("save");
        saveItem.addActionListener(this);
        removeItem=new JMenuItem("remove");
        removeItem.addActionListener(this);
        jpm.add(saveItem);
        jpm.add(removeItem);
        addMouseListener(this);
        
        setLayout(new GridLayout());
    }
    
    public Image getImg()
    {
    	if(selectedImg!=null)
    	{
    		return selectedImg.getImage();
    	}
    	return ((JImage)this.getComponent(0)).getImage();
    }
    
    @Override
	public Component add(Component comp) 
    {
    	int n=this.getComponentCount();
    	GridLayout gl=(GridLayout) this.getLayout();
    	int rows=gl.getRows();
    	if(n!=0&&n%rows==0)//���濪ʼ�ж��Ƿ�û���
    	{
//    		Rectangle rect=((JImage)getComponent(0)).getArea();
//    		int width=(int) rect.getWidth();
//    		int height=(int) rect.getHeight();
    		Image img=((JImage)getComponent(0)).getImage();
    		int width=img.getWidth(null);
    		int height=img.getHeight(null);
    		int totalWidth=n/rows*width;//ÿһ��ͼƬռ���ܿ��
    		int totalHeight=rows*height;//ÿһ��ͼƬռ���ܸ߶�

    		//����
    		if(totalWidth/totalHeight>this.getWidth()/this.getHeight())
    		{
    			gl.setRows(rows+1);
    		}
    	}
    	
		return super.add(comp);
	}
    
    
    
    
    
    
    
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		//if(e.getButton()==MouseEvent.BUTTON3)//�������Ҽ�����
		if(e.isPopupTrigger())
		{
			currComponent=this.getComponentAt(e.getPoint());
			if(currComponent!=this)//���᷵��null
			{
				JImage img=(JImage)currComponent;
				if(img.contains(e.getPoint()))//���������ڵ�λ��ͼƬ��Χ��
				{
					jpm.show(e.getComponent(),e.getX(),e.getY());
				}
			}
		}
		else if(e.getButton()==MouseEvent.BUTTON1)//���������
		{
			currComponent=this.getComponentAt(e.getPoint());
			if(currComponent!=this)//���᷵��null
			{
				JImage img=(JImage)currComponent;
				if(img.contains(e.getPoint()))//���������ڵ�λ��ͼƬ��Χ��
				{
					if(!img.equals(selectedImg))
					{
						if(selectedImg!=null)
							selectedImg.setSelected(false);
						selectedImg=img;
						selectedImg.setSelected(true);
					}
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource()==saveItem)
		{
			((JImage)currComponent).saveAs();
		}
		else if(e.getSource()==removeItem)
		{
			if(selectedImg.equals(currComponent))
				selectedImg=null;
			this.remove(currComponent);
			validate();
			repaint();
		}
	}

}
