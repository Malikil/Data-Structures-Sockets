import java.util.ArrayList;
import java.util.Date;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class MyServer
{
	public static void main(String[] args) 
	{
		ServerSocket serverSock = null;
		try
		{
			final int PORT = 8000;
			System.out.println("Waiting for a connection on port " + PORT);
			serverSock = new ServerSocket(PORT);
			Socket connectionSock;
			
			ArrayList<Thread> clientList = new ArrayList<>();
			ClientHandler ch;
			int id = 0;
			while(true)
			{
				System.out.println("Waiting for a client");
				connectionSock = serverSock.accept();
				System.out.println("Server welcomes client # : " + (++id));
				ch = new ClientHandler(connectionSock,id);
				clientList.add(new Thread(ch));
				clientList.get(clientList.size() - 1).start();
			}
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
		finally
		{
			System.out.println("Bye");
			if (serverSock != null)
				try
				{
					serverSock.close() ;
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
}
