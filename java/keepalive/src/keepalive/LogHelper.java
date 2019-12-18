package keepalive;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import oracle.ide.log.LogManager;

class LogHelper {

	// TODO: Check if log messages can be places among SQLDev logging
	static final void LogMessage(String level, String msg) {
        final String LOG_PREFIX = "> KEEPALIVE";
		
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        String currentTime = " [" + dateFormat.format(date) + "] ";
        
        LogManager.getLogManager().getMsgPage().log(LOG_PREFIX + currentTime + level + ": " + msg + "\n");
    }
}
