
import java.io.PrintWriter;
import java.io.FileOutputStream; 
import java.io.FileNotFoundException;
import java.io.BufferedReader; 
import java.io.File;
import java.io.FileReader; 
import java.io.*; 

public class OutFile{
  
  public static void main(String args[]){
    try{
        File file = new File("ticket.txt");
        PrintWriter outfile = new PrintWriter(new OutputStreamWriter(new FileOutputStream("output.txt")),true);
        BufferedReader infile = new BufferedReader(new FileReader("output.txt")); 
   
    //Write content to file
    
        outfile.println("******");
        outfile.println("*TEST*");
        outfile.println("******"); 
        outfile.println();
        outfile.println("END");
        outfile.close();  //must close file at end
    
    
    //Read the contents of the file but only print the word END in the console 
    
        String s = infile.readLine();
        while(!(s.equals("END"))){
        s = infile.readLine(); // reads the file line-by-line
 
        }
        /*
        String data = infile.readLine();    //read from txt
        while(data != null)
        {
            System.out.println(data);
            data = infile.readLine();
        }
        */
    
        //System.out.println(s);
        infile.close(); // must close file at end
    
        }
        catch(FileNotFoundException fnf){}
        catch(IOException e){}
  }
}