/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.ArrayList;
import resource.WavefrontModel;


/**
 *
 * @author Andy
 */
public class RigidBody extends AbstractEntity {

    public RigidBody(graphics.ThreeDModel m) {
        super(m);
    }
    
    public static RigidBody rigidBodyFromPath(String mpath) {
        WavefrontModel w = new WavefrontModel(mpath);
        w.create();
        graphics.ThreeDModel model = new graphics.ThreeDModel(w);
        model.create();
        return new RigidBody(model);
    }
    
    @Override
    public void collide(ArrayList<physics.Plane> collisions) {
    }

    @Override
    public void collide(physics.PhysicalEntity collisions) {
    }

}
