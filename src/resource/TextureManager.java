/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resource;

import game.Manager;
import game.ResourceManager;
import java.io.IOException;
import java.util.ArrayList;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 *
 * @author Andy
 */
public class TextureManager implements Manager{

    
    ArrayList<TextureResource> textures;
    
    static TextureManager instance;
    
    public static TextureManager getInstance() {
        if(instance == null) {
            instance = new TextureManager();
        }
        return instance;
    }
    
    public TextureResource loadTextureResource(String path) {
        TextureResource t = new TextureResource(path);
        ResourceManager.getInstance().loadResource(t);
        return t;
    }

    @Override
    public void create() {
        textures = new ArrayList<TextureResource>();
    }

    @Override
    public String getName() {
        return "TextureManager";
    }

    @Override
    public void update(int delta) {
    }

    @Override
    public void destroy() {
    }
    
}
