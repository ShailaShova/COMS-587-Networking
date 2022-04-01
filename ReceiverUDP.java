import java.net.*;
import java.nio.file.attribute.AclFileAttributeView;
import java.sql.Date;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.time.*;

public class ReceiverUDP extends Thread {
    
    List<Beacon> beacons;
    public ReceiverUDP(List<Beacon> beacons) {
        this.beacons = beacons;
    }

    @Override
    public void run() {
        DatagramSocket sock = null;

        try {
            // 1. creating a server socket, parameter is local port number
            sock = new DatagramSocket(7777);

            // buffer to receive incoming data
            byte[] buffer = new byte[65536];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);

            // 2. Wait for an incoming data
            System.out.println("Server socket created. Waiting for incoming data...");

            // communication loop

            while (true) {
                sock.receive(incoming);
                byte[] data = new byte[incoming.getLength()];

                data = incoming.getData();
                String s = new String(data, 0, incoming.getLength());
                // System.out.println(s);
                Beacon bc = beaconReceiver(s);
                Instant instant = Instant.now();
                long currentTime = instant.getEpochSecond();
                bc.arrivalTime = (int)currentTime;

                //List<Beacon> activeAgents = new ArrayList<>();
                //AgentMonitor agentMonitor = new AgentMonitor(activeAgents);
                //agentMonitor.start();

                if(this.isNewBeacon(bc)) {
                    this.beacons.add(bc);

                    System.out.println("New Agent Joined with ID -> " + bc.ID);
                    
                    ClientTCP clientAgent = new ClientTCP();
                    clientAgent.start();

                }
            
                else {
                    System.out.println("Beacon Received -> " + bc.ID);
                }
               
                // System.out.println("Host Adress " + incoming.getAddress().getHostAddress() + " : " + "Port Number "
                //         + incoming.getPort() + " The beacon information " + " --" + bc.ID + "--" + bc.startUpTime + "--"
                //         + bc.IP + "--" + bc.cmdPort);

            }

        }

        catch (IOException e) {
            System.err.println("IOException " + e);
        }

    }

    private boolean isNewBeacon(Beacon bc) {
        for(Beacon a: this.beacons) {
            System.out.println(a.ID + " "+   bc.ID);
            if (a.ID == bc.ID)return false;
        }
        return true;
    }

    public Beacon beaconReceiver(String s) {
        int ID=0, cmdPort = 0;
        long  startUpTime = 0, timeInterval = 0;
        //int CurrentTime = 0;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            ID = (ID * 10 + (s.charAt(i) - '0'));
        }
        for (int i = 10; i < 20; i++) {
            startUpTime = (startUpTime * 10 + (s.charAt(i) - '0'));
        }
        for (int i = 20; i < 30; i++) {
            timeInterval = (timeInterval * 10 + (s.charAt(i) - '0'));
        }
        boolean fl = false;
        for (int i = 30; i < 45; i++) {
            if (s.charAt(i) != '0')
                fl = true;
            if (fl)
                sb.append(s.charAt(i));
        }
        String IP = sb.toString();
        for (int i = 45; i < 55; i++) {
            cmdPort = cmdPort * 10 + s.charAt(i);
        }
 
        return new Beacon(ID, startUpTime, timeInterval, IP, cmdPort);
    }

}
