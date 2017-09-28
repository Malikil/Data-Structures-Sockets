import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ServerLog
{
	ArrayList<String> logs;
	public ServerLog()
	{
		logs = new ArrayList<>();
	}
	
	public ServerLog(String log)
	{
		logs = new ArrayList<>();
		logs.add(log);
	}
	
	public void addLog(String log)
	{
		DateFormat formatter = new SimpleDateFormat("<yyyy/MM/dd HH:mm:ss> ");
		logs.add(formatter.format(new Date()) + log);
	}
	
	public void containsLog(String log)
	{
		containsLog(log, false);
	}
	
	public boolean containsLog(String log, boolean stripTime)
	{
		if (!stripTime)
		{
			return logs.contains(log); 
		}
		else
		{
			for (String l : logs)
			{
				String[] temp = l.split("> ");
				if (log.equals(temp[temp.length - 1]))
					return true;
			}
			
			return false;
		}
	}
	
	public void clearLogs()
	{
		logs.clear();
	}
	
	public String[] getLogs()
	{
		return (String[])logs.toArray();
	}
}
