public class Beacon {
    int ID;
    long startUpTime;
    long timeInterval;
    String IP;
    int cmdPort;
    long arrivalTime;

    public Beacon(int ID, long StartUpTime, long timeInterval, String IP, int CmdPort) {
        this.ID = ID;
        this.startUpTime = StartUpTime;
        this.timeInterval = timeInterval;
        this.IP = IP;
        this.cmdPort = CmdPort;
        this.arrivalTime = 0;
    }

    public void set_arrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
