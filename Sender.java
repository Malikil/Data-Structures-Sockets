import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Sender
{
	private ArrayList<Socket> clientList;
	
	public Sender()
	{
		clientList = new ArrayList<>();
	}
	
	public void addClient(Socket client)
	{
		clientList.add(client);
	}
	
	public ServerLog sendMessage(String message)
	{
		ServerLog log = new ServerLog();
		for (Socket client : clientList)
		{
			try
			{
				new PrintWriter(client.getOutputStream(), true).println(message);
			}
			catch (IOException ex)
			{
				log.addLog("Failed to send to client.");
			}
		}
		log.addLog("Messages Sent.");
		return log;
	}
}