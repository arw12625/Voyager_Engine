/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import java.util.ArrayList;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Andy
 */
public class Octree<T extends Boundable> implements Boundable {
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
	    for (Octree Octree : children) {
		if (Octree.boundary.intersects(range)) {
		    objectsInRange.addAll(Octree.queryRange(range));
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
        Vector3f min = boundary.getMin();
	Vector3f mymax = boundary.getMax();
	Vector3f myhalf = new Vector3f((float)(min.getX() + mymax.getX()) / 2.0f,
				     (float)(min.getY() + mymax.getY()) / 2.0f,
				     (float)(min.getZ() + mymax.getZ()) / 2.0f);
	Vector3f max = boundary.getDimension();
        Vector3f half = (Vector3f) new Vector3f(max).scale(0.5f);
				    

	//System.out.println(myNumber + ": Subdividing" + boundary);
	//System.out.println("min " + min);
	//System.out.println("half " + half);
        children = new Octree[] {
	    new Octree(new BoundingBox(min,
					 new Vector3f(half.getX(), half.getY(), half.getZ())))
	    ,new Octree(new BoundingBox(new Vector3f(half.getX() + min.getX(), min.getY(), min.getZ()),
					  new Vector3f(max.getX(), half.getY(), half.getZ())))
	    ,new Octree(new BoundingBox(new Vector3f(min.getX(), min.getY(), half.getZ() + min.getZ()),
					  new Vector3f(half.getX(), half.getY(), max.getZ())))
	    ,new Octree(new BoundingBox(new Vector3f(half.getX() + min.getX(), min.getY(), half.getZ() + min.getZ()),
					  new Vector3f(max.getX(), half.getY(), max.getZ())))
 
	    ,new Octree(new BoundingBox(new Vector3f(min.getX(), half.getY() + min.getY(), min.getZ()),
					  new Vector3f(half.getX(), max.getY(), half.getZ())))
	    ,new Octree(new BoundingBox(new Vector3f(half.getX() + min.getX(), half.getY() + min.getY(), min.getZ()),
					  new Vector3f(max.getX(), max.getY(), half.getZ())))
	    ,new Octree(new BoundingBox(new Vector3f(min.getX(), half.getY() + min.getY(), half.getZ() + min.getZ()),
					  new Vector3f(half.getX(), max.getY(), max.getZ())))
	    ,new Octree(new BoundingBox(new Vector3f(half.getX() + min.getX(), half.getY() + min.getY(), half.getZ() + min.getZ()),
					  new Vector3f(max.getX(), max.getY(), max.getZ())))
	};
	
    }

    @Override
    public BoundingBox getBounds() {
        return boundary;
    }
    
    public boolean isEmpty() {
        return objects.size() > 0;
    }
}
