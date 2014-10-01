/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var grav = new Gravity();

var ter = new WavefrontModel("water_map");
var tercol = new WavefrontModel("water_map");
ter.create();
tercol.create();

ScriptUtil.waitUntilLoaded(ter);
ScriptUtil.waitUntilLoaded(tercol);
var terDisp = new ThreeDModel(ter);
terDisp.create();
ScriptUtil.waitUntilLoaded(terDisp);
ThreeDGraphicsManager.getInstance().add(terDisp);
var cm = new CollisionMesh(tercol.getObjects());
cm.create();
ThreeDPhysicsManager.getInstance().setCollisionMesh(cm);

var agg = AggregateModelEntity.aggregateModelEntityFromPath("agg8");
var gs = ScriptManager.getInstance().loadScript("rocket.js");
agg.addScript(gs);
ThreeDGraphicsManager.getInstance().add(agg);
agg.setPosition(new Vector3f(30, -97, -40));
ThreeDPhysicsManager.getInstance().add(agg);
agg.addForceGenerator(grav);

var player = new FocusPlayer(agg);
player.create();
Game.setPlayer(player);
ThreeDGraphicsManager.getInstance().setViewPoint(player.getViewPoint());
    
    
var s = new SkySphere(SkySphere.SkyType.PLAIN_NIGHT);
s.create();
ThreeDGraphicsManager.getInstance().addGraphic3D(s, -1);
    