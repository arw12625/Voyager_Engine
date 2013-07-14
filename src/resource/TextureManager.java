/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resource;

import game.GameObject;
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
public class TextureManager extends Manager {

    ArrayList<TextureResource> textures;
    static TextureManager instance;

    public static TextureManager getInstance() {
        if (instance == null) {
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
    public void remove(GameObject obj) {
        if (textures.contains(obj)) {
            textures.remove(obj);
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public boolean add(GameObject obj) {
        if (obj instanceof TextureResource) {
            textures.add((TextureResource) obj);
            return true;
        }
        return false;
    }
}
