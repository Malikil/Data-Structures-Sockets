import java.util.ArrayList;
import java.util.Date;
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
	
	public MyServer()
	{
		clientList = new ArrayList<>();
		gui = new ServerGUI(this);
		gui.setVisible(true);
	}
	
	public void addClient(ClientHandler client)
	{
		clientList.add(client);
		addLog(client.toString() + " joined.");
		updateClients();
	}
	
	public void removeClient(ClientHandler client)
	{
		clientList.remove(client);
		addLog(client.toString() + " left.");
		updateClients();
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
		String[] temp = new String[clientList.size()];
		for (int i = 0; i < clientList.size(); i++)
		{
			temp[i] = clientList.get(i).toString();
		}
		gui.setClients(temp);
		for (ClientHandler client : clientList)
			client.sendClientList(temp);
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
			short id = 1;
			while(true)
			{
				server.addLog("Waiting for a client");
				connectionSock = serverSock.accept();
				server.addLog("Server welcomes client #" + id);
				ch = new ClientHandler(connectionSock, id, server);
				Thread t = new Thread(ch);
				t.start();
				server.addClient(ch);
				if (++id < 1)
					id = 1;
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
					server.addLog("Server craashed");
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
