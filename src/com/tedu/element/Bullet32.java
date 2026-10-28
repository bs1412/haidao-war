package com.tedu.element;
import java.awt.Graphics;
import com.tedu.manager.GameLoad;
import com.tedu.show.GameJFrame;

public class Bullet32 extends ElementObj{
    private int moveNum = 6;
    public Bullet32() {}
    @Override
    public ElementObj createElement(String str) {
        String[] split = str.split(",");
        int y=0;
        for(String s:split) {
            String[] kv = s.split(":");
            if(kv[0].equals("y")) y=Integer.parseInt(kv[1]);
        }
        setX(0);setY(y);setW(30);setH(30);
        return this;
    }
    @Override
    public void showElement(Graphics g) {
        g.drawImage(GameLoad.imgMap.get("bullet32").getImage(),getX(),getY(),getW(),getH(),null);
    }
    @Override
    protected void move() {
        if(getX()>GameJFrame.GameX) {
            setLive(false);
            return;
        }
        setX(getX()+moveNum);
    }
}