/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package script;

import game.*;
import input.InputManager;
import input.KeyStatus;
import java.util.ArrayList;
import java.util.HashMap;
import org.lwjgl.input.Keyboard;
import javax.script.*;
import org.mozilla.javascript.Scriptable;

/**
 *
 * @author Andy
 */
public class Console extends GameObject implements update.Updateable {

    StringBuilder line;
    ArrayList<String> inputs;
    ArrayList<String> lines;
    ScriptManager scriptManager;
    Scriptable scope;
    HashMap<Integer, Character> keyNorm;
    HashMap<Integer, Character> keyShift;
    resource.FontResource consoleFont;
    graphics.Menu terminal;
    graphics.HudGraphic lineGraphic;
    private int upCounter;
    private int underCounter;
    private static final int counterReset = 500;
    private boolean under;
    private boolean showError;
    private boolean show;
    private boolean inputEnable;
    private HashMap<Integer, Boolean> keyPressedStates;
    static final int returnKey = Keyboard.KEY_RETURN;
    static final int backKey = Keyboard.KEY_BACK;
    static final int lshiftKey = Keyboard.KEY_LSHIFT;
    static final int rshiftKey = Keyboard.KEY_RSHIFT;
    static final int upKey = Keyboard.KEY_UP;
    static final int downKey = Keyboard.KEY_DOWN;
    static final int leftKey = Keyboard.KEY_LEFT;
    static final int rightKey = Keyboard.KEY_RIGHT;
    static Console instance;

    @Override
    public void create() {
        super.create();
        input.InputManager keyboard = input.InputManager.getInstance();
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

        resource.FontManager fm = resource.FontManager.getInstance();
        consoleFont = fm.createFont("console/console", new org.newdawn.slick.Color(102, 135, 172));
        lineGraphic = new graphics.HudGraphic("Console", resource.TextureManager.getInstance().loadTextureResource("console/terminal.png"), "", consoleFont, 10, 8);
        
        lineGraphic.create();
        terminal = new graphics.Menu(0, 0, false, lineGraphic, true, null, null);
        terminal.create();
        graphics.ThreeDGraphicsManager.getInstance().add(terminal);

        scriptManager = ScriptManager.getInstance();
        scope = scriptManager.getChildScope(scriptManager.globalScope);
        loadConsoleScripts();

        setEnabled(false);
        setShowError(true);
        
        inputs.add("create('rocket/', 'rocket');"); // TODO: Remove this.
        inputs.add("var r = create('rocket/', 'rocket');");
        inputs.add("ScriptUtil.insertModel({ prefix: \"rocket/\", path: \"rocket\" })");
    }

    public static Console getInstance() {
        if (instance == null) {
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
    public boolean update(int delta) {
        if (inputEnable) {
            //for this iteration, update the pressed keys 
            updateKeys();

            String oldLine = line.toString();
            input.InputManager keyboard = input.InputManager.getInstance();
            boolean shift = keyboard.getKey(lshiftKey).isDown() || keyboard.getKey(rshiftKey).isDown();
            for (Integer i : keyPressedStates.keySet()) {
                if (keyPressedStates.get(i)) {

                    if (i == returnKey && shift) {
                        //line.append('\n');
                    } else if (i == backKey && line.length() > 0) {
                        line.delete(line.length() - 1, line.length());
                    } else if (i == upKey) {
                        upCounter++;
                    } else if (i == downKey) {
                        upCounter--;
                    } else {
                        Character c = shift ? keyShift.get(i) : keyNorm.get(i);
                        line.append(c);
                    }
                }
            }
            if (upCounter < 0) {
                upCounter = 0;
            }
            if (upCounter > inputs.size()) {
                upCounter = inputs.size();
            }
            if (keyPressedStates.get(upKey) || keyPressedStates.get(downKey) && inputs.size() > 0) {
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

            if (keyPressedStates.get(returnKey) && !shift) {
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

        return false;
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
        int start = Math.max(lines.size() + counter - (graphics.ThreeDGraphicsManager.getInstance().getHeight() / consoleFont.getHeight() - 1), 0);
        for (int i = start; i < lines.size(); i++) {
            display.append(lines.get(i) + "\n");
        }
        display.append(format + (under ? "_" : ""));
        lineGraphic.setText(display.toString());

    }

    private String getPrefix() {
        return "< ";// + Game.getPlayer().getName() + " > ";
    }

    private ArrayList<String> wordWrap(String input) {
        ArrayList<String> wrapped = new ArrayList<String>();
        int width = (int) ((graphics.ThreeDGraphicsManager.getInstance().getWidth()) / consoleFont.getWidth());
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
        wordWrap.insert(0, prefix);
        int width = (int) ((graphics.ThreeDGraphicsManager.getInstance().getWidth()) / consoleFont.getWidth());
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
            scriptManager.evaluateText(command, scope);
        } catch (Exception se) {
            write("Not a valid command!");
            if (showError) {
                write(se.getMessage());
            }
        }
    }

    public void setShowError(boolean show) {
        this.showError = show;
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
        scriptManager.loadAndExecute("engine_scripts/Console.js");
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
        keyNorm.put(Keyboard.KEY_COLON, ';');
        keyShift.put(Keyboard.KEY_COLON, ':');

        keyPressedStates = new HashMap<Integer, Boolean>();
        //Add keys that are pressed to a map for "debouncing purposes"
        //The buckets are initialized to false and is iterated over through the keys
        for (int key : keyNorm.keySet()) {
            keyPressedStates.put(key, false);
        }
        //A list of non character keys that are pressed rather than held
        int[] specialKeys = {upKey, downKey, leftKey, rightKey, returnKey, backKey};
        for (int key : specialKeys) {
            keyPressedStates.put(key, false);
        }
    }

    private void updateKeys() {
        InputManager im = InputManager.getInstance();
        //Iterate through the keys of the pressed states keys
        for (int key : keyPressedStates.keySet()) {
            keyPressedStates.put(key, im.getKey(key).isPressed());
        }
    }
}
