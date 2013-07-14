/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import java.util.ArrayList;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import util.Utilities;

/**
 *
 * @author Andy
 */
public class BoundingBox implements Boundable {

    Vector3f position;
    Vector3f dimension;
    Vector3f half;
    Quaternion orientation;
    
    public BoundingBox(Vector3f position, Vector3f dimension) {
        this(position, dimension, new Quaternion());
    }
    
    public BoundingBox(Vector3f position, Vector3f dimension, Quaternion orientation) {
        this.position = position;
        this.dimension = dimension;
        this.orientation = orientation;
        half = new Vector3f(dimension);
        half.scale(.5f);
    }
    
    public void translate(Vector3f t) {
        Vector3f.add(position, t, position);
    }
    
    public void rotate(Quaternion q) {
        Quaternion.mul(orientation, q, orientation);
    }
    
    public void setPosition(Vector3f position) {
        this.position = position;
    }
    
    public void setOrientation(Quaternion orientation) {
        this.orientation = orientation;
    }
    
    public Vector3f getPosition() {
        return position;
    }
    
    public Quaternion getOrientation() {
        return orientation;
    }
    
    public Vector3f getDimension() {
        return dimension;
    }
    
    public Vector3f getHalf() {
        return half;
    }
    
    public Vector3f getMin() {
        return Vector3f.sub(position, half, null);
    }
    
    public Vector3f getMax() {
        return Vector3f.add(position, half, null);
    }
    
    public Vector3f[] getVertexes() {
        
        Vector3f min = getMin();
        Vector3f max = getMax();
        Vector3f[] verts = new Vector3f[]{
            min,
            new Vector3f(min.getX(), min.getY(), max.getZ()),
            new Vector3f(min.getX(), max.getY(), max.getZ()),
            new Vector3f(min.getX(), max.getY(), min.getZ()),
            new Vector3f(max.getX(), min.getY(), max.getZ()),
            new Vector3f(max.getX(), min.getY(), min.getZ()),
            new Vector3f(max.getX(), max.getY(), min.getZ()),
            max
        };
        for (int i = 0; i < verts.length; i++) {
            verts[i] = Utilities.transform(verts[i], orientation);
        }
        return verts;
        
    }
    
    public Vector3f[] getAxes() {
        Vector3f[] axes = {new Vector3f(1, 0, 0), new Vector3f(0, 1, 0), new Vector3f(0, 0, 1)};
        for (int i = 0; i < axes.length; i++) {
            axes[i] = Utilities.transform(axes[i], orientation);
        }
        return axes;
    }
    
    public Vector3f getCenter() {
        return position;
    }
    
    public boolean contains(Vector3f v) {
        Vector3f min = getMin();
        Vector3f max = getMax();
        if (valueInRange(v.getX(), min.getX(), max.getX())
                && valueInRange(v.getY(), min.getY(), max.getY())
                && valueInRange(v.getZ(), min.getZ(), max.getZ())) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean contains(BoundingBox b) {
        Vector3f min = getMin();
        Vector3f max = getMax();
        Vector3f bMin = b.getMin();
        Vector3f bDim = b.getDimension();
        float bWidth = bDim.getX();
        float bHeight = bDim.getY();
        float bDepth = bDim.getZ();
        if (valueInRange(bMin.getX(), min.getX(), max.getX() - bWidth)
                && valueInRange(bMin.getY(), min.getY(), max.getY() - bHeight)
                && valueInRange(bMin.getZ(), min.getZ(), max.getZ() - bDepth)) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean intersects(BoundingBox b) {
        Vector3f min = getMin();
        Vector3f max = getMax();
        Vector3f bMin = b.getMin();
        Vector3f bMax = b.getMax();
        boolean xOverlap = valueInRange(min.getX(), bMin.getX(), bMax.getX())
                || valueInRange(bMin.getX(), min.getX(), max.getX());
        boolean yOverlap = valueInRange(min.getY(), bMin.getY(), bMax.getY())
                || valueInRange(bMin.getY(), min.getY(), max.getY());
        boolean zOverlap = valueInRange(min.getZ(), bMin.getZ(), bMax.getZ())
                || valueInRange(bMin.getZ(), min.getZ(), max.getZ());
        
        return xOverlap && yOverlap && zOverlap;
    }
    
    public boolean valueInRange(float value, float min, float max) {
        return (value >= min) && (value <= max);
    }
    
    public static BoundingBox boundsFromVerts(Vector3f... verts) {
        Vector3f min = new Vector3f(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
        Vector3f max = new Vector3f(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);
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
        return boundsFromMinMax(min, max);
    }
    
    public static BoundingBox boundsFromMinMax(Vector3f min, Vector3f max) {
        Vector3f dim = Vector3f.sub(max, min, null);
        Vector3f half = new Vector3f(dim);
        half.scale(0.5f);
        Vector3f pos = Vector3f.add(min, half, null);
        return new BoundingBox(pos, dim);
    }
    
    public static BoundingBox boundsFromBounds(ArrayList<? extends Boundable> bounds) {
        Vector3f[] verts = new Vector3f[bounds.size() * 2];
        for(int i = 0; i < bounds.size(); i++) {
            BoundingBox b = bounds.get(i).getBounds();
            verts[2 * i] = b.getMax();
            verts[2 * i + 1] = b.getMin();
        }
        return boundsFromVerts(verts);
    }

    @Override
    public BoundingBox getBounds() {
        return this;
    }
}
