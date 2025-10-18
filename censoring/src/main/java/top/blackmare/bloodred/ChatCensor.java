package top.blackmare.bloodred;

import top.blackmare.bloodred.config.ChatConfig;
import top.blackmare.bloodred.config.ConfigManager;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ChatCensor {
    private final ConfigManager configManager;

    public ChatCensor(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public String censorMessage(String message) {
        ChatConfig config = configManager.getConfig();
        
        if (config.bannedWords == null || config.bannedWords.isEmpty()) {
            return message;
        }

        String result = message;
        boolean messageWasCensored = false;

        for (String bannedWord : config.bannedWords) {
            if (bannedWord == null || bannedWord.trim().isEmpty()) {
                continue;
            }

            // Create regex pattern for word boundaries
            String patternString = "\\b" + Pattern.quote(bannedWord.trim()) + "\\b";
            Pattern pattern;
            
            if (config.caseSensitive) {
                pattern = Pattern.compile(patternString);
            } else {
                pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
            }

            Matcher matcher = pattern.matcher(result);
            
            if (matcher.find()) {
                messageWasCensored = true;
                String replacement = createCensoredWord(bannedWord, config);
                result = matcher.replaceAll(replacement);
            }
        }

        if (messageWasCensored && config.logCensoredMessages) {
            CensorIt.LOGGER.info("Censored message from player (length: {}, censored words: {})", 
                message.length(), countCensoredWords(message, config));
        }

        return result;
    }

    private String createCensoredWord(String word, ChatConfig config) {
        String censorChar = config.censorCharacter;
        
        switch (config.censorMode.toLowerCase()) {
            case "full":
                return censorChar.repeat(word.length());
                
            case "first_last":
                if (word.length() <= 2) {
                    return censorChar.repeat(word.length());
                }
                return word.charAt(0) + censorChar.repeat(word.length() - 2) + word.charAt(word.length() - 1);
                
            case "partial":
            default:
                if (word.length() <= 1) {
                    return censorChar;
                } else if (word.length() <= 3) {
                    return word.charAt(0) + censorChar.repeat(word.length() - 1);
                } else {
                    return word.charAt(0) + censorChar.repeat(word.length() - 2) + word.charAt(word.length() - 1);
                }
        }
    }

    public boolean containsBannedWord(String message) {
        ChatConfig config = configManager.getConfig();
        
        if (config.bannedWords == null || config.bannedWords.isEmpty()) {
            return false;
        }

        for (String bannedWord : config.bannedWords) {
            if (bannedWord == null || bannedWord.trim().isEmpty()) {
                continue;
            }

            String patternString = "\\b" + Pattern.quote(bannedWord.trim()) + "\\b";
            Pattern pattern;
            
            if (config.caseSensitive) {
                pattern = Pattern.compile(patternString);
            } else {
                pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
            }

            if (pattern.matcher(message).find()) {
                return true;
            }
        }

        return false;
    }
    
    private int countCensoredWords(String message, ChatConfig config) {
        int count = 0;
        for (String bannedWord : config.bannedWords) {
            if (bannedWord == null || bannedWord.trim().isEmpty()) {
                continue;
            }
            String patternString = "\\b" + Pattern.quote(bannedWord.trim()) + "\\b";
            Pattern pattern = config.caseSensitive 
                ? Pattern.compile(patternString)
                : Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
            if (pattern.matcher(message).find()) {
                count++;
            }
        }
        return count;
    }
}