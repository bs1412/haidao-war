package com.tedu.controller;

import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.tedu.element.Boom;
import com.tedu.element.Bullet32;
import com.tedu.element.ElementObj;
import com.tedu.element.Enemy;
import com.tedu.element.EnemyFile;
import com.tedu.element.Play;
import com.tedu.element.PlayFile;
import com.tedu.element.Prop;
import com.tedu.element.Prop7;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;
import com.tedu.show.GameJFrame;

public class GameThread extends Thread{
	private ElementManager em;
	private boolean isGameRunning = true;
	private boolean isGameOver = false;
	private boolean isGameStart = false;
	private int bossTriggerCount = 0;   // 新增：累计击杀普通敌人数量
	
	public GameThread() {
		em=ElementManager.getManager();
	}
	
	@Override
	public void run() {
		while(isGameRunning) {
			if(isGameStart && !isGameOver) {
	            gameLoad();
	            gameRun();
	            gameOver();
	        }
	        try {
	            Thread.sleep(50);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
		}
	}

	private void gameLoad() {
		load();
	}

	private void gameRun() {
	    long gameTime = 0L;
	    int enemySpawnTimer = 0;
	    int propSpawnTimer = 0;
	    int prop7SpawnTimer = 0;
	    int bullet32Timer = 0;
	    java.util.Random random = new java.util.Random();

	    int enemySpawnInterval = 150;
	    final int MIN_SPAWN_INTERVAL = 30;
	    final int DIFFICULTY_STEP = 500;
	    final int MAX_ENEMY_COUNT = 12;
	    
	    // 击杀多少只普通船刷新BOSS
	    //final int BOSS_TRIGGER_KILL = 10;
//	    // 标记场上是否已经存在BOSS，防止重复生成
	    boolean hasBossOnMap = false;
	       // 标记是否需要生成Boss
	    

	    while(isGameRunning) {
//			测试打印boss信息
//	        List<ElementObj> enemyList = em.getElementsByKey(GameElement.ENEMY);
//	        System.out.println("当前敌人数量：" + enemyList.size());
//	        for (ElementObj e : enemyList) {
//	            Enemy en = (Enemy) e;
//	            if (en.isBoss()) {
//	                System.out.println("Boss 存在，血量=" + en.getBossHp() + ", isLive=" + en.isLive());
//	            }
//	        }
		    
	        
	        Map<GameElement, List<ElementObj>> all = em.getGameElements();
	        //每帧检测当前地图是否有存活BOSS
	        hasBossOnMap = false;
	        List<ElementObj> allEnemies = em.getElementsByKey(GameElement.ENEMY);
	        for(ElementObj e : allEnemies){
	            Enemy enemy = (Enemy)e;
	            if(enemy.isBoss() && enemy.getBossHp() > 0){
	                hasBossOnMap = true;
	                break;
	            }
	        }
	        moveAndUpdate(all, gameTime);
	        ElementPK();
	     
	        // 海盗船从左侧完全逃出地图，玩家扣1点血
	        List<ElementObj> enemies = em.getElementsByKey(GameElement.ENEMY);
	        for (int i = enemies.size() - 1; i >= 0; i--) {
	            ElementObj enemy = enemies.get(i);
	            if (enemy instanceof Enemy && ((Enemy) enemy).isBoss()) {
	                continue; // 跳过 Boss
	            }
	            if (enemy.getX() + enemy.getW() < 0) {
	                enemy.setLive(false);
	                List<ElementObj> playerList = em.getElementsByKey(GameElement.PLAY);
	                if (!playerList.isEmpty()) {
	                    Play player = (Play) playerList.get(0);
	                    if (player.isLive()) {
	                        player.beHit(1);
	                    }
	                }
	            }
	            
	        }

	        List<ElementObj> players = em.getElementsByKey(GameElement.PLAY);
	        if (!players.isEmpty()) {
	            Play player = (Play) players.get(0);
	            player.updatePower32();
	        }

	        // 敌人生成
	        if(isGameRunning) {
	        	enemySpawnTimer++;
	        	if (enemySpawnTimer >= enemySpawnInterval) {
	        		enemySpawnTimer = 0;
	        		int currentEnemyCount = em.getElementsByKey(GameElement.ENEMY).size();
	        		if (currentEnemyCount < MAX_ENEMY_COUNT) {
	        			em.addElement(new Enemy().createElement(""), GameElement.ENEMY);
	        		}	
	        	}
	        }
	        
//	        if(!hasBossOnMap && em.getKillCount() >= BOSS_TRIGGER_KILL){
//	            em.addElement(new Enemy().createElement("boss"), GameElement.ENEMY);
//	        }
	        

	        // 难度递增
	        if (gameTime > 0 && gameTime % DIFFICULTY_STEP == 0 
	                && enemySpawnInterval > MIN_SPAWN_INTERVAL) {
	            enemySpawnInterval -= 5;
	        }

	        // 回血道具
	        if(isGameRunning) {
	        	propSpawnTimer++;
	        	
		        if (propSpawnTimer >= 1000) {
		            propSpawnTimer = 0;
		            em.addElement(new Prop().createElement(""), GameElement.PROP);
		        }
			}
	        // 道具7
	        if(isGameRunning) {
	        	prop7SpawnTimer++;
		        if (prop7SpawnTimer >= 1500) {
		            prop7SpawnTimer = 0;
		            em.addElement(new Prop7().createElement(""), GameElement.PROP7);
		        }
	        }
	       

	        // 32子弹发射
	        if (isGameRunning && !players.isEmpty()) {
	            Play player = (Play) players.get(0);
	            if (player.isPower32Active()) {
	                bullet32Timer++;
	                if (bullet32Timer >= 20) {
	                    bullet32Timer = 0;
	                    int randomY = random.nextInt(GameJFrame.GameY - 140);
	                    em.addElement(new Bullet32().createElement("y:" + randomY), GameElement.BULLET32);
	                }
	            }
	        }
	        
	        boolean allDead = true;
	        for (ElementObj p : players) {
	            Play player = (Play) p;
	            if (player.isLive()) {
	                allDead = false;
	                break;
	            }
	        }
	        if (allDead) {
	            break; // 跳出内层while循环，去执行gameOver
	        }
	        gameTime++;
	        try {
	            sleep(10);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }
	}

    public void ElementPK() {
    	boolean needSpawnBoss = false;
    	
        // 玩家子弹打敌人
        List<ElementObj> enemys = em.getElementsByKey(GameElement.ENEMY);
        List<ElementObj> files = em.getElementsByKey(GameElement.PLAYFILE);
        for(int i=0;i<enemys.size();i++) {
            Enemy enemy = (Enemy) enemys.get(i);
            if (enemy.isDestroyed()) continue;
            if (enemy.isBoss() && enemy.getBossHp() <= 0) continue;
            for(int j=0;j<files.size();j++) {
                ElementObj file = files.get(j);
                if(enemy.pk(file)) {
                    file.setLive(false);
                    if(enemy.isBoss()){
                        // BOSS：只扣血，不执行普通船摧毁逻辑
                        PlayFile bullet = (PlayFile) file;
                        int dmg = bullet.getType() == 1 ? 3 : 1;
//                        System.out.println("普通子弹命中，类型=" + bullet.getType() + "，伤害=" + dmg);
                        enemy.bossBeHit(dmg);
                    }else {
                    	enemy.hit();
                    	em.addKill();
                    	//新增计数逻辑
                    	boolean hasBoss = false;
                        List<ElementObj> allEnemies = em.getElementsByKey(GameElement.ENEMY);
                        for (ElementObj e : allEnemies) {
                            Enemy en = (Enemy) e;
                            if (en.isBoss() && en.getBossHp() > 0) {
                                hasBoss = true;
                                break;
                            }
                        }
                        if (!hasBoss) {
                            bossTriggerCount++;
                            if (bossTriggerCount >= 10) {//多少只小海盗船生成一只boss
                                needSpawnBoss = true;
                                bossTriggerCount = 0;
                            }
                        }
                    }
                    int boomX = enemy.getX() + enemy.getW()/2 - 25;
                    int boomY = enemy.getY() + enemy.getH()/2 - 25;
                    em.addElement(new Boom().createElement("x:" + boomX + ",y:" + boomY), GameElement.DIE);
                    break;
                }
            }
        }
        

        // 敌方子弹打玩家
        List<ElementObj> enemyBullets = em.getElementsByKey(GameElement.ENEMYFILE);
        List<ElementObj> players = em.getElementsByKey(GameElement.PLAY);
        for (int i = 0; i < enemyBullets.size(); i++) {
            ElementObj bullet = enemyBullets.get(i);
            for (int j = 0; j < players.size(); j++) {
                Play player = (Play) players.get(j);
                if (!player.isLive()) continue;
                if (player.pk(bullet)) {
                	bullet.setLive(false);
                    int damage = 1;   // 默认伤害
                    // 判断是否为Boss子弹
                    if (bullet instanceof EnemyFile && ((EnemyFile) bullet).isFromBoss()) {
                        damage = 5;   // Boss 子弹伤害，可调整
                    }
                    player.beHit(damage);
                    break;
                }
            }
        }

        // 拾取回血道具
        List<ElementObj> props = em.getElementsByKey(GameElement.PROP);
        List<ElementObj> playerList = em.getElementsByKey(GameElement.PLAY);
        for (int i = 0; i < props.size(); i++) {
            ElementObj prop = props.get(i);
            for (int j = 0; j < playerList.size(); j++) {
                Play player = (Play) playerList.get(j);
                if (!player.isLive()) continue;
                if (player.pk(prop)) {
                    prop.setLive(false);
                    player.addHp();
                    break;
                }
            }
        }

        // 拾取道具7
        List<ElementObj> prop7List = em.getElementsByKey(GameElement.PROP7);
        for (int i = 0; i < prop7List.size(); i++) {
            ElementObj p7 = prop7List.get(i);
            for (int j = 0; j < playerList.size(); j++) {
                Play player = (Play) playerList.get(j);
                if (!player.isLive()) continue;
                if (player.pk(p7)) {
                    p7.setLive(false);
                    player.activePower32();
                    break;
                }
            }
        }

        // 32子弹打敌人
        List<ElementObj> b32List = em.getElementsByKey(GameElement.BULLET32);
        for (int i = 0; i < b32List.size(); i++) {
            ElementObj b32 = b32List.get(i);
            for (int j = 0; j < enemys.size(); j++) {
                Enemy enemy = (Enemy) enemys.get(j);
                if (enemy.isDestroyed()) continue;
                if (enemy.isBoss() && enemy.getBossHp() <= 0) continue;
                if (b32.pk(enemy)) {
                    b32.setLive(false);
                    if(enemy.isBoss()){
//                    	System.out.println("32子弹命中，伤害=5");
                    	enemy.bossBeHit(2);
                        
                    }else {
	                    enemy.hit();
	                    em.addKill();
	                    //新增计数逻辑
	                    boolean hasBoss = false;
	                    List<ElementObj> allEnemies = em.getElementsByKey(GameElement.ENEMY);
	                    for (ElementObj e : allEnemies) {
	                        Enemy en = (Enemy) e;
	                        if (en.isBoss() && en.getBossHp() > 0) {
	                            hasBoss = true;
	                            break;
	                        }
	                    }
	                    if (!hasBoss) {
	                        bossTriggerCount++;
	                        if (bossTriggerCount >=10 ) {
	                            needSpawnBoss = true;
	                            bossTriggerCount = 0;
	                        }
	                    }
                    }
                    
                    int boomX = enemy.getX() + enemy.getW()/2 - 25;
                    int boomY = enemy.getY() + enemy.getH()/2 - 25;
                    em.addElement(new Boom().createElement("x:" + boomX + ",y:" + boomY), GameElement.DIE);
                    break;
                }
            }
        }
        
        if (needSpawnBoss) {
            boolean hasBoss = false;
            List<ElementObj> allEnemies = em.getElementsByKey(GameElement.ENEMY);
            for (ElementObj e : allEnemies) {
                Enemy en = (Enemy) e;
                if (en.isBoss() && en.getBossHp() > 0) {
                    hasBoss = true;
                    break;
                }
            }
            if (!hasBoss) {
                em.addElement(new Enemy().createElement("boss"), GameElement.ENEMY);
            }
        }
        
        
    }

	public void moveAndUpdate(Map<GameElement, List<ElementObj>> all,long gameTime) {
		for(GameElement ge:GameElement.values()) {
			List<ElementObj> list = all.get(ge);
			for(int i=list.size()-1;i>=0;i--){	
				ElementObj obj=list.get(i);
				if(!obj.isLive()) {
					obj.die();
					list.remove(i);
					continue;
				}
				obj.model(gameTime);
			}
		}	
	}
	//结束弹窗显示得分加击杀
	private void gameOver() {
		List<ElementObj> players = em.getElementsByKey(GameElement.PLAY);
	    boolean allPlayerDead = true;
	    for (ElementObj p : players) {
	        Play player = (Play) p;
	        if (player.isLive()) {
	            allPlayerDead = false;
	            break;
	        }
	    }
	 // 玩家全部阵亡且游戏还在运行时，弹出结束选择框
	    if (allPlayerDead && isGameRunning) {
	    	isGameOver = true;
	        int totalKill = em.getKillCount();
	        // 自定义两个按钮的文字
	        Object[] btnOptions = {"再玩一局", "退出游戏"};
	        // 先清空所有元素，避免弹窗期间还在刷新
	        em.clearAllElement();
	        
	        int bossKill =em.getBossKillCount();
	        int score = totalKill +bossKill * 60;
	        JLabel msgLabel = new JLabel("<html><body style='text-align:center'>击败BOSS:"+bossKill+"<br>总击杀：" + totalKill + "<br>本次得分：" + score + "</body></html>");
	        msgLabel.setHorizontalAlignment(JLabel.CENTER);

	        int select = JOptionPane.showOptionDialog(
	                null,
	                msgLabel,  // 这里换成上面的JLabel对象
	                "游戏结束",
	                JOptionPane.DEFAULT_OPTION,
	                JOptionPane.PLAIN_MESSAGE,
	                null,
	                btnOptions,
	                btnOptions[0]
	        );

	        if (select == 0) {
	        	// 再玩一局：重置击杀数，外层循环会自动重新加载游戏
	        	isGameOver = false;
	        	isGameStart = false;
	        	em.resetKillCount();
	        } else {
	            // 退出游戏
	            isGameRunning = false;
	            System.exit(0);
	        }
	    }
	}
	//结束开始状态
	public boolean isGameRunning() {
	    return isGameRunning;
	}
	public boolean isGameOver() {
	    return isGameOver;
	}
	public boolean isGameStart() {
	    return isGameStart;
	}

	// 点击开始按钮后调用，正式启动游戏
	public void startGame() {
	    isGameStart = true;
	}
	
	public void load() {
		bossTriggerCount = 0;
	    ImageIcon icon = GameLoad.imgMap.get("playerShip_right");
	    ElementObj obj = new Play(100, 100, 50, 50, icon);
	    em.addElement(obj, GameElement.PLAY);
	    em.addElement(new Enemy().createElement(""), GameElement.ENEMY);
	}
}