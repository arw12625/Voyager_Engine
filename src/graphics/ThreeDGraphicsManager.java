/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import game.*;
import static org.lwjgl.opengl.GL11.*;
import java.util.ArrayList;
import static util.Utilities.*;

/**
 *
 * @author Andy
 */
public class ThreeDGraphicsManager extends GraphicsManager {

    float fieldOfView, aspectRatio, zNear, zFar;
    ArrayList<ThreeD> graphics3D;
    ArrayList<Integer> zIndices3D;
    ArrayList<TwoD> graphics2D;
    ArrayList<Integer> zIndices2D;
    ViewPoint vp;
    static ThreeDGraphicsManager instance;

    @Override
    public void create() {
        super.create();

        graphics3D = new ArrayList<ThreeD>();
        zIndices3D = new ArrayList<Integer>();
        graphics2D = new ArrayList<TwoD>();
        zIndices2D = new ArrayList<Integer>();

        glEnable(GL_TEXTURE_2D);

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        glEnable(GL_DEPTH_TEST);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_FASTEST);

        glShadeModel(GL_SMOOTH);

        glDisable(GL_DITHER);

        glEnable(GL_LIGHTING);

        glEnable(GL_LIGHT0);
        glLight(GL_LIGHT0, GL_DIFFUSE, asFloatBuffer(new float[]{.3f, .3f, .3f, 1}));
        glLight(GL_LIGHT0, GL_POSITION, asFloatBuffer(new float[]{0f, 0f, 0f, 1}));

        glEnable(GL_LIGHT2);
        glLight(GL_LIGHT2, GL_DIFFUSE, asFloatBuffer(new float[]{.5f, .5f, .5f, 1}));
        glLight(GL_LIGHT2, GL_POSITION, asFloatBuffer(new float[]{0f, 0f, 0f, 1}));

        glEnable(GL_LIGHT1);
        glLight(GL_LIGHT1, GL_AMBIENT, asFloatBuffer(new float[]{.0f, .0f, .0f, 1}));
        glLight(GL_LIGHT1, GL_DIFFUSE, asFloatBuffer(new float[]{.7f, .7f, .7f, 1}));

        glEnable(GL_COLOR_MATERIAL);

    }

    public static ThreeDGraphicsManager getInstance() {
        if (instance == null) {
            instance = new ThreeDGraphicsManager();
        }
        return instance;
    }

    private void threeDView() {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_LIGHTING);
        glEnable(GL_CULL_FACE);
    }

    private void overlayView() {

        glLoadIdentity();
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, getWidth(), getHeight(), 0, 0, 1);
        glMatrixMode(GL_MODELVIEW);

        glDisable(GL_CULL_FACE);
        glDisable(GL_LIGHTING);
        glDisable(GL_DEPTH_TEST);

    }

    @Override
    public synchronized void render() {
        util.DebugMessages.getInstance().write("Rendering started");

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        if (vp != null) {
            vp.perspectiveView();
            vp.adjustToView();
            glLight(GL_LIGHT2, GL_POSITION, asFloatBuffer(new float[]{vp.getX(), vp.getY(), vp.getZ(), 1}));
            glBindTexture(GL_TEXTURE_2D, 0);
        }

        threeDView();
        ArrayList<ThreeD> threeDCopy = new ArrayList<ThreeD>(graphics3D);
        for (Displayable de : threeDCopy) {
            if (de instanceof GameObject) {
                ((GameObject) de).runScripts("render", null);
            }
            de.render();
        }

        overlayView();
        ArrayList<TwoD> twoDCopy = new ArrayList<TwoD>(graphics2D);
        for (Displayable de : twoDCopy) {
            if (de instanceof GameObject) {
                ((GameObject) de).runScripts("render", null);
            }
            de.render();
        }

        super.render();
        util.DebugMessages.getInstance().write("Rendering finished");
    }

    public void addGraphic3D(ThreeD de, int z) {
        int i = 0;
        while (i < graphics3D.size() && zIndices3D.get(i) < z) {
            i++;
        }
        graphics3D.add(i, de);
        zIndices3D.add(i, z);
    }

    public synchronized void addGraphic2D(TwoD de, int z) {
        int i = 0;
        while (i < graphics2D.size() && zIndices2D.get(i) < z) {
            i++;
        }
        graphics2D.add(i, de);
        zIndices2D.add(i, z);
    }

    public synchronized void setViewPoint(ViewPoint vp) {
        this.vp = vp;
    }

    @Override
    public void remove(GameObject obj) {
        if (graphics2D.contains(obj)) {
            graphics2D.remove(obj);
        }
        if (graphics3D.contains(obj)) {
            graphics3D.remove(obj);
        }
    }

    public boolean add(GameObject obj) {
        if (obj instanceof ThreeD) {
            addGraphic3D((ThreeD) obj, 0);
            return true;
        }
        if (obj instanceof TwoD) {
            addGraphic2D((TwoD) obj, 0);
            return true;
        }
        return false;
    }
}
