/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Andy
 */
public class ThreeDTestGraphic implements graphics.ThreeD {

    
    Vector3f[] points;
    org.newdawn.slick.Color c;

    public ThreeDTestGraphic() {
        points = new Vector3f[3];
        for (int i = 0; i < points.length; i++) {
            points[i] = getRandomPoint();
        }
        c = new org.newdawn.slick.Color(.5f, .5f, .5f);
    }
    
    @Override
    public void render() {
        glColor3f(.5f, .5f, .5f);
        glBegin(GL_QUADS);
        glVertex3f(-10, 10, 50);
        glVertex3f(10, 10, 50);
        glVertex3f(10, -10, 50);
        glVertex3f(-10, -10, 50);
        glEnd();

        glRotatef(-90, 0f, 1f, 0f);
        glBegin(GL_QUADS);
        glVertex3f(-10, 10, 50);
        glVertex3f(10, 10, 50);
        glVertex3f(10, -10, 50);
        glVertex3f(-10, -10, 50);
        glEnd();

        glRotatef(-90, 0f, 1f, 0f);
        glBegin(GL_QUADS);
        glVertex3f(-10, 10, 50);
        glVertex3f(10, 10, 50);
        glVertex3f(10, -10, 50);
        glVertex3f(-10, -10, 50);
        glEnd();

        glRotatef(-90, 0f, 1f, 0f);
        glBegin(GL_QUADS);
        glVertex3f(-10, 10, 50);
        glVertex3f(10, 10, 50);
        glVertex3f(10, -10, 50);
        glVertex3f(-10, -10, 50);
        glEnd();

    }

    private Vector3f getRandomPoint() {
        return new Vector3f((float) Math.random() * 20 - 10, (float) Math.random() * 20 - 10, 0);
    }
    
}
