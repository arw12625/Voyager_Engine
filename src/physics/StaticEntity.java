/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import java.util.ArrayList;
import java.util.Arrays;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Andy
 */
public class StaticEntity extends BoundingBox {

    float mass;
    Matrix3f inertiaTensor;
    float invMass;
    Matrix3f invInertiaTensor;
    boolean collide;
    static final float defaultMass = 1f;

    public StaticEntity(BoundingBox b) {
        this(b, defaultMass);
    }

    public StaticEntity(BoundingBox b, float mass) {
        this(b, mass, getRectangularPrismInertiaTensor(b, mass));
    }

    public StaticEntity(BoundingBox b, float mass, Matrix3f inertiaTensor) {
        super(b);
        this.mass = mass;
        this.inertiaTensor = inertiaTensor;
        this.invMass = 1f / mass;
        this.invInertiaTensor = new Matrix3f();
        invInertiaTensor.load(inertiaTensor);
        invInertiaTensor.invert();
        collide = true;
    }

    public float getInvMass() {
        return invMass;
    }

    public float getMass() {
        return mass;
    }

    public Vector3f getCenterOfMass() {
        return getCenter();
    }
    
    public Matrix3f getInertiaTensor() {
        return inertiaTensor;
    }

    public Matrix3f getInvInertiaTensor() {
        return invInertiaTensor;
    }

    public void collide(StaticEntity s) {
    }
    public void collide(Plane collision, Vector3f contact) {
    }

    public boolean isCollidable() {
        return collide;
    }

    public void setCollideable(boolean collide) {
        this.collide = collide;
    }

    public ArrayList<Vector3f> getContactVertices() {
        if (collide) {
            ArrayList<Vector3f> list = new ArrayList(Arrays.asList(getGlobalVertices()));
            return list;
        } else {
            return new ArrayList<Vector3f>();
        }
    }

    public static Matrix3f getRectangularPrismInertiaTensor(final BoundingBox b, final float mass) {
        return new Matrix3f() {

            {
                float widthSquare = (float) Math.pow(b.getDimension().getX(), 2);
                float heightSquare = (float) Math.pow(b.getDimension().getY(), 2);
                float depthSquare = (float) Math.pow(b.getDimension().getZ(), 2);
                float mult = mass / 12f;
                m00 = mult * (heightSquare * depthSquare);
                m11 = mult * (widthSquare * depthSquare);
                m22 = mult * (heightSquare * widthSquare);

            }
        };
    }
}
