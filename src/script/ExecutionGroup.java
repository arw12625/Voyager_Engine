/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package script;

import java.util.ArrayList;
import org.mozilla.javascript.Scriptable;

/**
 *
 * @author Andy
 */
public class ExecutionGroup {

    private ArrayList<GameScript> group;

    public ExecutionGroup(ArrayList<GameScript> group) {
        this.group = group;
    }

    public static ExecutionGroup createExecutionGroup(Scriptable scope, String... paths) {
        ArrayList<GameScript> scripts = new ArrayList<GameScript>();
        for (String path : paths) {
            scripts.add(ScriptManager.getInstance().loadScript(path, scope));
        }
        return new ExecutionGroup(scripts);
    }

    public boolean isRunning() {
        boolean running = false;
        for (GameScript gs : group) {
            running = running || gs.isRunning();
        }
        return running;
    }

    public void execute() {
        for (GameScript gs : group) {
            ScriptManager.getInstance().addToQueue(gs);
        }
    }
}
