package net.tvc.backend.censorit.config;

import java.util.Arrays;
import java.util.List;

public class AnvilConfig {
    public List<String> bannedWords;
    public String censorCharacter;
    public boolean caseSensitive;
    public boolean logCensoredItems;
    public String censorMode; // "full", "partial", "first_last"
    public boolean preventRename; // If true, prevents renaming entirely when banned word is detected
    public String defaultReplacementName; // Default name to use when preventing rename

    public AnvilConfig() {
        // Default values
        this.bannedWords = Arrays.asList("badword", "example", "inappropriate");
        this.censorCharacter = "*";
        this.caseSensitive = false;
        this.logCensoredItems = true;
        this.censorMode = "partial"; // full = "****", partial = "b***d", first_last = "b***d"
        this.preventRename = false;
        this.defaultReplacementName = "Censored Item";
    }

    public AnvilConfig(List<String> bannedWords, String censorCharacter, boolean caseSensitive, 
                     boolean logCensoredItems, String censorMode, boolean preventRename, 
                     String defaultReplacementName) {
        this.bannedWords = bannedWords;
        this.censorCharacter = censorCharacter;
        this.caseSensitive = caseSensitive;
        this.logCensoredItems = logCensoredItems;
        this.censorMode = censorMode;
        this.preventRename = preventRename;
        this.defaultReplacementName = defaultReplacementName;
    }
}