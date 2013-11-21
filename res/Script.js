/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
importClass(Packages.game.Game);

importClass(Packages.resource.WavefrontModel);
importClass(Packages.graphics.ThreeDModel);
importClass(Packages.graphics.ThreeDGraphicsManager);
importClass(Packages.physics.CollisionMesh);
importClass(Packages.physics.ThreeDPhysicsManager);
importClass(Packages.update.Updateable);
importClass(Packages.update.UpdateManager);
importClass(Packages.script.ScriptUtil);
importPackage(java.lang);

System.out.println("Script.js start");

function quit() {
    Game.quit();
}
function exit() {
    Game.quit();
}
function testing() {
    System.out.println("rawr");
}

System.out.println("Script.js Done");