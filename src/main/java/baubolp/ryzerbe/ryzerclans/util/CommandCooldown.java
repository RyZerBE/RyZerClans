package baubolp.ryzerbe.ryzerclans.util;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class CommandCooldown {

    private long millis;
    private Timer timer;
    public CommandCooldown(long millis){
        this.millis = millis;
        this.timer = new Timer();
    }

    private HashMap<String, Long> map = new HashMap<>();

    public boolean hasCooldown(String userid){
        if (!map.containsKey(userid)) return false;

        return map.get(userid) + millis > System.currentTimeMillis();
    }

    public void add(String userid){
        if (!map.containsKey(userid))
            map.put(userid, System.currentTimeMillis());

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                map.remove(userid);
            }
        },millis);
    }
}
