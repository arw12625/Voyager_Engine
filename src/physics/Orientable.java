/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import org.lwjgl.util.vector.Quaternion;

/**
 *
 * @author Andy
 */
public interface Orientable {
    public Quaternion getOrientation();
    public void setOrientation(Quaternion q);
}
