import java.util.Iterator;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * We use CopyOnWriteArrayList for the current words, so no ConcurrentModificationException occur
 */
public class WordFactory {

    private final int XOFFSET = 200;
    private int wordLength;
    private int wordsPerLevel;
    private int maxWidth, maxHeight;
    private Stack<Word> wordStorage;
    private CopyOnWriteArrayList<Word> currentWords;
    private Library library;
    private Random random = new Random();

    public WordFactory(int wordLength, int wordsPerLevel, int maxWidth, int maxHeight){
        this.wordLength = wordLength;
        this.wordsPerLevel = wordsPerLevel;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.wordStorage = new Stack<>();
        this.currentWords = new CopyOnWriteArrayList<>();

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
        for(String wordString : library.get(this.wordLength, 30)){
            this.wordStorage.push(createWord(wordString));
        }
    }

    public Word createWord(String wordString){
        int startPosition = random.nextInt(maxWidth-XOFFSET);
        Word word = new Word(wordString, startPosition, -10);
        return word;
    }

    public boolean addWordToCurrent(){
        if (!wordStorage.isEmpty()){
            currentWords.add(wordStorage.pop());
            return true;
        }
        return false;
    }

    public CopyOnWriteArrayList<Word> getCurrentWords(){
        return currentWords;
    }

    public void destroyWord(Word word){
        currentWords.remove(word);
    }

    public void destroyWord(String word){
        currentWords.remove(word);
    }

    public boolean matched(String input){
        for (Word word : currentWords){
            if(word.getWord().equals(input)){
                word.setSolved(true);
                return true;
            }
        }
        return false;
    }

    public void moveWords(float velocity, float speed){

        final Iterator<Word> iterator = this.currentWords.iterator();
        while(iterator.hasNext()){
            Word word = iterator.next();
            if((word.getY()+ speed)* velocity > maxHeight){
                currentWords.remove(word);
            }
            else {
                word.moveDown(velocity, speed);
            }
        }






    }


}
