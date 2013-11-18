/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import game.*;
import java.util.ArrayList;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector3f;
import util.DebugMessages;
import util.Utilities;

/**
 *
 * @author Andy
 */
public class ThreeDPhysicsManager extends StandardManager implements update.Updateable {

    ArrayList<PhysicalEntity> pe;
    CollisionMesh collisionMesh;
    float restitution = 0.1f;
    static ThreeDPhysicsManager instance;

    @Override
    public void create() {
        super.create();
        pe = new ArrayList<PhysicalEntity>();
    }

    public static ThreeDPhysicsManager getInstance() {
        if (instance == null) {
            instance = new ThreeDPhysicsManager();
        }
        return instance;
    }

    @Override
    public boolean update(int delta) {

        DebugMessages.getInstance().write("Physics starting");

        //Motion update
        float division = 6;
        float collision = 1;
        float time = delta / 1000f / division;
        for (int j = 0; j < division; j++) {
            for (PhysicalEntity e : pe) {

                e.setAwake(!e.canSleep());
                e.updateForces();
                if (e.isAwake()) {
                    e.integrate(time);
                }

            }

            ArrayList<PhysicalEntity> pcopy = new ArrayList<PhysicalEntity>(pe);
            for (PhysicalEntity e : pe) {
                if (collisionMesh != null && !collisionMesh.isEmpty()) {
                    ArrayList<Plane> cols = collisionMesh.getPlanes(e);
                    if (!cols.isEmpty()) {
                        Vector3f[] vertices;
                        for (int i = 0; i < collision; i++) {
                            vertices = e.getBounds().getGlobalVertices();
                            for (Vector3f v : vertices) {
                                float highestVelocity = Float.MAX_VALUE;
                                Plane toResolve = null;
                                Vector3f relativeContact = Vector3f.sub(v, e.getPosition(), null);
                                for (Plane p : cols) {
                                    float distance = p.getDistance(v);
                                    if (distance > 0) {
                                        continue;
                                    }

                                    Vector3f angularComponent = Vector3f.cross(e.getAngularVelocity(), relativeContact, null);
                                    Vector3f pointVelocity = Vector3f.add(e.getVelocity(), angularComponent, null);
                                    float seperatingVelocity = Vector3f.dot(pointVelocity, p.getNormal());

                                    if (seperatingVelocity < highestVelocity) {
                                        highestVelocity = seperatingVelocity;
                                        toResolve = p;
                                    }
                                }

                                if (highestVelocity >= 0) {
                                    continue;
                                }

                                float newVelocity = -highestVelocity;
                                newVelocity *= restitution;

                                /*
                                 * Vector3f accCausedVelocity =
                                 * e.getAcceleration(); float
                                 * accCausedSepVelocity =
                                 * Vector3f.dot(accCausedVelocity,
                                 * toResolve.getNormal()) * time; if
                                 * (accCausedSepVelocity < 0) { newVelocity +=
                                 * accCausedSepVelocity; if (newVelocity < 0) {
                                 * newVelocity = 0; } }
                                 */

                                float deltaV = newVelocity - highestVelocity;
                                Vector3f normal = new Vector3f(toResolve.getNormal());

                                Vector3f deltaVPerImpulseVec = new Vector3f();
                                Vector3f.cross(relativeContact, normal, deltaVPerImpulseVec);
                                Matrix3f.transform(e.getInvInertiaTensor(), deltaVPerImpulseVec, deltaVPerImpulseVec);
                                Vector3f.cross(deltaVPerImpulseVec, relativeContact, deltaVPerImpulseVec);
                                // Work out the change in velocity in contact coordinates.
                                float deltaVPerImpulse = Vector3f.dot(deltaVPerImpulseVec, normal);
                                // Add the linear component of velocity change.
                                deltaVPerImpulse += e.getInvMass();

                                Vector3f impulse = new Vector3f(normal);
                                impulse.scale(deltaV / deltaVPerImpulse);
                                e.applyImpulseAtPoint(impulse, v);
                            }
                        }
                        for (int i = 0; i < collision; i++) {
                            vertices = e.getBounds().getGlobalVertices();
                            for (Vector3f v : vertices) {
                                float highestPenetration = Float.MAX_VALUE;
                                Plane toResolve = null;
                                for (Plane p : cols) {
                                    float distance = p.getDistance(v);
                                    if (distance < highestPenetration) {
                                        highestPenetration = distance;
                                        toResolve = p;
                                    }
                                }
                                if (highestPenetration > 0) {
                                    continue;
                                }
                                e.setPosition(Utilities.addScaledVector(e.getPosition(), toResolve.getNormal(), -1f * highestPenetration));
                            }
                        }
                        e.collide(cols);
                    }
                }
                pcopy.remove(e);

                for (PhysicalEntity other : pcopy) {
                    if (e.getBounds().intersects(other.getBounds())) {
                        e.collide(other);
                        other.collide(e);
                    }
                }
            }
        }

        DebugMessages.getInstance().write("Physics finished");

        return false;
    }

    @Override
    public boolean add(GameObject obj) {
        if (obj instanceof PhysicalEntity) {
            pe.add((PhysicalEntity) obj);
            return true;
        }
        return false;
    }

    @Override
    public void remove(GameObject obj) {
        if (pe.contains(obj)) {
            pe.remove(obj);
        }
    }

    public void setCollisionMesh(CollisionMesh cm) {
        this.collisionMesh = cm;
    }

    public CollisionMesh getCollisionMesh() {
        return collisionMesh;
    }
}
