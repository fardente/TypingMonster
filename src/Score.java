//##############################################
//## Packages

import java.util.*;
import java.io.*;

//##############################################
//## Class definition
public class Score {

    //##############################
    //## Main class (testing)
    
    public static void main(String[] args) {
        Score scores = new Score();
        scores.increasePoints(1);
        scores.increasePoints(1);
        scores.increasePoints(1);
        scores.increasePoints(1);
        scores.increasePoints(1);
        scores.increasePoints(1);
        scores.increasePoints(1);
        scores.increasePoints(1);
        scores.increasePoints(1);
        scores.increasePoints(20);

        try {
            scores.saveScore("Andy");
        } catch (Exception ex) {
        }

        for (highScoreValue entry : scores.getHighScore()) {
            Integer key = entry.getPoints();
            String value = entry.getName();
            System.out.println(key + " => " + value);
        }
    }

    //##############################
    //## Properties
    
    private int points;
    private int startPoints = 100;
    private int increaseRatio = 10;
    private String fileName = "./Highscore.xml";

    List<highScoreValue> highScore;

    //##############################
    //## Constructor
    
    public Score() {
        this.highScore = new ArrayList<>();
        this.points = 0;

        try {
            this.highScore = loadHighscore();
            sortHighscore();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    //##############################
    //## Methods (Set / Get)
    
    private void setPoints(int points) {
        this.points = points;
    }

    //##############################
    //## Methods (Set / Get)
    
    public int getPoints() {
        return points;
    }

    //##############################
    //## Methods
    // Set points back to zero
    public void reset() {
        setPoints(0);
    }

    // Save the current score to highscore file
    public void saveScore(String username) throws Exception {
        this.highScore.add(new highScoreValue(username, points));
        this.sortHighscore();

        try {
            File file = new File(fileName);
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            writer.write("<Highscore>");
            writer.newLine();
            for (highScoreValue entry : highScore) {
                Integer userPoints = entry.getPoints();
                username = entry.getName();

                writer.write("\t<Score>");
                writer.newLine();
                writer.write("\t\tUsername:" + username);
                writer.newLine();
                writer.write("\t\tPoints:" + userPoints);
                writer.newLine();
                writer.write("\t<\\Score>");
                writer.newLine();
            }
            writer.write("<\\Highscore>");

            writer.close();
        } finally {
        }
    }

    // Return a list of all highscores (ASC)
    // @return TreeTable<int, String>
    public List<highScoreValue> getHighScore() {
        return this.highScore;
    }

    private List<highScoreValue> loadHighscore() throws Exception {
        List<highScoreValue> data = new ArrayList<>();

        try {
            int state = 0; // 0 = Unkown; 1 = root; 2 = Score
            String username = null;
            Integer userPoints = null;

            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            String zeile = br.readLine();

            while (zeile != null) {
                zeile = zeile.replaceAll("([ {2,}\\t]{0,}<)", "");
                zeile = zeile.replaceAll("(>[ {2,}\\t]{0,})", "");

                if (state == 0) {
                    if (zeile.equals("Highscore")) {
                        state = 1; // Root found
                    }
                } else if (state == 0) {
                    if (zeile.equals("\\Highscore")) {
                        break;  // End of XML found
                    }
                } else if (state == 1) {
                    if (zeile.equals("Score")) {
                        state = 2;
                    }
                } else if ((state == 1 || state == 2) && zeile.equals("\\Score")) {
                    
                    if (username != null && userPoints != null) {
                        highScoreValue newValue = new highScoreValue(username, userPoints);
                        data.add(newValue);
                    }

                    state = 1; // End of score
                    userPoints = null;
                    username = null;
                } else if (state == 2) {
                    zeile = zeile.replaceAll("([ {2,}\\t]{0,})", "");

                    if (zeile.contains("Username:")) {
                        username = zeile.replace("Username:", "");
                    } else if (zeile.contains("Points:")) {
                        try {
                            userPoints = Integer.parseInt(zeile.replace("Points:", ""));
                        } catch (NumberFormatException e) {
                        }
                    }
                }

                zeile = br.readLine();
            }

            br.close();
        } catch (FileNotFoundException e) {
        }

        return data;
    }

    /* Not used for now
    // Increase the current points, based on the game-level
    public void increasePoints(int level) {
        if (level > 0) {
            level -= 1;
            this.points += (int) ((double) this.startPoints / 100 * (100 + (this.increaseRatio * level)));
        }
    }*/

    public void increasePoints(int wordLength) {
        this.points += wordLength;
    }

    // Increase the current points, based on the game-level
    public void decreasePoints(int level) {
        if (level > 0) {
            this.points += (int) ((double) this.startPoints / 100 * (100 - (this.increaseRatio * level)));
        }
    }
    
    private void sortHighscore()
    {
        for(int i = 0; i < this.highScore.size() - 1; i++){
            for(int p = 0; p < this.highScore.size() - 1; p++){
                if(highScore.get(p).getPoints() < highScore.get(p + 1).getPoints()){
                    highScoreValue tmp = highScore.get(p);
                    highScore.set(p, highScore.get(p + 1));
                    highScore.set(p + 1, tmp);
                }
            }
        }
    }
}
