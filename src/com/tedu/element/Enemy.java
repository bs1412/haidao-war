package com.tedu.element;
import java.awt.Graphics;
import java.util.Random;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;
import com.tedu.show.GameJFrame;

public class Enemy extends ElementObj{
    private boolean isDestroyed = false;
    private int destroyRemainFrame = 150;
    private int speed;
    private int shootInterval;
    private int shootTimer;
    
 // BOSS专属字段
    private boolean isBoss = false;
    private int bossHp = 50;    // BOSS总血量
    private int bossMoveSpeed = 1;
    private boolean bossMoveDown = true; // BOSS上下移动方向
    private BossSkill bossSkill;
    
    @Override
    public void showElement(Graphics g) {
    	if(isBoss){
            // 根据当前血量切换BOSS图片
            String bossImgKey;
            if(bossHp > 128){
                bossImgKey = "boss0";
            }else if(bossHp > 68){
                bossImgKey = "boss1";
            }else if(bossHp > 0){
                bossImgKey = "boss2";
            }else{
                bossImgKey = "boss3";
            }
            g.drawImage(GameLoad.imgMap.get(bossImgKey).getImage(),getX(),getY(),getW(),getH(),null);
        }else {
        	if(isDestroyed) {
	            g.drawImage(GameLoad.imgMap.get("enemyShip_destroyed").getImage(),getX(),getY(),getW(),getH(),null);
	        }else{
	            g.drawImage(getIcon().getImage(),getX(),getY(),getW(),getH(),null);
	        }
        }
    	
        
    }
    
    //测试哪段让Boss消失了
    @Override
    public void setLive(boolean live) {
        if (isBoss && !live && bossHp > 0) {
            System.err.println("===== Boss 异常死亡！血量=" + bossHp + " =====");
            new Exception("调用堆栈").printStackTrace();
        }
        super.setLive(live);
    }
    
    
    @Override
    public ElementObj createElement(String str) {
        Random ran = new Random();
     //判断是否创建BOSS
        if(str.contains("boss")){
            isBoss = true;
            bossHp = 188;
//            System.out.println("BOSS 生成，初始血量=" + bossHp);
            setX(GameJFrame.GameX - 158);
            int randomY = ran.nextInt(GameJFrame.GameY - 160) + 50;
            setY(randomY);
            setW(102);setH(102);
            bossMoveDown = ran.nextBoolean();
            bossSkill = new BossSkill(this);
            return this;
        }
        
        int x = GameJFrame.GameX;
        int y = ran.nextInt(GameJFrame.GameY - 140);
        setX(x);setY(y);setW(50);setH(50);
        setIcon(GameLoad.imgMap.get("enemyShip"));
        speed = ran.nextInt(1)+1;
        shootInterval = ran.nextInt(401)+80;
        shootTimer = ran.nextInt(shootInterval);
        return this;
    }
    @Override
    protected void add(long gameTime) {
    	if (isBoss) {
            if (bossSkill != null) {
                bossSkill.update();
            }
            return;   // Boss 不发射普通子弹
    	}
        if(isBoss||isDestroyed) return;
        
        shootTimer++;
        if(shootTimer >= shootInterval) {
            shootTimer = 0;
            int bx = getX();
            int by = getY() + getH()/2 -5;
            ElementObj bullet = new EnemyFile().createElement("x:"+bx+",y:"+by+",f:left");
            ElementManager.getManager().addElement(bullet, GameElement.ENEMYFILE);
        }
    }
    public void hit() {
    	if(isBoss) return;
    	isDestroyed = true;
    }
 // BOSS受伤扣血
    public void bossBeHit(int damage){
        if(!isBoss || bossHp <= 0) return;
//        System.out.println("【扣血前】当前血量=" + bossHp + "，本次伤害=" + damage);
        bossHp -= damage;
//        System.out.println("【扣血后】剩余血量=" + bossHp);
        if(bossHp <= 0){
            ElementManager.getManager().addKill();
            ElementManager.getManager().addBossKill(); //Boss击杀+1
//            System.out.println("BOSS 死亡！");
        }
    }
    // 判断是否是BOSS
    public boolean isBoss(){
        return isBoss;
    }
    public int getBossHp(){
        return bossHp;
    }
    
    public boolean isDestroyed() {return isDestroyed;}
    @Override
    protected void move() {
        if(isBoss){
            if(bossHp > 0){  // 存活时上下移动
                if(bossMoveDown){
                    setY(getY() + bossMoveSpeed);
                    if(getY() + getH() > GameJFrame.GameY - 20) bossMoveDown = false;
                }else{
                    setY(getY() - bossMoveSpeed);
                    if(getY() < 20) bossMoveDown = true;
                }
            } else {          // 死亡后静止，等待倒计时结束
                destroyRemainFrame--;
                if(destroyRemainFrame <= 0) setLive(false);
            }
            return;
        }
    	//普通船
        if(!isDestroyed) {
            setX(getX()-speed);
            if(getX()+getW()<0) setLive(false);
        }else{
            destroyRemainFrame--;
            if(destroyRemainFrame<=0) setLive(false);
        }
    }
}