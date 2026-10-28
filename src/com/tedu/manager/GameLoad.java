package com.tedu.manager;

import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;

public class GameLoad {
	public static Map<String,ImageIcon> imgMap;
	static {
		imgMap=new HashMap<>();
		imgMap.put("mapBg", new ImageIcon("image/ship/map.png"));
		imgMap.put("playerShip_up", new ImageIcon("image/ship/play1/ship (1)3.png"));
		imgMap.put("playerShip_down", new ImageIcon("image/ship/play1/ship (1)1.png"));
		imgMap.put("playerShip_left", new ImageIcon("image/ship/play1/ship (1)2.png"));
		imgMap.put("playerShip_right", new ImageIcon("image/ship/play1/ship (1).png"));
		imgMap.put("prop7", new ImageIcon("image/ship/play1/7.png"));
		imgMap.put("bullet32", new ImageIcon("image/ship/play1/32.png"));
		imgMap.put("hpProp", new ImageIcon("image/ship/play1/dinghyLarge1.png"));
		imgMap.put("playerBullet", new ImageIcon("image/ship/play1/paodan.png"));
		imgMap.put("superBullet_right", new ImageIcon("image/ship/play1/superpaodan.png"));
		imgMap.put("superBullet_down", new ImageIcon("image/ship/play1/superpaodan3.png"));
		imgMap.put("superBullet_left", new ImageIcon("image/ship/play1/superpaodan2.png"));
		imgMap.put("superBullet_up", new ImageIcon("image/ship/play1/superpaodan1.png"));
		imgMap.put("enemyShip", new ImageIcon("image/ship/play2/ship (3).png"));
		imgMap.put("enemyShip_destroyed", new ImageIcon("image/ship/play2/ship (15).png"));
		imgMap.put("enemyBullet", new ImageIcon("image/ship/play2/paodan2.png"));
		imgMap.put("boom", new ImageIcon("image/boom/boom.png"));
		// BOSSËÄ½×¶ÎÍ¼Æ¬
		imgMap.put("boss0", new ImageIcon("image/ship/play2/ship(20).png")); // ÂúÑª50
		imgMap.put("boss1", new ImageIcon("image/ship/play2/ship(21).png")); // ²ÐÑª30
		imgMap.put("boss2", new ImageIcon("image/ship/play2/ship(22).png")); // ÖØÉË10
		imgMap.put("boss3", new ImageIcon("image/ship/play2/ship(23).png")); // ´Ý»Ù0
		
		imgMap.put("bossBullet", new ImageIcon("image/ship/play2/paodan3.png")); 
	}
}