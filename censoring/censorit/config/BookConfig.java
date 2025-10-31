package net.tvc.backend.censorit.config;

import java.util.Arrays;
import java.util.List;

public class BookConfig {
    public List<String> bannedWords;
    public String censorCharacter;
    public boolean caseSensitive;
    public boolean logCensoredBooks;
    public String censorMode; // "full", "partial", "first_last"
    public boolean preventBookEdit; // If true, prevents book editing entirely when banned word is detected
    public boolean preventBookSigning; // If true, prevents book signing when banned words detected
    public String defaultReplacementText; // Default text to use when preventing book edit
    public String defaultBookTitle; // Default title when preventing book signing

    public BookConfig() {
        // Default values
        this.bannedWords = Arrays.asList("badword", "example", "inappropriate");
        this.censorCharacter = "*";
        this.caseSensitive = false;
        this.logCensoredBooks = true;
        this.censorMode = "partial"; // full = "****", partial = "b***d", first_last = "b***d"
        this.preventBookEdit = false;
        this.preventBookSigning = false;
        this.defaultReplacementText = "[Censored]";
        this.defaultBookTitle = "Censored Book";
    }

    public BookConfig(List<String> bannedWords, String censorCharacter, boolean caseSensitive, 
                     boolean logCensoredBooks, String censorMode, boolean preventBookEdit, 
                     boolean preventBookSigning, String defaultReplacementText, String defaultBookTitle) {
        this.bannedWords = bannedWords;
        this.censorCharacter = censorCharacter;
        this.caseSensitive = caseSensitive;
        this.logCensoredBooks = logCensoredBooks;
        this.censorMode = censorMode;
        this.preventBookEdit = preventBookEdit;
        this.preventBookSigning = preventBookSigning;
        this.defaultReplacementText = defaultReplacementText;
        this.defaultBookTitle = defaultBookTitle;
    }
}