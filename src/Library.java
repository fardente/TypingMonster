/**
 *
 * @author G510
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
public class Library {

    /**
     * @param args the command line arguments
     */
    private ArrayList<String> word=new ArrayList<String>();
    private String tosend[]=new String [20];
    private int total=128985;
    private ArrayList<String> alreadysend=new ArrayList<String>();
    Library()
    {
        loadfile();
    }
    public void loadfile()
    {
        //read the whole file and store the sentences in the arraylist 
      //only the sentences with also the words #BOS and #EOS
      String file="res/library.txt";
     String line;
     int count=0;
     //String[] words=new String[8];
     String words; 
     BufferedReader src1;
     String encoding="UTF-8";
     int fre;
    // BufferedReader src2;
     //NameFrequency obj;
  //blist=new NoRepeatsList();
  //glist=new NoRepeatsList();
  try 
  {
    src1 = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
    while ((line = src1.readLine()) != null)
    {
          words = line;
          word.add(words);
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
    public String[] get(int length)
    {
        ArrayList<Integer> arr=new ArrayList<Integer>();
        Random randomGenerator = new Random();
       
    for (int i = 0; i < 20; ){
      int randomInt = randomGenerator.nextInt(total);
      if(!arr.contains(randomInt) && (word.get(randomInt).length()==length))
      {
          arr.add(randomInt);
          tosend[i]=word.get(randomInt);
          i++;
      }
    }
    int a;
    for (int j=0;j<20;j++)
    {
        alreadysend.add(word.get(j));
        a=arr.get(j);
        word.remove(a);
    }
    total=total-20;
    return tosend;
    }
    public void returned(String array[])
    {
        for(int i=0;i<20;i++)
        {
            if(!array[i].equals(""))
            {
                word.add(array[i]);
                total++;
            }
        }
    }
//----------------------------------------------------------------------------------
     public String[] get(int length,int numwords)
    {
        ArrayList<Integer> arr=new ArrayList<Integer>();
        Random randomGenerator = new Random();
        String []send=new String[numwords];       
    for (int i = 0; i < numwords; ){
      int randomInt = randomGenerator.nextInt(total);
      if(!arr.contains(randomInt) && (word.get(randomInt).length()==length))
      {
          arr.add(randomInt);
          send[i]=word.get(randomInt);
          i++;
      }
    }
    int a;
    for (int j=0;j<20;j++)
    {
        alreadysend.add(word.get(j));
        a=arr.get(j);
        word.remove(a);
    }
    total=total-numwords;
    return send;
    }
    public static void main(String[] args) {
        // TODO code application logic here
         Library l=new Library();
         String []a=new String[1000];
         a=l.get(4,1000);
         
         for(int i=0;i<1000;i++)
         {
             System.out.println(a[i]);
         }
    }
    
}
