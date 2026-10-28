package com.tedu.element;
import java.awt.Graphics;
import com.tedu.manager.GameLoad;
import com.tedu.show.GameJFrame;

public class EnemyFile extends ElementObj{
	private int moveNum = 3;
	private String fx = "left";
	private boolean fromBoss = false;//子弹是否boss发射
	
    private String imageKey = null;   // 自定义图片key
    private int dx = 0;               // 水平速度（负：向左）
    private int dy = 0;               // 垂直速度（负：向上）
	
	public EnemyFile() {}
	@Override
	public ElementObj createElement(String str) {
		String[] split = str.split(",");
		for(String s:split) {
			String[] kv = s.split(":");
			switch(kv[0]) {
			case "x":setX(Integer.parseInt(kv[1]));break;
			case "y":setY(Integer.parseInt(kv[1]));break;
			case "f":fx=kv[1];break;
            case "img": imageKey = kv[1]; break;   // 新增
            case "dx":  dx = Integer.parseInt(kv[1]); break;
            case "dy":  dy = Integer.parseInt(kv[1]); break;
			}
		}
		setW(10);setH(10);
		return this;
	}
	@Override
	public void showElement(Graphics g) {
		// 优先使用自定义图片
        if (imageKey != null) {
            g.drawImage(GameLoad.imgMap.get(imageKey).getImage(), getX(), getY(), getW(), getH(), null);
            return;
        }
        // 默认使用普通敌弹
        g.drawImage(GameLoad.imgMap.get("enemyBullet").getImage(), getX(), getY(), getW(), getH(), null);
	}
	@Override
	protected void move() {
		if(getX()<0||getX()>GameJFrame.GameX||getY()<0||getY()>GameJFrame.GameY) {
			setLive(false);
			return;
		}
		if (dx != 0 || dy != 0) {
            setX(getX() + dx);
            setY(getY() + dy);
        } else {
        	switch(fx) {
				case "up":setY(getY()-moveNum);break;
				case "left":setX(getX()-moveNum);break;
				case "right":setX(getX()+moveNum);break;
				case "down":setY(getY()+moveNum);break;
		    }
		
		}
	}
	public void setFromBoss(boolean fromBoss) {
	    this.fromBoss = fromBoss;
	}

	public boolean isFromBoss() {
	    return fromBoss;
	}
}