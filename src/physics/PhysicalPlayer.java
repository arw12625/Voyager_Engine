/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import game.Player;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import util.Utilities;
import static util.Utilities.*;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Andy
 */
public class PhysicalPlayer extends Player implements graphics.ThreeD, Boundable {

    graphics.ViewPoint vp;
    physics.PhysicalEntity pe;
    private float xAngle;
    private float yAngle;
    private float piOver180 = (float) (Math.PI / 180f);
    private Quaternion orientation;
    private float maxDeviation = 85f * piOver180;

    @Override
    public void create() {
        super.create();
        BoundingBox playerBounds = new BoundingBox(new Vector3f(), new Vector3f(.5f, 1f, .5f));
        playerBounds.create();
        pe = new physics.PhysicalEntity(playerBounds, 5) {

            @Override
            public void collide(ArrayList<Plane> collisions) {
            }

            @Override
            public void collide(PhysicalEntity collision) {
                //System.out.println(collision);
            }

            
            public boolean update(int delta) {
                return false;
            }
        };
        pe.create();
        ThreeDPhysicsManager.getInstance().add(pe);
        graphics.ThreeDGraphicsManager.getInstance().add(this);
        vp = new graphics.ViewPoint(pe.getBounds().getPosition(), pe.getBounds().getOrientation());
        pe.addForceGenerator(new ForceGenerator() {

            @Override
            public void applyForce(PhysicalEntity pe) {
                input.InputManager keyboard = input.InputManager.getInstance();
                Vector3f go = new Vector3f();
                if (keyboard.get(Keyboard.KEY_UP).isDown() || keyboard.get(Keyboard.KEY_W).isDown()) {
                    go.translate(0, 0, -1);
                }
                if (keyboard.get(Keyboard.KEY_DOWN).isDown() || keyboard.get(Keyboard.KEY_S).isDown()) {
                    go.translate(0, 0, 1);
                }
                if (keyboard.get(Keyboard.KEY_LEFT).isDown() || keyboard.get(Keyboard.KEY_A).isDown()) {
                    go.translate(-1, 0, 0);
                }
                if (keyboard.get(Keyboard.KEY_RIGHT).isDown() || keyboard.get(Keyboard.KEY_D).isDown()) {
                    go.translate(1, 0, 0);
                }
                if (go.lengthSquared() != 0) {
                    pe.setAwake(true);
                    go = transform(go, orientation);
                    go.scale(80);
                    pe.applyForce(go);
                    //pe.setVelocity(go);
                }
            }
        });
    }

    @Override
    public synchronized  boolean update(int delta) {
        float dx = input.InputManager.getInstance().getDX() / 100f;
        float dy = -input.InputManager.getInstance().getDY() / 100f;
        xAngle += dy;
        yAngle += dx;
        if (xAngle > maxDeviation) {
            xAngle = maxDeviation;
        }
        if (xAngle < -maxDeviation) {
            xAngle = -maxDeviation;
        }
        xAngle %= Math.PI;
        yAngle %= Math.PI * 2;
        orientation = (Quaternion.mul(quatFromAxisAngle(new Vector3f(1, 0, 0), xAngle), quatFromAxisAngle(new Vector3f(0, 1, 0), yAngle), null));
        vp.setPosition(pe.getBounds().getPosition());
        vp.setOrientation(orientation);
        
        return false;
    }

    public synchronized graphics.ViewPoint getViewPoint() {
        return vp;
    }

    public synchronized PhysicalEntity getPhysicalEntity() {
        return pe;
    }

    @Override
    public synchronized void render() {
        pe.getBounds().render();
    }

    @Override
    public BoundingBox getBounds() {
        return getPhysicalEntity().getBounds();
    }
}
