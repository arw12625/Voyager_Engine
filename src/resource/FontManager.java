/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resource;

import game.GameObject;
import game.Manager;
import java.awt.Font;
import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;

/**
 *
 * @author Andy
 */
public class FontManager extends Manager {

    public ArrayList<FontResource> fonts;
    public static FontManager instance;

    @Override
    public void create() {
        super.create();
        fonts = new ArrayList<FontResource>();
        createFont("default");
    }

    public static FontManager getInstance() {
        if (instance == null) {
            instance = new FontManager();
        }
        return instance;
    }

    public FontResource createFont(String name) {
        return createFont(name, name + ".fnt", name + "_0.png");
    }

    public FontResource createFont(String name, Color c) {
        return createFont(name, name + ".fnt", name + "_0.png", c);
    }

    public FontResource createFont(String name, String fontPath, String texturePath) {
        return createFont(name, fontPath, texturePath, Color.black);
    }

    public FontResource createFont(String name, String fontPath, String texturePath, Color c) {
        FontResource font = new FontResource(name, fontPath, texturePath, c);
        font.create();
        fonts.add(font);
        return font;
    }

    public FontResource getFont(String name) {
        FontResource font = null;
        for (FontResource fo : fonts) {
            if (fo.getFullName().equals(name)) {
                font = fo;
            }
        }
        return font;
    }

    public FontResource getFont(int index) {
        return fonts.get(index);
    }

    public FontResource getDefaultFont() {
        return getFont(0);
    }

    @Override
    public void remove(GameObject obj) {
        if(fonts.contains(obj)) {
            fonts.remove(obj);
        }
    }
}
