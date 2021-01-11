package com.jonmarx.core;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.Properties;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

/**
 * The Main Program. cannot be overwritten at all, 
 * performs basic modloading
 * (optional) table loading
 * initial datatable filling
 * 2nd stage datatable filling
 * 
 * and runtime mods (like objects in a game)
 * @author Jon
 */
public class Main {
    private static DatatableManager table;
    
    public static void main(String[] args) throws IOException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
        table = new DatatableManager(new DefaultDatatableImpl());
        
        Properties prop = new Properties();
        String file = "/config/config.properties";
        
        // find where THIS JAR is at
        String decodedPath;
        try {
            String[] path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath().split("/");
            path[path.length - 1] = "";
            String path2 = "";
            for(String str : path) {
                path2 += str;
                path2 += "/";
            }
            decodedPath = URLDecoder.decode(path2, "UTF-8");
        } catch(Exception e) {
            ByteArrayOutputStream stacktrace = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(stacktrace));
            JOptionPane.showMessageDialog(null, stacktrace.toString());
            decodedPath = "";
        }
        
        String externalConfig = decodedPath + "mods.properties";
        
        //externalConfig = "mods.properties";
        
        File config = new File(externalConfig);
        if(!config.exists()) {
            try {
                config.createNewFile();
            } catch(Exception e) {
                ByteArrayOutputStream stacktrace = new ByteArrayOutputStream();
                e.printStackTrace(new PrintStream(stacktrace));
                JOptionPane.showMessageDialog(null, stacktrace.toString());
            }
            loadConfig(config);
        }
        
        Properties externalProp = new Properties();
        externalProp.load(new FileInputStream(config));
        
        try {
            prop.load(Main.class.getResourceAsStream(file));
        } catch (Exception ex) {
            System.err.println("properties couldn't be loaded at " + file);
            System.exit(1);
        }
        
        // do a test if it should not use defaults
        if(prop.get("allowDatatableOverride") == "true") {
            JOptionPane.showMessageDialog(null, "in the next frame you will select a new database mod, press cancel to cancel");
        } else if(!prop.get("allowDatatableOverride").equals("false")) {
            System.err.println("allowDatatableOverride is a invalid value in " + file);
            System.exit(2);
        }
        
        // establish globals
        Globals globals = JsePlatform.standardGlobals();
        globals.set("table", CoerceJavaToLua.coerce(table));
        
        // load java headers
        if(externalProp.get("javaheader").equals("0")) {
            String mods = guiLoadMods("Please select mods for java class loading, press cancel to stop", decodedPath);
            if(mods.equals("")) mods = "null";
            externalProp.setProperty("javaheader", mods);
        }
        String[] jars = (externalProp.get("javaheader").equals("null") ? new String[0] : ((String) externalProp.get("javaheader")).split(","));
        URL[] jarloc = new URL[jars.length];
        for(int i = 0; i < jars.length; i++) {
            jarloc[i] = new File(jars[i]).toURI().toURL();
        }
        URLClassLoader newLoader = new URLClassLoader(jarloc, globals.getClass().getClassLoader());
        Thread.currentThread().setContextClassLoader(newLoader);
        
         
        Field scl = ClassLoader.class.getDeclaredField("scl"); // Get system class loader
        scl.setAccessible(true); // Set accessible
        scl.set(null, newLoader);
        
        // !MAKE SURE TO LOAD WITH "globals"!
        LuaValue[] chunks;
        if(externalProp.get("load1").equals("0")) {
            String mods = guiLoadMods("Please select mods for phase-1 loading, press cancel to stop", decodedPath);
            if(mods.equals("")) mods = "null";
            externalProp.setProperty("load1", mods);
        }
        
        chunks = loadMods(((String) externalProp.get("load1")).split(","), globals);
        
        // load initializer mods
        for(LuaValue chunk : chunks) {
            executeChunk(chunk, newLoader);
        }
        // !MAKE SURE TO LOAD WITH "globals"!
        if(externalProp.get("load2").equals("0")) {
            String mods = guiLoadMods("Please select mods for phase-2 loading, press cancel to stop", decodedPath);
            if(mods.equals("")) mods = "null";
            externalProp.setProperty("load2", mods);
        }
        
        chunks = loadMods(((String) externalProp.get("load2")).split(","), globals);
        
        // load post-initializer mods
        for(LuaValue chunk : chunks) {
            executeChunk(chunk, newLoader);
        }
        
        // !MAKE SURE TO LOAD WITH "globals"!
        if(externalProp.get("load3").equals("0")) {
            String mods = guiLoadMods("Please select mods for phase-3 loading, press cancel to stop", decodedPath);
            if(mods.equals("")) mods = "null";
            externalProp.setProperty("load3", mods);
        }
        
        chunks = loadMods(((String) externalProp.get("load3")).split(","), globals);
        
        // load runtime mods
        for(LuaValue chunk : chunks) {
            executeChunk(chunk, newLoader);
        }
        
        externalProp.store(new FileOutputStream(config), "");
    }
    
    public static void loadConfig(File file) throws FileNotFoundException {
        PrintStream out = new PrintStream(new FileOutputStream(file));
        String[] lines = {"# this is the configuration file for mod order, please change all mod values to 0 to reset them and change at runtime", "load1 = 0", "load2 = 0", "load3 = 0", "javaheader = null"};
        for(String line : lines) {
            out.println(line);
        }
        out.close();
    }
    
    public static LuaValue[] loadMods(String[] files, Globals globals) {
        if(files[0].equals("null")) return new LuaValue[0];
        LuaValue[] values = new LuaValue[files.length];
        for(int i = 0; i < files.length; i++) {
            values[i] = globals.loadfile(files[i]);
        }
        return values;
    }
    
    public static String guiLoadMods(String initmessage, String defaultpath) {
        if(!initmessage.equals("")) JOptionPane.showMessageDialog(null, initmessage);
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(defaultpath));
        String files = "";
        while(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            files += ",";
            files += chooser.getSelectedFile().getPath();
        }
        if(files.equals("")) return "";
        return files.substring(1);
    }
    
    public static void executeChunk(LuaValue chunk, ClassLoader cl) {
        Thread t = new Thread(new Runnable() {
           @Override
           public void run() {
               chunk.call();
           } 
        });
        t.start();
    }
}