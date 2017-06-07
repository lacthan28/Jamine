package jamine.lang;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lacthan28 on 6/6/2017 - 10:11 PM.
 */
public class BaseLang {
    public static final String FALLBACK_LANGUAGE = "eng";
    private static final int MAX = 50;
    public static final ArrayList<String> filePath = new ArrayList<>();

    public static ArrayList<String> array_filter(File[] file) {
        for (File aFile : file) {
            if (aFile.isFile()) {
                processFile(aFile);
            } else if (aFile.isDirectory()) {
                File[] file2 = aFile.listFiles();
                if (file2 != null) {
                    for (File aFile2 : file2) {
                        if (aFile2.isFile())
                            processFile(aFile2);
                    }
                }
            }
        }
        return filePath;
    }

    private static void processFile(File f) {
        if (f.getName().endsWith(".ini")) {
            filePath.add(f.getAbsolutePath());
        }
    }

    public static HashMap<String, String> getLanguageList(String path) {
        getLanguageList("");
        if (path.isEmpty()) {
            path = jamine.PATH + "src/pocketmine/lang/locale/";
        }

        File f = new File(path);
        if (f.isDirectory()) {
            File[] allFiles = f.listFiles();

            if (allFiles != null) {
                ArrayList<String> files = array_filter(allFiles);

                HashMap<String, String> result = new HashMap<>();

                for (String file : files) {
                    HashMap<String, String> strings = new HashMap<>();
                    BaseLang.loadLang(path + file, strings);
                    if (strings.get("language.name") != null) {
                        result.put(file.substring(0, -4), strings.get("language.name"));
                    }
                }
                return result;
            }
        }
        return new HashMap<>();
    }

    String langName;

    String[] lang = new String[MAX];
    String[] fallbackLang = new String[MAX];

    public BaseLang(String lang, String path, String fallback) {

        this.langName = lang.toLowerCase();

        if (path == null) {
            path = jamine.PATH + "src/pocketmine/lang/locale/";
        }

        if (!BaseLang.loadLang(file = path + this.langName + ".ini", this.lang))
            MainLogger.getLogger().error("Missing required language file file");
        if (!BaseLang.loadLang(file = path.fallback.".ini", this.fallbackLang))
            MainLogger.getLogger().error("Missing required language file file");
    }

    public void getName() {
        return this.get("language.name");
    }

    public void getLang() {
        return this.langName;
    }

    protected static void loadLang(String path, HashMap<String, String> d) {
        if (file_exists(path)) {
            d = parse_ini_file(path, false, INI_SCANNER_RAW);
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param string      str
     * @param string[]    params
     * @param string|null onlyPrefix
     * @return string
     */
    public void translateString(str, array params =[], onlyPrefix =null) {
        baseText = this.get(str);
        baseText = this.parseTranslation((baseText != = null and(onlyPrefix == = null or strpos(str, onlyPrefix) == = 0)) ? baseText : str, onlyPrefix)

        foreach(params as i = > p)
        baseText = str_replace("{%i}", this.parseTranslation((string) p), baseText, onlyPrefix);

        return str_replace("%0", "", baseText); //fixes a client bug where %0 in translation will cause freeze
    }

    public void translate(TextContainer c) {
        if (c instanceof TranslationContainer) {
            baseText = this.internalGet(c.getText());
            baseText = this.parseTranslation(baseText != = null ? baseText : c.getText());

            foreach(c.getParameters()as i = > p)
            baseText = str_replace("{%i}", this.parseTranslation(p), baseText);
        } else {
            baseText = this.parseTranslation(c.getText());
        }

        return baseText;
    }

    public void internalGet(id) {
        if (isset(this.lang[id])) {
            return this.lang[id];
        }
        elseif(isset(this.fallbackLang[id]))
        return this.fallbackLang[id];

        return null;
    }

    public void get(id) {
        if (isset(this.lang[id])) {
            return this.lang[id];
        }
        elseif(isset(this.fallbackLang[id]))
        return this.fallbackLang[id];

        return id;
    }

    protected void parseTranslation(text, onlyPrefix =null) {
        newString = "";

        replaceString = null;

        len = strlen(text);
        for (i = 0; i < len; ++i) {
            c = text
            i
            if (replaceString != = null) {
                ord = ord(c);
                if (
                        (ord >= 0x30 and ord <=0x39) // 0-9
                or(ord >= 0x41and ord <= 0x5a) // A-Z
                or(ord >= 0x61and ord <= 0x7a) or // a-z
                c == = "." or c ==="-"
                )
                replaceString. = c;
                else
                if ((t = this.internalGet(substr(replaceString, 1))) != = null
                and(onlyPrefix == = null or strpos(replaceString, onlyPrefix) == = 1))
                newString. = t;
                    else
                newString. = replaceString;
                replaceString = null;

                if (c == = "%") {
                    replaceString = c;
                } else {
                    newString. = c;
                }
            } elseif(c == = "%")
            replaceString = c;
            else
            newString. = c;
        }

        if (replaceString != = null) {
            if ((t = this.internalGet(substr(replaceString, 1))) != = null
            and(onlyPrefix == = null or strpos(replaceString, onlyPrefix) == = 1))
            newString. = t;
            else
            newString. = replaceString;
        }

        return newString;
    }
}
