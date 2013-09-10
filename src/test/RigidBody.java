/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.ArrayList;


/**
 *
 * @author Andy
 */
public class RigidBody extends AbstractEntity {

    public RigidBody(graphics.ThreeDModel m) {
        super(m);
    }
    
    public static RigidBody rigidBodyFromPath(String mpath) {
        physics.Mesh mesh = new physics.Mesh(mpath);
        mesh.create();
        graphics.ThreeDModel model = new graphics.ThreeDModel(mesh);
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
