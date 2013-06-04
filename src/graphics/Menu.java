/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import static org.lwjgl.opengl.GL11.*;
import update.Event;

/**
 *
 * @author Andy
 */
public class Menu implements TwoDGraphic {

    String name;
    float x, y;
    float width, height;
    TwoDGraphic td;
    Menu[] sub;
    boolean absolute;
    boolean show;
    boolean showChildren;
    Event event;

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

    public Menu(float x, float y, boolean absolute, HudGraphic hg, boolean show, Menu[] sub, Event event) {
        this("Filler", x, y, hg.getWidth(), hg.getHeight(), absolute, hg, show, sub, event);
    }

    public Menu(String name, float x, float y, boolean absolute, HudGraphic hg, boolean show, Menu[] sub, Event event) {
        this(name, x, y, hg.getWidth(), hg.getHeight(), absolute, hg, show, sub, event);
    }

    public Menu(String name, float x, float y, float width, float height, boolean absolute, TwoDGraphic td, boolean show, Menu[] sub, Event event) {

        this.name = name;
        this.width = width;
        this.height = height;
        this.td = td;
        this.show = show;
        this.sub = sub;
        this.absolute = absolute;
        if (absolute) {
            this.x = x;
            this.y = y;
        }
        translate(x, y);
        this.event = event;
        this.showChildren = true;
    }

    @Override
    public void render() {
        if (show && td != null) {
            glPushMatrix();
            glTranslatef(x, y, 0);
            td.render();
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

    @Override
    public void update(int delta) {
        td.update(delta);
    }

    public void mouseClick(int mx, int my) {
        if (event != null && show && inArea(mx, my)) {
            event.execute();
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

    @Override
    public String getName() {
        return name;
    }
}
