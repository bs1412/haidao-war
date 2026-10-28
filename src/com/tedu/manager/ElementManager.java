package com.tedu.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tedu.element.ElementObj;

public class ElementManager {
	private Map<GameElement,List<ElementObj>> gameElements;
	private int killCount=0;
	private int bossKillCount =0;
	
	public Map<GameElement, List<ElementObj>> getGameElements() {
		return gameElements;
	}
	public void addElement(ElementObj obj,GameElement ge) {
		gameElements.get(ge).add(obj);
	}
	public List<ElementObj> getElementsByKey(GameElement ge){
		return gameElements.get(ge);
	}	
	public void addKill() {
		killCount++;// 击杀数+1
	}
	public int getKillCount() {
		return killCount;
	}// 获取总击杀数
	public void addBossKill() {
	    bossKillCount++;
	}//boss击杀数
	public int getBossKillCount() {
	    return bossKillCount;
	}
	public void resetKillCount(){
	    killCount = 0;
	    bossKillCount=0;
	}//结束后重置击杀数
	private static ElementManager EM=null;
	public static synchronized ElementManager getManager() {
		if(EM == null) EM=new ElementManager();
		return EM;
	}
	private ElementManager() {
		init();
	}
	public void init() {
		gameElements=new HashMap<>();
		killCount = 0;
		for(GameElement ge:GameElement.values()) {
			gameElements.put(ge,new ArrayList<>());
		}
	}
	
	// 清空所有游戏元素
	public void clearAllElement(){
		for (GameElement key : gameElements.keySet()) {
	        gameElements.get(key).clear();
	    }
	}
	
}