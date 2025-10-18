package top.blackmare.bloodred;

import top.blackmare.bloodred.config.AnvilConfig;
import top.blackmare.bloodred.config.ConfigManager;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class AnvilCensor {
    private final ConfigManager configManager;

    public AnvilCensor(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public String censorItemName(String itemName) {
        AnvilConfig config = configManager.getAnvilConfig();
        
        if (config.bannedWords == null || config.bannedWords.isEmpty()) {
            return itemName;
        }

        String result = itemName;
        boolean itemWasCensored = false;

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
                itemWasCensored = true;
                
                if (config.preventRename) {
                    // Return default replacement name instead of censoring
                    result = config.defaultReplacementName;
                    break; // No need to check other words if we're replacing entirely
                } else {
                    // Censor the banned word
                    String replacement = createCensoredWord(bannedWord, config);
                    result = matcher.replaceAll(replacement);
                }
            }
        }

        if (itemWasCensored && config.logCensoredItems) {
            CensorIt.LOGGER.info("Censored item name (length: {}, censored words: {})", 
                itemName.length(), countCensoredWords(itemName, config));
        }

        return result;
    }

    private String createCensoredWord(String word, AnvilConfig config) {
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

    public boolean containsBannedWord(String itemName) {
        AnvilConfig config = configManager.getAnvilConfig();
        
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

            if (pattern.matcher(itemName).find()) {
                return true;
            }
        }

        return false;
    }
    
    private int countCensoredWords(String itemName, AnvilConfig config) {
        int count = 0;
        for (String bannedWord : config.bannedWords) {
            if (bannedWord == null || bannedWord.trim().isEmpty()) {
                continue;
            }
            String patternString = "\\b" + Pattern.quote(bannedWord.trim()) + "\\b";
            Pattern pattern = config.caseSensitive 
                ? Pattern.compile(patternString)
                : Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
            if (pattern.matcher(itemName).find()) {
                count++;
            }
        }
        return count;
    }
}