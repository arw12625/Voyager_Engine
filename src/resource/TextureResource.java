/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resource;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.opengl.TextureImpl;

/**
 *
 * @author Andy
 */
public class TextureResource extends Resource implements Texture {

    String name;
    String path;
    Texture t;
    
    public TextureResource(String path) {
        this.name = path;
        this.path = path;
    }
    
    @Override
    public boolean load() {
        try {
            TextureImpl.unbind();
            t = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/" + path));
        } catch (IOException ex) {
            Logger.getLogger(TextureResource.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    @Override
    public void release() {
        t.release();
    }

    @Override
    public boolean hasAlpha() {
        return t.hasAlpha();
    }

    @Override
    public String getTextureRef() {
        return t.getTextureRef();
    }

    @Override
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, getTextureID());
    }

    @Override
    public int getImageHeight() {
        return t.getImageHeight();
    }

    @Override
    public int getImageWidth() {
        return t.getImageWidth();
    }

    @Override
    public float getHeight() {
        return t.getHeight();
    }

    @Override
    public float getWidth() {
        return t.getWidth();
    }

    @Override
    public int getTextureHeight() {
        return t.getTextureHeight();
    }

    @Override
    public int getTextureWidth() {
        return t.getTextureWidth();
    }

    @Override
    public int getTextureID() {
        return t.getTextureID();
    }

    @Override
    public byte[] getTextureData() {
        return t.getTextureData();
    }

    @Override
    public void setTextureFilter(int i) {
        t.setTextureFilter(i);
    }
    
    
}
