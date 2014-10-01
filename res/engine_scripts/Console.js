/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
importClass(Packages.script.Console);
 */
with(script) {
    function cls() { Console.getInstance().clearScreen(); }
    function echo(str) { Packages.script.Console.getInstance().write(str); }
    function err(show) { Console.getInstance().setShowError(show); }
    function yolo() { echo('Praise be Sanjay'); }
    function sanic() { echo('Gotta go fast! m94'); }
}
