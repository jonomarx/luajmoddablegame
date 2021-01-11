/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.base;

import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * A basic wrapper for a game class. Written for "base3.lua"
 * @author Jon
 */
public class Game extends JPanel {
    private GameImpl impl;
    
    public Game(GameImpl impl) {
        this.impl = impl;
    }
    
    public void run() {
        impl.run();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        impl.paint(g);
    }
    
    public void update() {
        impl.update();
    }
}
