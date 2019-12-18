package keepalive;

import oracle.ide.Context;
import oracle.ide.controller.Controller;
import oracle.ide.controller.IdeAction;

public class KeepaliveController implements Controller {

    private static boolean started = false;
    private Thread keeper = null;

    @Override
    public boolean handleEvent(IdeAction ideAction, Context context) {
        if (!started) {
            PollIntervalDialog dlg = new PollIntervalDialog(null);
            dlg.setVisible(true);
            
            Integer pollInterval = dlg.getPollInterval();
            if(pollInterval != null) {
                started = true;
                ConnectionPinger conn = new ConnectionPinger(pollInterval);
                keeper = new Thread(conn);
                keeper.start();
            }
        } else {
            started = false;
            keeper.interrupt();
        }
        return true;
    }

    @Override
    public boolean update(IdeAction ideAction, Context context) {
        return true;
    }
}