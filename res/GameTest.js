/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

importClass(Packages.test.GameTest);

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
importClass(org.lwjgl.input.Keyboard);
importClass(org.lwjgl.input.Mouse);
importPackage(java.lang);
importClass(org.lwjgl.util.vector.Vector3f);

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
var player = new CameraPlayer();
player.create();
Game.setPlayer(player);
ThreeDGraphicsManager.getInstance().setViewPoint(player.getViewPoint());

var grav = new Gravity();
var ter = new WavefrontModel("terrain-fix");
ter.create();

yield(ter);
var terDisp = new ThreeDModel(ter);
terDisp.create();
yield(terDisp);
ThreeDGraphicsManager.getInstance().add(terDisp);
var cm = new CollisionMesh(ter.getObjects());
cm.create();
ThreeDPhysicsManager.getInstance().setCollisionMesh(cm);
    
    
player.setPosition(new Vector3f(-5,5, -5));
/*player.getPhysicalEntity().addForceGenerator(grav);*/

var green = RigidBody.rigidBodyFromPath("tall_fix");
green.create();
ThreeDGraphicsManager.getInstance().add(green);
green.setPosition(new Vector3f(-15, 10, -10));
ThreeDPhysicsManager.getInstance().add(green);
green.addForceGenerator(grav);
    
    
var s = new SkySphere(SkySphere.SkyType.PLAIN_NIGHT);
s.create();
ThreeDGraphicsManager.getInstance().addGraphic3D(s, -100);
    
Game.setInitializing(false);