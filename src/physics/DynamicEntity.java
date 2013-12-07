/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import graphics.ThreeDGraphicsManager;
import graphics.VectorGraphic;
import java.util.ArrayList;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import util.Utilities;

/**
 *
 * @author Andy
 */
public class DynamicEntity extends StaticEntity  {

    private Vector3f velocity;
    private Vector3f angularVelocity;
    private Vector3f acceleration;
    private Vector3f angularAcceleration;
    private ArrayList<ForceGenerator> forceGenerators;
    private Vector3f forceBuffer;
    private Vector3f torqueBuffer;
    private float linearDrag = 0.998f;
    private float angularDrag = .996f;
    private boolean awake;
    private float minSpeedSquared = 0.0015f;
    private float vAvg;
    private static final Vector3f zero = new Vector3f();

    public DynamicEntity(BoundingBox b) {
        this(b, defaultMass);
    }
    
    public DynamicEntity(BoundingBox b, float mass) {
        this(b, mass, getRectangularPrismInertiaTensor(b, mass));
    }
    
    public DynamicEntity(final BoundingBox b, final float mass, final Matrix3f inertiaTensor) {
        super(b, mass, inertiaTensor);
        reset();
    }
    
    public synchronized void reset() {
        velocity = new Vector3f();
        angularVelocity = new Vector3f();
        acceleration = new Vector3f();
        angularAcceleration = new Vector3f();
        forceGenerators = new ArrayList<ForceGenerator>();
        forceBuffer = new Vector3f();
        torqueBuffer = new Vector3f();
        vAvg = 100;
        awake = true;
    }

    public void resetBuffers() {
        forceBuffer.set(zero);
        torqueBuffer.set(zero);
    }
    
    public synchronized void integrate(float delta) {
        // Calculate linear acceleration from force inputs.
        acceleration = (Vector3f) new Vector3f(forceBuffer).scale(invMass);
        // Calculate angular acceleration from torque inputs.
        angularAcceleration = new Vector3f(torqueBuffer);
        angularAcceleration.scale(invMomentOfInertiaAround(torqueBuffer));
        // Adjust velocities
        // Update linear velocity from both acceleration and impulse.
        velocity = Utilities.addScaledVector(velocity, acceleration, delta);
        // Update angular velocity from both acceleration and impulse.
        angularVelocity = Utilities.addScaledVector(angularVelocity, angularAcceleration, delta);
        // Impose drag.
        velocity.scale((float) Math.pow(linearDrag, delta * 100));
        angularVelocity.scale((float) Math.pow(angularDrag, delta * 100));
        // Adjust positions
        // Update linear position.
        setPosition(Utilities.addScaledVector(getPosition(), velocity, delta));
        // Update angular position.

        setOrientation(Utilities.addScaledVector(getOrientation(), Utilities.inverseTransform(angularVelocity, getOrientation()), -delta).normalise(null));

        // Normalize the orientation, and update the matrices with the new
        // position and orientation.

        vAvg += velocity.lengthSquared() + angularVelocity.lengthSquared() / 2f;
        vAvg *= 0.5f;
        if (canSleep() && isAwake()) {
            setAwake(false);
        }
    }

    public void updateForces() {
        for (ForceGenerator f : forceGenerators) {
            f.applyForce(this);
        }
    }

    public void addForceGenerator(ForceGenerator fg) {
        forceGenerators.add(fg);
    }

    public synchronized void applyForce(Vector3f force) {
        Vector3f.add(forceBuffer, force, forceBuffer);
    }

    public synchronized void applyForceAtPoint(Vector3f force, Vector3f application) {
        Vector3f.add(forceBuffer, force, forceBuffer);
        Vector3f.add(torqueBuffer, Vector3f.cross(Vector3f.sub(application, getPosition(), null), force, null), torqueBuffer);
    }

    public synchronized void applyTorque(Vector3f torque) {
        Vector3f.add(torqueBuffer, torque, torqueBuffer);
    }

    public synchronized void applyImpulseAtPoint(Vector3f impulse, Vector3f application) {
        velocity = Utilities.addScaledVector(velocity, impulse, invMass);
        Vector3f angularImpulse = Vector3f.cross(Vector3f.sub(application, getPosition(), null), impulse, null);
        Vector3f deltaAngularVelocity = new Vector3f(angularImpulse);
        deltaAngularVelocity.scale(invMomentOfInertiaAround(angularImpulse));
        Vector3f.add(angularVelocity, deltaAngularVelocity, angularVelocity);
    }

    public synchronized Vector3f getVelocity() {
        return velocity;
    }

    public synchronized Vector3f getAngularVelocity() {
        return angularVelocity;
    }

    public synchronized Vector3f getAcceleration() {
        return acceleration;
    }

    public synchronized Vector3f getAngularAcceleration() {
        return angularAcceleration;
    }

    public synchronized void setVelocity(Vector3f velocity) {
        this.velocity = velocity;
    }

    public synchronized void setAngularVelocity(Vector3f angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public synchronized void setAcceleration(Vector3f acceleration) {
        this.acceleration = acceleration;
    }

    public synchronized void setAngularAcceleration(Vector3f angularAcceleration) {
        this.angularAcceleration = angularAcceleration;
    }

    public synchronized void setLinearDrag(float drag) {
        this.linearDrag = drag;
    }

    public synchronized void setAngularDrag(float drag) {
        this.angularDrag = drag;
    }

    public synchronized boolean canSleep() {
        return vAvg < minSpeedSquared;
    }

    public synchronized void setAwake(boolean awake) {
        this.awake = awake;
        if (awake) {
            vAvg = 100;
        } else {
            velocity.set(zero);
            angularVelocity.set(zero);
            vAvg = 0;
        }
    }

    public synchronized boolean isAwake() {
        return awake;
    }

    public synchronized float invMomentOfInertiaAround(Vector3f axis) {
        if (axis.lengthSquared() == 0) {
            return 0;
        }
        Vector3f s = new Vector3f(axis);
        s.normalise();
        return 1.0f / Vector3f.dot(s, Utilities.transform(Matrix3f.transform(inertiaTensor, Utilities.inverseTransform(s, getOrientation()), null), getOrientation()));
    }

}
