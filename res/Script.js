/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
importClass(Packages.game.Game);
function quit() { Game.quit(); }
function exit() { Game.quit(); }


function create(prefix, path) {
    importClass(Packages.resource.WavefrontModel);
    importClass(Packages.graphics.ThreeDModel);
    importClass(Packages.graphics.ThreeDGraphicsManager);
    importClass(Packages.physics.CollisionMesh);
    importClass(Packages.physics.ThreeDPhysicsManager);
    importClass(Packages.update.Updateable);
    importClass(Packages.update.UpdateManager);

    var r = new JavaAdapter(Updateable, {
        update: function(delta) {
            var wfModel = new WavefrontModel(prefix, path);
            wfModel.create();
            var tdModel = new ThreeDModel(wfModel);
            tdModel.create();
            ThreeDGraphicsManager.getInstance().add(tdModel);
            var cm = new CollisionMesh(wfModel.getObjects());
            cm.create();
            ThreeDPhysicsManager.getInstance().setCollisionMesh(cm);
            
            return true;
        }
    });
    UpdateManager.getInstance().add(r);
    
    return r;
}

function testing() {
    echo("rawr");    
}
