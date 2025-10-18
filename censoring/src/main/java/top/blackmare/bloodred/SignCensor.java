package top.blackmare.bloodred;

import top.blackmare.bloodred.config.SignConfig;
import top.blackmare.bloodred.config.ConfigManager;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class SignCensor {
    private final ConfigManager configManager;

    public SignCensor(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public String censorSignText(String signText) {
        SignConfig config = configManager.getSignConfig();
        
        if (config.bannedWords == null || config.bannedWords.isEmpty()) {
            return signText;
        }

        String result = signText;
        boolean signWasCensored = false;

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
                signWasCensored = true;
                
                if (config.preventSignEdit) {
                    // Return default replacement text instead of censoring
                    result = config.defaultReplacementText;
                    break; // No need to check other words if we're replacing entirely
                } else {
                    // Censor the banned word
                    String replacement = createCensoredWord(bannedWord, config);
                    result = matcher.replaceAll(replacement);
                }
            }
        }

        if (signWasCensored && config.logCensoredSigns) {
            CensorIt.LOGGER.info("Censored sign text (length: {}, censored words: {})", 
                signText.length(), countCensoredWords(signText, config));
        }

        return result;
    }

    private String createCensoredWord(String word, SignConfig config) {
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

    public boolean containsBannedWord(String signText) {
        SignConfig config = configManager.getSignConfig();
        
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

            if (pattern.matcher(signText).find()) {
                return true;
            }
        }

        return false;
    }
    
    private int countCensoredWords(String signText, SignConfig config) {
        int count = 0;
        for (String bannedWord : config.bannedWords) {
            if (bannedWord == null || bannedWord.trim().isEmpty()) {
                continue;
            }
            String patternString = "\\b" + Pattern.quote(bannedWord.trim()) + "\\b";
            Pattern pattern = config.caseSensitive 
                ? Pattern.compile(patternString)
                : Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
            if (pattern.matcher(signText).find()) {
                count++;
            }
        }
        return count;
    }
}