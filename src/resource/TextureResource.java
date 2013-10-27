/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resource;

import java.io.IOException;
import java.io.InputStream;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.opengl.TextureImpl;

/**
 *
 * @author Andy
 */
public class TextureResource extends GraphicsResource implements Texture {

    String name;
    String path;
    Texture t;
    InputStream s;

    public TextureResource(String path) {
        this.name = path;
        this.path = path;
    }

    @Override
    public boolean load() {
        s = ResourceLoader.getResourceAsStream("res/" + path);
        return true;
    }
    
    @Override
    public boolean processGraphics() {
        try {
            TextureImpl.unbind();
            t = TextureLoader.getTexture("PNG", s);
        } catch (IOException ex) {
            ex.printStackTrace();
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
