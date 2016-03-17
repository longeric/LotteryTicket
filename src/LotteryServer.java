import java.net.*;
import java.io.*;
import java.util.Random;
import java.util.Calendar;

public class LotteryServer{
    
    public static void main(String[] args){
    	System.out.println("what happen !");
    	int portNum = 4445;//Integer.parseInt(args[0]); 
    	boolean listening = true; //listens for connections
                
        try (ServerSocket serverSocket = new ServerSocket(portNum)) { 
           LotteryCipher lc = new LotteryCipher(); 
            LotteryLife llife = new LotteryLife(lc);
            llife.start(); 
           
            while (listening) {
                new LotteryThread(serverSocket.accept(), lc, llife).start();
            }
        } catch (IOException e) {}
  }
}

class LotteryLife extends Thread{ // used to keep track of the time a lottery game lasts
 
 LotteryCipher lc; 
 Calendar gameEnd; // how long one is able enter the lottery
 Calendar nextGame; // when the game ends
 
  LotteryLife(LotteryCipher lc){ 
   this.lc = lc; 
   
  }
  
 
  
  public synchronized void run(){
   
      while(true){
        try{
       System.out.println("A NEW KEY");
       lc.newKey();
       adjustTime(30000); // change the amount of time the game lasts -- the game ends every 60 secs here but 
       sleep(60000);       // stops accepting new players after 30 seconds
     
        
        }catch(InterruptedException ie){}
        
        
      }
  }
      
     public synchronized void adjustTime(int milliseconds){
        gameEnd = Calendar.getInstance(); 
        gameEnd.add(Calendar.MILLISECOND, milliseconds); 
        nextGame = Calendar.getInstance();
        nextGame.add(Calendar.MILLISECOND, milliseconds*2); 
      }
    
  
  
  
  
}
class LotteryThread extends Thread{
    
    private Socket socket = null;
    private LotteryCipher lc; 
    LotteryLife llife; 
    
    public static String generateResult(){       //generate a result
        Random random = new Random();
        int r = random.nextInt(999);        
        return String.format("%03d", r);
    }

    
    public LotteryThread(Socket socket, LotteryCipher lc, LotteryLife llife) {
        super("Lottery Game");
        this.socket = socket;      
        this.lc = lc; 
        this.llife = llife; 
       }
    
   @Override
    public void run(){     
        try(     
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            )
        {
                                       
            boolean go = true;
            Calendar cal = Calendar.getInstance(); // get the current time
            
          while(go){ 
             
            String f = in.readLine();
            
             if(f.equals("Ask the result")) // this means that we get the results of the lottery game 
            { 
               String res = generateResult(); // works as "instant lottery" each thread get a different result
               out.println(res);
               String[] fromTicket = in.readLine().split("::");
                String number = fromTicket[0];      //we read number and code from ticket.txt file in the client
                String code = fromTicket[1];
                          
                String decryptNumber = lc.decryptInfo(code);        //decrypt the code
              
                
                if(!(number.equals(decryptNumber)))                         //and compare to the number
                    out.println("Your ticket is expired or invalid");
             
               else if(number.equals(res)){       
                      out.println("Congratulations! You won the prize!");
                        }
                    else{
                       out.println("What a pity! You didn't win!");
                    }
               }
           else{  //if not reading "Ask the result", that means customer input a number, we should accept the number, encrypt and send back
               if((cal.compareTo(llife.gameEnd)) < 0){    
                 out.println(); 
                 f = in.readLine(); 
                 out.println(lc.encryptInfo(f));     //output the encryption code here
                        
         } 
            else{
              out.println("GAME END"); 
              out.println("Game no longer available. Wait until after " + llife.nextGame.getTime().toString() + " to try again"); 
            }
             }
            
            go = false;  
            
        }
        }catch(IOException e){}
   
    }  
}