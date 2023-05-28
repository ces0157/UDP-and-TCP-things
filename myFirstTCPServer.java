import java.net.*;  // for Socket, ServerSocket, and InetAddress
import java.io.*;   // for IOException and Input/OutputStream

public class myFirstTCPServer {

   private static final int BUFSIZE = 32;   // Size of receive buffer

   public static void main(String[] args) throws IOException {
   
      if (args.length != 1)  // Test for correct # of args
         throw new IllegalArgumentException("Parameter(s): <Port>");
      int servPort = Integer.parseInt(args[0]);
    // Create a server socket to accept client connection requests
      ServerSocket servSock = new ServerSocket(servPort);   
   
      int recvMsgSize;   // Size of received message
      byte[] byteBuffer = new byte[BUFSIZE];  // Receive buffer
      short A = 0; //output value
      
      System.out.println("Server established, waiting for connection...");
      for (;;) { // Run forever, accepting and servicing connections
         Socket clntSock = servSock.accept();     // Get client connection
      
         System.out.println("Handling client at " +
            clntSock.getInetAddress().getHostAddress() + " on port " +
                 clntSock.getPort());
      
         InputStream in = clntSock.getInputStream();
         OutputStream out = clntSock.getOutputStream();
         
         System.out.println("Recieving data\nDisplaying as byte array...");
         // Receive until client closes connection, indicated by -1 return
         while ((recvMsgSize = in.read(byteBuffer)) != -1) {
         
            System.out.println("Displaying bytes recieved in Hex...");
            for(byte b: byteBuffer){
               String st = String.format("%02X", b);
               System.out.print(st);
            }
            
            String recv = new String(byteBuffer, "UTF-16");
            System.out.println("\n");
            System.out.println("Original string recieved was: " + recv);
            
            //resets the buffer
            for(int i = 0; i < byteBuffer.length; i++){
               byteBuffer[i] = 0;
            }
            
         //check for invalid values
            try {
               short num = Short.valueOf(recv.trim());
               byte[] outBuffer = {(byte) (num >> 8),
                                (byte) num};
               System.out.print("Creating new short: " + num);
               System.out.print("\n");
               System.out.print("Sending back to client (hex representation):");
               for(byte b: outBuffer){
                  String st = String.format("%02X", b);
                  System.out.print(st);
               }
               System.out.println("\n");
               out.write(outBuffer, 0, 2);
            
            } catch (NumberFormatException e) {
               byte[] outBuffer = {(byte) 0xff, (byte) 0xff};
               out.write(outBuffer, 0, 2);
               System.out.println("String larger than 2 bytes or invalid, sending back 0xFFF");
            }      
         }
         clntSock.close();  // Close the socket.  We are done with this client!
         
         /* NOT REACHED */
      }
   }
   
}
