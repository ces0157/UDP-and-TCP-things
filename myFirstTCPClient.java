import java.net.*;  // for Socket
import java.io.*;   // for IOException and Input/OutputStream
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

public class myFirstTCPClient {

   public static void main(String[] args) throws IOException {
   
    
      if ((args.length < 2) || (args.length > 3))  // Test for correct # of args
         throw new IllegalArgumentException("Parameter(s): <Server> <Word> [<Port>]");
     
      String server = args[0];       // Server name or IP address
    // Convert input String to bytes using the default character encoding
      byte[] byteBuffer = args[1].getBytes("UTF-16");
   
      int servPort = (args.length == 3) ? Integer.parseInt(args[2]) : 7;
   
      ArrayList<Long> list = new ArrayList<Long>();
      long total = 0;
   
    // Create socket that is connected to server on specified port
      Socket socket = new Socket(server, servPort);
      System.out.println("Connected to server...sending echo string");
   
      InputStream in = socket.getInputStream();
      OutputStream out = socket.getOutputStream();
      Scanner scan = new Scanner(System.in);
   
      System.out.println("Please enter a number between 0 and 65535 or press quit: ");
      String number = scan.nextLine();
   
      while(!number.equals("quit")) {
         
         byteBuffer = number.getBytes("UTF-16");
         String st = "";
         System.out.print("Hex Representation of current data: ");
        //prints out the hexadecimal numbers on the screen
         for(byte b: byteBuffer){
            st = String.format("%02X", b);
            System.out.print(st);
         }
      
         //long start = System.nanoTime();
         long start = System.currentTimeMillis();
         System.out.print("\n");
         out.write(byteBuffer);  // Send the encoded string to the server
      
      
        
      
      // Receive the same string back from the server
         int totalBytesRcvd = 0;  // Total bytes received so far
         int bytesRcvd;           // Bytes received in last read
      
      
         while (totalBytesRcvd < byteBuffer.length) {
         
         
            if(totalBytesRcvd == 2){
            
               System.out.print("Bytes Recived: ");
               byte[] outputBuffer = {byteBuffer[0], byteBuffer[1]};
               for(byte b: outputBuffer){
                  st = String.format("%02X", b);
                  System.out.print(st);
               }
               //long end = System.nanoTime();
               long end = System.currentTimeMillis();
               long duration =  (end - start);
               System.out.println("\nDuration of that request was: " + duration + " Miliseconds");
               list.add(duration);
               total = total + duration;
            
               System.out.println("\nPlease enter a number between 0 and 65535 or press quit: ");
               number = scan.nextLine();
               break;
            
            }
         
         /*
         if(byteBuffer = in.read(byteBuffer, totalBytesRcvd, byteBuffer.length - totalBytesRcvd == 2)){
            System.out.println("Byte's received");
            for(int i = 0; i < byteBuffer.length; i++){
                System.out.println(byteBuffer[i]);
            }
         }
         */
         
            if ((bytesRcvd = in.read(byteBuffer, totalBytesRcvd,  
                        byteBuffer.length - totalBytesRcvd)) == -1)
               throw new SocketException("Connection close prematurely");
            totalBytesRcvd += bytesRcvd;
         }
      
         if(!number.equals("quit")){
            continue;
         }
      
      
      
         System.out.println("Received: " + new String(byteBuffer));
      
         socket.close();  // Close the socket and its streams
      
      }
   
      System.out.println("Maximum duration was: " + Collections.max(list) + " milliseconds");
      System.out.println("Minimum duration was: " + Collections.min(list)+ " milliseconds");
      System.out.println("Average duration was: " + total / list.size() + " milliseconds" ); 
   }
}
