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
    org.newdawn.slick.Color color;
    
    public enum SkyType {
        PLAIN_DAY (new org.newdawn.slick.Color(0.45f, 0.5f, 0.6f)),
        PLAIN_NIGHT (new org.newdawn.slick.Color(0.05f, 0.05f, 0.1f));
        
        private final org.newdawn.slick.Color color;
        
        SkyType(org.newdawn.slick.Color color) {
            this.color = color;
        }
        
        public org.newdawn.slick.Color getColor() {
            return color;
        }
    }

    public SkySphere() {
        this(SkyType.PLAIN_DAY);
    }
    
    public SkySphere(SkyType st) {
        this(st.getColor());
    }
    
    public SkySphere(Color c) {
        this.sphere = new Sphere();
        sphere.setOrientation(GLU.GLU_INSIDE);
        this.color = c;
    }

    @Override
    public void render() {
        glPushMatrix();
        glLoadIdentity();
        Color.white.bind();
        color.bind();
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
