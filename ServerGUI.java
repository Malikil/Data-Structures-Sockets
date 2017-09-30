import java.awt.*;
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
	private JList sortedList, clientList;
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
		
		portField = new JTextField();
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
		
		sortedList = new JList();
		listPane = new JScrollPane(sortedList);
		attach(listPane,525,75,100,500);
		
		clientList = new JList();
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
	
	public static void main(String[] arg)
	{
		ServerGUI serv = new ServerGUI();
		serv.setVisible(true);
	}
	
	public void setIP(String ip)
	{
		
	}
}