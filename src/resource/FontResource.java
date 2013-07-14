/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resource;

import game.Resource;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Font;
import org.newdawn.slick.font.effects.ColorEffect;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.TextureImpl;

/**
 *
 * @author Andy
 */
public class FontResource extends Resource {

    String name;
    String fontPath;
    String texturePath;
    Font aFont;
    Color color;

    public FontResource(String fontPath, String texturePath) {
        this(fontPath, fontPath, texturePath);
    }

    public FontResource(String name, String fontPath, String texturePath) {
        this(name, fontPath, texturePath, Color.black);
    }

    public FontResource(String name, String fontPath, String texturePath, Color color) {
        this.name = name;
        this.fontPath = fontPath;
        this.texturePath = texturePath;
        this.color = color;
    }

    @Override
    public boolean load() {
        try {
            TextureImpl.unbind();
            aFont = new AngelCodeFont("res/" + fontPath, "res/" + texturePath);
        } catch (SlickException ex) {
            Logger.getLogger(FontResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public void drawString(String text, int x, int y) {
        drawString(text, x, y, this.color);
    }

    public void drawString(String text, int x, int y, Color c) {
        TextureImpl.unbind();
        aFont.drawString(x, y, text, c);
    }

    public int getWidth() {
        return aFont.getWidth("w");
    }

    public int getHeight() {
        return aFont.getLineHeight();
    }
}
