import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

public class ServerGUI extends JFrame
{
	//Top
	private JLabel infoLabel, ipLabel, portLabel;
	private JTextField ipField, portField;
	//Mid
	private JLabel serverLogLabel, sortedListLabel, clientListLabel;
	//Bot
	private JTextArea serverLogArea;
	private DefaultListModel<Integer> sortedListMod;
	private JList<Integer> sortedListDisplay;
	private JList<String> clientList;
	private JScrollPane logPane, listPane, clientPane;
	//Other
	private JLabel broadcastLabel;
	private JTextField broadcastField;
	
	public ServerGUI()
	{
		this.setLayout(null);
		this.setBounds(10,10,1000,700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// ========== TOP PANEL ==========
		
		infoLabel = new JLabel("Connection Info:");
		attach(infoLabel,10,10,600,15);
		
		ipLabel = new JLabel("IP:");
		attach(ipLabel,10,30,25,15);
		
		portLabel = new JLabel("Port:");
		attach(portLabel,300,30,50,15);
		
		ipField = new JTextField();
		attach(ipField,30,30,250,20);
		ipField.setEditable(false);
		
		portField = new JTextField("8000");
		attach(portField,333,30,100,20);
		portField.setEditable(false);
		
		// ========== MID PANEL ==========
		
		serverLogLabel = new JLabel("Server log : ");
		attach(serverLogLabel,10,50,450,20);
		
		sortedListLabel = new JLabel("Sorted list : ");
		attach(sortedListLabel,525,50,450,20);
		
		clientListLabel = new JLabel("Client List : ");
		attach(clientListLabel,800,50,450,20);
		
		// ========== BOT PANEL ===========

		serverLogArea = new JTextArea();
		logPane = new JScrollPane(serverLogArea);
		attach(logPane,10,75,500,500);
		
		sortedListMod = new DefaultListModel<>();
		sortedListDisplay = new JList<>(sortedListMod);
		listPane = new JScrollPane(sortedListDisplay);
		attach(listPane,525,75,100,500);
		
		clientList = new JList<>();
		clientPane = new JScrollPane(clientList);
		attach(clientPane,800,75,150,500);
		
		// ========== OTHER PANEL ===========
		
		broadcastLabel = new JLabel("Enter Message To Broadcast : ");
		attach(broadcastLabel,10,580,500,20);
		
		broadcastField = new JTextField();
		attach(broadcastField,10,600,500,20);
	}
	
	public void attach(Component c, int x, int y, int w, int h)
	{
		add(c);
		c.setBounds(x, y, w,h);
	}
	
	public void setIP(String ip)
	{
		ipField.setText(ip);
	}
	
	public void setPort(String port)
	{
		portField.setText(port);
	}
	
	public void addLog(String log)
	{
		serverLogArea.append(log + "\n");
	}
	
	public Integer[] addListItem(int num)
	{
		for (int i = 0; i < sortedListMod.size(); i++)
		{
			if (sortedListMod.getElementAt(i).intValue() > num)
			{
				sortedListMod.add(i, num);
				Integer[] temp = new Integer[sortedListMod.size()];
				for (int j = 0; j < sortedListMod.size(); j++)
					temp[j] = sortedListMod.get(j);
				return temp;
			}
		}
		// If this point is reached, the number should be added to the end of
		// the ArrayList because it's either empty or the number is bigger than
		// the last value.
		sortedListMod.addElement(num);
		Integer[] temp = new Integer[sortedListMod.size()];
		for (int j = 0; j < sortedListMod.size(); j++)
			temp[j] = sortedListMod.get(j);
		return temp;
	}
	
	public void setClients(String[] clients)
	{
		clientList.setListData(clients);
	}
}
