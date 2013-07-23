/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import game.GameObject;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Andy
 */
public class SkySphere extends GameObject implements ThreeD {

    Sphere sphere;

    public SkySphere() {

        this.sphere = new Sphere();
        sphere.setOrientation(GLU.GLU_INSIDE);

    }

    @Override
    public void render() {
        glPushMatrix();
        glLoadIdentity();
        Color.white.bind();
        (new Color(0.45f, 0.5f, 0.6f)).bind();
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
