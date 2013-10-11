/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
importPackage(Packages.graphics);
importClass(Packages.input.InputManager);
importClass(org.lwjgl.input.Keyboard);
importClass(org.lwjgl.input.Mouse);

InputManager.getInstance().put(Keyboard.KEY_UP);
InputManager.getInstance().put(Keyboard.KEY_DOWN);
InputManager.getInstance().put(Keyboard.KEY_LEFT);
InputManager.getInstance().put(Keyboard.KEY_RIGHT);
Mouse.setGrabbed(true);

var s = new SkySphere(SkySphere.SkyType.PLAIN_DAY);
s.create();
ThreeDGraphicsManager.getInstance().addGraphic3D(s, -100);
