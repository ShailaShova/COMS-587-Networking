#include <iostream>
#include <thread>
#include <sys/socket.h>
#include <netinet/in.h>
#include <strings.h>
#include <sys/types.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <stdlib.h>
#include<time.h>

using namespace std;

#define MAXLINE 1000

struct Beacon
{
    
    int ID;          // randomly generated during startup
    int StartUpTime; // the time when the client starts
    int TimeInterval;
    char IP[15];     // the IP address of this client
    int CmdPort;
    Beacon()
    {
        StartUpTime = time(0);
        TimeInterval = 60;
    }
};

class BeaconSender
{
public:
    void beaconSender()
    {
        char buffer[1024];
        string messages;
        int sockfd, n;

        struct sockaddr_in serverInfo;
        serverInfo.sin_addr.s_addr = inet_addr("127.0.0.1");
        serverInfo.sin_port = htons(7777);
        serverInfo.sin_family = PF_INET;
        
        Beacon bc;
     
        srand(time(0)
        );
        bc.ID=rand();
        strcpy(bc.IP, "127.0.0.1");
        bc.CmdPort = 80;

        unsigned char *x = (unsigned char *)malloc(sizeof(bc));
        memcpy(x, (const unsigned char *)&bc, sizeof(bc));

        Beacon copyBeacon;
        memcpy(&copyBeacon, x, sizeof(copyBeacon));
        
        printf("Beacon ID and startUpTime: %d %d \n", copyBeacon.ID, copyBeacon.StartUpTime);

        // Create datagram socket
        sockfd = socket(PF_INET, SOCK_DGRAM, 0);
        if (connect(sockfd, (struct sockaddr *)&serverInfo, sizeof(serverInfo)) < 0)
        {
            printf("\n Error : Connect Failed \n");
            exit(0);
        }
        
        string message = convertBeaconToString(bc);
        
        //time_t SLEEP_TIME=60;
        
        while (true)
        {
            time_t SLEEP_TIME=bc.TimeInterval;
            printf("%d\n", bc.ID);

            if (sendto(sockfd, message.c_str(), (int)message.size(), 0, (struct sockaddr *)NULL, sizeof(serverInfo)) < 0)
            {
                cout << "Can not send" << endl;
            }
             sleep(SLEEP_TIME);
            
        }
        close(sockfd);
       
    }
    string convertNumToInt(int num)
    {
        string s;
        if (num == 0)
            s = "";
        else
        {
            while (num)
            {
                s += ('0' + num % 10);
                num /= 10;
            }
        }
        int l = 10 - (int)s.size();
        for (int i = 0; i < l; i++)
            s += '0';

       reverse(s.begin(), s.end());

        return s;
    }

    string convertBeaconToString(Beacon bc)
    {
        string s;
        int l = strlen(bc.IP);
        for (int i = l - 1; i >= 0; i--)
            s += bc.IP[i];
        for (int i = 0; i < 15 - l; i++)
            s += '0';
        reverse(s.begin(), s.end());
        return convertNumToInt(bc.ID) +
         convertNumToInt(bc.StartUpTime) + 
         convertNumToInt(bc.TimeInterval) +
         s + convertNumToInt(bc.CmdPort);
    }
};
