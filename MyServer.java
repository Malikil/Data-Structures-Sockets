import java.util.ArrayList;
import java.util.Date;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.io.IOException;

public class MyServer
{
	private ArrayList<ClientHandler> clientList;
	private ServerGUI gui;
	
	public MyServer()
	{
		clientList = new ArrayList<>();
		gui = new ServerGUI();
		gui.setVisible(true);
	}
	
	public void addClient(ClientHandler client)
	{
		clientList.add(client);
		addLog(client.toString() + " joined.");
		gui.setClients(generateClientArray());
	}
	
	public void removeClient(ClientHandler client)
	{
		clientList.remove(client);
		addLog(client.toString() + " left.");
		gui.setClients(generateClientArray());
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
	
	public void addLog(String log)
	{
		gui.addLog(timestamp(log));
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
	
	private String[] generateClientArray()
	{
		String[] temp = new String[clientList.size()];
		for (int i = 0; i < clientList.size(); i++)
		{
			temp[i] = clientList.get(i).toString();
		}
		return temp;
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
			int id = 0;
			while(true)
			{
				server.addLog("Waiting for a client");
				connectionSock = serverSock.accept();
				server.addLog("Server welcomes client # : " + (++id));
				ch = new ClientHandler(connectionSock, id, server);
				server.addClient(ch);
				Thread t = new Thread(ch);
				t.start();
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
}
