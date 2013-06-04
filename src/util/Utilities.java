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
import physics.BoundingBox;

/**
 *
 * @author Andy
 */
public class Utilities {

    public static FloatBuffer asFloatBuffer(float... values) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(values.length);
        buffer.put(values);
        buffer.flip();
        return buffer;
    }

    public static Vector3f addScaledVector(Vector3f left, Vector3f right, float scale) {
        return Vector3f.add(left, (Vector3f) (new Vector3f(right)).scale(scale), null);
    }

    public static Quaternion addScaledVector(Quaternion orig, Vector3f v, float scale) {
        Quaternion q = new Quaternion(v.getX(), v.getY(), v.getZ(), 0);
        q.scale(scale);
        Quaternion quat = new Quaternion(orig);
        Quaternion.mul(q, quat, q);
        quat.setW(quat.getW() + q.getW() * 0.5f);
        quat.setX(quat.getX() + q.getX() * 0.5f);
        quat.setY(quat.getY() + q.getY() * 0.5f);
        quat.setZ(quat.getZ() + q.getZ() * 0.5f);
        return quat;
    }

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

    public static Vector3f transform(Vector3f orig, Quaternion q) {
        Quaternion resQ = new Quaternion();
        Quaternion origQ = new Quaternion(orig.getX(), orig.getY(), orig.getZ(), 0);
        Quaternion.mul(inverse(q), origQ, resQ);
        Quaternion.mul(resQ, q, resQ);
        return new Vector3f(resQ.getX(), resQ.getY(), resQ.getZ());
    }

    public static Quaternion inverse(Quaternion q) {
        return new Quaternion(-q.getX(), -q.getY(), -q.getZ(), q.getW());
    }

    public static Quaternion quatFromAxisAngle(Vector3f v, float theta) {
        Vector3f vn = new Vector3f();
        v.normalise(vn);
        float half = theta / 2;
        float sin = (float) Math.sin(half);
        float cos = (float) Math.cos(half);
        return new Quaternion(v.getX() * sin, v.getY() * sin, v.getZ() * sin, cos);
    }
}
