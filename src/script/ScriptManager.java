/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package script;

import game.GameObject;
import game.Manager;
import game.StandardManager;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author Andy
 */
public class ScriptManager extends StandardManager {

    ScriptEngine engine;
    ArrayList<GameScript> scripts;
    static ScriptManager instance;

    public static ScriptManager getInstance() {
        if (instance == null) {
            instance = new ScriptManager();
        }
        return instance;
    }

    @Override
    public void create() {
        super.create();
        scripts = new ArrayList<GameScript>();
        // create a script engine manager
        ScriptEngineManager factory = new ScriptEngineManager();
        // create a JavaScript engine
        this.engine = factory.getEngineByName("JavaScript");
        // evaluate JavaScript code from String
        loadStartupScripts();
    }

    private void loadStartupScripts() {
        loadAndExecute("Script.js");
    }

    public void eval(String command) throws ScriptException {
        engine.eval(command);
    }

    public void execute(GameScript s) throws ScriptException {
        if (s.getContext() != null) {
            engine.eval(s.getScript(), s.getContext());
        } else {
            engine.eval(s.getScript());
        }
    }

    @Override
    public boolean add(GameObject obj) {
        if(obj instanceof GameScript) {
            scripts.add((GameScript)obj);
            return true;
        }
        return false;
    }
    
    @Override
    public void remove(GameObject obj) {
        if(scripts.contains(obj)) {
            scripts.remove(obj);
        }
    }
    
    public GameScript loadScript(String path) {
        resource.TextResource text = new resource.TextResource(path);
        text.create();
        GameScript g = new GameScript(text.getTextString());
        g.create();
        add(g);
        return g;
    }
    
    public void loadAndExecute(String path) {
        try {
            execute(loadScript(path));
        } catch (ScriptException ex) {
            ex.printStackTrace();
        }
    }
}
