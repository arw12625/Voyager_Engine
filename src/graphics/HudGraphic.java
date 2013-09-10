/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import game.GameObject;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Andy
 */
public class HudGraphic extends GameObject implements TwoD {

    String name;
    resource.TextureResource texture;
    int width, height;
    String text;
    resource.FontResource font;
    int textx, texty;

    public HudGraphic(resource.TextureResource texture, String text) {
        this(texture, text, resource.FontManager.getInstance().getFont(0), 0, 0);
    }
    
    public HudGraphic(String name, resource.TextureResource texture, String text) {
        this(name, texture, text, resource.FontManager.getInstance().getFont(0), 0, 0);
    }

    public HudGraphic(resource.TextureResource texture, String text, resource.FontResource font, int textx, int texty) {
        this(text, texture, text, font, textx, texty);
    }
    public HudGraphic(String name, resource.TextureResource texture, String text, resource.FontResource font, int textx, int texty) {

        this.name = name;
        
        if (texture != null) {
            this.texture = texture;
            this.width = texture.getImageWidth();
            this.height = texture.getImageHeight();
        } else {
            this.width = 0;
            this.height = 0;
        }

        if (text != null) {
            this.text = text;
        }
        if (font != null) {
            this.font = font;
        }

        this.textx = textx;
        this.texty = texty;
    }

    @Override
    public void render() {
        if (texture != null) {
            org.newdawn.slick.Color.white.bind();
            texture.bind();
            glBegin(GL_QUADS);
            glTexCoord2f(0, 0);
            glVertex2f(0, 0);
            glTexCoord2f(texture.getWidth(), 0);
            glVertex2f(width, 0);
            glTexCoord2f(texture.getWidth(), texture.getHeight());
            glVertex2f(width, height);
            glTexCoord2f(0, texture.getHeight());
            glVertex2f(0, height);
            glEnd();
            glBindTexture(GL_TEXTURE_2D, 0);
        }

        if (text != null) {
            font.drawString(text, textx, texty);
            glBindTexture(GL_TEXTURE_2D, 0);
        }
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
