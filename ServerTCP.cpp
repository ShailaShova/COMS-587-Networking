#include <iostream>
#include <thread>
#include <sys/socket.h>
#include <netinet/in.h>
#include <strings.h>
#include <sys/types.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include<time.h>

using namespace std;

class CommandAgent
{
public:
    void CA()
    {
        int server_socket = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP);
        
        // bind to a port
        struct sockaddr_in sin;
        memset(&sin, 0, sizeof(sin));
        sin.sin_len = sizeof(sin); // comment this line out if running on pyrite (linux)
        sin.sin_family = AF_INET;  // or AF_INET6 (address family)
        sin.sin_port = htons(5555);
        sin.sin_addr.s_addr = INADDR_ANY;
        if (::bind(server_socket, (struct sockaddr *)&sin, sizeof(sin)) < 0)
        {
            printf("bind error\n");
        }

        listen(server_socket, 5); /* maximum 5 connections will be queued */
        int counter = 0;
        while (1)
        {
            struct sockaddr client_addr;
            unsigned int client_len;
        
            printf("accepting client request....\n");
            int client_socket = accept(server_socket, &client_addr, &client_len);
            //printf("request %d comes ...\n", counter++);
            //printf("processing client request %d \n", client_socket);


            char packet_length_bytes[4];
            receiveFully(client_socket, packet_length_bytes, 4);
            printBinaryArray(packet_length_bytes, 4);
        
            int packet_length = toInteger32(packet_length_bytes);
            char *buffer = (char *)malloc(packet_length);
            //printf("The buffer is : %s\n",buffer);
            receiveFully(client_socket, buffer, packet_length);
            convertUpperCase(buffer, packet_length); // doing upper case here
            //printf("buffer1 = %s\n",buffer);
            
            string timeString = myTime();
            string osname = getLocalOS();
            string newline = "\n";
            const char *timeAndOs = (timeString +newline+ osname).c_str();

             if(strcmp(buffer, "GETLOCALTIME") == 0) { 

                printf("Local Time  %s",convertLengthToString(strlen(timeAndOs)).c_str());
                send(client_socket, convertLengthToString(strlen(timeAndOs)).c_str(), 2, 0);
                send(client_socket, timeAndOs, strlen(timeAndOs), 0);
                //send(client_socket, buffer, strlen(buffer), 0);
                }  
        }
    }

  
    string convertLengthToString(int l) {
        string s;
        s += (char)('0' + l / 10);
        s += (char)('0' + l % 10);
        return s;
    }
    
    string myTime()
    {
        time_t curTime;
        struct tm*time_info;
        char timeString[9];
        time(&curTime);
        time_info=localtime(&curTime);
        strftime(timeString,sizeof(timeString),"%H:%M",time_info);
        printf("time:--");
        puts(timeString);

        return timeString;
    }

    string getLocalOS() {
        string OS;
        #ifdef _WIN32
            printf("Windows \n");
            OS = "Windows";
        #elif _WIN64
            printf("Windows 64 bit\n")
            OS = "Windows 64 bit";
        #elif __APPLE__
            printf("MAC OS\n");
            OS = "MAC OS";
        #elif __linux__
            printf("Linux \n");
            OS = "Linux";
        #elif __unix__
            printf("Unix \n")
            OS = "Other Unix OS";
        #else
            printf("Undefined \n")
            OS = "UNDEFINED";
        #endif
        return OS;
    }

      void convertUpperCase(char *buffer,int length)
        {
        int i=0;
        while(i<length)
        {
            if(buffer[i]>='a'&& buffer[i]<='z')
            {
                buffer[i]=buffer[i] -'a' +'A';
            }
            i++;
        }
    }

     int receive_one_byte(int client_socket, char *cur_char)
        {
        ssize_t bytes_received = 0;
        while (bytes_received != 1)
        {
            bytes_received = recv(client_socket, cur_char, 1, 0);
        }
        return 1;
    }

     int receiveFully(int client_socket, char *buffer, int length)
        {
        char *cur_char = buffer;
        ssize_t bytes_received = 0;
        while (bytes_received != length)
        {
            receive_one_byte(client_socket, cur_char);
            cur_char++;
            bytes_received++;
        }
        return 1;
    }

    void printBinaryArray(char *buffer, int length)
        {
        int i = 0;
        while (i < length)
        {
            i++;
        }
       
    }

    int toInteger32(char *bytes)
        {
        int tmp = (bytes[0] << 24) +
                  (bytes[1] << 16) +
                  (bytes[2] << 8) +
                  bytes[3];
        return tmp;
    }
};
