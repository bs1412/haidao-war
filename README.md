# 海盗大战 🏴‍☠️

一个用 Java Swing 实现的 2D 射击小游戏：控制战机移动、发射炮弹、击败敌人和 Boss、拾取道具。

> Java 实训课程项目（Eclipse 工程）。

## ✨ 玩法

- 键盘操控战机移动与射击
- 多种敌人与 Boss，包含 Boss 技能
- 子弹、道具系统，碰撞判定与血量

## 🛠️ 技术点

- Java Swing 图形界面
- 游戏主循环（GameThread）与键盘事件监听
- 面向对象设计：元素基类 + 玩家 / 敌人 / Boss / 道具派生类
- 元素管理器与图片资源加载

## 📂 目录结构

- `src/com/tedu/` —— 源码
- `image/` —— 图片素材
- `.classpath` / `.project` —— Eclipse 工程文件

## 🚀 如何运行

用 Eclipse 或 IntelliJ IDEA 导入工程，运行入口类 `com.tedu.game.GameStart`。
