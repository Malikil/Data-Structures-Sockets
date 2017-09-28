import java.awt.*;
import javax.swing.*;

public class ServerGUI extends JFrame
{
	private Container con;
	
	private JPanel top, mid, bot;
	
	private JLabel ipLabel, portLabel;
	private JTextField ipField, portField;
	
	private JLabel serverLogLabel, sortedListLabel, clientListLabel;
	private JTextArea serverLogArea;
	private JList<String> sortedList, clientList;
	private JScrollPane logPane, listPane, clientPane;
	
	/*public static void main(String[] arg)
	{
		ServerGUI serv = new ServerGUI();
		serv.setVisible(true);
	}*/ // Debug main for displaying form
	
	ServerGUI()
	{
		setSize(1000, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		con = getContentPane();
		con.setLayout(new GridLayout(3, 1, 5, 5));
		
		// ========== TOP PANEL ==========
		top = new JPanel();
		top.setLayout(new GridLayout(1, 4, 5, 5));
		con.add(top);
		
		ipLabel = new JLabel("IP : ");
		top.add(ipLabel);
		
		ipField = new JTextField();
		ipField.setEditable(false);
		top.add(ipField);
		
		portLabel = new JLabel("Port : ");
		top.add(portLabel);
		
		portField = new JTextField();
		portField.setEditable(false);
		top.add(portField);
		
		// ========== MID PANEL ==========
		mid = new JPanel();
		mid.setLayout(new GridLayout(1, 3, 5, 5));
		con.add(mid);
		
		serverLogLabel = new JLabel("Server log : ");
		mid.add(serverLogLabel);
		
		sortedListLabel = new JLabel("Sorted list : ");
		mid.add(sortedListLabel);
		
		clientListLabel = new JLabel("Client List : ");
		mid.add(clientListLabel);
		
		// ========== BOT PANEL ===========
		bot = new JPanel();
		bot.setLayout(new GridLayout(1, 3, 5, 5));
		con.add(bot);
		
		serverLogArea = new JTextArea();
		logPane = new JScrollPane(serverLogArea);
		bot.add(logPane);
		
		sortedList = new JList<>();
		listPane = new JScrollPane(sortedList);
		bot.add(listPane);
		
		clientList = new JList<>();
		clientPane = new JScrollPane(clientList);
		bot.add(clientPane);
	}
	
	public void setIP(String ip)
	{
		ipLabel.setText(ip);
	}
	
	public void setPort(String port)
	{
		portLabel.setText(port);
	}
	
	public void setSortedList(String[] list)
	{
		sortedList.setListData(list);
	}
	
	public void setClientList(String[] clients)
	{
		clientList.setListData(clients);
	}
	
	public void addLog(String log)
	{
		serverLogArea.append(log + "\n");
	}
}
