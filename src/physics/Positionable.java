/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Andy
 */
public interface Positionable {
    public Vector3f getPosition();
    public void setPosition(Vector3f v);
}
