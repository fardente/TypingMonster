
public class highScoreValue {
    private String name;
    private int points;
    
    public highScoreValue(String name, int points)
    {
        this.name = name;
        this.points =  points;
    }
    
    public String getName() { return name; }
    
    public int getPoints() { return points; }
}
