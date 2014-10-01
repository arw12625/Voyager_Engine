/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package script;

import game.GameObject;
import game.StandardManager;
import java.util.ArrayList;
import org.mozilla.javascript.*;
import util.DebugMessages;

/**
 *
 * @author Andy
 */
public class ScriptManager extends StandardManager {

    ArrayList<GameScript> scripts;
    ArrayList<GameScript> scriptsToRun;
    Context context;
    public Scriptable globalScope;
    Function setCurrent;
    static ScriptManager instance;
    private GameScript[] startupScripts;

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

        context = Context.enter();

        globalScope = new ImporterTopLevel(context);

        eval("var currentObject; function setCurrentObject(obj) { currentObject = obj; }", globalScope);
        setCurrent = (Function) globalScope.get("setCurrentObject", globalScope);

        String[] packageNames = {"game", "graphics", "input", "physics", "resource", "script", "sound", "state", "test", "update", "util"};
        for (String packageName : packageNames) {
            eval("var " + packageName + " = JavaImporter(Packages." + packageName + ")", globalScope);
        }

        context.exit();


        // evaluate JavaScript code from String
        String[] startupPaths = {"Script.js"};
        startupScripts = new GameScript[startupPaths.length];
        for (int i = 0; i < startupScripts.length; i++) {
            startupScripts[i] = loadGlobalScript("engine_scripts/" + startupPaths[i]);
        }
        loadStartupScripts();
    }

    public void loadStartupScripts() {
        for (GameScript gs : startupScripts) {
            execute(gs);
        }
    }

    private boolean startupFinished() {
        boolean finished = true;
        for (GameScript gs : startupScripts) {
            finished = finished && !gs.isRunning();
        }
        return finished;
    }

    private void eval(String command, Scriptable scope) {
        eval("no_name", command, scope);
    }

    private void eval(String name, String command, Scriptable scope) {
        Context c = Context.enter();
        try {
            c.evaluateString(scope, command, "yolo", 0, null);
        } catch (Exception e) {
            System.err.println(command);
            e.printStackTrace();//Console.getInstance().write(e.getMessage());
        } finally {
            c.exit();
        }
    }

    public void evaluateText(String text, Scriptable scope) {
        eval("text_script", text, scope);
    }

    private void execute(GameScript s) {
        try {
            eval(s.getFullName(), s.getScript(), (Scriptable) s.getScope());
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

    public GameScript loadScriptFromText(String script) {
        GameScript gs = new GameScript(script, globalScope);
        gs.create();
        add(gs);
        return gs;
    }
    
    public GameScript loadScript(String path, Scriptable scope) {
        resource.TextResource text = new resource.TextResource(path);
        text.create();
        while (text.isLoaded() == false) {
            Thread.yield();
        }
        GameScript g = new GameScript(addScriptWrapper(text.getTextString()), scope);
        g.create();
        add(g);
        return g;
    }

    public Scriptable getChildScope(Scriptable parent) {
        Context c = Context.enter();
        Scriptable newScope = c.newObject(parent);
        newScope.setPrototype(parent);
        newScope.setParentScope(null);
        c.exit();
        return newScope;
    }

    public GameScript loadScript(String path) {
        return loadScript(path, getChildScope(globalScope));
    }

    public GameScript loadGlobalScript(String path) {
        return loadScript(path, globalScope);
    }

    public void loadAndExecute(String path) {
        scriptsToRun.add(loadScript(path));
    }

    public void executeScripts() {
        ArrayList<GameScript> scriptsCopy = new ArrayList<GameScript>(scriptsToRun);
        for (GameScript gs : scriptsCopy) {
            //System.out.println("Script Start" + gs.getFullName());
            execute(gs);
            //System.out.println("Script End");
            scriptsToRun.remove(gs);
        }
    }

    public Object runScriptFunc(GameScript s, String func, Object[] args) {
        Context cx = Context.enter();
        Object result = null;
        try {
            Function fct = (Function) s.getScope().get(func, s.getScope());
            result = fct.call(cx, s.getScope(), s.getScope(), args);
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

    private String addScriptWrapper(String textString) {
        String header = "this_script.setRunning(true);";
        String footer = "this_script.setRunning(false);";
        return header + textString + footer;
    }

    public void addToQueue(GameScript gs) {
        scriptsToRun.add(gs);
    }
}
