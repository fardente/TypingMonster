import org.newdawn.slick.*;

import java.util.logging.Level;
import java.util.logging.Logger;


public class Game extends BasicGame {

    //We need to store our stuff:
    //Like:
    //public GameState gamestate;
    //public int score;
    //private Timer timer;
    //...
    private static int HEIGHT = 480;
    private static int WIDTH = 640;
    private static int frameRate = 60;
    private static String gameTitle = "Typing Maniac Clone";
    private static int playTime = 30; //Seconds to play

    private int wordRate = 1200; //how often new words drop in milliseconds
    private long wordTimer = 0;
    private long gameTimer = 0;

    public WordFactory wf;
    public Word word;
    private float velocity;
    private float speed;
    private String inputString = "";


    public Game(String gamename) {
        super(gamename);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        /*Create the instances of our classes here:
        e.g.:
        private WordBag wb = new WordBag(file);
        private Timer timer = new Timer();
         */
        wf = new WordFactory(4,WIDTH, HEIGHT);
        this.velocity = 1;
        this.speed = 0.7f;
    }

    @Override
    public void update(GameContainer gc, int passedTime) throws SlickException {
        /*
        This is the loop, here we check for user input and see if something happened
         */
        this.gameTimer += passedTime;
        if (gameTimer > playTime*1000){
            gc.exit();
        }
        this.wordTimer += passedTime;
        if (wordTimer > wordRate){
            wordTimer = 0;
            wf.addWordToCurrent();
        }
        Input input = gc.getInput();

        if (input.isKeyPressed(Input.KEY_G)) {

            inputString += "g";
            System.out.println(inputString);
        }
        if (input.isKeyPressed(Input.KEY_ENTER)) {

            System.out.println("Key G pressed");

        }
        if (input.isKeyPressed(Input.KEY_BACK)) {

            inputString = inputString.substring(0, inputString.length()-1);
            System.out.println(inputString);
        }
        wf.moveWords(this.velocity, this.speed);

    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        Image img = new Image("res/img/bg.jpg");
        img = img.getScaledCopy(WIDTH, HEIGHT);
        img.draw(0,0);
        for (Word word : wf.getCurrentWords()){
            String input = "a";
            if (word.getWord().startsWith(input)){
                highlightString(word, input, g);
            }
            else {
                g.drawString(word.getWord(), word.getX(), word.getY());
            }
        }
        g.setColor(Color.black);
        g.drawString("x            Seems to work!", 240, 200);
        g.drawString("Time : " + (playTime - gameTimer/1000), WIDTH-100, HEIGHT/10);
        g.drawString(inputString, 300, HEIGHT-100);
    }

    public static void main(String[] args) {
        try {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new Game(gameTitle));
            appgc.setDisplayMode(WIDTH, HEIGHT, false);
            appgc.setTargetFrameRate(frameRate);
            appgc.start();
        } catch (SlickException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void highlightString(Word word, String input, Graphics g){
        String highlighted = input;
        String notHigh = word.getWord().substring(input.length());
        for (int i = 0; i<input.length(); i++){
            notHigh = " " + notHigh;
        }
        g.drawString(notHigh, word.getX(), word.getY());
        g.setColor(Color.red);
        g.drawString(input, word.getX(), word.getY());
        g.setColor(Color.black);

    }
}