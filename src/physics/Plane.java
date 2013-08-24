/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import graphics.ThreeD;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Andy
 */
public class Plane implements Boundable, ThreeD {
    
    public BoundingBox bounds;
    Vector3f normal;
    float p;

    public Plane(Vector3f[] v, Vector3f n) {

        bounds = BoundingBox.boundsFromVerts(v);
        this.normal = n;
        p = -Vector3f.dot(v[0], n);

    }

    public BoundingBox getBounds() {

        return bounds;

    }

    public String toString() {

        return bounds.toString();

    }
    
    public float getDistance(Vector3f v) {
        return Vector3f.dot(normal, v) + p;
    }
    
    public Vector3f getNormal() {
        return normal;
    }
    
    public Vector3f getMiddle() {
        return bounds.getPosition();
    }

    @Override
    public void render() {
        bounds.render();
    }
}
