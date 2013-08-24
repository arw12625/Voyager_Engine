/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Andy
 */
public class VectorGraphic extends game.GameObject implements graphics.ThreeD {

    Vector3f position;
    Vector3f vec;

    public VectorGraphic(Vector3f pos, Vector3f vec) {
        this.position = pos;
        this.vec = vec;
    }

    @Override
    public void create() {
        super.create();
    }

    public void setPosition(Vector3f pos) {
        this.position = pos;
    }

    public void setVector(Vector3f v) {
        this.vec = new Vector3f(v);
        if (v.lengthSquared() != 0) {
            vec.normalise();
        }
    }

    @Override
    public void render() {
        glDisable(GL_DEPTH_TEST);
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
        glEnable(GL_DEPTH_TEST);
    }
}
