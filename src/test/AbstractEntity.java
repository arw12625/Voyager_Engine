/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import graphics.ThreeD;
import graphics.ThreeDModel;
import java.util.ArrayList;
import physics.PhysicalEntity;
import physics.Plane;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Andy
 */
public abstract class AbstractEntity extends PhysicalEntity implements ThreeD {

    ThreeDModel m;

    public AbstractEntity(ThreeDModel m) {
        super(m.getMesh().getBounds());
        this.m = m;
    }

    @Override
    public void update(int delta) {
    }

    @Override
    public void render() {
        Vector3f pos = getPosition();
        Quaternion orientation = getOrientation();
        float angle = (float)(Math.acos(orientation.getW()) * 2 * 180 / Math.PI);
        glPushMatrix();
        glTranslatef(pos.getX(), pos.getY(), pos.getZ());
        glRotatef(-angle, orientation.getX(), orientation.getY(), orientation.getZ());
        m.render();
        glPopMatrix();
        getBounds().render();
    }
}
