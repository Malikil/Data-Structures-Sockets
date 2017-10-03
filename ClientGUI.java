import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.*;

public static void main(String[] args)
{
	ClientGUI gui = new ClientGUI();
	gui.setVisible(true);
}

public class ClientGUI extends JFrame {

	private JLabel ipLabel = new JLabel("IP");
	private JLabel portLabel = new JLabel("PORT");
	private JLabel statusLabel = new JLabel("CHAT LOG");
	private JLabel listLabel = new JLabel("LIST");
	private JButton connectButton = new JButton("Connect");
	private JButton sendButton = new JButton("Send");
	private JTextField ipInput = new JTextField();
	private JTextField portInput = new JTextField();
	private JTextField sendInput = new JTextField();
	private JList<String> viewDisplay = new JList<String>();
	private JTextArea viewStatus = new JTextArea();
	private JScrollPane scroller = new JScrollPane(viewStatus, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	private JScrollPane scroller2 = new JScrollPane(viewDisplay, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	MyClient mc = null;

	public ClientGUI()
	{
		setSize(1000,700);
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    attach(ipLabel, 10,10, 40,30);
	    attach(ipInput, 80, 10, 200,30);
	    attach(portLabel, 350,10, 100,30);
	    attach(portInput, 450,10, 200,30);
	    attach(connectButton, 760,10, 200,30);
	    connectButton.addActionListener(new connectButtonListener());
	    attach(statusLabel, 90,50, 80,30);
	    attach(scroller, 80, 90, 200, 450);
	    attach(listLabel, 450,50, 40,30);
	    attach(scroller2, 450, 90, 200, 450);
	    attach(sendInput, 80, 570, 580, 30);
	    attach(sendButton, 760,570, 200,30);
	    sendButton.addActionListener(new sendButtonListener());
	}
	
	public void attach(Component a, int b, int c, int d, int e )
	{
		a.setBounds(b,c,d,e);
		add(a);
	}
	
	private class connectButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
		
			String IP = ipInput.getText();
			String Port = portInput.getText();
			
			try {
				mc = new MyClient(IP, Port, viewStatus, viewDisplay); //Passes JTextArea and JList
				viewStatus.append("Connection made \r\n");
				mc.start();
				mc.sendData("Hello");
				
				
				
			} catch (UnknownHostException e1) {
				viewStatus.append("Your host sucks \r\n");
			} catch (IOException e1) {
				viewStatus.append("Wrong input \r\n");
			} catch (NumberFormatException e1)
			{
				viewStatus.append("Put in numbers man \r\n");
			} catch (IllegalArgumentException e1)
			{
				viewStatus.append("Your input out of range");
			}
			
			
			
			
			
		}
	}
	
	private class sendButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			
				String userinput = sendInput.getText(); //We don't need to check for '@' anymore.
				mc.sendData(userinput);
			
		}
	}
	
	
}
