package rip.skyland.phase.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public enum CC {

    BLACK('0', "black"),
    DARK_BLUE('1', "dark_blue"),
    DARK_GREEN('2', "dark_green"),
    DARK_AQUA('3', "dark_aqua"),
    DARK_RED('4', "dark_red"),
    PURPLE('5', "dark_purple"),
    DARK_PURPLE('5', "dark_purple"),
    GOLD('6', "gold"),
    ORANGE('6', "gold"),
    GRAY('7', "gray"),
    DARK_GRAY('8', "dark_gray"),
    BLUE('9', "blue"),
    GREEN('a', "green"),
    AQUA('b', "aqua"),
    RED('c', "red"),
    LIGHT_PURPLE('d', "light_purple"),
    PINK('d', "light_purple"),
    YELLOW('e', "yellow"),
    WHITE('f', "white"),
    MAGIC('k', "obfuscated"),
    BOLD('l', "bold"),
    STRIKETHROUGH('m', "strikethrough"),
    UNDERLINE('n', "underline"),
    ITALIC('o', "italic"),
    RESET('r', "reset"),
    NONE("none");
    public static Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + '§' + "[0-9A-FK-OR]");
    private static HashMap<Character, CC> BY_CHAR = new HashMap<>();
    private char code;

    private String toString;
    private String name;

    CC(String name) {
        this.name = name;
        this.toString = "";
    }

    CC(char code, String name) {
        this.code = code;
        this.name = name;
        this.toString = new String(new char[]{'§', code});
    }

    public CC nextCC() {
        int o = this.ordinal()+1 >=26 ? 0 : this.ordinal()+1;

        while(CC.values()[o].name.equalsIgnoreCase(this.name)) {
            o++;
        }

        return CC.values()[o];
    }

    public String toString() { return this.toString; }

    public static String stripColor(String input) {
        return input == null ? null : STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }

    public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
        char[] b = textToTranslate.toCharArray();

        for(int i = 0; i < b.length - 1; ++i) {
            if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
                b[i] = 167;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }

        return new String(b);
    }

    public static CC getChatColorByCode(String colorCode){
        switch (colorCode){
            case "&b" : return CC.AQUA;
            case "&0" : return CC.BLACK;
            case "&9" : return CC.BLUE;
            case "&l" : return CC.BOLD;
            case "&3" : return CC.DARK_AQUA;
            case "&1" : return CC.DARK_BLUE;
            case "&8" : return CC.DARK_GRAY;
            case "&2" : return CC.DARK_GREEN;
            case "&5" : return CC.DARK_PURPLE;
            case "&4" : return CC.DARK_RED;
            case "&6" : return CC.GOLD;
            case "&7" : return CC.GRAY;
            case "&a" : return CC.GREEN;
            case "&o" : return CC.ITALIC;
            case "&d" : return CC.LIGHT_PURPLE;
            case "&k" : return CC.MAGIC;
            case "&c" : return CC.RED;
            case "&r" : return CC.RESET;
            case "&m" : return CC.STRIKETHROUGH;
            case "&n" : return CC.UNDERLINE;
            case "&e" : return CC.YELLOW;
            default: return CC.WHITE;
        }
    }


    public static String translate(String textToTranslate) {
        return translateAlternateColorCodes('&', textToTranslate);
    }

    public static List<String> translate(List<String> textToTranslate) {
        List<String> translatedStrings = new ArrayList<>();
        textToTranslate.forEach(string -> translatedStrings.add(translate(string)));
        return translatedStrings;
    }

    public static CC getByChar(char code) {
        return BY_CHAR.get(code);
    }

    public String getName() {
        return this.name;
    }
    public static String getLastColors(String input) {
        String result = "";
        int length = input.length();

        for(int index = length - 1; index > -1; --index) {
            char section = input.charAt(index);
            if (section == 167 && index < length - 1) {
                char c = input.charAt(index + 1);
                CC color = getByChar(c);
                if (color != null) {
                    result = color.toString() + result;
                    if (color.equals(RESET)) {
                        break;
                    }
                }
            }
        }

        return result;
    }

    static {
        CC[] var0 = values();

        for (CC colour : var0) {
            BY_CHAR.put(colour.code, colour);
        }

    }
}