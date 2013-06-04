/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import game.Manager;

/**
 *
 * @author Andy
 */
public class DebugMessages implements Manager {

    boolean enabled;
    
    static DebugMessages instance;

    @Override
    public void create() {
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

    @Override
    public String getName() {
        return "DebugMessages";
    }

    @Override
    public void update(int delta) {
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
