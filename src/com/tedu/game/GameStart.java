package com.tedu.game;

import com.tedu.controller.GameListener;
import com.tedu.controller.GameThread;
import com.tedu.show.GameJFrame;
import com.tedu.show.GameMainJPanel;

public class GameStart {
    public static void main(String[] args) {
        GameJFrame gj = new GameJFrame();
        GameThread th = new GameThread();
        GameMainJPanel jp = new GameMainJPanel(th);
        GameListener listener = new GameListener();

        gj.setjPanel(jp);
        gj.setKeyListener(listener);
        gj.setThead(th);
        gj.start();

    }
}