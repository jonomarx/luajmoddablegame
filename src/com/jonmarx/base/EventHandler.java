package com.jonmarx.base;

/**
 *
 * @author Jon
 */
public class EventHandler {
    private EventHandlerImpl impl;
    
    public EventHandler(EventHandlerImpl impl) {
        this.impl = impl;
    }
    
    public void addEventCondition(String condition, Runnable event) {
        impl.addEventCondition(condition, event);
    }
    
    public void removeEventCondition(Runnable event) {
        impl.removeEventCondition(event);
    }
    
    public void processEvent(String condition) {
        impl.processEvent(condition);
    }
}
