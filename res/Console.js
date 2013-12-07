/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
importClass(Packages.script.Console);
 */
importPackage(Packages.script);
importPackage(java.lang);
function cls() { Console.getInstance().clearScreen(); }
function echo(str) { Console.getInstance().write(str); }
function err(show) { Console.getInstance().setShowError(show); }
function yolo() { Console.echo('Praise be Sanjay'); }