/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import graphics.ThreeDGraphicsManager;
import graphics.VectorGraphic;
import java.util.ArrayList;
import java.util.Arrays;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector3f;
import util.Utilities;

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
    Vector3f relativeCenterOfMass;
    static final float defaultMass = 1f;

    public StaticEntity(BoundingBox b) {
        this(b, defaultMass);
    }

    public StaticEntity(BoundingBox b, float mass) {
        this(b, mass, getRectangularPrismInertiaTensor(b, mass));
    }

    public StaticEntity(BoundingBox b, float mass, Matrix3f inertiaTensor) {
        this(b, mass, inertiaTensor, Utilities.zeroVec);
    }
    
    public StaticEntity(BoundingBox b, float mass, Matrix3f inertiaTensor, Vector3f relativeCenterOfMass) {
        super(b);
        this.mass = mass;
        this.inertiaTensor = inertiaTensor;
        this.invMass = 1f / mass;
        this.invInertiaTensor = new Matrix3f();
        invInertiaTensor.load(inertiaTensor);
        invInertiaTensor.invert();
        collide = true;
        this.relativeCenterOfMass = relativeCenterOfMass;
    }

    public float getInvMass() {
        return invMass;
    }

    public float getMass() {
        return mass;
    }

    public Vector3f getLocalCenterOfMass() {
        return relativeCenterOfMass;
    }

    public Vector3f getGlobalCenterOfMass() {
        return Vector3f.add(getPosition(), Utilities.transform(getLocalCenterOfMass(), orientation), null);
    }
    
    public Matrix3f getInertiaTensor() {
        return inertiaTensor;
    }

    public Matrix3f getInvInertiaTensor() {
        return invInertiaTensor;
    }

    public Matrix3f invInertiaTensorWorld() {
        Matrix3f mat = Utilities.matFromQuat(orientation);
        Matrix3f inv = Utilities.matFromQuat(Utilities.inverse(orientation));
        return Matrix3f.mul(mat, Matrix3f.mul(invInertiaTensor, inv, null), null);
    }

    public float invMomentAround(Vector3f axis) {
        Vector3f norm = new Vector3f();
        norm.normalise();
        return Matrix3f.transform(invInertiaTensorWorld(), axis, null).length();
    }
    
    public void collide(StaticEntity s) {
    }
    public void collide(Plane collision, Vector3f contact) {
        VectorGraphic test = new VectorGraphic(contact, new Vector3f(0,10,0));
        test.create();
        //ThreeDGraphicsManager.getInstance().add(test);
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
