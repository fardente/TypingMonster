/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author G510
 */

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;

public class Library {

    /**
     * @param args the command line arguments
     */
    private ArrayList<String> word;      //ArrayList of words
    private String tosend[];            //array of words to send  
    private int total;                 //total number of words in a file
    int randomInt;                    //random number  
    Random randomGenerator;          //random number generator
    
    
    /**
     * sets total to 0
     * calls loadfile() method
     */
    Library()
    {
        total=0;
        randomGenerator = new Random();
        word=new ArrayList<String>();
        loadfile();        
    }
    /**
     * reads the library.txt file and loads the words in word ArrayList and update total
     */
    public void loadfile()
    {
       //read the whole file and store the sentences in the arraylist 
      //only the sentences with also the words #BOS and #EOS
        String file="res/library.txt";
        String line;
        String words;
        BufferedReader src1;
        Pattern p = Pattern.compile("[^a-z]");

     try 
     {
      src1 = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
      while ((line = src1.readLine()) != null)
      {
          words = line;
          if (!p.matcher(words).find()){
              word.add(words);
              total++;
          }
      }
      src1.close();
     }
        catch (FileNotFoundException e)
        {
            System.out.println(e.getMessage());
            System.exit(0);        
        }
        catch (UnsupportedEncodingException e)
        {
            System.out.println(e.getMessage());
            System.exit(0);
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }
    /**
     * This method generates the random integers and the checks if length of word at given index= lenght of word
     * saves it in tosend array and returns
     * @param length     length of the word
     * @param numwords   number of words
     * @return 
     */

     public String[] get(int length,int numwords)
    {
        ArrayList<Integer> arr=new ArrayList<Integer>();   //check if index is not already generated
        tosend=new String[numwords];       
        for (int i = 0; i < numwords; )
        {
            randomInt = randomGenerator.nextInt(total);
            if(!arr.contains(randomInt) && (word.get(randomInt).length()==length))
            {
             arr.add(randomInt);
             tosend[i]=word.get(randomInt);
             i++;
            }
        }
    return tosend;
    }
    public static void main(String[] args) {
        // TODO code application logic here
         Library l=new Library();
         String []a=new String[1000];
         System.out.println(l.total);
         a=l.get(4,1000);
         
         for(int i=0;i<1000;i++)
         {
             System.out.println(a[i]);
         }
         System.out.println(l.total);
    }
    
}
