/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package update;

import game.InputManager;
import game.PhysicsManager;
import game.SoundManager;
import game.UpdateManager;
import java.util.ArrayList;
import physics.ThreeDPhysicsManager;
import util.DebugMessages;

/**
 *
 * @author Andy
 */
public class ThreeDUpdateManager extends UpdateManager {

    ArrayList<Entity> entities;
    static ThreeDUpdateManager instance;

    @Override
    public void create() {
        super.create();
        entities = new ArrayList<Entity>();
    }

    public static ThreeDUpdateManager getInstance() {
        if(instance == null) {
            instance = new ThreeDUpdateManager();
        }
        return instance;
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void updateGame(int delta) {
        
        DebugMessages.getInstance().write("Delta: " + delta + "  FPS: " + (1000 / delta));
        DebugMessages.getInstance().write("Updates starting");
        
        InputManager.getInstance().processInputs();
                
        ArrayList<Entity> eCopy = new ArrayList<Entity>(entities);
        for (Entity e : eCopy) {
            e.update(delta);
        }
        ThreeDPhysicsManager.getInstance().update(delta);
        SoundManager.getInstance().update(delta);
        
        DebugMessages.getInstance().write("Updates finished");
    }

    @Override
    public void addEntity(Entity e) {
        entities.add(e);
    }

    @Override
    public void removeEntity(Entity e) {
        entities.remove(e);
    }
    
}
