/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import game.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Andy
 */
public class ViewPoint {

    Vector3f position;
    Quaternion orientation;
    public float fieldOfView;
    public float aspectRatio;
    public float zNear, zFar;

    public ViewPoint() {

        this(new Vector3f(), new Quaternion());

    }

    public ViewPoint(Vector3f position, Quaternion orientation) {

        this(position, orientation, 60, 4f / 3f, 0.1f, 500f);

    }

    public ViewPoint(Vector3f position, Quaternion orientation,
            float fov, float aspectRatio, float zNear, float zFar) {

        this.position = position;
        this.orientation = orientation;
        this.fieldOfView = fov;
        this.aspectRatio = aspectRatio;
        this.zNear = zNear;
        this.zFar = zFar;

    }
    
    public void perspectiveView() {
        
        glLoadIdentity();
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(fieldOfView, aspectRatio, zNear, zFar);
        glMatrixMode(GL_MODELVIEW);

    }
    
    public void moveFromWorld(Vector3f u) {

        position.translate(u.getX(), u.getY(), u.getZ());

    }

    public void moveFromWorld(float x, float y, float z) {

        position.translate(x, y, z);

    }

    public void rotate(Quaternion q) {
        Quaternion.mul(orientation, q, orientation);
    }

    public void adjustToView() {
        float angle = (float)(Math.acos(orientation.getW()) * 2 * 180 / Math.PI);
        glRotatef(angle, orientation.getX(), orientation.getY(), orientation.getZ());
        glTranslatef(-position.getX(), -position.getY(), -position.getZ());
    }

    public float getX() {

        return position.getX();

    }

    public float getY() {

        return position.getY();

    }

    public float getZ() {

        return position.getZ();

    }

    public void setPosition(float x, float y, float z) {

        setPosition(new Vector3f(x, y, z));

    }

    public void setPosition(Vector3f v) {
        this.position = v;
    }

    public void setOrientation(Quaternion orientation) {
        this.orientation = orientation;
    }

    public Vector3f getPosition() {
        return position;
    }
}