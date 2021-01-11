/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.core;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;

/**
 * A basic Mod wrapper class
 * accepts jars or lua scripts
 * (jars WIP)
 * @author Jon
 */
public class Mod {
    private String location;
    private boolean isLua;
    
    public Mod(String location, boolean isLua) {
        this.location = location;
        this.isLua = isLua;
    }
    
    public void load(DatatableManager table, Globals globals) {
        if(!isLua) {
            System.err.println("forwarding mod load to java method");
            load(table);
        }
        LuaValue chunk = globals.loadfile(location);
        chunk.call();
    }
    
    public void load(DatatableManager table) {
        // WIP
    }
}
