/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import game.*;
import graphics.BoundingBoxGraphic;
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

    ArrayList<StaticEntity> se;
    ArrayList<DynamicEntity> de;
    CollisionMesh collisionMesh;
    float restitution = 0.2f;
    static ThreeDPhysicsManager instance;
    private float coefficientOfFriction = .3f;

    @Override
    public void create() {
        super.create();
        se = new ArrayList<StaticEntity>();
        de = new ArrayList<DynamicEntity>();
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
        float division = 1;
        float collision = 4;
        float time = delta / 1000f / division;
        for (int j = 0; j < division; j++) {
            for (DynamicEntity e : de) {

                e.updateForces();
                if (e.isAwake()) {
                    e.integrate(time);
                }

            }

            ArrayList<StaticEntity> pcopy = new ArrayList<StaticEntity>(se);
            for (StaticEntity e : se) {
                boolean dynamic = e instanceof DynamicEntity;
                DynamicEntity cast = null;
                if (dynamic) {
                    cast = (DynamicEntity) e;
                }
                if (collisionMesh != null && !collisionMesh.isEmpty()) {
                    ArrayList<Plane> cols = collisionMesh.getPlanes(e);
                    if (!cols.isEmpty()) {
                        ArrayList<Vector3f> vertices;
                        for (int i = 0; i < collision; i++) {
                            float highestVelocity = Float.MAX_VALUE;
                            Plane collisionPlane = null;
                            Vector3f collisionVector = null;
                            Vector3f collisionVelocity = null;
                            vertices = e.getContactVertices();
                            for (Vector3f v : vertices) {
                                Vector3f relativeContact = Vector3f.sub(v, e.getPosition(), null);
                                for (Plane p : cols) {
                                    float distance = p.getDistance(v);
                                    if (distance < 0) {

                                        e.collide(p, v);
                                        if (dynamic) {
                                            Vector3f angularComponent = Vector3f.cross(cast.getAngularVelocity(), relativeContact, null);
                                            Vector3f pointVelocity = Vector3f.add(cast.getVelocity(), angularComponent, null);
                                            float seperatingVelocity = Vector3f.dot(pointVelocity, p.getNormal());

                                            if (seperatingVelocity < highestVelocity) {
                                                highestVelocity = seperatingVelocity;
                                                collisionPlane = p;
                                                collisionVector = v;
                                                collisionVelocity = pointVelocity;
                                            }
                                        }
                                    }
                                }

                            }
                            if (dynamic) {
                                if (highestVelocity < 0) {

                                    Vector3f normal = new Vector3f(collisionPlane.getNormal());

                                    float accCausedV = Vector3f.dot((Vector3f) (new Vector3f(cast.getAcceleration()).scale(time)), normal);
                                    float newVelocity = -highestVelocity - accCausedV;
                                    newVelocity *= restitution;

                                    float deltaV = newVelocity - highestVelocity - accCausedV;
                                    Vector3f relativeContact = Vector3f.sub(collisionVector, cast.getPosition(), null);

                                    Vector3f axis = Vector3f.cross(relativeContact, normal, null);
                                    float deltaVPerImpulse = cast.getInvMass() + relativeContact.lengthSquared() * cast.invMomentOfInertiaAround(axis);

                                    float impulse = deltaV / deltaVPerImpulse;
                                    Vector3f impulseVector = new Vector3f(normal);
                                    impulseVector.scale(impulse);
                                    Vector3f slide = Vector3f.sub(collisionVelocity, (Vector3f) (new Vector3f(normal).scale(highestVelocity)), null);
                                    Vector3f friction = new Vector3f();
                                    if (slide.lengthSquared() > .1f) {
                                        slide.normalise();
                                        friction = (Vector3f) slide.scale(-coefficientOfFriction * impulse);
                                    }
                                    
                                    cast.applyImpulseAtPoint(Vector3f.add(impulseVector, friction, null), collisionVector);
                                    
                                }
                            }
                        }
                        for (int i = 0; i < collision; i++) {
                            vertices = e.getContactVertices();
                            float highestPenetration = Float.MAX_VALUE;
                            Plane collisionPlane = null;
                            Vector3f collisionVector = null;
                            for (Vector3f v : vertices) {
                                for (Plane p : cols) {
                                    float distance = p.getDistance(v);
                                    if (distance < 0 && distance < highestPenetration) {
                                        e.collide(p, v);
                                        highestPenetration = distance;
                                        collisionVector = v;
                                        collisionPlane = p;
                                    }
                                }
                            }
                            if (dynamic && highestPenetration < 0) {
                                e.setPosition(Utilities.addScaledVector(e.getPosition(), collisionPlane.getNormal(), -1.01f * highestPenetration));
                            }
                        }
                    }
                }
                pcopy.remove(e);

                for (StaticEntity other : pcopy) {
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
        if (obj instanceof StaticEntity) {
            se.add((StaticEntity) obj);
            if (obj instanceof DynamicEntity) {
                de.add((DynamicEntity) obj);
            }
            return true;
        }
        return false;
    }

    @Override
    public void remove(GameObject obj) {
        if (se.contains(obj)) {
            se.remove(obj);
            if(de.contains(obj)) {
                de.remove(obj);
            }
        }
    }

    public void setCollisionMesh(CollisionMesh cm) {
        this.collisionMesh = cm;
    }

    public CollisionMesh getCollisionMesh() {
        return collisionMesh;
    }
}
