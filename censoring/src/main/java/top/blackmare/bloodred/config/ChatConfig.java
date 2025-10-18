package top.blackmare.bloodred.config;

import java.util.Arrays;
import java.util.List;

public class ChatConfig {
    public List<String> bannedWords;
    public String censorCharacter;
    public boolean caseSensitive;
    public boolean logCensoredMessages;
    public String censorMode; // "full", "partial", "first_last"

    public ChatConfig() {
        // Default values
        this.bannedWords = Arrays.asList("badword", "example");
        this.censorCharacter = "*";
        this.caseSensitive = false;
        this.logCensoredMessages = true;
        this.censorMode = "partial"; // full = "****", partial = "b***d", first_last = "b***d"
    }

    public ChatConfig(List<String> bannedWords, String censorCharacter, boolean caseSensitive, 
                     boolean logCensoredMessages, String censorMode) {
        this.bannedWords = bannedWords;
        this.censorCharacter = censorCharacter;
        this.caseSensitive = caseSensitive;
        this.logCensoredMessages = logCensoredMessages;
        this.censorMode = censorMode;
    }
}