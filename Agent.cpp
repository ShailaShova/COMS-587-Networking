#include "ServerTCP.cpp"
#include "SenderUDP.cpp"

using namespace std;

#define MAXLINE 1000


int main()
{
    BeaconSender *bc = new BeaconSender();
    CommandAgent *ca = new CommandAgent();
    thread th1(&BeaconSender::beaconSender, bc);
    thread th2(&CommandAgent::CA, ca);
    th1.join();
    th2.join();

    return 0;
}
