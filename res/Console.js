/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
importClass(Packages.script.Console);
 */
importClass(Packages.script.Console);
importPackage(java.lang);
function cls() { Console.getInstance().clearScreen(); }
function echo(str) { Console.getInstance().write(str); }
function err(show) { Console.getInstance().setShowError(show); }
function yolo() { echo('Praise be Sanjay'); }