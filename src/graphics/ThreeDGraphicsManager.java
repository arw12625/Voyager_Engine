/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import game.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;
import java.util.ArrayList;
import script.Console;
import util.DebugMessages;
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
        glLightModel(GL_LIGHT_MODEL_AMBIENT, asFloatBuffer(new float[]{0.5f, 0.5f, 0.45f, 0f}));
        glLight(GL_LIGHT0, GL_DIFFUSE, asFloatBuffer(new float[]{.8f, .8f, .75f, 0}));
        glLight(GL_LIGHT0, GL_POSITION, asFloatBuffer(new float[]{-.3f, 200f, -.3f, 0}));
        glEnable(GL_COLOR_MATERIAL);
        glColorMaterial(GL_FRONT, GL_DIFFUSE);
        /*
         * glFogi(GL_FOG_MODE, GL_LINEAR); // Fog Mode glFog(GL_FOG_COLOR,
         * asFloatBuffer(new float[]{0.45f, 0.5f, 0.55f, 1f})); // Set Fog Color
         * glFogf(GL_FOG_DENSITY, 0.35f); // How Dense Will The Fog Be
         * glHint(GL_FOG_HINT, GL_DONT_CARE); // Fog Hint Value
         * glFogf(GL_FOG_START, 100f); // Fog Start Depth glFogf(GL_FOG_END,
         * 200f); // Fog End Depth glEnable(GL_FOG); // Enables GL_FOG
         */

    }

    public static ThreeDGraphicsManager getInstance() {
        if (instance == null) {
            instance = new ThreeDGraphicsManager();
        }
        return instance;
    }

    public void threeDView() {
        glEnable(GL_CULL_FACE);
        glEnable(GL_LIGHTING);
        glEnable(GL_DEPTH_TEST);
    }

    public void overlayView() {

        glLoadIdentity();
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, getWidth(), getHeight(), 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);

        glDisable(GL_CULL_FACE);
        glDisable(GL_LIGHTING);
        glDisable(GL_DEPTH_TEST);

    }

    @Override
    public void render() {

        DebugMessages.getInstance().write("Rendering started");

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        vp.perspectiveView();
        vp.adjustToView();
        threeDView();

        ArrayList<Displayable> d3Copy = new ArrayList<Displayable>(graphics3D);
        for (Displayable de : d3Copy) {
            de.render();
        }

        overlayView();

        ArrayList<Displayable> d2Copy = new ArrayList<Displayable>(graphics2D);
        for (Displayable de : d2Copy) {
            de.render();
        }

        super.render();
        DebugMessages.getInstance().write("Rendering finished");
    }

    public void addGraphic3D(ThreeD de, int z) {
        int i = 0;
        while (i < graphics3D.size() && zIndices3D.get(i) < z) {
            i++;
        }
        graphics3D.add(i, de);
        zIndices3D.add(i, z);
    }

    public void addGraphic2D(TwoD de, int z) {
        int i = 0;
        while (i < graphics2D.size() && zIndices2D.get(i) < z) {
            i++;
        }
        graphics2D.add(i, de);
        zIndices2D.add(i, z);
    }

    public void setViewPoint(ViewPoint vp) {
        this.vp = vp;
    }

    @Override
    public void remove(GameObject obj) {
        if(graphics2D.contains(obj)) {
            graphics2D.remove(obj);
        }
        if(graphics3D.contains(obj)) {
            graphics3D.remove(obj);
        }
    }

    public boolean add(GameObject obj) {
        if(obj instanceof ThreeD) {
            addGraphic3D((ThreeD)obj, 0);
            return true;
        }
        if(obj instanceof TwoD) {
            addGraphic2D((TwoD)obj, 0);
            return true;
        }
        return false;
    }

}
