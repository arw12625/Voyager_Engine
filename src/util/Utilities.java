/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Andy
 */
public class Utilities {

    //A constant vector with magnitude zero
    public static final Vector3f zeroVec = new Vector3f();
    public static final Quaternion zeroQuat = new Quaternion();

    //Converts a list of floats into a FloatBuffer
    public static FloatBuffer asFloatBuffer(float... values) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(values.length);
        buffer.put(values);
        buffer.flip();
        return buffer;
    }

    //Scales the right vector and adds it to the left vector
    public static Vector3f addScaledVector(Vector3f left, Vector3f right, float scale) {
        return Vector3f.add(left, (Vector3f) (new Vector3f(right)).scale(scale), null);
    }

    //Scales the vector by the scale and adds it to the quaternion
    //For the purposes of updating an orientation by an angular velocity
    public static Quaternion addScaledVector(Quaternion orig, Vector3f v, float scale) {
        Quaternion q = new Quaternion(v.getX(), v.getY(), v.getZ(), 0);
        q.scale(scale / 2);
        Quaternion quat = new Quaternion(orig);
        Quaternion.mul(q, quat, q);            
        return addQuaternion(quat, q);
    }

    //Quaternion addition
    public static Quaternion addQuaternion(Quaternion left, Quaternion right) {
        return new Quaternion(
                left.getX() + right.getX(),
                left.getY() + right.getY(),
                left.getZ() + right.getZ(),
                left.getW() + right.getW());
    }

    //Converts a quaternion into a rotation matrix
    public static Matrix3f matFromQuat(Quaternion q) {

        float x = q.getX(), y = q.getY(), z = q.getZ(), w = q.getW();
        float mag = w * w + x * x + y * y + z * z;
        float s = (mag > 0.0) ? 2f / mag : 0f;
        float X = x * s, Y = y * s, Z = z * s;
        final float wX = w * X, wY = w * Y, wZ = w * Z;
        final float xX = x * X, xY = x * Y, xZ = x * Z;
        final float yY = y * Y, yZ = y * Z, zZ = z * Z;

        return new Matrix3f() {

            {
                m00 = 1 - (yY + zZ);
                m10 = xY - wZ;
                m20 = xZ + wY;
                m01 = xY + wZ;
                m11 = 1 - (xX + zZ);
                m21 = yZ - wX;
                m02 = xZ - wY;
                m12 = yZ + wX;
                m22 = 1 - (xX + yY);
            }
        };
        
    }

    //Scales a matrix by a constant
    public static Matrix3f scale(final Matrix3f m, final float scale) {
        return new Matrix3f() {

            {
                m00 = m.m00 * scale;
                m01 = m.m01 * scale;
                m02 = m.m02 * scale;
                m10 = m.m10 * scale;
                m11 = m.m11 * scale;
                m12 = m.m12 * scale;
                m20 = m.m20 * scale;
                m21 = m.m21 * scale;
                m22 = m.m22 * scale;
            }
        };
    }

    //Applies a quaternion rotation to a vector
    //q*v*q^-1
    public static Vector3f transform(Vector3f orig, Quaternion q) {
        Quaternion resultQ = new Quaternion();
        Quaternion vecQ = new Quaternion(orig.getX(), orig.getY(), orig.getZ(), 0);
        Quaternion.mul(q, vecQ, resultQ);
        Quaternion.mul(resultQ, inverse(q), resultQ);
        return new Vector3f(resultQ.getX(), resultQ.getY(), resultQ.getZ());
    }

    //Applies the inverse of a quaternion rotation to a vector
    public static Vector3f inverseTransform(Vector3f orig, Quaternion q) {
        return transform(orig, inverse(q));
    }

    //Inverts a quaternion, returns the conjugate
    public static Quaternion inverse(Quaternion q) {
        return new Quaternion(-q.getX(), -q.getY(), -q.getZ(), q.getW());
    }

    //Composes a quaternion from a axis angle
    public static Quaternion quatFromAxisAngle(Vector3f v, float theta) {
        Vector3f vn = new Vector3f();
        v.normalise(vn);
        float half = theta / 2;
        float sin = (float) Math.sin(half);
        float cos = (float) Math.cos(half);
        Quaternion q = new Quaternion(v.getX() * sin, v.getY() * sin, v.getZ() * sin, cos);
        q.normalise();
        return q;
    }

    //returns a quaternion representing a change of basis
    public static Quaternion quatFromBasis(final Vector3f x, final Vector3f y, final Vector3f z) {
        Matrix3f rot = new Matrix3f() {

            {
                m00 = x.getX();
                m10 = x.getY();
                m20 = x.getZ();
                m01 = y.getX();
                m11 = y.getY();
                m21 = y.getZ();
                m02 = z.getX();
                m12 = z.getY();
                m22 = z.getZ();
            }
        };
        Quaternion q = new Quaternion();
        q.setFromMatrix(rot);
        q.normalise();
        return q;
    }

    //returns the skew matrix of the vector
    public static Matrix3f skewMatrix(final Vector3f v) {
        return new Matrix3f() {

            {
                m10 = -v.getZ();
                m20 = v.getY();
                m01 = v.getZ();
                m21 = -v.getX();
                m02 = -v.getY();
                m12 = v.getX();
            }
        };
    }

    //return true if the vectors are approximately the same
    public static boolean approximate(Vector3f u, Vector3f v, float threshhold) {
        Vector3f diff = Vector3f.sub(u, v, null);
        return diff.lengthSquared() < threshhold;
    }
    
    //return true if the vectors are approximately the same
    public static boolean approximate(Vector3f u, Vector3f v) {
        return approximate(u, v, 0.0001f);
    }

    //transform q2 by q1
    public static Quaternion transform(Quaternion q1, Quaternion q2) {
        return Quaternion.mul(q1, q2, null);
    }
    
    //return the angle around the axis from the quaternion
    public static float angleFromQuat(Quaternion q) {
        return (float) (Math.acos(q.getW()) * 2);
    }
    
    public static Vector3f axisFromQuat(Quaternion q) {
        Vector3f v = new Vector3f(q.getX(), q.getY(), q.getZ());
        if(v.lengthSquared() != 0) {
            v.normalise();
        }
        return v;
    }
    
    public static Matrix3f outerProduct(Vector3f u, Vector3f v) {
        Matrix3f m = new Matrix3f();
        m.m00 = u.getX() * v.getX();
        m.m01 = u.getX() * v.getY();
        m.m02 = u.getX() * v.getZ();
        m.m10 = u.getY() * v.getX();
        m.m11 = u.getY() * v.getY();
        m.m12 = u.getY() * v.getZ();
        m.m20 = u.getZ() * v.getX();
        m.m21 = u.getZ() * v.getY();
        m.m22 = u.getZ() * v.getZ();
        return m;
    }
}
