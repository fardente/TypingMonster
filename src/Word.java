public class Word {

    private float x,y;
    private String word;
    private boolean isSolved;

    public Word(String word, float x, float y){
        this.word = word;
        this.x = x;
        this.y = y;
        this.isSolved = false;
    }

    public String getWord() {
        return word;
    }

    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

    public void setSolved(boolean solved) {
        isSolved = solved;
    }
    public boolean isSolved() {
        return isSolved;
    }

    public void moveDown(float velocity, float speed){
        this.y = (this.y + speed)* velocity;
    }
}
