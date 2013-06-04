/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package script;

import game.GameObject;
import javax.script.ScriptContext;

/**
 *
 * @author Andy
 */
public class GameScript implements GameObject {

    String name;
    String script;
    
    public GameScript(String name, String script) {
        this.name = name;
        this.script = script;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public String getScript() {
        return script;
    }
    
    public ScriptContext getContext() {
        return null;
    }
    
}
