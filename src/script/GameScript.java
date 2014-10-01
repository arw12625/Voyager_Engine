/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package script;

import game.GameObject;
import javax.script.ScriptContext;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ast.Scope;

/**
 *
 * @author Andy
 */
public class GameScript extends GameObject {

    private String script;
    private Scriptable scope;
    private boolean script_is_running;

    protected GameScript(String script) {
        this(script, null);
    }

    protected GameScript(String script, Scriptable scope) {
        this.script = script;
        setScope(scope);
    }

    @Override
    public void create() {
        super.create();
    }

    public String getScript() {
        return script;
    }

    public Scriptable getScope() {
        return scope;
    }

    public void setScope(Scriptable scope) {
        this.scope = scope;
        if (scope != null) {
            scope.put("this_script", scope, this);
        }
    }

    public void setRunning(boolean running) {
        script_is_running = running;
    }

    public boolean isRunning() {
        return script_is_running;
    }
}
