import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MyServer implements ActionListener
{
	private ArrayList<ClientHandler> clientList;
	private ServerGUI gui;
	private HashSet<String> usedNames;
	// Be aware, a HashSet isn't synchronized in this implementation. For this
	// instance I expect it won't cause problems because of what it's being used for
	// and how it's being accessed, but check here early if problems start to arise.
	
	public MyServer()
	{
		clientList = new ArrayList<>();
		gui = new ServerGUI(this);
		gui.setVisible(true);
		usedNames = new HashSet<String>();
	}
	
	public void addClient(ClientHandler client)
	{
		clientList.add(client);
		usedNames.add(client.toString());
		addLog(client.toString() + " joined.");
		updateClients();
	}
	
	public void removeClient(ClientHandler client)
	{
		clientList.remove(client);
		usedNames.remove(client.toString());
		addLog(client.toString() + " left.");
		updateClients();
	}
	
	/**
	 * Will update which names are currently being used.
	 * Returns a value code depending on the success of the change:
	 * 		-1: Could not find the old name
	 * 		 0: The desired name is already in use
	 * 		 1: The name was changed successfully
	 * @param old The old username
	 * @param updated The desired username
	 * @return An int indicating the result of the operation
	 */
	public int changeNick(String old, String updated)
	{
		if (usedNames.contains(updated))
			return 0;
		else
			if (usedNames.remove(old))
			{
				usedNames.add(updated);
				return 1;
			}
			else
				return -1;
	}
	
	public String getAvailableNick()
	{
		int i = 0;
		while (usedNames.contains("Client #" + (++i)));
		return ("Client #" + i);
	}
	
	public void messageReceived(String message, ClientHandler receiver)
	{
		if (message.charAt(0) == '#')
			try
			{
				int num = Integer.parseInt(message.substring(1));
				Integer[] newList = gui.addListItem(num);
				for (ClientHandler client : clientList)
					client.sendList(newList);
			}
			catch (NumberFormatException ex)
			{
				// The message doesn't contain a number after all, just send it to clients
			}
		
		message = timestamp(receiver.toString() + ": " + message);
		gui.addLog(message);
		
		for (ClientHandler client : clientList)
		{
			client.sendMessage(message);
		}
	}
	
	public void privateMessage(ClientHandler sender, String target, String message)
	{
		for (ClientHandler client : clientList)
			if (client.toString().equals(target))
			{
				client.sendMessage(
						timestamp("Whisper from " + sender.toString() + ": " + message));
				sender.sendMessage(
						timestamp("Whisper to " + target + ": " + message));
				return;
			}
		sender.sendMessage("Could not find user with name \"" + target + "\"");
	}
	
	public void addLog(String log)
	{
		gui.addLog(timestamp(log));
	}
	
	public void updateClients()
	{
		gui.setClients((String[])usedNames.toArray());
		for (ClientHandler client : clientList)
			client.sendClientList((String[])usedNames.toArray());
	}
	
	// ==================== PRIVATE METHODS ====================
	
	private String timestamp()
	{
		return new SimpleDateFormat("<yyyy/MM/dd HH:mm:ss> ").format(new Date());
	}
	
	private String timestamp(String str)
	{
		return timestamp() + str;
	}
	
	// =========================================================
	
	public static void main(String[] args)
	{
		MyServer server = new MyServer();
		
		ServerSocket serverSock = null;
		try
		{
			final int PORT = 8000;
			server.addLog("Waiting for a connection on port " + PORT);
			serverSock = new ServerSocket(PORT);
			
			Socket connectionSock;
			ClientHandler ch;
			while(true)
			{
				server.addLog("Waiting for a client");
				connectionSock = serverSock.accept();
				server.addLog("Connection accepted.");
				ch = new ClientHandler(connectionSock, server, server.getAvailableNick());
				Thread t = new Thread(ch);
				t.start();
				server.addClient(ch);
			}
		}
		catch (IOException e)
		{
			server.addLog("Server crashed");
			System.out.println(e.getMessage());
		}
		finally
		{
			server.addLog("Bye");
			if (serverSock != null)
				try
				{
					serverSock.close();
				}
				catch (IOException e)
				{
					server.addLog("Server crashed");
					e.printStackTrace();
				}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		for (ClientHandler client : clientList)
			client.sendMessage(e.getActionCommand());
		gui.addLog(timestamp(e.getActionCommand()));
		gui.clearMessageEntry();
	}
}
