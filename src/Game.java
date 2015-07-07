import org.newdawn.slick.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Game extends BasicGame {

    public Game(String gamename) {
        super(gamename);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {
        Input input = gc.getInput();
        if (input.isKeyDown(Input.KEY_G)) {

            System.out.println("Key G pressed");

        }
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        Image img = new Image("res/img/bg.jpg");
        img = img.getScaledCopy(640,480);
        img.draw(0,0);
        g.setColor(Color.black);
        g.drawString("Seems to work!", 240, 200);
    }

    public static void main(String[] args) {
        try {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new Game("Simple Slick Game"));
            appgc.setDisplayMode(640, 480, false);
            appgc.setTargetFrameRate(60);
            appgc.start();
        } catch (SlickException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}