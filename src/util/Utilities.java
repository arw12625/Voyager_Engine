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

    public static Matrix3f matFromQuat(Quaternion q) {

        float x = q.getX(), y = q.getY(), z = q.getZ(), w = q.getW();
        float mag = w * w + x * x + y * y + z * z;
        float s = (mag > 0.0) ? 2f / mag : 0f;
        float X = x * s, Y = y * s, Z = z * s;
        final float wX = w * X, wY = w * Y, wZ = w * Z;
        final float xX = x * X, xY = x * Y, xZ = x * Z;
        final float yY = y * Y, yZ = y * Z, zZ = z * Z;

        return new Matrix3f() {{
                m00 = 1 - (yY + zZ); m10 = xY - wZ; m20 = xZ + wY;
                m01 = xY + wZ; m11 = 1 - (xX + zZ); m21 = yZ - wX;
                m02 = xZ - wY; m12 = yZ + wX; m22 = 1 - (xX + yY);
        }};
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

    public static Vector3f inverseTransform(Vector3f orig, Quaternion q) {
        return transform(orig, inverse(q));
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
        Quaternion q = new Quaternion(v.getX() * sin, v.getY() * sin, v.getZ() * sin, cos);
        q.normalise();
        return q;
    }

    public static Quaternion quatFromBasis(final Vector3f x, final Vector3f y, final Vector3f z) {
        Matrix3f rot = new Matrix3f() {{
            m00 = x.getX(); m10 = x.getY(); m20 = x.getZ();
            m01 = y.getX(); m11 = y.getY(); m21 = y.getZ();
            m02 = z.getX(); m12 = z.getY(); m22 = z.getZ();
        }};
        rot = Utilities.scale(rot, -1);
        rot.invert();
        Quaternion q = new Quaternion();
        q.setFromMatrix(rot);
        q.normalise();
        return q;
    }
    
    public static Matrix3f skewMatrix(final Vector3f v) {
        return new Matrix3f() {{
                m10 = -v.getZ();
                m20 = v.getY();
                m01 = v.getZ();
                m21 = -v.getX();
                m02 = -v.getY();
                m12 = v.getX();
        }};
    }

    public static boolean approximate(Vector3f u, Vector3f v) {
        Vector3f diff = Vector3f.sub(u, v, null);
        return diff.lengthSquared() < 0.0001f;
    }
}
