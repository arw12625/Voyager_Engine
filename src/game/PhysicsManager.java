/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import util.DebugMessages;

/**
 *
 * @author Andy
 */
public abstract class PhysicsManager implements Manager {
    
    @Override
    public void create() {
        DebugMessages.getInstance().write("PhysicsManager created");
    }

    @Override
    public void destroy() {
        DebugMessages.getInstance().write("PhysicsManager destroyed");
    }
    
    @Override
    public String getName() {
        return "PhysicsManager";
    }
    
}
