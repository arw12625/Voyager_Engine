/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import game.*;
import org.lwjgl.input.Mouse;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import physics.Boundable;
import physics.BoundingBox;
import util.Utilities;

/**
 *
 * @author Andy
 */
public class ViewPoint extends GameObject {

    Boundable b;
    public float fieldOfView;
    public float aspectRatio;
    public float zNear, zFar;

    public ViewPoint() {

        this(new Vector3f(), new Quaternion());

    }

    public ViewPoint(Vector3f position, Quaternion orientation) {

        this(position, orientation, 60, 4f / 3f, 0.1f, 500f);

    }

    public ViewPoint(final Vector3f position, final Quaternion orientation,
            float fov, float aspectRatio, float zNear, float zFar) {

        this(new BoundingBox() {{
        setPosition(position);
        setOrientation(orientation);}},
        fov, aspectRatio, zNear, zFar);

    }
    
    public ViewPoint(Boundable b) {
        this(b, 60, 4f / 3f, 0.1f, 500f);
    }
    
    public ViewPoint(Boundable b, float fov, float aspectRatio, float zNear, float zFar) {
        this.b = b;
        this.fieldOfView = fov;
        this.aspectRatio = aspectRatio;
        this.zNear = zNear;
        this.zFar = zFar;
    }

    public synchronized void perspectiveView() {

        glLoadIdentity();
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(fieldOfView, aspectRatio, zNear, zFar);
        glMatrixMode(GL_MODELVIEW);



    }

    public synchronized void moveFromWorld(Vector3f u) {

        b.getBounds().translate(new Vector3f(u.getX(), u.getY(), u.getZ()));

    }

    public synchronized void moveFromWorld(float x, float y, float z) {

        b.getBounds().translate(new Vector3f(x, y, z));

    }

    public synchronized void rotate(Quaternion q) {
        b.getBounds().setOrientation(Quaternion.mul(b.getBounds().getOrientation(), q, null));
    }

    public synchronized void adjustToView() {
        float angle = (float) (Math.acos(b.getBounds().getOrientation().getW()) * 2 * 180 / Math.PI);
        glRotatef(angle, b.getBounds().getOrientation().getX(),
                b.getBounds().getOrientation().getY(),
                b.getBounds().getOrientation().getZ());
        glTranslatef(-b.getBounds().getPosition().getX(),
                -b.getBounds().getPosition().getY(),
                -b.getBounds().getPosition().getZ());
    }

    public synchronized float getX() {

        return b.getBounds().getPosition().getX();

    }

    public synchronized float getY() {

        return b.getBounds().getPosition().getY();

    }

    public synchronized float getZ() {

        return b.getBounds().getPosition().getZ();

    }

    public synchronized void setPosition(float x, float y, float z) {

        setPosition(new Vector3f(x, y, z));

    }

    public synchronized void setPosition(Vector3f v) {
        b.getBounds().setPosition(v);
    }

    public synchronized void setOrientation(Quaternion orientation) {
        b.getBounds().setOrientation(orientation);
    }

    public synchronized Vector3f getPosition() {
        return b.getBounds().getPosition();
    }
}