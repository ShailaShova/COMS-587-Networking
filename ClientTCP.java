import java.net.*;
import java.io.*;
import java.util.Arrays;


public class ClientTCP extends Thread {
    @Override
    public void run() {
        String serverIP = "127.0.0.1";

        Socket clientSocket;
      //while(true){   
        try {
            clientSocket = new Socket(serverIP, 5555);
            clientSocket.getReceiveBufferSize();
            DataInputStream inStream = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream outStream = new DataOutputStream(clientSocket.getOutputStream());
           
            String mess = "GetLocalTime";
            byte []buff = mess.getBytes();
            byte[] bufLengthInBinary = toBytes(mess.length());
            printBinaryArray(bufLengthInBinary, "here");

            outStream.write(bufLengthInBinary, 0, bufLengthInBinary.length);
            outStream.write(buff, 0, buff.length);
            outStream.flush();

            //inStream.readFully(bufLengthInBinary); // ignore the first 4 bytes
            //System.out.println(new String()bufLengthInBinary);
            //inStream.readFully(buff);

            byte[] messageByte = new byte[2];
            
            boolean end = false;
            String dataString = "";

            try 
            {
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                int lenofmessage = 2;
                boolean first = true;
                while(!end)
                {
                    int bytesRead = in.read(messageByte);
                    dataString += new String(messageByte, 0, bytesRead);
                    if (dataString.length() >= 1)
                    {
                        lenofmessage = Integer.parseInt(dataString);
                        if (first){
                            dataString = "";
                            first = false;
                        }
                        end = true;  
                    }
                }
                
                end = false;
                byte[] messageByte2 = new byte[lenofmessage];
                while(!end)
                {
                    int bytesRead = in.read(messageByte2);
                    dataString += new String(messageByte2, 0, bytesRead);
                    if (dataString.length() >= lenofmessage)
                    {
                        end = true;     
                    }
                }
                System.out.println("Time and Operating system: " + dataString);
            }
            catch (Exception e)
            {
                System.out.println("MESSAGE: exception" + dataString);

                e.printStackTrace();
            }

            //System.out.println(return_value);
            // System.out.println(buff);

        } catch (IOException e1) {
            
            System.out.println("DEBUG 1: exception 1");

            e1.printStackTrace();
        }
 
    }

    static void printBinaryArray(byte[] b, String buff)
    {
        for (int i=0; i<b.length; i++)
        {
            //System.out.print(b[i] + " ");
        }
        
    }

    static private byte[] toBytes(int i)
    {
        byte[] result = new byte[4];
        result[0] = (byte) (i >> 24);
        result[1] = (byte) (i >> 16);
        result[2] = (byte) (i >> 8);
        result[3] = (byte) (i /*>> 0*/);
       
        return result;
    }

   
}


