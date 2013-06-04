/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package script;

import game.*;
import graphics.HudGraphic;
import graphics.Menu;
import graphics.ThreeDGraphicsManager;
import org.newdawn.slick.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.input.Keyboard;

import javax.script.*;
import resource.FontManager;
import resource.FontResource;
import resource.TextureManager;

/**
 *
 * @author Andy
 */
public class Console implements Manager {

    StringBuilder line;
    ArrayList<String> inputs;
    ArrayList<String> lines;
    ScriptManager scriptManager;
    HashMap<Integer, Character> keyNorm;
    HashMap<Integer, Character> keyShift;
    FontResource consoleFont;
    Menu terminal;
    HudGraphic lineGraphic;
    private int upCounter;
    private int underCounter;
    private static final int counterReset = 500;
    private boolean under;
    private boolean error;
    private boolean show;
    private boolean inputEnable;
    static final int returnKey = Keyboard.KEY_RETURN;
    static final int backKey = Keyboard.KEY_BACK;
    static final int lshiftKey = Keyboard.KEY_LSHIFT;
    static final int rshiftKey = Keyboard.KEY_RSHIFT;
    static final int upKey = Keyboard.KEY_UP;
    static final int downKey = Keyboard.KEY_DOWN;
    static Console instance;

    @Override
    public void create() {
        InputManager keyboard = InputManager.getInstance();
        defineKeySets();
        for (Integer i : keyNorm.keySet()) {
            keyboard.put(i);
        }
        keyboard.put(returnKey);
        keyboard.put(backKey);
        keyboard.put(lshiftKey);
        keyboard.put(rshiftKey);
        keyboard.put(upKey);
        keyboard.put(downKey);

        line = new StringBuilder();
        inputs = new ArrayList<String>();
        lines = new ArrayList<String>();

        FontManager fm = FontManager.getInstance();
        consoleFont = fm.createFont("console", new Color(102, 135, 172));
        lineGraphic = new HudGraphic("Console", TextureManager.getInstance().loadTextureResource("terminal.png"), "", consoleFont, 10, 8);
        terminal = new Menu(0, 0, false, lineGraphic, true, null, null);
        ThreeDGraphicsManager.getInstance().addGraphic2D(terminal, 100);
        
        scriptManager = ScriptManager.getInstance();
        loadConsoleScripts();

        setEnabled(false);

    }

    public static Console getInstance() {
        if(instance == null) {
            instance = new Console();
        }
        return instance;
    }

    @Override
    public void destroy() {
    }

    public void write(String message) {
        lines.addAll(wordWrap(message));
    }

    @Override
    public String getName() {
        return "Console";
    }

    @Override
    public void update(int delta) {
        if (inputEnable) {
            String oldLine = line.toString();
            InputManager keyboard = InputManager.getInstance();
            boolean shift = keyboard.get(lshiftKey).isDown() || keyboard.get(rshiftKey).isDown();
            for (Integer i : keyNorm.keySet()) {
                KeyStatus k = keyboard.get(i);
                if (k.isPressed()) {
                    Character c = shift ? keyShift.get(i) : keyNorm.get(i);
                    line.append(c);
                }
            }
            if (keyboard.get(returnKey).isPressed() && shift) {
                //line.append('\n');
            }
            if (keyboard.get(backKey).isPressed() && line.length() > 0) {
                line.delete(line.length() - 1, line.length());
            }

            if (keyboard.get(upKey).isPressed()) {
                upCounter++;
            }
            if (keyboard.get(downKey).isPressed()) {
                upCounter--;
            }
            if (upCounter < 0) {
                upCounter = 0;
            }
            if (upCounter > inputs.size()) {
                upCounter = inputs.size();
            }
            if (keyboard.get(upKey).isPressed() || keyboard.get(downKey).isPressed() && inputs.size() > 0) {
                if (upCounter == 0) {
                    line = new StringBuilder();
                } else {
                    line = new StringBuilder(inputs.get(inputs.size() - upCounter));
                }
            }

            underCounter -= delta;
            if (underCounter < 0) {
                underCounter = counterReset;
                under = !under;
            }

            updateMessage();

            if (keyboard.get(returnKey).isPressed() && !shift) {
                String str = line.toString();
                if (!str.equals("")) {
                    inputs.add(str);
                }
                String[] lineSubs = formatString(str).split("\n");
                for (String s : lineSubs) {
                    lines.add(s);
                }
                upCounter = 0;
                execute(str);
                line.delete(0, str.length());
            }
        }
    }

    private void updateMessage() {
        StringBuilder display = new StringBuilder();
        String format = formatString(line.toString());
        int counter = 0;
        for (char c : format.toCharArray()) {
            if (c == '\n') {
                counter++;
            }
        }
        int start = Math.max(lines.size() + counter - (ThreeDGraphicsManager.getInstance().getHeight() / consoleFont.getHeight() - 1), 0);
        for (int i = start; i < lines.size(); i++) {
            display.append(lines.get(i) + "\n");
        }
        display.append(format + (under ? "_" : ""));
        lineGraphic.setMessage(display.toString());

    }

    private String getPrefix() {
        return "< ";// + Game.getPlayer().getName() + " > ";
    }

    private ArrayList<String> wordWrap(String input) {
        ArrayList<String> wrapped = new ArrayList<String>();
        int width = (int) ((ThreeDGraphicsManager.getInstance().getWidth()) / consoleFont.getWidth());
        int i = 0;
        while (i < input.length()) {
            if (i + width < input.length()) {
                wrapped.add(input.substring(i, i + width));
            } else {
                wrapped.add(input.substring(i));
            }
            i += width;
        }
        //System.out.println(wrapped);
        return wrapped;
    }

    private String formatString(String input) {
        String prefix = getPrefix();
        StringBuilder wordWrap = new StringBuilder(input);
        wordWrap.insert(0, getPrefix());
        int width = (int) ((ThreeDGraphicsManager.getInstance().getWidth()) / consoleFont.getWidth());
        for (int i = 1; i < wordWrap.length() / width + 1; i++) {
            wordWrap.insert(i * width, "\n");
        }
        return wordWrap.toString();
    }

    public String getLine() {
        return line.toString();
    }

    private void execute(String command) {
        try {
            scriptManager.eval(command);
        } catch (ScriptException se) {
            write("Not a valid command!");
            if (error) {
                write(se.getMessage());
            }
        }
    }

    public void setShowError(boolean show) {
        this.error = show;
    }

    public void setEnabled(boolean value) {
        setShow(value);
        setInput(value);
    }

    public void setShow(boolean show) {
        this.show = show;
        terminal.setShow(show);
    }

    public void setInput(boolean show) {
        this.inputEnable = show;
    }

    public void loadConsoleScripts() {
        try {
            scriptManager.eval("importClass(Packages.script.Console)");
            scriptManager.eval("function cls() { Console.getInstance().clearScreen() }");
            scriptManager.eval("function echo(str) { Console.getInstance().write(str) }");
            scriptManager.eval("function err(show) { Console.getInstance().setShowError(show) }");
        } catch (ScriptException ex) {
            write("Console Scripts failed to load");
            ex.printStackTrace();
        }
    }

    public void clearScreen() {
        lines.clear();
    }

    public void defineKeySets() {
        keyNorm = new HashMap<Integer, Character>();
        keyShift = new HashMap<Integer, Character>();

        char[] charNorm = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        char[] charShift = "ABCDEFGHIJKLMNOPQRSTUVWXYZ)!@#$%^&*(".toCharArray();
        ArrayList<Integer> keyIndex = new ArrayList<Integer>();
        for (char c : charNorm) {
            keyIndex.add(Keyboard.getKeyIndex(("" + c).toUpperCase()));
        }
        for (int i = 0; i < keyIndex.size(); i++) {
            keyNorm.put(keyIndex.get(i), charNorm[i]);
            keyShift.put(keyIndex.get(i), charShift[i]);
        }
        keyNorm.put(Keyboard.KEY_SPACE, ' ');
        keyShift.put(Keyboard.KEY_SPACE, ' ');
        keyNorm.put(Keyboard.KEY_MINUS, '-');
        keyShift.put(Keyboard.KEY_MINUS, '_');
        keyNorm.put(Keyboard.KEY_EQUALS, '=');
        keyShift.put(Keyboard.KEY_EQUALS, '+');
        keyNorm.put(Keyboard.KEY_LBRACKET, '[');
        keyShift.put(Keyboard.KEY_LBRACKET, '{');
        keyNorm.put(Keyboard.KEY_RBRACKET, ']');
        keyShift.put(Keyboard.KEY_RBRACKET, '}');
        keyNorm.put(Keyboard.KEY_SEMICOLON, ';');
        keyShift.put(Keyboard.KEY_SEMICOLON, ':');
        keyNorm.put(Keyboard.KEY_BACKSLASH, '\\');
        keyShift.put(Keyboard.KEY_BACKSLASH, '|');
        keyNorm.put(Keyboard.KEY_APOSTROPHE, '\'');
        keyShift.put(Keyboard.KEY_APOSTROPHE, '\"');
        keyNorm.put(Keyboard.KEY_COMMA, ',');
        keyShift.put(Keyboard.KEY_COMMA, '<');
        keyNorm.put(Keyboard.KEY_PERIOD, '.');
        keyShift.put(Keyboard.KEY_PERIOD, '>');
        keyNorm.put(Keyboard.KEY_SLASH, '/');
        keyShift.put(Keyboard.KEY_SLASH, '?');
    }
}
