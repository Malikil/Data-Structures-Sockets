import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable
{
	private MyServer parentServer;
	private Socket socket;
    private int id;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket, int id, MyServer server)
    {
        this.socket = socket;
        this.id = id;
        parentServer = server;
    }
    
    public void sendMessage(String message)
    {
    	out.println(message);
    }
    
    public void sendList(int[] list)
    {
    	String message = "#";
    	for (int n : list)
    		message += ("," + n);
    	out.print(message);
    }

	@Override
	public void run()
	{
		System.out.println("Building connection with client# " + id + " at " + socket);
		try
		{
			InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            in = new BufferedReader(isr);
           
            // (outputstream, autoflush)
            out = new PrintWriter(socket.getOutputStream(), true);

            // Send a welcome message to the client.
            out.println("Welcome Client # " + id);
            out.println("Enter @ to quit");
            

            String msg;
            // waiting for client to send message
            while (true)
            {
                msg = in.readLine();
                if (msg == null || msg.equals("@"))
                    break;
                
                parentServer.messageReceived(msg, this);
                // System.out.println("Message from client #" + id + ", [" + msg + "]");
            }
        }
		catch (IOException e)
		{
            System.out.println("Error client# " + id + ": " + e);
        }
		finally
		{
            try
            {
                socket.close();
            }
            catch (IOException e)
            {
                System.out.println("Client # :" + id + " ... Couldn't close a socket");
            }
            System.out.println("client# " + id + " left");
        }
	}
}
