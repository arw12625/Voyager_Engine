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
public interface ForceGenerator {
    
    public void applyForce(PhysicalEntity pe);
    
}
