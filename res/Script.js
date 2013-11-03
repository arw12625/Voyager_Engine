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
importPackage(java.lang);

System.out.println("Script.js start");

function quit() {
    Game.quit();
}
function exit() {
    Game.quit();
}

function create(prefix, path) {

    var r = new JavaAdapter(Updateable, {
        update: function(delta) {
            try {
                var wfModel = new WavefrontModel(prefix, path);
                wfModel.create();
                yield(wfModel);
                var tdModel = new ThreeDModel(wfModel);
                tdModel.create();
                yield(tdModel);
                ThreeDGraphicsManager.getInstance().add(tdModel);
                var cm = new CollisionMesh(wfModel.getObjects());
                cm.create();
                ThreeDPhysicsManager.getInstance().setCollisionMesh(cm);
            } catch (e) {
                System.out.println("Wtf, " + e);
            }
            return true;
        }
    });
    UpdateManager.getInstance().add(r);
    
    return r;
}

function testing() {
    System.out.println("rawr");
}

function yield(resource) {
    if(resource instanceof Resource) {
        while(!resource.isLoaded()) {
        }
    }
    if(resource instanceof GraphicsResource) {
        while(!resource.isProcessed()) {
        }
    }
}

System.out.println("Script.js Done");