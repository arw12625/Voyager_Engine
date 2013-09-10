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
public class Menu extends GameObject implements TwoD {

    String name;
    float x, y;
    float width, height;
    TwoD graphic;
    Menu[] sub;
    boolean absolute;
    boolean show;
    boolean showChildren;
    update.Action action;

    public Menu() {
        this("Wrapper");
    }

    public Menu(String name) {
        this(name, 0, 0, false, true, null);
    }

    public Menu(float x, float y, boolean absolute, boolean show, Menu[] sub) {
        this("Filler", x, y, 0, 0, absolute, null, show, sub, null);
    }

    public Menu(String name, float x, float y, boolean absolute, boolean show, Menu[] sub) {
        this(name, x, y, 0, 0, absolute, null, show, sub, null);
    }

    public Menu(float x, float y, boolean absolute, HudGraphic hg, boolean show, Menu[] sub, update.Action action) {
        this("Filler", x, y, hg.getWidth(), hg.getHeight(), absolute, hg, show, sub, action);
    }

    public Menu(String name, float x, float y, boolean absolute, HudGraphic hg, boolean show, Menu[] sub, update.Action action) {
        this(name, x, y, hg.getWidth(), hg.getHeight(), absolute, hg, show, sub, action);
    }

    public Menu(String name, float x, float y, float width, float height, boolean absolute, TwoD graphic, boolean show, Menu[] sub, update.Action action) {

        this.name = name;
        this.width = width;
        this.height = height;
        this.graphic = graphic;
        this.show = show;
        this.sub = sub;
        this.absolute = absolute;
        if (absolute) {
            this.x = x;
            this.y = y;
        }
        translate(x, y);
        this.action = action;
        this.showChildren = true;
    }

    @Override
    public void render() {
        if (show && graphic != null) {
            glPushMatrix();
            glTranslatef(x, y, 0);
            graphic.render();
            glPopMatrix();
        }
        if (showChildren) {
            if (sub != null) {
                for (Menu m : sub) {
                    m.render();
                }
            }
        }
    }

    public void mouseClick(int mx, int my) {
        if (action != null && show && inArea(mx, my)) {
            action.execute();
        }
        if (showChildren && sub != null) {
            for (Menu m : sub) {
                m.mouseClick(mx, my);
            }
        }
    }

    public void hide() {
        show = false;
    }

    public void show() {
        show = true;
    }

    public void hideChildren() {
        showChildren = false;
    }

    public void showChildren() {
        showChildren = true;
    }

    public void hideBranch() {
        hide();
        hideChildren();
    }

    public void showBranch() {
        show();
        showChildren();
    }

    private boolean inArea(int mx, int my) {
        return x <= mx && mx <= x + width && y <= my && my <= y + height;
    }

    private void translate(float x, float y) {
        if (!absolute) {
            this.x += x;
            this.y += y;
        }
        if (sub != null) {
            for (Menu m : sub) {
                m.translate(x, y);
            }
        }
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public void setShowChildren(boolean showChildren) {
        this.showChildren = showChildren;
    }

    public void setShowBranch(boolean showBranch) {
        setShow(showBranch);
        setShowChildren(showBranch);
    }
}
