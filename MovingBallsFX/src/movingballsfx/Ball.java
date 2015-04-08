package movingballsfx;

import java.util.Random;
import javafx.scene.paint.Color;

public class Ball {

    private int xPos, yPos, speed;
    private int minX, maxX;
    private Color color;
    private int minCsX;
    private int maxCsX;
    private Monitor mon;
    
    public Ball(int minX, int maxX, int minCsX, int maxCsX, int yPos, Color color, Monitor monitor) {
        this.xPos = minX;
        this.yPos = yPos;
        this.minX = minX;
        this.maxX = maxX;
        this.minCsX = minCsX;
        this.maxCsX = maxCsX;
        this.speed = 10 + (new Random()).nextInt(5);
        this.color = color;
        this.mon = monitor;
    }

    public void move() throws InterruptedException{
        xPos++;
        
        if(this.isEnteringCs() && this.color == Color.RED){
            mon.enterReader();
        }
        else if(this.isEnteringCs() && this.color == Color.BLUE){
            mon.enterWriter();
        }
        else if(this.isLeavingCs()){
            if(this.color == Color.RED)
                mon.exitReader();
            if(this.color == Color.BLUE)
                mon.exitWriter();
        }
            
        
        if (xPos > maxX) {
            xPos = minX;
        }
    }

    public int getXPos() {
        return xPos;
    }

   public int getYPos() {
        return yPos;
    }

    public Color getColor() {
        return color;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean isEnteringCs() {
        return xPos == minCsX;
    }
    
    public boolean isLeavingCs() {
        return xPos == maxCsX;
    }
    
    public void remove(){
        if(this.color == Color.RED && xPos >= minCsX && xPos <= maxCsX){
            mon.exitReader();
        }
        if(this.color == Color.BLUE&& xPos >= minCsX && xPos <= maxCsX){
            mon.exitWriter();
        }
    }
}
