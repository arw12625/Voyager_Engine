/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import game.GameObject;
import org.lwjgl.util.vector.Vector3f;
import physics.BoundingBox;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Andy
 */
public class BoundingBoxGraphic extends GameObject implements ThreeD {
    
    boolean displayCenter;
    boolean displayVertices;
    boolean displayEdges;
    BoundingBox b;
    
    public BoundingBoxGraphic(BoundingBox b) {
        displayCenter = true;
        displayVertices = true;
        displayEdges = true;
        this.b = b;
    }
    
    public void setBoundingBox(BoundingBox b) {
        this.b = b;
    }
    
    @Override
    public synchronized void render() {
        Vector3f[] verts = b.getGlobalVertices();
        Vector3f[] alignedVerts = b.getAlignedBounds().getGlobalVertices();
        if(displayCenter) {
            glColor3f(1f, 1f, 1f);

            glDisable(GL_DEPTH_TEST);
            glPointSize(20);
            glBegin(GL_POINTS);
            myVertex3f(b.getPosition());
            glEnd();
            glEnable(GL_DEPTH_TEST);
        }
        
        if (displayVertices) {
            glColor3f(1f, 1f, 1f);

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
        if (displayEdges) {
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
    
    public void drawCenter(boolean center) {
        this.displayCenter = center;
    }

    public void drawVertices(boolean vertices) {
        this.displayVertices = vertices;
    }

    public void drawEdges(boolean edges) {
        this.displayEdges = edges;
    }
}
