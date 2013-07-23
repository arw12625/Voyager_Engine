/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package script;

import game.GameObject;
import game.Manager;
import game.StandardManager;
import java.util.ArrayList;
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
        try {
            eval("importClass(Packages.game.Game)");
            eval("function quit() { Game.quit() }");
            eval("function exit() { Game.quit() }");
        } catch (ScriptException ex) {
            ex.printStackTrace();
        }
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
}
