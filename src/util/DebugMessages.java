/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import game.GameObject;
import game.Manager;

/**
 *
 * @author Andy
 */
public class DebugMessages extends Manager {

    boolean enabled;
    
    static DebugMessages instance;

    @Override
    public void create() {
        super.create();
    }

    public static DebugMessages getInstance() {
        if (instance == null) {
            instance = new DebugMessages();
        }
        return instance;
    }

    @Override
    public void destroy() {
    }

    public void write(String message) {
        if (enabled) {
            System.out.println(message);
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    @Override
    public void remove(GameObject obj) {
    }
}
