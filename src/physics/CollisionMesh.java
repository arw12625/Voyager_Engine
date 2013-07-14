/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import game.GameObject;
import graphics.Face;
import graphics.Mesh;
import graphics.ThreeDModel;
import java.util.ArrayList;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Andy
 */
public class CollisionMesh extends GameObject implements Boundable {
    
    Octree<Plane> planes;
    
    public CollisionMesh(BoundingBox b) {
        planes = new Octree<Plane>(b);
    }
    
    public CollisionMesh(Mesh collision) {
        this(collision.getBounds());
        addMesh(collision);
    }
    
    public CollisionMesh(ArrayList<Mesh> collision) {
        this(BoundingBox.boundsFromBounds(collision));
        for(Mesh m : collision) {
            addMesh(m);
        }
    }

    @Override
    public BoundingBox getBounds() {
        return planes.getBounds();
    }

    public void addMesh(Mesh collision) {
        for (Face face : collision.getFaces()) {

            Vector3f[] verts = new Vector3f[3];

            for (int i = 0; i < 3; i++) {
                verts[i] = collision.getVertices().get(face.getVertexIndices()[i]);
            }

            planes.insert(new Plane(verts, collision.getNormals().get(face.getNormalIndices()[0])));
        }
    }
    
    public ArrayList<Plane> getPlanes(PhysicalEntity e) {
        return planes.queryRange(e.getAlignedBounds());
    }
    
    public boolean isEmpty() {
        return planes == null || planes.isEmpty();
    }
    
}
