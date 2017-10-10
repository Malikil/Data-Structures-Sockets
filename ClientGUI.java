import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class ClientGUI extends JFrame
{
	public static void main(String[] args)
	{
		ClientGUI gui = new ClientGUI();
		gui.setVisible(true);
	}

	private JLabel ipLabel = new JLabel("IP");
	private JLabel portLabel = new JLabel("PORT");
	private JLabel statusLabel = new JLabel("CHAT LOG");
	private JLabel listLabel = new JLabel("LIST");
	private JLabel usersLabel = new JLabel("ONLINE NOW");
	private JButton connectButton = new JButton("Connect");
	private JButton sendButton = new JButton("Send");
	private JTextField ipInput = new JTextField();
	private JTextField portInput = new JTextField("8000");
	private JTextField sendInput = new JTextField();
	private JList<String> viewDisplay = new JList<String>();
	private JTextArea viewStatus = new JTextArea();
	private JScrollPane scroller = new JScrollPane(viewStatus, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	private JScrollPane scroller2 = new JScrollPane(viewDisplay, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	private JList<String> clientList = new JList<String>();
	private JScrollPane clientPane = new JScrollPane(clientList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	MyClient mc = null;
	ClientGUI self = this;
	DefaultCaret caret = (DefaultCaret)viewStatus.getCaret();
	boolean connected = false;

	public ClientGUI()
	{
		setSize(1000,700);
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    viewStatus.setEditable(false);
	    caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
	    attach(ipLabel, 10,10, 40,30);
	    attach(ipInput, 80, 10, 200,30);
	    attach(portLabel, 350,10, 100,30);
	    attach(portInput, 450,10, 200,30);
	    attach(connectButton, 760,10, 200,30);
	    connectButton.addActionListener(new connectButtonListener(this));
	    attach(statusLabel, 90,50, 80,30);
	    attach(scroller, 80, 90, 570, 450);
	    attach(listLabel, 760,280, 40,30);
	    attach(usersLabel, 760,50, 90,30);
	    attach(scroller2, 760, 320, 200, 220);
	    attach(sendInput, 80, 570, 570, 30);
	    attach(sendButton, 760,570, 200,30);
	    attach(clientPane, 760, 90, 200, 180);
	    sendButton.addActionListener(new sendButtonListener());
	    sendInput.setEditable(false);
	    self.getRootPane().setDefaultButton(connectButton);
	}
	
	public void setNumberList(String[] list)
	{
		viewDisplay.setListData(list);
	}
	
	public void setClientList(String[] list)
	{
		clientList.setListData(list);
	}
	
	public void displayMessage(String msg)
	{
		viewStatus.append(msg + "\n");
	}
	
	public void attach(Component a, int b, int c, int d, int e )
	{
		a.setBounds(b,c,d,e);
		add(a);
	}
	
	private class connectButtonListener implements ActionListener
	{
		private ClientGUI ownerGUI;
		connectButtonListener(ClientGUI gui)
		{
			ownerGUI = gui;
		}
		
		public void actionPerformed(ActionEvent e)
		{
		
			if(!connected)
			{
				String IP = ipInput.getText();
				String Port = portInput.getText();
			
				try
				{
					mc = new MyClient(IP, Port, ownerGUI);
					viewStatus.append("Connection made\n");
					mc.start();
					mc.sendData("Hello");
					sendInput.setEditable(true);
					ipInput.setEditable(false);
					portInput.setEditable(false);
					self.getRootPane().setDefaultButton(sendButton);
					connectButton.setText("Disconnect");
					connected = true;
				}
				catch (UnknownHostException e1)
				{
					viewStatus.append("Your host sucks \r\n");
				}
				catch (IOException e1)
				{
					viewStatus.append("Wrong input or host still asleep \r\n");
				}
				catch (NumberFormatException e1)
				{
					viewStatus.append("Put in numbers man \r\n");
				}
				catch (IllegalArgumentException e1)
				{
					viewStatus.append("Your input out of range");
				}
			}
			else
			{
				try {
					mc.close();
					viewStatus.setText("Disconnected from server");
					sendInput.setEditable(false);
					connectButton.setText("Connect");
					self.getRootPane().setDefaultButton(connectButton);
					ipInput.setEditable(true);
					portInput.setEditable(true);
					connected = false;
					clientList.setListData(new String[0]);
					viewDisplay.setListData(new String[0]);
				}
				catch (IOException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	
	private class sendButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String userInput = sendInput.getText();
			if (!userInput.equals(""))
			{
				if (userInput.startsWith("/nick") && userInput.contains(","))
					viewStatus.append("You cannot use commas in your nickname\n");
				else
				{
					mc.sendData(userInput);
					sendInput.setText("");
					caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
					viewStatus.setCaretPosition(DefaultCaret.OUT_BOTTOM);
				}
			}
		}
	}
}
