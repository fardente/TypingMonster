import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Game extends BasicGame implements ManiacInputListener {

    //##############################
    //## GAME CONSTANTS
    private static String GAMETITLE = "Typing Maniac Clone";
    private static int PLAYTIME = 30; //Seconds to play

    //##############################
    //## GAME VARS
    private boolean lastTenSeconds, pauseState;
    private String input;
    public WordFactory wordFactory;
    private float velocity, speed;
    private long wordTimer = 0; //Counts time up to wordRate
    private long gameTimer = 0;
    private int wordLength = 4; //Initial setting, will increase with Level, MUST NEVER BE BIGGER THAN 10!
    private int numberOfWords = 30; //Initial setting, will increase with Level
    private int wordRate = 1200; //how often new words drop in milliseconds

    //##############################
    //## GRAPHICS
    private static int HEIGHT = 480;
    private static int WIDTH = 640;
    private static int FRAMERATE = 60;

    private static String BG_IMAGE_SRC = "res/img/bg.jpg"; //Background image location
    private Image BG_IMAGE, land, play, startImage;

    private Color TIMER_COLOR = Color.gray;
    private Color WORD_COLOR = Color.darkGray;
    private Color HIGHLIGHT_COLOR = Color.blue;
    private Color INPUT_COLOR = Color.black;

    TrueTypeFont wordFont, inputFont, scoreFont, timerFont;

    //##############################
    //## Sound
    private Sound type;
    Sound backspace;
    Sound enter;
    Sound alarm;
    Sound correct;
    Sound incorrect;
    Sound music;


    //##############################
    //## Score
    private Score score;
    private int start = 0;

    Rectangle rect;

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
        //##############################
        //## GAME VARS
        lastTenSeconds = false;
        pauseState = false;
        input = "";
        wordFactory = new WordFactory(wordLength, numberOfWords, WIDTH, HEIGHT);
        velocity = 1;
        speed = 1.7f;

        //##############################
        //## INPUT
        ManiacInput maniacInput = new ManiacInput();
        maniacInput.addListener(this);
        gc.getInput().addKeyListener(maniacInput);

        //##############################
        //## GRAPHICS
        BG_IMAGE = new Image(BG_IMAGE_SRC);
        land = new Image("res/home.png");
        play = new Image("res/play.png");
        startImage = new Image("res/start.png");
        wordFont = new TrueTypeFont(new Font("Verdana", Font.PLAIN, 24), true);
        inputFont = new TrueTypeFont(new Font("Verdana", Font.BOLD, 32), true);
        scoreFont = new TrueTypeFont(new Font("Verdana", Font.BOLD, 16), true);
        timerFont = new TrueTypeFont(new Font("Verdana", Font.BOLD, 16), true);

        //##############################
        //## Sound
        type = new Sound("res/audio/typing.ogg");
        backspace = new Sound("res/audio/typereverse.ogg");
        alarm = new Sound("res/audio/alarm.ogg");
        incorrect = new Sound("res/audio/incorrect.ogg");
        correct = new Sound("res/audio/incorrect.ogg");

        //##############################
        //## Score
        score = new Score();
    }

    @Override
    public void update(GameContainer gc, int passedTime) throws SlickException {

        int mx= Mouse.getX();
        int my=Mouse.getY();
        String mox=Integer.toString(mx);
        String moy=Integer.toString(my);


        if((mx > 50 && mx < 50+startImage.getWidth()) && (my>85 && my<85+startImage.getHeight()))
        {
            if(Mouse.isButtonDown(0)) {
                start = 1;
                gameTimer = 0;
            }
        }


        if(pauseState == false && start ==1)
        {
            this.gameTimer += passedTime;
            if (gameTimer > PLAYTIME * 1000) {
                if (wordLength > 9) {
                    gc.exit();
                }
                else {
                    pauseState = true;
                    gameTimer = 0;
                    wordLength++;
                    numberOfWords += 2;
                    speed += 0.1f;
                    /*gc.reinit(); Would be nice, but is unfortunately broken when using sound.. it doesnt shut down the
                        openAL audio process, which will complain if it is initialized more than once...
                        So we have to reset everything ourselves:
                    */
                    wordFactory = new WordFactory(wordLength, numberOfWords, WIDTH, HEIGHT);
                    input = "";
                    pauseState = false;
                }
            }

            if (PLAYTIME - gameTimer / 1000 < 10 && !lastTenSeconds) {
                lastTenSeconds = true;
                alarm.play();
            }
            this.wordTimer += passedTime;
            if (wordTimer > wordRate) {
                wordTimer = 0;
                wordFactory.addWordToCurrent();
            }
            wordFactory.moveWords(velocity, speed);
        }
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        if(start==0)
        {
            g.drawImage(land, 60,50);
            g.drawImage(play,50,330);
        }
        else {
            //Draw BG-Image
            BG_IMAGE.getScaledCopy(WIDTH, HEIGHT);
            BG_IMAGE.draw(0, 0);

            //Draw (highlight) Current Words
            for (Word word : wordFactory.getCurrentWords()){
                if (word.getWord().startsWith(input)){
                    highlightString(word, input, wordFont);
                }
                else {
                    wordFont.drawString(word.getX(), word.getY(), word.getWord(), WORD_COLOR);
                }
            }

            if(lastTenSeconds){
                TIMER_COLOR = Color.red;
            }

            //Draw the timer
            timerFont.drawString(WIDTH-40, HEIGHT/20, "" + (PLAYTIME - gameTimer /1000), TIMER_COLOR);

            //Draw input
            inputFont.drawString(WIDTH/2-(inputFont.getWidth(input)/2), HEIGHT-70, input, INPUT_COLOR);

            //Draw the Score
            scoreFont.drawString(WIDTH-40, HEIGHT/10, "" + score.getPoints());
        }
    }

    private void highlightString(Word word, String input, TrueTypeFont font){
        String notHigh = word.getWord().substring(input.length());
        for (int i = 0; i<input.length(); i++){
            notHigh = "_" + notHigh;
        }
        font.drawString(word.getX(), word.getY(), notHigh, WORD_COLOR);
        font.drawString(word.getX(), word.getY(), input, HIGHLIGHT_COLOR);
    }

    @Override
    public void PauseRequest()
    {
        if(!pauseState)
            pauseState = true;
        else
            pauseState = false;
    }

    @Override
    public void RemoveRequest(String word){
        if(!pauseState){
            if (wordFactory.destroyWord(word).equals(false))
                incorrect.play();
            score.increasePoints(word.length());
            input = "";
        }
    }

    @Override
    public void CharRequest(String typedChars){
        if(!pauseState)
            input =  typedChars;
    }

    public static void main(String[] args) {
        try {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new Game(GAMETITLE));
            appgc.setDisplayMode(WIDTH, HEIGHT, false);
            appgc.setTargetFrameRate(FRAMERATE);
            appgc.setShowFPS(false);
            appgc.start();
        } catch (SlickException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}