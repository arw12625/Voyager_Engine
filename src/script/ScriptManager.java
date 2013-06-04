/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package script;

import game.Manager;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author Andy
 */
public class ScriptManager implements Manager {

    ScriptEngine engine;
    static ScriptManager instance;

    public static ScriptManager getInstance() {
        if (instance == null) {
            instance = new ScriptManager();
        }
        return instance;
    }

    @Override
    public void create() {
        // create a script engine manager
        ScriptEngineManager factory = new ScriptEngineManager();
        // create a JavaScript engine
        this.engine = factory.getEngineByName("JavaScript");
        // evaluate JavaScript code from String
        loadStartupScripts();
    }

    @Override
    public void destroy() {
    }

    @Override
    public void update(int delta) {
    }

    @Override
    public String getName() {
        return "ScriptManager";
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
}
