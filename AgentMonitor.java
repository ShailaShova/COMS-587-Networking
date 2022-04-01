import java.net.*;
import java.nio.file.attribute.AclFileAttributeView;
import java.sql.Date;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.time.*;

public class AgentMonitor extends Thread {
    
    List<Beacon> activeAgents;
    public AgentMonitor(List<Beacon> activeAgents) {
        this.activeAgents = activeAgents;
    }

    @Override
    public void run() {
        while(true) {
            Instant instant = Instant.now();
            long currentTime = instant.getEpochSecond();
            
            List<Beacon>deadAgents = new ArrayList<>();;
            for(Beacon bc: this.activeAgents) {
                if(currentTime > bc.arrivalTime + 2 * bc.timeInterval) {
                    deadAgents.add(bc);
                }
            }
            for(Beacon bc: deadAgents) {
                this.activeAgents.remove(bc);
                System.out.println("The agent with ID " + bc.ID + " is Died.");
            }
        }
    }
}
