/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import java.util.ArrayList;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import update.Entity;
import static util.Utilities.*;

/**
 *
 * @author Andy
 */
public abstract class PhysicalEntity implements Entity, Boundable {

    BoundingBox bounds;
    Vector3f velocity;
    Vector3f angularVelocity;
    Vector3f acceleration;
    Vector3f angularAcceleration;
    ArrayList<ForceGenerator> forceGenerators;
    Vector3f forceBuffer;
    Vector3f torqueBuffer;
    float invMass;
    Matrix3f invInertiaTensor;
    float linearDrag = 0.99f;
    float angularDrag = 0.99f;
    boolean awake;
    private float minSpeedSquared = 0.001f;
    private float vAvg;
    private Vector3f zero = new Vector3f();

    public PhysicalEntity(BoundingBox bounds) {
        this(bounds, 1);
    }
    
    public PhysicalEntity(BoundingBox bounds, float mass) {
        this(bounds, mass, getRectangularPrismInertiaTensor(bounds, mass));
    }
    
    public PhysicalEntity(BoundingBox bounds, float mass, Matrix3f inertiaTensor) {
        this.bounds = bounds;
        velocity = new Vector3f();
        angularVelocity = new Vector3f();
        acceleration = new Vector3f();
        angularAcceleration = new Vector3f();
        forceGenerators = new ArrayList<ForceGenerator>();
        forceBuffer = new Vector3f();
        torqueBuffer = new Vector3f();
        invMass = 1f / mass;
        invInertiaTensor = (Matrix3f) inertiaTensor.invert();
        vAvg = 100;
        awake = true;
    }
    
    @Override
    public BoundingBox getBounds() {
        return bounds;
    }
    
    public abstract void collide(ArrayList<Plane> collisions);
    public abstract void collide(PhysicalEntity collisions);
    
    public void updateForces() {
        for(ForceGenerator f : forceGenerators) {
            f.applyForce(this);
        }
    }
    
    public void integrate(float delta) {
        // Calculate linear acceleration from force inputs.
        Vector3f lastFrameAcceleration = new Vector3f(acceleration);
        acceleration = (Vector3f) forceBuffer.scale(invMass);
        // Calculate angular acceleration from torque inputs.
        angularAcceleration = Matrix3f.transform(invInertiaTensor, torqueBuffer, null);
        // Adjust velocities
        // Update linear velocity from both acceleration and impulse.
        velocity = addScaledVector(velocity, lastFrameAcceleration, delta);
        // Update angular velocity from both acceleration and impulse.
        angularVelocity = addScaledVector(angularVelocity, angularAcceleration, delta);
        // Impose drag.
        velocity.scale((float) Math.pow(linearDrag, delta));
        angularVelocity.scale((float) Math.pow(angularDrag, delta));
        // Adjust positions
        // Update linear position.
        bounds.setPosition(addScaledVector(bounds.getPosition(), velocity, delta));
        // Update angular position.
        bounds.setOrientation(addScaledVector(bounds.getOrientation(), angularVelocity, delta).normalise(null));
        // Normalize the orientation, and update the matrices with the new
        // position and orientation.
        
        // Clear accumulators.
        forceBuffer.set(zero);
        torqueBuffer.set(zero);
        
        vAvg += velocity.lengthSquared() + angularVelocity.lengthSquared();
        vAvg *= 0.7;
    }

    public void addForceGenerator(ForceGenerator fg) {
        forceGenerators.add(fg);
    }
    
    public void applyForce(Vector3f force) {
        Vector3f.add(forceBuffer, force, forceBuffer);
    }

    public void applyForceAtPoint(Vector3f force, Vector3f application) {
        Vector3f.add(torqueBuffer, Vector3f.cross(Vector3f.sub(application, bounds.getPosition(), null), force, null), forceBuffer);
    }

    public void applyTorque(Vector3f torque) {
        Vector3f.add(torqueBuffer, torque, torqueBuffer);
    }
    
    public boolean canSleep() {
        return vAvg < minSpeedSquared;
    }
    
    public void setAwake(boolean awake) {
        this.awake = awake;
        if(awake) {
            vAvg = 100;
        }
        if(!awake) {
            velocity.set(zero);
            angularVelocity.set(zero);
            vAvg = 0;
        }
    }
    
    public boolean isAwake() {
        return awake;
    }
    
    public static Matrix3f getRectangularPrismInertiaTensor(final BoundingBox b, final float mass) {
        return new Matrix3f() {{
            float widthSquare = (float)Math.pow(b.getDimension().getX(), 2);
            float heightSquare = (float)Math.pow(b.getDimension().getY(), 2);
            float depthSquare = (float)Math.pow(b.getDimension().getZ(), 2);
            float mult = mass / 12f;
            m00 = mult * (heightSquare * depthSquare);
            m11 = mult * (widthSquare * depthSquare);
            m22 = mult * (heightSquare * widthSquare);
        }};
    }
}
