package com.tedu.element;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import com.tedu.manager.GameLoad;

public class Boom extends ElementObj {
    private int liveFrame = 20;
    public Boom() {}
    @Override
    public ElementObj createElement(String str) {
        String[] split = str.split(",");
        for(String s:split) {
            String[] kv = s.split(":");
            switch(kv[0]) {
            case "x":setX(Integer.parseInt(kv[1]));break;
            case "y":setY(Integer.parseInt(kv[1]));break;
            }
        }
        setW(50);setH(50);
        return this;
    }
    @Override
    public void showElement(Graphics g) {
        ImageIcon icon = GameLoad.imgMap.get("boom");
        g.drawImage(icon.getImage(),getX(),getY(),getW(),getH(),null);
    }
    @Override
    protected void move() {
        liveFrame--;
        if(liveFrame<=0) setLive(false);
    }
}