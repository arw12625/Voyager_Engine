/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

importPackage(Packages.game, Packages.graphics, Packages.input,
    Packages.physics, Packages.resource, Packages.script, Packages.sound,
    Packages.test, Packages.update, Packages.util);
    
importPackage(org.lwjgl.input);
importClass(org.lwjgl.util.vector.Vector3f);
importClass(org.lwjgl.util.vector.Quaternion);

importPackage(org.lwjgl.opengl);

var temp = java.lang;

function print(obj) {
    temp.System.out.println(obj);
}
