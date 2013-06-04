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
    ArrayList<ThreeDGraphic> graphics3D;
    ArrayList<Integer> zIndices3D;
    ArrayList<TwoDGraphic> graphics2D;
    ArrayList<Integer> zIndices2D;
    ViewPoint vp;
    static ThreeDGraphicsManager instance;

    public ThreeDGraphicsManager() {
        graphics3D = new ArrayList<ThreeDGraphic>();
        zIndices3D = new ArrayList<Integer>();
        graphics2D = new ArrayList<TwoDGraphic>();
        zIndices2D = new ArrayList<Integer>();
    }

    @Override
    public void create() {
        super.create();

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
        glLightModel(GL_LIGHT_MODEL_AMBIENT, asFloatBuffer(new float[]{0.3f, 0.3f, 0.27f, 1f}));
        glLight(GL_LIGHT0, GL_DIFFUSE, asFloatBuffer(new float[]{.65f, .65f, .65f, 1}));
        glLight(GL_LIGHT0, GL_POSITION, asFloatBuffer(new float[]{-.3f, 200f, -.3f, 1}));

        glEnable(GL_COLOR_MATERIAL);
        glColorMaterial(GL_FRONT, GL_DIFFUSE);

        glFogi(GL_FOG_MODE, GL_LINEAR);        // Fog Mode
        glFog(GL_FOG_COLOR, asFloatBuffer(new float[]{0.45f, 0.5f, 0.55f, 1f}));            // Set Fog Color
        glFogf(GL_FOG_DENSITY, 0.35f);              // How Dense Will The Fog Be
        glHint(GL_FOG_HINT, GL_DONT_CARE);          // Fog Hint Value
        glFogf(GL_FOG_START, 100f);             // Fog Start Depth
        glFogf(GL_FOG_END, 200f);               // Fog End Depth
        glEnable(GL_FOG);                   // Enables GL_FOG

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
        glOrtho(0, width, height, 0, 1, -1);
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

        ArrayList<DisplayableEntity> d3Copy = new ArrayList<DisplayableEntity>(graphics3D);
        for (DisplayableEntity de : d3Copy) {
            de.render();
        }

        overlayView();

        ArrayList<DisplayableEntity> d2Copy = new ArrayList<DisplayableEntity>(graphics2D);
        for (DisplayableEntity de : d2Copy) {
            de.render();
        }

        DebugMessages.getInstance().write("Rendering finished");
    }

    public void addGraphic3D(ThreeDGraphic de) {
        addGraphic3D(de, 0);
    }

    public void addGraphic3D(ThreeDGraphic de, int z) {
        int i = 0;
        while (i < graphics3D.size() && zIndices3D.get(i) < z) {
            i++;
        }
        graphics3D.add(i, de);
        zIndices3D.add(i, z);
    }

    public void removeGraphic3D(ThreeDGraphic de) {
        graphics3D.remove(de);
    }

    public void addGraphic2D(TwoDGraphic de, int z) {
        int i = 0;
        while (i < graphics2D.size() && zIndices2D.get(i) < z) {
            i++;
        }
        graphics2D.add(i, de);
        zIndices2D.add(i, z);
        //graphics2D.add(de);
        //System.out.println(graphics2D);
    }

    public void addGraphic2D(TwoDGraphic de) {
        addGraphic2D(de, 0);
    }

    public void removeGraphic2D(TwoDGraphic de) {
        graphics2D.remove(de);
    }

    public void setViewPoint(ViewPoint vp) {
        this.vp = vp;
    }
}
