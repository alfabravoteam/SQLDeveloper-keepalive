package keepalive;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import oracle.dbtools.raptor.utils.Connections;

public class ConnectionPinger implements Runnable {

	private static final String QUERY_FIELD = "SYSDATE";
	private static final String QUERY_STRING = "SELECT " + QUERY_FIELD + " FROM DUAL";
	
    private boolean execute;
    private int interval;

    public ConnectionPinger(Integer interval) {
        try {
            this.interval = interval;
            if (this.interval < 60) {
                LogHelper.LogMessage("WARNING", "Timeout is too low (less than 60 seconds).");
                LogHelper.LogMessage("INFO", "Timeout set to default (600 seconds).");
                this.interval = 600;
            } else {
            	LogHelper.LogMessage("INFO", "Timeout set to " + this.interval + " seconds.");
            }
        } catch (Exception e) {
        	LogHelper.LogMessage("WARNING", "Error while setting timeout.");
        	LogHelper.LogMessage("INFO", "Timeout set to default (600 seconds).");
            this.interval = 600;
        }
    }

    @Override
    public void run() {
        this.execute = true;
        ResultSet rs = null;
        
    	LogHelper.LogMessage("INFO", "Process started.");
    	
        while (this.execute) {
            try {
                String[] connectionNames = Connections.getInstance().getConnNames();
                LogHelper.LogMessage("INFO", "Event triggered, scanning " + connectionNames.length + " saved connections...");
                
                for (int i = 0; i < connectionNames.length; i++) {
                    String connectionName = connectionNames[i];
                    String displayConnectionName = connectionName;
                    // TODO: Check why was this needed - checks for #
                    if (displayConnectionName.contains("%23")) {
                        displayConnectionName = connectionName.substring(connectionName.indexOf("%23") + 3);
                    }
                    
                    if (Connections.getInstance().isConnectionOpen(connectionName)) {
                    	
                    	try ( Statement sqlStatement = Connections.getInstance().getConnection(connectionName).createStatement(); ) {
                    		rs = sqlStatement.executeQuery(QUERY_STRING);
                    		while (rs.next()) {
                    			LogHelper.LogMessage("DEBUG", "The validation query returned: " + rs.getString(QUERY_FIELD));
                    		}
                    	} catch (Exception ex) {
                    		LogHelper.LogMessage("ERROR", "An unexpected error: " + ex.getMessage());
                    	}

                    	LogHelper.LogMessage("INFO", displayConnectionName + " successfully kept alive!");
                    } else {
                    	// NO ACTION (the connection is not open)
                    }
                }
                
                LogHelper.LogMessage("INFO", "Event finished, next event in " + this.interval + " seconds.");
                Thread.sleep(this.interval * 1000);
            } catch (InterruptedException e) {
                this.execute = false;
                LogHelper.LogMessage("INFO", "keepalive stopped.");
                
            } catch (Exception e) {
            	LogHelper.LogMessage("ERROR", e.getMessage());
                // for other errors we can continue pinging connections
                // but sleep the thread to avoid infinite loop pinging the connection thousands times a second
                try {
                    Thread.sleep(this.interval * 1000);
                } catch (InterruptedException f) {
                    this.execute = false;
                    LogHelper.LogMessage("INFO", "Process stopped.");
                }
            }
            
        }
    }
}
