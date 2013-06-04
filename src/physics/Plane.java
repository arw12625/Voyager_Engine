/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Andy
 */
public class Plane {
    
    public BoundingBox bounds;
    public float a, b, c, d;

    public Plane(Vector3f[] v, Vector3f n) {

        bounds = BoundingBox.boundsFromVerts(v);
        a = n.getX();
        b = n.getY();
        c = n.getZ();
        d = a * v[0].getX() + b * v[0].getY() + c * v[0].getZ();

    }

    public BoundingBox getBounds() {

        return bounds;

    }

    public String toString() {

        return bounds.toString();

    }
    
    public Vector3f getNormal() {
        return new Vector3f(a, b, c);
    }
    
    public Vector3f getMiddle() {
        return bounds.getPosition();
    }
}
