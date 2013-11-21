/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import graphics.ThreeDGraphicsManager;
import graphics.VectorGraphic;
import java.util.ArrayList;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import resource.WavefrontModel;
import util.Utilities;

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
        while (!w.isLoaded()) {
            Thread.yield();
        }
        graphics.ThreeDModel model = new graphics.ThreeDModel(w);
        model.create();
        while (!model.isProcessed()) {
            Thread.yield();
        }
        return new RigidBody(model);
    }

    @Override
    public boolean update(int delta) {
        boolean x = super.update(delta);

        return x;
    }

    @Override
    public void collide(ArrayList<physics.Plane> collisions) {
    }

    @Override
    public void collide(physics.PhysicalEntity collisions) {
    }
}
