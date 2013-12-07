/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function enableDebug(enable) {
    GameTest.enableDebug(enable);
}


importPackage(Packages.game);
importPackage(Packages.resource);
importPackage(Packages.sound);
importPackage(Packages.physics);
importPackage(Packages.util);
importPackage(Packages.test);
importPackage(Packages.graphics);
importPackage(Packages.script);
importPackage(Packages.input);
importPackage(java.lang);
importPackage(org.lwjgl.input);
importClass(org.lwjgl.util.vector.Vector3f);
importClass(org.lwjgl.util.vector.Quaternion);

FontManager.getInstance().create();
TextureManager.getInstance().create();
SoundManager.getInstance().create();
ThreeDPhysicsManager.getInstance().create();
DebugMessages.getInstance().create();
GameStateManager.getInstance().create();
Console.getInstance().create();

InputManager.getInstance().put(Keyboard.KEY_UP);
InputManager.getInstance().put(Keyboard.KEY_DOWN);
InputManager.getInstance().put(Keyboard.KEY_LEFT);
InputManager.getInstance().put(Keyboard.KEY_RIGHT);
InputManager.getInstance().put(Keyboard.KEY_W);
InputManager.getInstance().put(Keyboard.KEY_S);
InputManager.getInstance().put(Keyboard.KEY_A);
InputManager.getInstance().put(Keyboard.KEY_D);
InputManager.getInstance().put(Keyboard.KEY_LSHIFT);
Mouse.setGrabbed(true);

var grav = new Gravity();
var ter = new WavefrontModel("water_map_fix");
ter.create();

ScriptUtil.waitUntilLoaded(ter);
var terDisp = new ThreeDModel(ter);
terDisp.create();
ScriptUtil.waitUntilLoaded(terDisp);
ThreeDGraphicsManager.getInstance().add(terDisp);
var cm = new CollisionMesh(ter.getObjects());
cm.create();
ThreeDPhysicsManager.getInstance().setCollisionMesh(cm);

var agg = AggregateModelEntity.aggregateModelEntityFromPath("landing_module_fix");
var gs = new GameScript("function update(ent, delta) { System.out.println(1000 / delta); }");
gs.create();
agg.addScript(gs);
ThreeDGraphicsManager.getInstance().add(agg);
agg.setPosition(new Vector3f(0, -200, 0));
ThreeDPhysicsManager.getInstance().add(agg);
agg.addForceGenerator(grav);

var player = new FocusPlayer(agg);
player.create();
Game.setPlayer(player);
ThreeDGraphicsManager.getInstance().setViewPoint(player.getViewPoint());

var keyListenerGenerator = new JavaAdapter(ForceGenerator, {
    applyForce: function(physEnt) {
        if (InputManager.getInstance().get(Keyboard.KEY_LSHIFT).isDown()) {
            var up = new Vector3f(0, 80, 0);
            physEnt.applyForce(Utilities.transform(up, physEnt.getOrientation()));
            physEnt.setAwake(true);
        }
        var torque = new Vector3f();
        if (InputManager.getInstance().get(Keyboard.KEY_W).isDown()) {
            torque.setX(torque.getX() - 1);
        }
        if (InputManager.getInstance().get(Keyboard.KEY_S).isDown()) {
            torque.setX(torque.getX() + 1);
        }
        if (InputManager.getInstance().get(Keyboard.KEY_A).isDown()) {
            torque.setZ(torque.getZ() + 1);
        }
        if (InputManager.getInstance().get(Keyboard.KEY_D).isDown()) {
            torque.setZ(torque.getZ() - 1);
        }
        if(torque.lengthSquared() > 0) {
            torque.normalise();
            torque.scale(25);
            physEnt.applyTorque(torque);
        }
    }});
agg.addForceGenerator(keyListenerGenerator);
    
var rocket = SimpleModelEntity.simpleModelEntityFromPath("solid_rocket_fix");
rocket.create();
ThreeDGraphicsManager.getInstance().add(rocket);
rocket.setPosition(new Vector3f(0, 8, 0));
ThreeDPhysicsManager.getInstance().add(rocket);
rocket.addForceGenerator(grav);
    
var s = new SkySphere(SkySphere.SkyType.PLAIN_NIGHT);
s.create();
ThreeDGraphicsManager.getInstance().addGraphic3D(s, -100);
    
Game.setInitializing(false);