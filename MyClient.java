import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;

public class MyClient extends Thread
{
	private BufferedReader serverInput = null ;
	private PrintWriter pw = null;
	private Socket connectionSock = null;
	private int id;
	public boolean ok_connect = true;
	
	public MyClient(String host, String vport) throws UnknownHostException, IOException {
		String hostname = host;
		int port = Integer.parseInt(vport);

		System.out.println("Connecting to server on port " + port);
		connectionSock = new Socket(hostname, port);
		InputStreamReader isr = new InputStreamReader(connectionSock.getInputStream());
		serverInput = new BufferedReader(isr);
		pw = new PrintWriter(connectionSock.getOutputStream(),true);
	}
	
	public static void main(String[] args) 
	{
		MyClient mc = null; 
		
		Scanner input = new Scanner(System.in);
		try
		{
			
			mc = new MyClient(args[0], args[1]);
			
			System.out.println("Connection made");
			mc.start();
			
			
			mc.sendData("Hello"); // sending to the server
			String userinput;
			while(mc.ok_connect) {	
				System.out.print("Your message: ");
				userinput = input.nextLine();
				if (userinput.equals("@"))
					break;
				mc.sendData(userinput);
			}
			
		}
		catch (IOException e)
		{
			System.out.println("error: " + e.getMessage());
		} finally{
			System.out.println("Client close");
			input.close();
			try {
				mc.close();
			} catch (Exception ex) {
				System.out.println(ex);
			}
			
		}
	}
	
	public void close() throws IOException {
		pw.close();
		serverInput.close();
		connectionSock.close();
	}
	public void sendData(String msg) {
		pw.println(msg); 
	}
	
	
	public void run() {
		String serverMsg;
		
		
		
		// used to listen message from server
		try {
			serverMsg = serverInput.readLine(); // assume that the id was the first message sent
			id = Integer.parseInt(serverMsg.split("#")[1].trim()); 
			while(true) {
				serverMsg = serverInput.readLine(); 
				if (serverMsg == null)
					break;
				System.out.println("Client# " + id + ", Received: " + serverMsg);
			}
		} catch (Exception ex) {
			ok_connect = false;
			System.out.println(ex);
			System.out.println("Disconnected from server");
		}
	}
}
