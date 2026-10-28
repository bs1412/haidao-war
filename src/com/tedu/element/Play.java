package com.tedu.element;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;

public class Play extends ElementObj {
    private boolean left = false, up = false, right = false, down = false;
    private String fx = "up";
    private boolean pkType = false;
    private int moveSpeed = 3;
    private int hp = 88;//生命值
    private int shootCounter = 0;   // 记录已发射的普通子弹数量

    private boolean power32Active = false;
    private int power32Remain = 0;

    public Play(int x, int y, int w, int h, ImageIcon icon) {
        super(x, y, w, h, icon);
    }
    @Override
    public void showElement(Graphics g) {
        updateImage();
        g.drawImage(this.getIcon().getImage(),getX(),getY(),getW(),getH(),null);
    }
    @Override
    public void keyClick(boolean bl, int key) {
        if (bl) {
            switch (key) {
                case 37:left=true;fx="left";break;
                case 38:up=true;fx="up";break;
                case 39:right=true;fx="right";break;
                case 40:down=true;fx="down";break;
                case 32:pkType=true;break;
            }
        } else {
            switch (key) {
                case 37:left=false;break;
                case 38:up=false;break;
                case 39:right=false;break;
                case 40:down=false;break;
                case 32:pkType=false;break;
            }
        }
    }
    @Override
    protected void move() {
        if(left&&getX()>0) setX(getX()-moveSpeed);
        if(up&&getY()>0) setY(getY()-moveSpeed);
        if(right&&getX()<720-getW()) setX(getX()+moveSpeed);
        if(down&&getY()<600-getH()) setY(getY()+moveSpeed);
    }
    @Override
    protected void updateImage() {
        switch (fx) {
            case "up":setIcon(GameLoad.imgMap.get("playerShip_up"));break;
            case "left":setIcon(GameLoad.imgMap.get("playerShip_left"));break;
            case "right":setIcon(GameLoad.imgMap.get("playerShip_right"));break;
            case "down":setIcon(GameLoad.imgMap.get("playerShip_down"));break;
            default:setIcon(GameLoad.imgMap.get("playerShip_up"));
        }
    }
    @Override
    protected void add(long gameTime) {
        if(!pkType) return;
        pkType=false;
        //int bulletType = new java.util.Random().nextInt(3)==0 ?1:0;
        int bulletType;
        if (power32Active) {
            // 道具激活期间，全部发射超级子弹，且不改变计数器
            bulletType = 1;
        } else {
            // 正常循环：2普1超
            if (shootCounter < 2) {
                bulletType = 0;
                shootCounter++;
            } else {
                bulletType = 1;
                shootCounter = 0;
            }
        }
        ElementObj element = new PlayFile().createElement(getBulletParam(bulletType));
        ElementManager.getManager().addElement(element, GameElement.PLAYFILE);
    }
    public String getBulletParam(int bulletType) {
        int x=getX(),y=getY();
        switch(fx) {
        case "up":x += getW()/2 -5;break;
        case "left":y += getH()/2 -5;break;
        case "right":x += getW();y += getH()/2 -5;break;
        case "down":y += getH();x += getW()/2 -5;break;
        }
        return "x:"+x+",y:"+y+",f:"+fx+",t:"+bulletType;
    }
    public void beHit(int damage) {
        hp -= damage;
        if (hp <= 0) setLive(false);
    }
    public void addHp() {
        if(hp<5) hp++;
    }
    public int getHp() {return hp;}
    public void activePower32() {
        power32Active = true;
        power32Remain = 500;
    }
    public void updatePower32() {
        if(power32Active) {
            power32Remain--;
            if(power32Remain<=0) power32Active=false;
        }
    }
    public boolean isPower32Active() {return power32Active;}
    public float getPower32RemainSec() {
        return (float)power32Remain / 100;
    }
}