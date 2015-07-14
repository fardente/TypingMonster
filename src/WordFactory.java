import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;

public class WordFactory {

    private final int XOFFSET = 200;
    private int wordLength;
    private int maxWidth, maxHeight;
    private Stack<Word> wordStorage;
    private ArrayList<Word> currentWords;
    private Library library;
    private Random random = new Random();

    public WordFactory(int wordLength, int maxWidth, int maxHeight){
        this.wordLength = wordLength;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.wordStorage = new Stack<>();
        this.currentWords = new ArrayList<>(10);

        this.library = new Library();
        library.loadfile();
        readWords();
    }

    public void setWordLength(int length){
        this.wordLength = length;
    }

    public int getWordLength(){
        return this.wordLength;
    }

    private void readWords(){
        this.wordStorage.clear();
        for(String wordString : library.get(this.wordLength)){
            this.wordStorage.push(createWord(wordString));
        }
    }

    public Word createWord(String wordString){
        int startPosition = random.nextInt(maxWidth-XOFFSET);
        Word word = new Word(wordString, startPosition, -50);
        return word;
    }

    public boolean addWordToCurrent(){
        if (!wordStorage.isEmpty()){
            currentWords.add(wordStorage.pop());
            return true;
        }
        return false;
    }

    public ArrayList<Word> getCurrentWords(){
        return currentWords;
    }

    private void destroyWord(Word word){
        currentWords.remove(word);
    }

    public void moveWords(float velocity, float speed){
        final Iterator<Word> iterator = this.currentWords.iterator();
        while(iterator.hasNext()){
            Word word = iterator.next();
            if((word.getY()+ speed)* velocity > maxHeight){
                iterator.remove();
            }
            else {
                word.moveDown(velocity, speed);
            }
        }






    }


}
