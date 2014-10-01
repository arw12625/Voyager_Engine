/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.ArrayList;
import java.util.HashMap;
import script.GameScript;
import script.ScriptManager;

/**
 *
 * @author Andy
 */
public abstract class GameObject {

    int id;
    Object_State state;
    ArrayList<script.GameScript> scripts;
    static HashMap<String, Integer> instances = new HashMap<String, Integer>();

    public GameObject() {
        scripts = new ArrayList<GameScript>();
        state = Object_State.NOT_CREATED;
    }

    public void create() {
        String name = this.getClass().getName();
        if (instances.containsKey(name)) {
            id = instances.get(name).intValue();
        } else {
            id = 0;
        }

        instances.put(name, id + 1);
        Game.addGameObject(this);

        runScripts("create", null);

        util.DebugMessages.getInstance().write(this.getFullName() + " created");
        state = Object_State.CREATED;
    }

    public void destroy() {
        runScripts("destroy", null);
        Game.removeGameObject(this);
        util.DebugMessages.getInstance().write(this.getFullName() + " destroyed");
        state = Object_State.DESTROYED;
    }

    public String getFullName() {
        return getClass().getName() + id;
    }

    public ArrayList<script.GameScript> getScripts() {
        return scripts;
    }

    public void addScript(script.GameScript script) {
            ScriptManager.getInstance().setCurrentObject(this);
        scripts.add(script);
        ScriptManager.getInstance().addToQueue(script);
    }

    public void runScripts(String func, Object[] args) {
        for (script.GameScript s : scripts) {
            ScriptManager.getInstance().setCurrentObject(this);
            script.ScriptManager.getInstance().runScriptFunc(s, func, args);
        }
    }
    
    enum Object_State {
        NOT_CREATED, CREATED, DESTROYED
    }
}
