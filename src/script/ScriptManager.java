/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package script;

import game.GameObject;
import game.Manager;
import game.StandardManager;
import java.util.ArrayList;
import java.util.HashMap;
import org.mozilla.javascript.*;
import org.mozilla.javascript.ast.Scope;
import util.DebugMessages;

/**
 *
 * @author Andy
 */
public class ScriptManager extends StandardManager {

    ArrayList<GameScript> scripts;
    ArrayList<GameScript> scriptsToRun;
    HashMap<String, Context> contexts;
    Scriptable globalScope;
    Function setCurrent;
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
        scriptsToRun = new ArrayList<GameScript>();

        contexts = new HashMap<String, Context>();

        Context cx = Context.enter();
        globalScope = new ImporterTopLevel(cx);
        eval("var currentObject; function setCurrentObject(obj) { currentObject = obj; }");
        setCurrent = (Function) globalScope.get("setCurrentObject", globalScope);

        cx.exit();


        // evaluate JavaScript code from String
        //loadStartupScripts();
    }

    public void loadStartupScripts() {
        loadAndExecute("Script.js");
    }

    public void eval(String command, Scriptable scope) {
        Context c = Context.enter();
        c.evaluateString(scope, command, "yolo", 1, null);
        c.exit();
    }

    public void eval(String command) {
        eval(command, globalScope);
    }

    public void execute(GameScript s) {
        try {
            if (s.getScope() == null) {
                eval(s.getScript());
            } else {
                eval(s.getScript(), s.getScope());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean add(GameObject obj) {
        if (obj instanceof GameScript) {
            scripts.add((GameScript) obj);
            return true;
        }
        return false;
    }

    @Override
    public void remove(GameObject obj) {
        if (scripts.contains(obj)) {
            scripts.remove(obj);
        }
    }

    public GameScript loadScript(String path) {
        resource.TextResource text = new resource.TextResource(path);
        text.create();
        System.out.println("LOADING SCRIPT: " + path);
        while (text.isLoaded() == false) {
            Thread.yield();
        }
        GameScript g = new GameScript(text.getTextString());
        g.create();
        add(g);
        return g;
    }

    public void loadAndExecute(String path) {
        scriptsToRun.add(loadScript(path));
    }

    public void executeScripts() {
        ArrayList<GameScript> scriptsCopy = new ArrayList<GameScript>(scriptsToRun);
        for (GameScript gs : scriptsCopy) {
            System.out.println("Script Start" + gs.getFullName());
            execute(gs);
            System.out.println("Script End");
            scriptsToRun.remove(gs);
        }
    }

    public boolean hasExecutables() {
        return !scriptsToRun.isEmpty();
    }

    public Object runScriptFunc(GameScript s, String func, Object[] args) {
        Context cx = Context.enter();
        Object result = null;
        try {
            Function fct = (Function) globalScope.get(func, globalScope);
            result = fct.call(cx, globalScope, globalScope, args);
            //System.out.println(result);
        } catch (Exception e) {
        } finally {
            cx.exit();
        }
        return result;
    }

    public void setCurrentObject(GameObject current) {
        Context c = Context.enter();
        setCurrent.call(c, globalScope, globalScope, new Object[]{current});
        c.exit();
    }
}
