package com.tedu.element;
import java.awt.Graphics;
import java.util.Random;
import com.tedu.manager.GameLoad;
import com.tedu.show.GameJFrame;

public class Prop7 extends ElementObj{
    private int speed = 2;
    public Prop7() {}
    @Override
    public ElementObj createElement(String str) {
        Random ran = new Random();
        int x = GameJFrame.GameX;
        int y = ran.nextInt(GameJFrame.GameY - 40);
        setX(x);setY(y);setW(36);setH(36);
        return this;
    }
    @Override
    public void showElement(Graphics g) {
        g.drawImage(GameLoad.imgMap.get("prop7").getImage(),getX(),getY(),getW(),getH(),null);
    }
    @Override
    protected void move() {
        setX(getX()-speed);
        if(getX()+getW()<0) setLive(false);
    }
}