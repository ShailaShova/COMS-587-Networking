import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class manager {
        public static void main(String[] args) throws Exception {
                List<Beacon> activeAgents = new ArrayList<>();

                ReceiverUDP receiverUDP = new ReceiverUDP(activeAgents);
                AgentMonitor agentMonitor = new AgentMonitor(activeAgents);
                //ClientTCP clientAgent = new ClientTCP();

                receiverUDP.start();
                agentMonitor.start();
                //clientAgent.start();
                


                
        }
}
