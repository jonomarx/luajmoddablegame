/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.core;

import java.util.Hashtable;

/**
 * Simple table with no security whatsoever.
 * @author Jon
 */
public class DefaultDatatableImpl implements DatatableImpl {

    public DefaultDatatableImpl() {
    }

    @Override
    public Object getValue(Hashtable<String, Object> table, String key) {
        return table.get(key);
    }

    @Override
    public void setValue(Hashtable<String, Object> table, String key, Object value) {
        table.put(key, value);
    }
}
