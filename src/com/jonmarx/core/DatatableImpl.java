/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.core;

import java.util.Hashtable;

/**
 * Allows to configure "permissions" onto the hashtable so some values can or cannot be edited
 * Making this an interface allows it to be made in lua or java
 * @author Jon
 */
public interface DatatableImpl {
    public Object getValue(Hashtable<String, Object> table, String key);
    public void setValue(Hashtable<String, Object> table, String key, Object value);
}
