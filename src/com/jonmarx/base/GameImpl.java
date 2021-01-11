/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.base;

import java.awt.Graphics;

/**
 * Interface of an implementaion of "Game", an example is in "base3.lua"
 * @author Jon
 */
public interface GameImpl {
    public void run();
    public void paint(Graphics g);
    public void update();
}
