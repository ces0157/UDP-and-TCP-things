import java.net.*;  // for DatagramSocket, DatagramPacket, and InetAddress

import javax.crypto.spec.DESKeySpec;

import java.io.*;   // for IOException

public class myFirstUDPServer {
   
  private static final int ECHOMAX = 12;  // Maximum size of echo datagram

  public static void main(String[] args) throws IOException {

    if (args.length != 1)  // Test for correct argument list
      throw new IllegalArgumentException("Parameter(s): <Port>");

    int servPort = Integer.parseInt(args[0]);

    DatagramSocket socket = new DatagramSocket(servPort);
   

    for (;;) {  // Run forever, receiving and echoing datagrams
        DatagramPacket packet = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
        socket.receive(packet);     // Receive packet from client
        
      System.out.println("Handling client at " +
        packet.getAddress().getHostAddress() + " on port " + packet.getPort());


        byte[] received = packet.getData();
        System.out.println("Displaying bytes recieved in Hex...");
        //print out the value in hex
        
        for(byte b: received){
            String st = String.format("%02X", b);
            System.out.print(st);
         }


         //prints out the original string
        
         try {
        
         String recv = new String(received, "UTF-16");
        recv = recv.trim();
        System.out.println("\n");
        System.out.println("Original string recieved was: " + recv);

            //converts the string to short
            short num = Short.valueOf(recv.trim());
            
            byte[] out = {(byte) (num >> 8),
                             (byte) num};
            System.out.print("Creating new short: " + num);
            System.out.print("\n");
            System.out.print("Sending back to client (hex representation):");
            
            //prints out the hex representation
            for(byte b: out){
               String st = String.format("%02X", b);
               System.out.print(st);
            }
            System.out.println("\n");
    
      packet.setData(out);      
      socket.send(packet);

    }

    catch(NumberFormatException e) {
        byte[] outBuffer = {(byte) 0xff, (byte) 0xff};
        packet.setData(outBuffer);
        socket.send(packet);
        System.out.println("String larger than 2 bytes or invalid, sending back 0xFFFF");


    }
      
          
       // Send the same packet back to client
      //packet.setLength(ECHOMAX); // Reset length to avoid shrinking buffer
    }
    /* NOT REACHED */
  }

}
