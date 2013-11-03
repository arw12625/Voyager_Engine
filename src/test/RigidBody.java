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
    
    public static RigidBody rigidBodyFromPath(String prefix, String path) {
        return rigidBodyFromWavefront(new WavefrontModel(prefix, path));
    }
    
    public static RigidBody rigidBodyFromPath(String mpath) {
        return rigidBodyFromWavefront(new WavefrontModel(mpath));
    }
    
    private static RigidBody rigidBodyFromWavefront(WavefrontModel w) {
        w.create();
        while(!w.isLoaded()) {
            Thread.yield();
        }
        graphics.ThreeDModel model = new graphics.ThreeDModel(w);
        model.create();
        while(!model.isProcessed()) {
            Thread.yield();
        }
        return new RigidBody(model);
    }
    
    @Override
    public void collide(ArrayList<physics.Plane> collisions) {
    }

    @Override
    public void collide(physics.PhysicalEntity collisions) {
    }

}
