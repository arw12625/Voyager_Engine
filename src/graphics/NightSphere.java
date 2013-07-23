/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import game.GameObject;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;
import org.newdawn.slick.Color;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Andy
 */
public class NightSphere extends GameObject implements ThreeD {

    Sphere sphere;

    public NightSphere() {

        this.sphere = new Sphere();
        sphere.setOrientation(GLU.GLU_INSIDE);

    }

    @Override
    public void render() {
        glPushMatrix();
        glLoadIdentity();
        Color.white.bind();
        (new Color(0.05f, 0.05f, 0.1f)).bind();
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_LIGHTING);
        sphere.draw(100, 10, 10);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_LIGHTING);
        glPopMatrix();
    }
}
