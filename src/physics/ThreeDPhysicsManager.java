/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import game.*;
import graphics.ThreeDGraphicsManager;
import graphics.VectorGraphic;
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
    float restitution = 0.2f;
    static ThreeDPhysicsManager instance;
    private float coefficientOfFriction = .3f;

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
        float division = 4;
        float collision = 4;
        float time = delta / 1000f / division;
        for (int j = 0; j < division; j++) {
            for (PhysicalEntity e : pe) {

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
                            float highestVelocity = Float.MAX_VALUE;
                            Plane collisionPlane = null;
                            Vector3f collisionVector = null;
                            Vector3f collisionVelocity = null;
                            vertices = e.getBounds().getGlobalVertices();
                            for (Vector3f v : vertices) {
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
                                        collisionPlane = p;
                                        collisionVector = v;
                                        collisionVelocity = pointVelocity;
                                    }
                                }

                            }

                            if (highestVelocity < 0) {

                                Vector3f normal = new Vector3f(collisionPlane.getNormal());

                                float accCausedV = Vector3f.dot((Vector3f) (new Vector3f(e.getAcceleration()).scale(time)), normal);
                                float newVelocity = -highestVelocity - accCausedV;
                                newVelocity *= restitution;

                                float deltaV = newVelocity - highestVelocity - accCausedV;
                                Vector3f relativeContact = Vector3f.sub(collisionVector, e.getPosition(), null);

                                Vector3f axis = Vector3f.cross(relativeContact, normal, null);
                                float deltaVPerImpulse = e.getInvMass() + relativeContact.lengthSquared() * e.invMomentOfInertiaAround(axis);

                                float impulse = deltaV / deltaVPerImpulse;
                                Vector3f impulseVector = new Vector3f(normal);
                                impulseVector.scale(impulse);
                                Vector3f slide = Vector3f.sub(collisionVelocity, (Vector3f) (new Vector3f(normal).scale(highestVelocity)), null);
                                Vector3f friction = new Vector3f();
                                if (slide.lengthSquared() > .1f) {
                                    slide.normalise();
                                    friction = (Vector3f) slide.scale(-coefficientOfFriction * impulse);
                                }
                                e.applyImpulseAtPoint(Vector3f.add(impulseVector, friction, null), collisionVector);

                            }
                        }
                        for (int i = 0; i < collision; i++) {
                            vertices = e.getBounds().getGlobalVertices();
                            float highestPenetration = Float.MAX_VALUE;
                            Plane collisionPlane = null;
                            Vector3f collisionVector = null;
                            for (Vector3f v : vertices) {
                                for (Plane p : cols) {
                                    float distance = p.getDistance(v);
                                    if (distance < highestPenetration) {
                                        highestPenetration = distance;
                                        collisionVector = v;
                                        collisionPlane = p;
                                    }
                                }
                            }
                            if (highestPenetration < 0) {
                                /*Vector3f normal = new Vector3f(collisionPlane.getNormal());
                                Vector3f relative = Vector3f.sub(collisionVector, e.getPosition(), null);
                                Vector3f axis = Vector3f.cross(relative, normal, null);
                                axis.normalise();
                                float momentOfInertia = e.invMomentOfInertiaAround(axis);
                                float linearInertia = e.getInvMass();
                                float angularInertia = collisionVector.lengthSquared() / momentOfInertia;
                                float totalInertia = linearInertia + angularInertia;
                                float linearMove = highestPenetration * linearInertia / totalInertia;
                                float angularMove = highestPenetration / collisionVector.length() * angularInertia / totalInertia;
                                e.setPosition(Utilities.addScaledVector(e.getPosition(), normal, -1.01f * linearMove));
                                e.setOrientation(Utilities.addScaledVector(e.getOrientation(), axis.negate(null), angularMove));*/
                                e.setPosition(Utilities.addScaledVector(e.getPosition(), collisionPlane.getNormal(), -1.01f * highestPenetration));
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
