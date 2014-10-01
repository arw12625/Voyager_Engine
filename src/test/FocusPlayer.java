/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import input.InputManager;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import physics.Positionable;
import util.Utilities;

/**
 *
 * @author Andy
 */
public class FocusPlayer extends game.Player {

    Positionable p;
    Vector3f position;
    Quaternion orientation;
    graphics.ViewPoint vp;
    float radius;
    float xAngle, yAngle;
    static final float maxXAngleDeviation = (float) Math.PI / 2.1f;
    static final float minRadius = 3;
    static final float maxRadius = 24;

    public FocusPlayer(Positionable p) {
        this.p = p;
        radius = 10;
    }

    @Override
    public void create() {
        super.create();
        this.position = new Vector3f(p.getPosition());
        this.orientation = new Quaternion();
        this.vp = new graphics.ViewPoint(position, orientation);
    }

    @Override
    public boolean update(int delta) {
        InputManager im = input.InputManager.getInstance();
        if (im.isMouseButtonDown(1)) {

            float dx = im.getDX() / 100f;
            float dy = -im.getDY() / 100f;
            xAngle += dy;
            yAngle += dx;
            if (xAngle > maxXAngleDeviation) {
                xAngle = maxXAngleDeviation;
            }
            if (xAngle < -maxXAngleDeviation) {
                xAngle = -maxXAngleDeviation;
            }
            xAngle %= Math.PI;
            yAngle %= Math.PI * 2;

        }

        float dRadius = -im.getDWheel() / 120f;
        radius += dRadius;
        if (radius > maxRadius) {
            radius = maxRadius;
        }
        if (radius < minRadius) {
            radius = minRadius;
        }

        orientation = (Quaternion) Quaternion.mul(Utilities.quatFromAxisAngle(new Vector3f(1, 0, 0), xAngle),
                Utilities.quatFromAxisAngle(new Vector3f(0, 1, 0), yAngle), null).normalise();
        Quaternion test = new Quaternion(orientation);
        test.negate();
        Vector3f.add(p.getPosition(), Utilities.transform(new Vector3f(0, 0, radius), test), position);

        vp.setOrientation(orientation);
        vp.setPosition(position);
        return false;
    }

    public graphics.ViewPoint getViewPoint() {
        return vp;
    }
}
