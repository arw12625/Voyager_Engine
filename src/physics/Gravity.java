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
public class Gravity implements ForceGenerator {

    @Override
    public void applyForce(PhysicalEntity pe) {
        pe.applyForce(new Vector3f(0, -pe.getMass() * 8, 0));
    }
    
}
