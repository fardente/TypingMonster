import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.Log;

import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Game extends BasicGame implements ManiacInputListener {

    //We need to store our stuff:
    //Like:
    //public GameState gamestate;
    //public int score;
    //private Timer timer;
    //...

    //TODO Dont use new word instances all the time, just create 20 words and change their string to the
    //new word and the position back to the top
    private static int HEIGHT = 480;
    private static int WIDTH = 640;
    private static int frameRate = 60;
    private static String gameTitle = "Typing Maniac Clone";
    private static int playTime = 30; //Seconds to play
    private static String BGIMAGE = "res/img/bg.jpg"; //Background image location
    private Image bgImg;

    private int wordRate = 1200; //how often new words drop in milliseconds
    private long wordTimer = 0;
    private long gameTimer = 0;
    private Color timerColor;

    public WordFactory wf;
    public Word word;
    private float velocity;
    private float speed;
    TrueTypeFont wordFont;
    TrueTypeFont inputFont;
    TrueTypeFont scoreFont;
    TrueTypeFont timerFont;

    private Color wordColor = Color.darkGray;
    private Color highlightColor = Color.blue;
    private Color inputColor = Color.black;

    //##############################
    //## Sound
    Sound type;
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

    Image land;
    Image play;
    Image startImage;

    Rectangle rect;

    private boolean lastTenSeconds = false;
    private boolean pauseState;
    private String input;

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

        //GAMESTATE
        input = "";
        pauseState = false;
        wf = new WordFactory(4, 30, WIDTH, HEIGHT);
        this.velocity = 1;
        this.speed = 1.7f;

        //INPUT
        rect = new Rectangle(WIDTH/2-WIDTH/10, HEIGHT-70, 20,60);
        ManiacInput maniacInput = new ManiacInput();
        maniacInput.addListener(this);
        gc.getInput().addKeyListener(maniacInput);

        //IMAGES
        bgImg = new Image(BGIMAGE);
        land=new Image("res/home.png");
        play=new Image("res/play.png");
        startImage=new Image("res/start.png");

        //FONTS
        timerColor = Color.gray;
        Font font = new Font("Verdana", Font.PLAIN, 24);
        wordFont = new TrueTypeFont(font, true);

        font = new Font("Verdana", Font.BOLD, 32);
        inputFont = new TrueTypeFont(font, true);

        font = new Font("Verdana", Font.BOLD, 16);
        scoreFont = new TrueTypeFont(font, true);

        font = new Font("Verdana", Font.BOLD, 16);
        timerFont = new TrueTypeFont(font, true);

        //SOUND
        type = new Sound("res/audio/typing.ogg");
        backspace = new Sound("res/audio/typereverse.ogg");
        alarm = new Sound("res/audio/alarm.ogg");
        incorrect = new Sound("res/audio/incorrect.ogg");
        correct = new Sound("res/audio/incorrect.ogg");

        //SCORE
        score = new Score();
    }

    @Override
    public void update(GameContainer gc, int passedTime) throws SlickException {
        /*
        This is the loop, here we check for user input and see if something happened
         */

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


        if(pauseState == false)
        {

            this.gameTimer += passedTime;
            if (gameTimer > playTime * 1000) {
                gc.exit();
            }

            if (playTime - gameTimer / 1000 < 10 && !lastTenSeconds) {
                lastTenSeconds = true;
                alarm.play();
            }
            this.wordTimer += passedTime;
            if (wordTimer > wordRate) {
                wordTimer = 0;
                wf.addWordToCurrent();
            }
            wf.moveWords(this.velocity, this.speed);
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
            bgImg.getScaledCopy(WIDTH, HEIGHT);
            bgImg.draw(0, 0);

            //Draw (highlight) Current Words
            for (Word word : wf.getCurrentWords()){
                if (word.getWord().startsWith(input)){
                    highlightString(word, input, wordFont);
                }
                else {
                    wordFont.drawString(word.getX(), word.getY(), word.getWord(), wordColor);
                }
            }

            if(lastTenSeconds){
                timerColor = Color.red;
            }

            //Draw the timer
            timerFont.drawString(WIDTH-40, HEIGHT/20, "" + (playTime - gameTimer/1000), timerColor);



            //Draw Inputbox according to input length
            rect.setWidth(inputFont.getWidth(input));
            g.draw(rect);

            //Draw input
            inputFont.drawString(WIDTH/2-WIDTH/10, HEIGHT-70, input, inputColor);

            //Draw the Score
            scoreFont.drawString(WIDTH-40, HEIGHT/10, "" + score.getPoints());
        }
    }

    public static void main(String[] args) {
        try {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new Game(gameTitle));
            appgc.setDisplayMode(WIDTH, HEIGHT, false);
            appgc.setTargetFrameRate(frameRate);
            appgc.setShowFPS(false);
            appgc.start();
        } catch (SlickException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void highlightString(Word word, String input, TrueTypeFont font){
        String notHigh = word.getWord().substring(input.length());
        for (int i = 0; i<input.length(); i++){
            notHigh = "_" + notHigh;
        }
        font.drawString(word.getX(), word.getY(), notHigh, wordColor);
        font.drawString(word.getX(), word.getY(), input, highlightColor);
    }

    @Override
    public void PauseRequest()
    {
        if(pauseState == false)
            pauseState = true;
        else
            pauseState = false;
    }

    @Override
    public void RemoveRequest(String word)
    {
        if(pauseState == false)
        {
            if (wf.destroyWord(word).equals(false))
                incorrect.play();
            score.increasePoints(word.length());
            input = "";
        }

    }

    @Override
    public void CharRequest(String typedChars)
    {
        if(pauseState == false)
        {
            input =  typedChars;
            Log.info("Char");
        }
    }
}