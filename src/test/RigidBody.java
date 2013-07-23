/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import resource.ResourceManager;
import physics.Mesh;
import graphics.ThreeDModel;
import java.util.ArrayList;
import physics.PhysicalEntity;
import physics.Plane;

/**
 *
 * @author Andy
 */
public class RigidBody extends AbstractEntity {

    String name;
    
    public RigidBody(String name, ThreeDModel m) {
        super(m);
        this.name = name;
    }
    
    public static RigidBody rigidBodyFromPath(String name, String mpath) {
        Mesh mesh = new Mesh(name, mpath);
        mesh.create();
        while(!mesh.isLoaded()) {
            Thread.yield();
        }
        ThreeDModel model = new ThreeDModel(mesh);
        model.create();
        return new RigidBody(name, model);
    }
    @Override
    public void collide(ArrayList<Plane> collisions) {
    }

    @Override
    public void collide(PhysicalEntity collisions) {
    }

}
