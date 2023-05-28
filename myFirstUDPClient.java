import java.net.*;  // for DatagramSocket, DatagramPacket, and InetAddress
import java.io.*;   // for IOException
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

public class myFirstUDPClient {

    private static final int TIMEOUT = 3000;   // Resend timeout (milliseconds)
    private static final int MAXTRIES = 5;     // Maximum retransmissions


    public static void main(String[] args) throws IOException {
        
        if(args.length != 2) {
            throw new IllegalArgumentException("Parameters(s) : <hostName> <Port> ");
        
        }

        long total = 0; //totalTime
        ArrayList<Long> list = new ArrayList<Long>(); //holds the time of each request


        InetAddress serverAddress = InetAddress.getByName(args[0]);  // Server address

        int servPort = (args.length == 2) ? Integer.parseInt(args[1]) : 7; //gets the port number

        DatagramSocket socket = new DatagramSocket(); //creates a new datagram socet

        socket.setSoTimeout(TIMEOUT);  // Maximum receive blocking time (milliseconds)

        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter a number between 0 and 65535 or press quit: ");
        
        String userNumber = scan.nextLine(); //gets the number the user entered

        while(!userNumber.equals("quit")) {
            
            byte[] bytesToSend = userNumber.getBytes("UTF-16");
            String st = "";
            System.out.print("HexRepresentation of current data:");
            
            //prints out the hexadecimal numbers on the screen
            for(byte b: bytesToSend){
                st = String.format("%02X", b);
                System.out.print(st);
            }

            long start = System.currentTimeMillis(); //starts the timing process
            System.out.print("\n");

            DatagramPacket sendPacket = new DatagramPacket(bytesToSend,  // Sending packet
            bytesToSend.length, serverAddress, servPort);

            DatagramPacket receivePacket =                              // Receiving packet
            new DatagramPacket(new byte[2], 2);

            int tries = 0;      // Packets may be lost, so we have to keep trying
            boolean receivedResponse = false; 

            /*
             * We will try to send the packet and receive the packet from the same source
             */
            do {
                socket.send(sendPacket);// Send the echo string

                try {
                    socket.receive(receivePacket);  // Attempt echo reply reception
            
                    if (!receivePacket.getAddress().equals(serverAddress)) { // Check source
                      throw new IOException("Received packet from an unknown source");
                    }
            
                    receivedResponse = true;
                    System.out.println("Packet has been received");

                } catch (InterruptedIOException e) {  // We did not get anything
                    tries += 1;
                    System.out.println("Timed out, " + (MAXTRIES-tries) + " more tries...");
                  }
    

            } while ((!receivedResponse) && (tries < MAXTRIES));
            
            long duration = 0;

            if (receivedResponse) {
                //displays hex of data
                byte[] rec = receivePacket.getData();
                System.out.print("Received: ");
                for(byte b: rec){
                    st = String.format("%02X", b);
                    System.out.print(st);
                 }
                 System.out.println("\n");

                 long finish= System.currentTimeMillis(); //finish time
                 duration = finish - start;
                 System.out.println("\nDuration of that request was: " + duration + " Miliseconds");
                 list.add(duration);
                 total = total + duration;

                 System.out.println("\nPlease enter a number between 0 and 65535 or press quit: ");
                 userNumber = scan.nextLine();
                 continue;


            }
                
            else {
                System.out.println("No response -- giving up.");
            }


            
            socket.close();

       }

       //prints out all the timing information
      System.out.println("Maximum duration was: " + Collections.max(list) + " milliseconds");
      System.out.println("Minimum duration was: " + Collections.min(list)+ " milliseconds");
      System.out.println("Average duration was: " + total / list.size() + " milliseconds" ); 
           


        



        
        

    }
}