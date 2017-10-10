import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable
{
	private MyServer parentServer;
	private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String nick = null;

    public ClientHandler(Socket socket, MyServer server, String initialNick)
    {
        this.socket = socket;
        parentServer = server;
        nick = initialNick;
    }
    
    public void sendMessage(String message)
    {
    	out.println(message);
    }
    
    public void sendList(Integer[] list)
    {
    	String message = "#";
    	for (Integer n : list)
    		message += ("," + n.intValue());
    	out.println(message);
    }
    
    public void sendClientList(String[] clientNames)
    {
    	String message = "~";
    	for (String name : clientNames)
    		message += "," + name;
    	while (out == null && !socket.isClosed());
    	out.println(message);
    }

	@Override
	public void run()
	{
		parentServer.addLog("Building connection with " + nick + " at " + socket);
		try
		{
			InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            in = new BufferedReader(isr);
           
            // (outputstream, autoflush)
            out = new PrintWriter(socket.getOutputStream(), true);

            // Send a welcome message to the client.
            out.println("Welcome " + nick + ". You can use /nick to change your nickname.");

            String msg;
            // waiting for client to send message
            while (true)
            {
                msg = in.readLine();
                if (msg == null)
                    break;
                else if (msg.startsWith("/nick"))
                {
                	String oldNick = nick;
                	if (msg.equals("/nick"))
                	{
                		nick = parentServer.getAvailableNick();
                		parentServer.changeNick(oldNick, nick);
                		parentServer.updateClients();
                	}
                	else if (msg.length() < 21)
                	{
                		nick = msg.substring(6);
                		if (nick.equals(""))
                			nick = parentServer.getAvailableNick();
                		if (parentServer.changeNick(oldNick, nick) == 0)
                		{
                			nick = oldNick;
                			out.println("Nickname is already in use");
                		}
                		else // Not currently checking for if the old name could not be found.
                			parentServer.updateClients();
                	}
                	else
                		out.println("Nickname must be less than 15 characters");
                }
                else if (msg.startsWith("/msg "))
                {
                	// Get the username to send the message to
                	String message = msg.substring(5);
                	String targetUser;
                	if (message.startsWith("\""))
                	{
                		// Find the second quote
                		int split = message.substring(1).indexOf("\" ");
                		targetUser = message.substring(1, split);
                		message = message.substring(split + 1);
                	}
                	else
                	{
                		int split = message.indexOf(" ");
                		targetUser = message.substring(0, split);
                		message = message.substring(split + 1);
                	}
                	parentServer.privateMessage(this, targetUser, message);
                }
                else
                	parentServer.messageReceived(msg, this);
            }
        }
		catch (IOException e)
		{
            System.out.println("Error " + nick + ": " + e);
        }
		finally
		{
            try
            {
                socket.close();
                parentServer.removeClient(this);
            }
            catch (IOException e)
            {
                System.out.println(nick + " ... Couldn't close a socket");
            }
            // System.out.println("client #" + id + " left");
        }
	}
	
	@Override
	public String toString()
	{
		return nick;
	}
}
