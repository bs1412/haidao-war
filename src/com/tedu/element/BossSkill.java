package com.tedu.element;

import java.awt.Graphics;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;

public class BossSkill extends ElementObj {
    private Enemy boss;
    private int cooldown = 350;
    private int currentCooldown = 0;

    public BossSkill(Enemy boss) {
        this.boss = boss;
        setX(0); setY(0); setW(0); setH(0);
    }

    @Override
    public void showElement(Graphics g) {
        // 技能本身不绘制
    }

    public void update() {
        if (currentCooldown > 0) {
            currentCooldown--;
            return;
        }
        fireFanBullets();
        currentCooldown = cooldown;
    }

    private void fireFanBullets() {
        int bx = boss.getX();
        int by = boss.getY() + boss.getH() / 2;
        int bulletCount = 5;
        int spreadAngle = 100;       // 扇形总角度（度）
        int speed = 5;              // 子弹速度
        
        

        for (int i = 0; i < bulletCount; i++) {
            // 计算角度：从 -spreadAngle/2 到 spreadAngle/2
            double angle = Math.toRadians(-spreadAngle/2 + i * (spreadAngle / (bulletCount - 1)));
            // 计算水平、垂直速度分量（向左为负x）
            int dx = (int)(-speed * Math.cos(angle));
            int dy = (int)(speed * Math.sin(angle));

            
            EnemyFile bullet = (EnemyFile) new EnemyFile().createElement(
                    "x:" + bx + ",y:" + by + ",img:bossBullet,dx:" + dx + ",dy:" + dy
                );
                //在这里标记为 Boss 子弹
                bullet.setFromBoss(true);
            
            bullet.setW(60);
            bullet.setH(40);
            ElementManager.getManager().addElement(bullet, GameElement.ENEMYFILE);
        }
//        System.out.println("Boss 释放扇形子弹！");
    }
}