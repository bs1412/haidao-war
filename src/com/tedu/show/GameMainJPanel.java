package com.tedu.show;
import com.tedu.element.Enemy;
import java.awt.FontMetrics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.tedu.element.ElementObj;
import com.tedu.element.Play;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;

public class GameMainJPanel extends JPanel implements Runnable{
	private ElementManager em;
	// 开始按钮的点击区域
	private Rectangle startBtn;
	public com.tedu.controller.GameThread gameThread;
	
	public GameMainJPanel(com.tedu.controller.GameThread gameThread) {
		this.gameThread = gameThread;
		init();
		addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            if(gameThread == null) return;
	            int x = e.getX();
	            int y = e.getY();
	            // 未开始状态下，点击按钮启动游戏
	            if(!gameThread.isGameStart() && startBtn != null && startBtn.contains(x, y)){
	                gameThread.startGame();
	            }
	        }
	    });
	}
	public void init() {
		em = ElementManager.getManager();
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		// 绘制背景地图
		ImageIcon mapImg = GameLoad.imgMap.get("mapBg");
//		System.out.println("地图图片是否加载成功：" + (mapImg != null));
		if(mapImg != null){
			g.drawImage(mapImg.getImage(),0,0,GameJFrame.GameX,GameJFrame.GameY,null);
		}
        // 游戏未开始：只显示开始按钮，不刷船和UI
        if(gameThread != null && !gameThread.isGameStart()){
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // 按钮居中计算
            int btnW = 220;
            int btnH = 65;
            int btnX = (GameJFrame.GameX - btnW) / 2;
            int btnY = (GameJFrame.GameY - btnH) / 2;
            
            // 保存按钮位置，用于点击检测
            startBtn = new Rectangle(btnX, btnY, btnW, btnH);
            
            // 黄色按钮背景 + 橙色描边
            g2d.setColor(new Color(255, 215, 0));
            g2d.fillRoundRect(btnX, btnY, btnW, btnH, 18, 18);
            g2d.setColor(new Color(255, 165, 0));
            g2d.drawRoundRect(btnX, btnY, btnW, btnH, 18, 18);
            
            // 按钮文字
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("黑体", Font.BOLD, 30));
            String text = "开始游戏";
            int textW = g2d.getFontMetrics().stringWidth(text);
            g2d.drawString(text, btnX + (btnW - textW)/2, btnY + 42);
            
            // 直接返回，不绘制后面的敌人、血量等游戏内容
            return;
        }

		// 绘制所有游戏元素
		Map<GameElement, List<ElementObj>> all = em.getGameElements();
		for(GameElement ge:GameElement.values()) {
			List<ElementObj> list = all.get(ge);
			for(int i=0;i<list.size();i++) {
				ElementObj obj=list.get(i);
				obj.showElement(g);
			}
		}
	    // UI文字：血量、击杀、32特效倒计时
	    g.setColor(Color.WHITE);
		g.setFont(new Font("微软雅黑", Font.BOLD, 18));
		int hp = 0;
		List<ElementObj> playerList = em.getElementsByKey(GameElement.PLAY);
		if (!playerList.isEmpty()) {
			Play player = (Play) playerList.get(0);
			hp = player.getHp();
		}
		g.drawString("血量：" + hp, 25, 35);
		g.drawString("击败：" + em.getKillCount(), 25, 60);
		if (!playerList.isEmpty()) {
		    Play player = (Play) playerList.get(0);
		    if (player.isPower32Active()) {
		        g.setColor(Color.YELLOW);
		        g.drawString("超级横扫：" + String.format("%.1f", player.getPower32RemainSec()) + "s", 25, 85);
		    }
		}
		//Boss血条
		List<ElementObj> enemies = em.getElementsByKey(GameElement.ENEMY);
		for (ElementObj e : enemies) {
		    Enemy enemy = (Enemy) e;
		    if (enemy.isBoss() && enemy.getBossHp() > 0) {
		        int bossHp = enemy.getBossHp();
		        int maxHp = 188;
		        int barWidth = 200;
		        int barHeight = 20;
		        int x = (GameJFrame.GameX - barWidth) / 2; // 居中
		        int y = 30; // 顶部偏移

		        // 背景（灰色）
		        g.setColor(Color.GRAY);
		        g.fillRect(x, y, barWidth, barHeight);
		        // 血量（红色）
		        g.setColor(Color.RED);
//		        int currentWidth = (int) ((double) bossHp / maxHp * barWidth);
		        int currentWidth = Math.min((int) ((double) bossHp / maxHp * barWidth), barWidth);
		        g.fillRect(x, y, currentWidth, barHeight);
		        // 边框
		        g.setColor(Color.BLACK);
		        g.drawRect(x, y, barWidth, barHeight);
		        // 文字
		        g.setColor(Color.WHITE);
		        g.setFont(new Font("Arial", Font.BOLD, 14));
		        String text = "BOSS HP: " + bossHp + "/" + maxHp;
		        FontMetrics fm = g.getFontMetrics();
		        int textX = x + (barWidth - fm.stringWidth(text)) / 2;
		        int textY = y + (barHeight + fm.getAscent()) / 2 - 2;
		        g.drawString(text, textX, textY);
		        break; // 只绘制第一个 Boss
		    }
		}
		
		
	}
	@Override
	public void run() {
		while(true) {
			this.repaint();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}		
		}
	}
}