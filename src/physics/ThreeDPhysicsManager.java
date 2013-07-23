/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import update.UpdateManager;
import game.*;
import java.util.ArrayList;
import util.DebugMessages;

/**
 *
 * @author Andy
 */
public class ThreeDPhysicsManager extends StandardManager implements update.Updateable {

    ArrayList<PhysicalEntity> pe;
    CollisionMesh collsionMesh;
    static ThreeDPhysicsManager instance;

    @Override
    public void create() {
        super.create();
        pe = new ArrayList<PhysicalEntity>();
    }

    public static ThreeDPhysicsManager getInstance() {
        if (instance == null) {
            instance = new ThreeDPhysicsManager();
        }
        return instance;
    }

    @Override
    public void update(int delta) {

        DebugMessages.getInstance().write("Physics starting");

        //Motion update
        float time = delta / 1000f;
        for (PhysicalEntity e : pe) {

            e.setAwake(!e.canSleep());
            e.updateForces();
            if (e.isAwake()) {
                e.integrate(time);
            }

        }

        //Collision Detection
        if (collsionMesh != null && !collsionMesh.isEmpty()) {
            for (PhysicalEntity e : pe) {
                ArrayList<Plane> cols = collsionMesh.getPlanes(e);
                for (Plane p : cols) {
                    if (e.getBounds().intersects(p.getBounds())) {
                    }
                }
            }
        }

        DebugMessages.getInstance().write("Physics finished");
    }

    @Override
    public boolean add(GameObject obj) {
        if(obj instanceof PhysicalEntity) {
            pe.add((PhysicalEntity)obj);
            return true;
        }
        return false;
    }

    @Override
    public void remove(GameObject obj) {
        if(pe.contains(obj)) {
            pe.remove(obj);
        }
    }
}
