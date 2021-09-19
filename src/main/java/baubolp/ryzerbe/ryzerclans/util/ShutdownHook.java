package baubolp.ryzerbe.ryzerclans.util;

import baubolp.ryzerbe.ryzerclans.RyZerClans;

public class ShutdownHook {

    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    RyZerClans.getCloudBridge().stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
