/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import game.GameObject;
import graphics.BoundingBoxGraphic;
import graphics.ThreeDGraphicsManager;
import java.util.ArrayList;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Andy
 */
public class CollisionMesh extends GameObject implements Boundable {
    
    Octree<Plane> planes;
    BoundingBoxGraphic bbg;
    
    public CollisionMesh(BoundingBox b) {
        planes = new Octree<Plane>(b);
        planes.create();
        bbg = new BoundingBoxGraphic(b);
        bbg.create();
        ThreeDGraphicsManager.getInstance().add(bbg);
        
    }
    
    public CollisionMesh(Mesh collision) {
        this(collision.getBounds());
        addMesh(collision);
    }
    
    public CollisionMesh(ArrayList<Mesh> collision) {
        this(BoundingBox.boundsFromBounds(collision, null));
        for(Mesh m : collision) {
            addMesh(m);
        }
    }
    
    @Override
    public BoundingBox getBounds() {
        return planes.getBounds();
    }

    public void addMesh(Mesh collision) {
        for (graphics.Face face : collision.getFaces()) {
            
            Vector3f[] verts = new Vector3f[3];

            for (int i = 0; i < 3; i++) {
                verts[i] = collision.getVertices().get(face.getVertexIndices()[i]);
            }

            Plane p = new Plane(verts, collision.getNormals().get(face.getNormalIndices()[0]));
            //ThreeDGraphicsManager.getInstance().add(new BoundingBoxGraphic(p.getBounds()));
            
            planes.insert(p);
        }
    }
    
    public void addMeshes(ArrayList<Mesh> meshes) {
        for(Mesh m : meshes) {
            addMesh(m);
        }
    }
    
    public ArrayList<Plane> getPlanes(BoundingBox b) {
        //bbg.setBoundingBox(getBounds());
        return planes.queryRange(b);
    }
    
    public boolean isEmpty() {
        return planes == null || planes.isEmpty();
    }
    
}
