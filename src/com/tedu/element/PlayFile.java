package com.tedu.element;
import java.awt.Graphics;
import com.tedu.manager.GameLoad;
import com.tedu.show.GameJFrame;

public class PlayFile extends ElementObj{
	private int moveNum = 3;
	private String fx;
	private int type = 0;
	public PlayFile() {}
	@Override
	public ElementObj createElement(String str) {
		String[] split = str.split(",");
		for(String s : split) {
			String[] kv = s.split(":");
			switch(kv[0]) {
			case "x":setX(Integer.parseInt(kv[1]));break;
			case "y":setY(Integer.parseInt(kv[1]));break;
			case "f":fx=kv[1];break;
			case "t":type=Integer.parseInt(kv[1]);break;
			}
		}
		if(type == 1) {
			moveNum = 6;
			if("right".equals(fx) || "left".equals(fx)){
		        setW(84);
		        setH(44);
		    }else{
		        setW(44);
		        setH(84);
		    }
		} else {
			moveNum = 3;
			setW(10);
			setH(10);
		}
		return this;
	}
	@Override
	public void showElement(Graphics g) {
		String key = type==1 ? "superBullet":"playerBullet";
		if(type == 1){
			// 超级子弹：根据方向拼接图片key，和GameLoad的命名统一
			key = "superBullet_" + fx;
		}else{
			// 普通子弹固定一张图
			key = "playerBullet";
		}
		g.drawImage(GameLoad.imgMap.get(key).getImage(),getX(),getY(),getW(),getH(),null);
	}
	@Override
	protected void move() {
		if(getX()<0||getX()>GameJFrame.GameX||getY()<0||getY()>GameJFrame.GameY) {
			setLive(false);
			return;
		}
		switch(fx) {
		case "up":setY(getY()-moveNum);break;
		case "left":setX(getX()-moveNum);break;
		case "right":setX(getX()+moveNum);break;
		case "down":setY(getY()+moveNum);break;
		}
	}
	public int getType(){
	    return type;
	}
}