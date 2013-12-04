/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Andy
 */
public class Transform extends game.GameObject implements Positionable, Orientable {
    
    Vector3f position;
    Quaternion orientation;
    
    public Transform() {
        this(new Vector3f());
    }
    public Transform(Vector3f position) {
        this(position, new Quaternion());
    }
    public Transform(Quaternion orientation) {
        this(new Vector3f(), orientation);
    }
    public Transform(Vector3f position, Quaternion orientation) {
        this.position = position;
        this.orientation = orientation;
    }

    @Override
    public Vector3f getPosition() {
        return position;
    }

    @Override
    public void setPosition(Vector3f v) {
        this.position = v;
    }

    @Override
    public Quaternion getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(Quaternion q) {
        this.orientation = q;
    }
}
