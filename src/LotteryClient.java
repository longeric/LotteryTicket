
import java.io.*;
import java.net.*;
import java.util.Calendar;
import java.util.Date; 
import java.util.Random;
import java.util.Scanner;

class IOFile
{
    public void storeFile(String number, String t){
        try{
            PrintWriter outfile = new PrintWriter(new OutputStreamWriter(new FileOutputStream("ticket.txt")),true);
            BufferedReader infile = new BufferedReader(new FileReader("ticket.txt"));
            outfile.println("*************Lottery Ticket**************");
            outfile.println();
            outfile.println("Ticket Number: " + number);
            outfile.println("Ticket Code: " + t);
            outfile.println();
            outfile.println("THIS TICKET IS AUTHENTIC");
            
            
            Calendar gameCal = Calendar.getInstance();
              gameCal.add(Calendar.MILLISECOND, 60000); 
              Date expDate = gameCal.getTime(); 
            outfile.println("Play before this date: " + expDate.toString());
            outfile.println("*****************************************");
            outfile.close();
        }
        catch(FileNotFoundException fnf){}
        //catch(IOException e){}        
    }
    public String outputFile(){         //I didn't done this part because I confused how to use output stream as parameter
      String s = null; 
      try{
            BufferedReader infile = new BufferedReader(new FileReader("ticket.txt"));
            s = infile.readLine(); 
            s = infile.readLine(); 
            s = infile.readLine().substring(15) + "::" + infile.readLine().substring(13); 
            infile.close(); 
        }
        catch(FileNotFoundException fnf){}
        catch(IOException e){} 
   
     return s; 
    }
    
    
}

public class LotteryClient{
  //  File file=new File("../ticket.txt");    //I don't know am I using right, see line 46, 57
    public static String generateResult(){       //generate a result
        Random random = new Random();
        int r = random.nextInt(999);        
        return String.format("%03d",r);
    }
    public static void main(String[] args){
   
        String hostName = "localhost";//args[0];
        int portNum = 4445;//Integer.parseInt(args[1]);
    
        try(   
            Socket ttSocket = new Socket(hostName, portNum);
            PrintWriter out = new PrintWriter(ttSocket.getOutputStream(),true);
            BufferedReader in = new BufferedReader(new InputStreamReader(ttSocket.getInputStream()));
                
        ){           
            
            Scanner input = new Scanner(System.in);
            IOFile fff = new IOFile();      //create a new class for txt file read
            
            System.out.println("\n********Welcome to lottery game********");
            System.out.println("---------------");
            System.out.println("1. Buy a number");
            System.out.println("2. Get prize");
            System.out.println("---------------");
            System.out.print("Please enter an option: ");
            
            String s = input.next();
            if(s.equals("1"))
            {
             out.println(); 
             String goodGame = in.readLine();
              if(goodGame.equals("GAME END")){
                System.out.println(in.readLine()); 
               
                }
             else{
               System.out.println("Please input a 3-digit number,\nor just input 'random' to randomly generate a number");
                s =input.next();
                if(s.equals("random"))
                    s=generateResult();
                
                
                out.println(s);        //send this number to server, server should encrypt this number and send back a code
          
                String t = in.readLine();       //get code from server
                System.out.println("This is the your number " + s + " with a code");                           
                System.out.println(t);
                
                fff.storeFile(s, t);
                }
            }
            if(s.equals("2"))
            {
                out.println("Ask the result");
                System.out.println("The last result is " + in.readLine());
                System.out.println("Please input your number (and code)"); 
                
             
                out.println(fff.outputFile());      //trouble here~~~~~~~~~~~~~
                
                String g = in.readLine();
                System.out.println();
                System.out.println(g);      //if we win/lose the game, in.readLine() give us the proper message
                System.out.println();              
            }
           
        }catch(UnknownHostException e){}catch(IOException e){}
 
  }                                          
}


  
  
  
