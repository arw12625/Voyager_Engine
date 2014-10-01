/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

importClass(Packages.test.GameTest);

importPackage(Packages.game);
importPackage(Packages.resource);
importPackage(Packages.sound);
importPackage(Packages.physics);
importPackage(Packages.util);
importPackage(Packages.test);
importPackage(Packages.graphics);
importPackage(Packages.script);
importPackage(Packages.input);

importClass(org.lwjgl.input.Keyboard);
importClass(org.lwjgl.input.Mouse);
importPackage(java.lang);
importClass(org.lwjgl.util.vector.Vector3f);
importClass(org.lwjgl.util.vector.Quaternion);
importClass(org.lwjgl.opengl.DisplayMode);

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

Mouse.setGrabbed(true);

var grav = new Gravity();
var ter = new WavefrontModel("launch_pad_fix");
ter.create();

ScriptUtil.waitUntilLoaded(ter);
var terDisp = new ThreeDModel(ter);
terDisp.create();
ScriptUtil.waitUntilLoaded(terDisp);
ThreeDGraphicsManager.getInstance().add(terDisp);
var cm = new CollisionMesh(ter.getObjects());
cm.create();
ThreeDPhysicsManager.getInstance().setCollisionMesh(cm);

var green = RigidBody.rigidBodyFromPath("tall_fix");
green.create();
ThreeDGraphicsManager.getInstance().add(green);
green.setPosition(new Vector3f(10, 16.6, 0));
green.setOrientation(Utilities.quatFromAxisAngle(new Vector3f(0, 0, 1), 3.14 / 4));
ThreeDPhysicsManager.getInstance().add(green);
green.addForceGenerator(grav);
    
    
var rocket = RigidBody.rigidBodyFromPath("solid_rocket_fix");
rocket.create();
ThreeDGraphicsManager.getInstance().add(rocket);
rocket.setPosition(new Vector3f(0, 8, 0));
ThreeDPhysicsManager.getInstance().add(rocket);
rocket.addForceGenerator(grav);
    
var s = new SkySphere(SkySphere.SkyType.PLAIN_NIGHT);
s.create();
ThreeDGraphicsManager.getInstance().addGraphic3D(s, -100);

var dingR = SoundManager.getInstance().loadSoundResource("jazz.wav");
dingR.create();
ScriptUtil.waitUntilLoaded(dingR);
var dingS = SoundManager.getInstance().createSound("DING", dingR);
dingS.create();
dingS.loop(true);
dingS.play();
SoundManager.getInstance().mute(true);

var player = new PhysicalPlayer();
player.create();
Game.setPlayer(player);
ThreeDGraphicsManager.getInstance().setViewPoint(player.getViewPoint());
ThreeDPhysicsManager.getInstance().add(player);
player.getPhysicalEntity().setPosition(new Vector3f(-18, 16.6, 7.7));
player.getPhysicalEntity().addForceGenerator(grav);

Game.setInitializing(false);
