package net.tvc.backend.censorit.config;

import java.util.Arrays;
import java.util.List;

public class SignConfig {
    public List<String> bannedWords;
    public String censorCharacter;
    public boolean caseSensitive;
    public boolean logCensoredSigns;
    public String censorMode; // "full", "partial", "first_last"
    public boolean preventSignEdit; // If true, prevents sign editing entirely when banned word is detected
    public String defaultReplacementText; // Default text to use when preventing sign edit

    public SignConfig() {
        // Default values
        this.bannedWords = Arrays.asList("badword", "example", "inappropriate");
        this.censorCharacter = "*";
        this.caseSensitive = false;
        this.logCensoredSigns = true;
        this.censorMode = "partial"; // full = "****", partial = "b***d", first_last = "b***d"
        this.preventSignEdit = false;
        this.defaultReplacementText = "[Censored]";
    }

    public SignConfig(List<String> bannedWords, String censorCharacter, boolean caseSensitive, 
                     boolean logCensoredSigns, String censorMode, boolean preventSignEdit, 
                     String defaultReplacementText) {
        this.bannedWords = bannedWords;
        this.censorCharacter = censorCharacter;
        this.caseSensitive = caseSensitive;
        this.logCensoredSigns = logCensoredSigns;
        this.censorMode = censorMode;
        this.preventSignEdit = preventSignEdit;
        this.defaultReplacementText = defaultReplacementText;
    }
}