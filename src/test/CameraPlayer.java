/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import util.Utilities;

/**
 *
 * @author Andy
 */
public class CameraPlayer extends game.Player {

    Vector3f position;
    Quaternion orientation;
    graphics.ViewPoint vp;
    private float xAngle;
    private float yAngle;
    private float piOver180 = (float) (Math.PI / 180f);
    private float maxDeviation = 85f * piOver180;

    @Override
    public void create() {
        super.create();
        position = new Vector3f();
        orientation = new Quaternion();
        vp = new graphics.ViewPoint(position, orientation);
    }

    @Override
    public boolean update(int delta) {
        input.InputManager keyboard = input.InputManager.getInstance();
        
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
        setOrientation(Quaternion.mul(Utilities.quatFromAxisAngle(new Vector3f(1, 0, 0), xAngle), Utilities.quatFromAxisAngle(new Vector3f(0, 1, 0), yAngle), null));
        
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
            go = Utilities.transform(go, orientation);
            go.scale(0.03f);
            Vector3f.add(position, go, position);
        }
        
        vp.setPosition(position);
        vp.setOrientation(orientation);
        
        return false;
    }

    public Vector3f getPosition() {
        return position;
    }
    
    public Quaternion getOrientation() {
        return orientation;
    }
    
    public void setPosition(Vector3f position) {
        this.position = position;
    }
    
    public void setOrientation(Quaternion orientation) {
        this.orientation = orientation;
    }
    
    public graphics.ViewPoint getViewPoint() {
        return vp;
    }
}
