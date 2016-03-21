package UI;
import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;

public class SettingDialog extends JDialog implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel pLabel, qLabel;
	private JTextField pField, qField;
    private Checkbox   xbox, ybox;
	
	private JButton okButton,cancelButton;
	private boolean result;
	
	/**显示参数设置的对话框
	 * @param pp parameter panel
	 * @param title
	 */
	public SettingDialog(JFrame parentContainer, String title,String[] paraName,String[] initValue)
	{
		super(parentContainer, title, true);
		Container dialogContentPane = getContentPane();
		
		JPanel pp=ParameterPane("参数",paraName,initValue);
		dialogContentPane.add(pp, BorderLayout.CENTER);
		
		JPanel buttonsPanel = new JPanel();  
		okButton= new JButton("确定");				
	    okButton.addActionListener(this);
	    buttonsPanel.add(okButton);
	    cancelButton= new JButton("取消");				
	    cancelButton.addActionListener(this);
	    buttonsPanel.add(cancelButton);
	    
		dialogContentPane.add(buttonsPanel, BorderLayout.SOUTH);
		
		pack();
		setLocationRelativeTo( parentContainer );//是对话框显示在父窗口的中央
		setVisible(true);
	}
	
	public boolean getResult()
	{
		return result;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource()==okButton)
		{
			result=true;
		}
		setVisible(false);
		dispose();
	}
	
	public JPanel ParameterPane(String title,String[] paraName,String[] initValue)//2个文本框型
	{
		JPanel pp=new JPanel();
		pLabel = new JLabel(paraName[0]);
		qLabel = new JLabel(paraName[1]);
		pField = new JTextField(initValue[0], 5);	
		qField = new JTextField(initValue[1], 5);	
		
		pp.add(pLabel);		
		pp.add(pField);
		pp.add(qLabel);
		pp.add(qField);

		pp.setBorder(new CompoundBorder(
			BorderFactory.createTitledBorder(title),
			BorderFactory.createEmptyBorder(10,10,10,10)));
		
		return pp;
	}
	
	public JPanel ParameterPane(String title, String[] paraName)   //2个按钮型
	{
		JPanel pp=new JPanel();
		CheckboxGroup cbg = new CheckboxGroup();
		xbox = new Checkbox(paraName[0], cbg, true);
		ybox = new Checkbox(paraName[1], cbg, true);
		pp.add(xbox); 
		pp.add(ybox);
		
		pp.setBorder(new CompoundBorder(
		    BorderFactory.createTitledBorder(title),
		    BorderFactory.createEmptyBorder(10, 10, 50, 10)));
		
		return pp;
	}
	
	public float getPadx() 
	{
		return Float.parseFloat(pField.getText());
	}
	
	public float getPady() 
	{
		return Float.parseFloat(qField.getText());
	}
	
	public int getRadioState()
	{
		if(xbox.getState())      return 0;
		else if(ybox.getState()) return 1;
		else return 0;
	}
}