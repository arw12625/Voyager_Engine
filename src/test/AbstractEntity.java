/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.ArrayList;
import org.lwjgl.input.Keyboard;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import util.Utilities;

/**
 *
 * @author Andy
 */
public abstract class AbstractEntity extends physics.PhysicalEntity implements graphics.ThreeD {

    graphics.ThreeDModel m;

    public AbstractEntity(graphics.ThreeDModel m) {
        super(m.getMesh().getBounds());
        this.m = m;
    }

    @Override
    public void update(int delta) {
        
        
        
        input.InputManager keyboard = input.InputManager.getInstance();
                Vector3f go = new Vector3f();
                if (keyboard.get(Keyboard.KEY_I).isDown()) {
                    go.translate(0, 0, -1);
                }
                if (keyboard.get(Keyboard.KEY_K).isDown()) {
                    go.translate(0, 0, 1);
                }
                if (keyboard.get(Keyboard.KEY_J).isDown()) {
                    go.translate(-1, 0, 0);
                }
                if (keyboard.get(Keyboard.KEY_L).isDown()) {
                    go.translate(1, 0, 0);
                }
                if (keyboard.get(Keyboard.KEY_U).isDown()) {
                    go.translate(0, 5, 0);
                }
                if (keyboard.get(Keyboard.KEY_O).isDown()) {
                    go.translate(0, -5, 0);
                }
                if (go.lengthSquared() != 0) {
                    go.scale(2);
                    applyForce(go);
                }
        
        
        
        
        Vector3f v = getBounds().getGlobalVertices()[0];
        Vector3f pointVelocity = Vector3f.add(this.getVelocity(), Vector3f.cross(Utilities.transform(this.getAngularVelocity(), this.getOrientation()), Vector3f.sub(v, this.getPosition(), null), null), null);
        System.out.println(pointVelocity.length());
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
