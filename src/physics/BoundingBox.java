/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import graphics.ThreeDGraphicsManager;
import graphics.VectorGraphic;
import java.util.ArrayList;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import util.Utilities;

/**
 *
 * @author Andy
 */
public class BoundingBox extends Transform implements Boundable {

    private Vector3f dimension;
    private Vector3f half;
    private Vector3f[] localVerts;
    private Vector3f[] localAxes;
    private Vector3f[] globalVerts;
    private Vector3f[] globalAxes;
    private BoundingBox alignedBounds;
    private boolean aligned;

    public BoundingBox() {
        this(new Vector3f(), new Vector3f());
    }

    public BoundingBox(Vector3f position, Vector3f dimension) {
        this(position, dimension, false);
    }

    public BoundingBox(Vector3f position, Vector3f dimension, boolean aligned) {
        this(position, dimension, new Quaternion());
        this.aligned = aligned;
    }

    public BoundingBox(Vector3f position, Vector3f dimension, Quaternion orientation) {
        this.position = position;
        this.dimension = dimension;
        this.orientation = orientation;
    }

    public BoundingBox(BoundingBox b) {
        this(b.getPosition(), b.getDimension(), b.getOrientation());
    }

    @Override
    public void create() {
        super.create();
        half = new Vector3f(dimension);
        half.scale(.5f);
        localVerts = new Vector3f[]{
            new Vector3f(-half.getX(), -half.getY(), -half.getZ()),
            new Vector3f(-half.getX(), -half.getY(), half.getZ()),
            new Vector3f(-half.getX(), half.getY(), half.getZ()),
            new Vector3f(-half.getX(), half.getY(), -half.getZ()),
            new Vector3f(half.getX(), -half.getY(), half.getZ()),
            new Vector3f(half.getX(), -half.getY(), -half.getZ()),
            new Vector3f(half.getX(), half.getY(), -half.getZ()),
            half
        };
        globalVerts = new Vector3f[localVerts.length];
        localAxes = new Vector3f[]{
            new Vector3f(1, 0, 0),
            new Vector3f(0, 1, 0),
            new Vector3f(0, 0, 1)
        };
        globalAxes = new Vector3f[localAxes.length];
        deriveGlobalData();
    }

    public synchronized void translate(Vector3f t) {
        Vector3f.add(position, t, position);
        deriveGlobalData();
    }

    public synchronized void rotate(Quaternion q) {
        Quaternion.mul(orientation, q, orientation);
        deriveGlobalData();
    }

    @Override
    public synchronized void setPosition(Vector3f position) {
        this.position = position;
        deriveGlobalData();
    }

    @Override
    public synchronized void setOrientation(Quaternion orientation) {
        this.orientation = orientation;
        deriveGlobalData();
    }

    @Override
    public synchronized Vector3f getPosition() {
        return position;
    }

    @Override
    public synchronized Quaternion getOrientation() {
        return orientation;
    }

    public synchronized Vector3f getDimension() {
        return dimension;
    }

    public synchronized Vector3f getHalf() {
        return half;
    }

    public synchronized Vector3f[] getLocalVertices() {
        return localVerts;
    }

    public synchronized Vector3f[] getLocalAxes() {
        return localAxes;
    }

    public synchronized Vector3f[] getGlobalVertices() {
        return globalVerts;
    }

    public synchronized Vector3f[] getGlobalAxes() {
        return globalAxes;
    }

    public synchronized Vector3f getCenter() {
        return position;
    }

    public synchronized BoundingBox getAlignedBounds() {
        if (aligned) {
            return this;
        } else {
            return alignedBounds;
        }
    }

    private synchronized void deriveGlobalData() {
        if (globalVerts != null) {
            for (int i = 0; i < globalVerts.length; i++) {
                globalVerts[i] = Utilities.transform(localVerts[i], orientation);
                globalVerts[i].translate(position.getX(), position.getY(), position.getZ());
            }
            if (globalAxes != null) {
                for (int i = 0; i < globalAxes.length; i++) {
                    globalAxes[i] = Utilities.transform(localAxes[i], orientation);
                }
                if (!aligned) {
                    alignedBounds = BoundingBox.boundsFromVerts(true, globalVerts);
                } else {
                    alignedBounds = null;
                }
            }
        }
    }

    public synchronized Vector3f localize(Vector3f v) {
        return Utilities.inverseTransform(Vector3f.sub(v, position, null), orientation);
    }

    public synchronized boolean contains(Vector3f v) {
        Vector3f local = localize(v);
        return Math.abs(local.getX()) < half.getX()
                && Math.abs(local.getY()) < half.getY()
                && Math.abs(local.getZ()) < half.getZ();
    }

    public synchronized boolean contains(BoundingBox b) {
        BoundingBox bAlign = b.getAlignedBounds();
        Vector3f rel = localize(bAlign.getPosition());
        Vector3f bAlignHalf = bAlign.getHalf();
        return Math.abs(rel.getX()) + bAlignHalf.x < getAlignedBounds().getHalf().getX() && //x overlap
                Math.abs(rel.getY()) + bAlignHalf.y < getAlignedBounds().getHalf().getY() && //y overlap
                Math.abs(rel.getZ()) + bAlignHalf.z < getAlignedBounds().getHalf().getZ();   //z overlap
    }

    public synchronized boolean intersects(BoundingBox b) {
        BoundingBox bAlign = b.getAlignedBounds();
        Vector3f rel = Vector3f.sub(bAlign.getPosition(), position, null);
        Vector3f bAlignHalf = bAlign.getHalf();
        return Math.abs(rel.getX()) - bAlignHalf.x < getAlignedBounds().getHalf().getX() && //x overlap
                Math.abs(rel.getY()) - bAlignHalf.y < getAlignedBounds().getHalf().getY() && //y overlap
                Math.abs(rel.getZ()) - bAlignHalf.z < getAlignedBounds().getHalf().getZ();   //z overlap

    }

    public boolean valueInRange(float value, float min, float max) {
        return (value >= min) && (value <= max);
    }

    public static BoundingBox boundsFromVerts(boolean aligned, Vector3f... verts) {
        Vector3f min = new Vector3f(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
        Vector3f max = new Vector3f(-Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE);
        for (Vector3f v : verts) {
            if (v.getX() > max.getX()) {
                max.setX(v.getX());
            }
            if (v.getY() > max.getY()) {
                max.setY(v.getY());
            }
            if (v.getZ() > max.getZ()) {
                max.setZ(v.getZ());
            }
            if (v.getX() < min.getX()) {
                min.setX(v.getX());
            }
            if (v.getY() < min.getY()) {
                min.setY(v.getY());
            }
            if (v.getZ() < min.getZ()) {
                min.setZ(v.getZ());
            }
        }
        return boundsFromMinMax(aligned, min, max);
    }

    public static BoundingBox boundsFromVerts(Vector3f... verts) {
        return boundsFromVerts(false, verts);
    }

    public static BoundingBox boundsFromMinMax(boolean aligned, Vector3f min, Vector3f max) {
        Vector3f dim = Vector3f.sub(max, min, null);
        Vector3f half = new Vector3f(dim);
        half.scale(0.5f);
        Vector3f pos = Vector3f.add(min, half, null);
        BoundingBox b = new BoundingBox(pos, dim, aligned);
        b.create();
        return b;
    }

    public static BoundingBox boundsFromMinMax(Vector3f min, Vector3f max) {
        return boundsFromMinMax(false, min, max);
    }

    public static BoundingBox boundsFromBounds(boolean aligned, ArrayList<? extends Boundable> bounds) {
        Vector3f[] verts = new Vector3f[bounds.size() * 2];
        for (int i = 0; i < bounds.size(); i++) {
            BoundingBox b = bounds.get(i).getBounds().getAlignedBounds();
            Vector3f max = Vector3f.add(b.getPosition(), b.getHalf(), null);
            Vector3f min = Vector3f.sub(b.getPosition(), b.getHalf(), null);
            verts[2 * i] = max;
            verts[2 * i + 1] = min;
        }
        return boundsFromVerts(aligned, verts);
    }

    public static BoundingBox boundsFromBounds(ArrayList<? extends Boundable> bounds) {
        return boundsFromBounds(false, bounds);
    }

    @Override
    public synchronized BoundingBox getBounds() {
        return this;
    }

    public static BoundingBox boundsFromRectangularPoints(Vector3f[] points) {
        if (points.length != 8) {
            throw new RuntimeException("Not a rectangular prism. Not 8 vertices");
        }
        Vector3f base = points[0];
        ArrayList<Vector3f> differences = new ArrayList<Vector3f>(7);
        float maxLength = 0;
        int index = 0;
        for (int i = 1; i < points.length; i++) {
            differences.add(Vector3f.sub(points[i], base, null));
            float length = differences.get(i - 1).lengthSquared();
            if (length > maxLength) {
                index = i - 1;
                maxLength = length;
            }
        }
        differences.remove(index);

        ArrayList<Vector3f> copy = new ArrayList<Vector3f>(differences);
        ArrayList<Vector3f> doubleCopy = new ArrayList<Vector3f>(copy);
        int count = 0;
        for (Vector3f v : copy) {
            for (Vector3f other : doubleCopy) {
                Vector3f addition = Vector3f.add(v, other, null);
                int toRemove = -1;
                for (int i = 0; i < differences.size(); i++) {
                    if (Utilities.approximate(differences.get(i), addition)) {
                        toRemove = i;
                    }
                }
                if (toRemove != -1) {
                    differences.remove(toRemove);
                    count++;
                }
            }
        }
        Vector3f dim = new Vector3f(differences.get(0).length(),
                differences.get(1).length(), differences.get(2).length());

        Vector3f[] axes = new Vector3f[3];
        for (int i = 0; i < axes.length; i++) {
            axes[i] = differences.get(i).normalise(null);
        }

        Quaternion orientation = Utilities.quatFromBasis(axes[0], axes[1], axes[2]);
        //orientation.negate();
        
        Vector3f pos = new Vector3f();
        for (Vector3f v : points) {
            Vector3f.add(pos, v, pos);
        }
        pos.scale(1f / 8f);

        for (Vector3f a : axes) {
            VectorGraphic vg = new VectorGraphic(pos, a);
            vg.create();
            ThreeDGraphicsManager.getInstance().add(vg);
        }

        BoundingBox b = new BoundingBox(pos, dim, orientation);
        b.create();
        return b;
    }

    public synchronized void setDimension(Vector3f dimension) {
        this.dimension = dimension;
    }

    @Override
    public String toString() {
        return getFullName() + position + dimension;
    }
}
