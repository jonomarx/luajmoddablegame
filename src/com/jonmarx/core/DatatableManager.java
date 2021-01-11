/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.core;

import java.util.Hashtable;

/**
 * Manages the public data table for mods, forwards responsibility to either java or lua implementation.
 * @author Jon
 */
public class DatatableManager {
    private Hashtable<String, Object> datatable = new Hashtable<>();
    private DatatableImpl impl;
    
    public DatatableManager(DatatableImpl impl) {
        this.impl = impl;
    }
    
    public Object getValue(String key) {
        return impl.getValue(datatable, key);
    }
    
    public void setValue(String key, Object value) {
        impl.setValue(datatable, key, value);
    }
}
