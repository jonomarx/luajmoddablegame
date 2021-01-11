/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.base;

/**
 * Event handler for "base3.lua", handles events
 * @see Game
 * @author Jon
 */
public interface EventHandlerImpl {
    public void addEventCondition(String condition, Runnable event);
    public void removeEventCondition(Runnable event);
    public void processEvent(String condition);
}
