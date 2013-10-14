/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import game.GameObject;
import graphics.ThreeDGraphicsManager;
import java.util.ArrayList;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Andy
 */
public class Octree<T extends Boundable> extends GameObject implements Boundable {

    final int MAX_OBJECTS = 250;
    ArrayList<T> objects;
    boolean hasSubdivided = false;
    Octree children[];
    BoundingBox boundary;
    int numChildren = 0;
    int myNumber = 0;
    // TODO: Delete
    int max = -1;
    static int total = 0;
    static int timesCalled = 0;
    static boolean alreadyCounting = false;
    static int totalObjects = 0;

    public Octree(T[] objects, BoundingBox boundary) {
        this(boundary);
        for (T object : objects) {
            insert(object);
        }
    }

    public Octree(BoundingBox boundary) {
        this.boundary = boundary;
        this.objects = new ArrayList<T>();
        this.hasSubdivided = false;
        myNumber = total++;
    }

    public void insert(T object) {
        if (!boundary.intersects(object.getBounds())) {
            return;
        }
        if (!hasSubdivided) {
            if (objects.size() > MAX_OBJECTS) {
                subdivide();
                ArrayList<T> oldObjects = objects;
                objects = new ArrayList<T>();
                for (T obj : oldObjects) {
                    insert(obj);
                }
            } else {
                objects.add(object);
            }
        } else {
            ArrayList<Octree> toAddTo = new ArrayList<Octree>(9);
            for (Octree Octree : children) {
                if (Octree.boundary.intersects(object.getBounds())) {
                    toAddTo.add(Octree);
                }
            }
            if (toAddTo.size() == 8) {
                this.objects.add(object);
            } else {
                for (Octree Octree : toAddTo) {
                    Octree.insert(object);
                }
            }
        }
    }

    public ArrayList<T> queryRange(BoundingBox range) {
        ArrayList<T> objectsInRange = new ArrayList<T>();
        for (T obj : objects) {
            if (range.intersects(obj.getBounds())) {
                objectsInRange.add(obj);
            }
        }
        if (hasSubdivided) {
            for (Octree octree : children) {
                if (octree.boundary.intersects(range)) {
                    objectsInRange.addAll(octree.queryRange(range));
                }
            }
            //System.out.println("objectsInRange.size(): " + objectsInRange.size() + "  || total: " + total + "  || totalObjects: " + getEntitiesLength());
        }
        return objectsInRange;
    }

    private int getEntitiesLength() {
        int count = objects.size();
        if (hasSubdivided) {
            for (Octree Octree : children) {
                count += Octree.getEntitiesLength();
            }
        }
        return count;
    }

    private void subdivide() {
        if (hasSubdivided) {
            System.err.println("Error!  Trying to subdivide an already subdivided Octree");
            System.exit(1);
        }
        hasSubdivided = true;
        //System.out.println("Subdividing" + boundary);

        Vector3f half = getBounds().getHalf();
        Vector3f newHalf = new Vector3f(half.getX() / 2, half.getY() / 2, half.getZ() / 2);

        //System.out.println(myNumber + ": Subdividing" + boundary);
        //System.out.println("half " + half);
        BoundingBox[] newBounds = {
            new BoundingBox(new Vector3f(-newHalf.getX(), -newHalf.getY(), -newHalf.getZ()), half),
            new BoundingBox(new Vector3f(-newHalf.getX(), -newHalf.getY(), newHalf.getZ()), half),
            new BoundingBox(new Vector3f(-newHalf.getX(), newHalf.getY(), -newHalf.getZ()), half),
            new BoundingBox(new Vector3f(-newHalf.getX(), newHalf.getY(), newHalf.getZ()), half),
            new BoundingBox(new Vector3f(newHalf.getX(), -newHalf.getY(), -newHalf.getZ()), half),
            new BoundingBox(new Vector3f(newHalf.getX(), -newHalf.getY(), newHalf.getZ()), half),
            new BoundingBox(new Vector3f(newHalf.getX(), newHalf.getY(), -newHalf.getZ()), half),
            new BoundingBox(new Vector3f(newHalf.getX(), newHalf.getY(), newHalf.getZ()), half)};

        Vector3f center = getBounds().getCenter();
        children = new Octree[8];
        for(int i = 0; i < children.length; i++) {
            newBounds[i].create();
            newBounds[i].setPosition(Vector3f.add(center, newBounds[i].getPosition(), null));
            children[i] = new Octree(newBounds[i]);
            //graphics.ThreeDGraphicsManager.getInstance().addGraphic3D(newBounds[i], 0);
        }
        

    }

    @Override
    public BoundingBox getBounds() {
        return boundary;
    }

    public boolean isEmpty() {
        return !(objects.size() > 0 || hasSubdivided);
    }
}
