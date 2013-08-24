/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import input.InputManager;
import game.Player;
import graphics.ThreeDGraphicsManager;
import graphics.ViewPoint;
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
public class PhysicalPlayer extends Player implements graphics.ThreeD {

    ViewPoint vp;
    PhysicalEntity pe;
    private float xAngle;
    private float yAngle;
    private float piOver180 = (float) (Math.PI / 180f);
    private float maxDeviation = 85f * piOver180;

    @Override
    public void create() {
        super.create();
        BoundingBox playerBounds = new BoundingBox(new Vector3f(), new Vector3f(.5f, 1f, .5f));
        playerBounds.create();
        pe = new PhysicalEntity(playerBounds) {

            @Override
            public void collide(ArrayList<Plane> collisions) {
            }

            @Override
            public void collide(PhysicalEntity collision) {
                System.out.println(collision);
            }

            @Override
            public void update(int delta) {
            }
        };
        pe.create();
        ThreeDPhysicsManager.getInstance().add(pe);
        ThreeDGraphicsManager.getInstance().add(this);
        vp = new ViewPoint(pe.getBounds().getPosition(), pe.getBounds().getOrientation());
        pe.addForceGenerator(new ForceGenerator() {

            @Override
            public void applyForce(PhysicalEntity pe) {
                InputManager keyboard = InputManager.getInstance();
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
                    go = transform(go, pe.getBounds().getOrientation());
                    go.scale(4);
                    pe.applyForce(go);
                    //pe.setVelocity(go);
                }
            }
        });
    }

    @Override
    public void update(int delta) {
        float dx = InputManager.getInstance().getDX() / 100f;
        float dy = -InputManager.getInstance().getDY() / 100f;
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
        pe.getBounds().setOrientation(Quaternion.mul(quatFromAxisAngle(new Vector3f(1, 0, 0), xAngle), quatFromAxisAngle(new Vector3f(0, 1, 0), yAngle), null));
        vp.setPosition(pe.getBounds().getPosition());
        vp.setOrientation(pe.getBounds().getOrientation());
    }

    public ViewPoint getViewPoint() {
        return vp;
    }

    public PhysicalEntity getPhysicalEntity() {
        return pe;
    }

    @Override
    public void render() {
        pe.getBounds().render();
    }
}
