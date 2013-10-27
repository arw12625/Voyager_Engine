/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resource;

import game.GameObject;
import game.Manager;
import java.util.ArrayList;

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
        t.create();
        textures.add(t);
        return t;
    }

    @Override
    public void create() {
        super.create();
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

}
