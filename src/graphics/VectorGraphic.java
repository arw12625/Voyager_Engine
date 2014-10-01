/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.opengl.GL11.*;
import physics.Positionable;

/**
 *
 * @author Andy
 */
public class VectorGraphic extends game.GameObject implements Positionable, graphics.ThreeD {

    Vector3f position;
    Vector3f vec;
    boolean normalised;

    public VectorGraphic(Vector3f pos, Vector3f vec) {
        this(pos, vec, false);
    }
    
    public VectorGraphic(Vector3f pos, Vector3f vec, boolean normalised) {
        this.position = pos;
        this.vec = vec;
        this.normalised = normalised;
        if (normalised && vec.lengthSquared() != 0) {
            vec.normalise(vec);
        }
    }

    @Override
    public void create() {
        super.create();
    }

    @Override
    public Vector3f getPosition() {
        return position;
    }
    
    @Override
    public void setPosition(Vector3f pos) {
        this.position = pos;
    }

    public void setVector(Vector3f v) {
        this.vec = new Vector3f(v);
        if (normalised && v.lengthSquared() != 0) {
            vec.normalise();
        }
    }

    @Override
    public void render() {
        glLineWidth(2);
        glPushMatrix();
        glTranslatef(position.getX(), position.getY(), position.getZ());
        glBegin(GL_LINES);
        glColor3f(1, 1, 1);
        glVertex3f(0, 0, 0);
        glColor3f(1, 0, 0);
        glVertex3f(vec.getX(), vec.getY(), vec.getZ());
        glEnd();
        glPopMatrix();
    }
}
