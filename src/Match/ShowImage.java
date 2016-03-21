package Match;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ShowImage extends JFrame
{
	private static final long serialVersionUID = 1L;

	public ShowImage(String imgName,Image img)
	{
		setTitle(imgName);
		JLabel label = new JLabel(new ImageIcon(img));
		add(label);
		pack(); //使jframe窗口大小根据内容自动调整
		setVisible(true);
	}
	
	public static void main(String[] args)
	{
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("img1.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		new ShowImage("img1.jpg",image);
	}
}
