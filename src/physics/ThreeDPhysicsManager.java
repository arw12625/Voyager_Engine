/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import game.*;
import java.util.ArrayList;
import util.DebugMessages;

/**
 *
 * @author Andy
 */
public class ThreeDPhysicsManager extends PhysicsManager {

    ArrayList<PhysicalEntity> pe;
    
    static ThreeDPhysicsManager instance;
    
    @Override
    public void create() {
        super.create();
        pe = new ArrayList<PhysicalEntity>();
    }
    
    public static ThreeDPhysicsManager getInstance() {
        if(instance == null) {
            instance = new ThreeDPhysicsManager();
        }
        return instance;
    }
    
    @Override
    public void update(int delta) {
        
        DebugMessages.getInstance().write("Physics starting");
        
        float time = delta / 1000f;
        for (PhysicalEntity e : pe) {

            e.setAwake(!e.canSleep());
            e.updateForces();
            if (e.isAwake()) {
                e.integrate(time);
            }

        }
        
        DebugMessages.getInstance().write("Physics finished");
    }

    public void addEntity(PhysicalEntity p) {
        pe.add(p);
    }
    
}
