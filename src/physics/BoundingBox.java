/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import java.util.ArrayList;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import util.Utilities;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Andy
 */
public class BoundingBox extends game.GameObject implements Boundable, graphics.ThreeD {

    private Vector3f position;
    private Vector3f dimension;
    private Vector3f half;
    private Quaternion orientation;
    private Vector3f[] localVerts;
    private Vector3f[] localAxes;
    private Vector3f[] globalVerts;
    private Vector3f[] globalAxes;
    private BoundingBox alignedBounds;
    private boolean aligned;
    private static boolean lines = true;
    private static boolean points = true;

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

    public void translate(Vector3f t) {
        Vector3f.add(position, t, position);
        deriveGlobalData();
    }

    public void rotate(Quaternion q) {
        Quaternion.mul(orientation, q, orientation);
        deriveGlobalData();
    }

    public void setPosition(Vector3f position) {
        this.position = position;
        deriveGlobalData();
    }

    public void setOrientation(Quaternion orientation) {
        this.orientation = orientation;
        deriveGlobalData();
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

    public Vector3f[] getLocalVertices() {
        return localVerts;
    }

    public Vector3f[] getLocalAxes() {
        return localAxes;
    }

    public Vector3f[] getGlobalVertices() {
        return globalVerts;
    }

    public Vector3f[] getGlobalAxes() {
        return globalAxes;
    }

    public Vector3f getCenter() {
        return position;
    }

    public BoundingBox getAlignedBounds() {
        if (aligned) {
            return this;
        } else {
            return alignedBounds;
        }
    }

    private void deriveGlobalData() {
        for (int i = 0; i < globalVerts.length; i++) {
            globalVerts[i] = Utilities.transform(localVerts[i], orientation);
            globalVerts[i].translate(position.getX(), position.getY(), position.getZ());
        }

        for (int i = 0; i < globalAxes.length; i++) {
            globalAxes[i] = Utilities.transform(localAxes[i], orientation);
        }

        if (!aligned) {
            alignedBounds = BoundingBox.boundsFromVerts(true, globalVerts);
        } else {
            alignedBounds = null;
        }
    }

    public Vector3f localize(Vector3f v) {
        return Utilities.inverseTransform(Vector3f.sub(v, position, null), orientation);
    }

    public boolean contains(Vector3f v) {
        Vector3f local = localize(v);
        return Math.abs(local.getX()) < half.getX()
                && Math.abs(local.getY()) < half.getY()
                && Math.abs(local.getZ()) < half.getZ();
    }

    public boolean contains(BoundingBox b) {
        BoundingBox bAlign = b.getAlignedBounds();
        Vector3f rel = localize(bAlign.getPosition());
        Vector3f bAlignHalf = bAlign.getHalf();
        boolean xOverlap = Math.abs(rel.getX()) + bAlignHalf.x < getAlignedBounds().getHalf().getX();
        boolean yOverlap = Math.abs(rel.getY()) + bAlignHalf.y < getAlignedBounds().getHalf().getY();
        boolean zOverlap = Math.abs(rel.getZ()) + bAlignHalf.z < getAlignedBounds().getHalf().getZ();
        return xOverlap && yOverlap && zOverlap;
    }

    public boolean intersects(BoundingBox b) {
        BoundingBox bAlign = b.getAlignedBounds();
        Vector3f rel = localize(bAlign.getPosition());
        Vector3f bAlignHalf = bAlign.getHalf();
        boolean xOverlap = Math.abs(rel.getX()) - bAlignHalf.x < getAlignedBounds().getHalf().getX();
        boolean yOverlap = Math.abs(rel.getY()) - bAlignHalf.y < getAlignedBounds().getHalf().getY();
        boolean zOverlap = Math.abs(rel.getZ()) - bAlignHalf.z < getAlignedBounds().getHalf().getZ();
        return xOverlap && yOverlap && zOverlap;
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
    public BoundingBox getBounds() {
        return this;
    }

    @Override
    public void render() {
        Vector3f[] verts = getGlobalVertices();
        Vector3f[] alignedVerts = getAlignedBounds().getGlobalVertices();
        if (points) {
            glColor3f(1f, 1f, 1f);

            glDisable(GL_DEPTH_TEST);
            glPointSize(20);
            glBegin(GL_POINTS);
            myVertex3f(position);
            glEnd();
            glEnable(GL_DEPTH_TEST);

            glPointSize(10);
            glBegin(GL_POINTS);
            for (Vector3f v : verts) {
                myVertex3f(v);
            }
            for (Vector3f v : alignedVerts) {
                myVertex3f(v);
            }
            glEnd();
        }
        if (lines) {
            glColor3f(1f, 1f, 1f);
            glLineWidth(3);
            Vector3f[][] cat = {verts, alignedVerts};
            glBegin(GL_LINES);
            for (Vector3f[] boxVerts : cat) {
                myVertex3f(boxVerts[0]);
                myVertex3f(boxVerts[1]);
                myVertex3f(boxVerts[0]);
                myVertex3f(boxVerts[3]);
                myVertex3f(boxVerts[0]);
                myVertex3f(boxVerts[5]);
                myVertex3f(boxVerts[1]);
                myVertex3f(boxVerts[2]);
                myVertex3f(boxVerts[1]);
                myVertex3f(boxVerts[4]);
                myVertex3f(boxVerts[2]);
                myVertex3f(boxVerts[3]);
                myVertex3f(boxVerts[2]);
                myVertex3f(boxVerts[7]);
                myVertex3f(boxVerts[3]);
                myVertex3f(boxVerts[6]);
                myVertex3f(boxVerts[4]);
                myVertex3f(boxVerts[5]);
                myVertex3f(boxVerts[4]);
                myVertex3f(boxVerts[7]);
                myVertex3f(boxVerts[5]);
                myVertex3f(boxVerts[6]);
                myVertex3f(boxVerts[6]);
                myVertex3f(boxVerts[7]);
            }
            glEnd();
        }
    }

    public void myVertex3f(Vector3f v) {
        glVertex3f(v.getX(), v.getY(), v.getZ());
    }

    public void drawPoints(boolean points) {
        BoundingBox.points = points;
    }

    public void drawlines(boolean lines) {
        BoundingBox.lines = lines;
    }
}
