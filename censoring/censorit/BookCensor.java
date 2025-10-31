package net.tvc.backend.censorit;

import net.tvc.backend.censorit.config.BookConfig;
import net.tvc.backend.censorit.config.ConfigManager;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class BookCensor {
    private final ConfigManager configManager;

    public BookCensor(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public String censorBookText(String bookText) {
        BookConfig config = configManager.getBookConfig();
        
        if (config.bannedWords == null || config.bannedWords.isEmpty()) {
            return bookText;
        }

        String result = bookText;
        boolean bookWasCensored = false;

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
                bookWasCensored = true;
                
                if (config.preventBookEdit) {
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

        if (bookWasCensored && config.logCensoredBooks) {
            CensorIt.LOGGER.info("Censored book text (length: {}, censored words: {})", 
                bookText.length(), countCensoredWords(bookText, config));
        }

        return result;
    }

    public String censorBookTitle(String bookTitle) {
        BookConfig config = configManager.getBookConfig();
        
        if (config.bannedWords == null || config.bannedWords.isEmpty()) {
            return bookTitle;
        }

        String result = bookTitle;
        boolean titleWasCensored = false;

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
                titleWasCensored = true;
                
                if (config.preventBookSigning) {
                    // Return default book title instead of censoring
                    result = config.defaultBookTitle;
                    break; // No need to check other words if we're replacing entirely
                } else {
                    // Censor the banned word
                    String replacement = createCensoredWord(bannedWord, config);
                    result = matcher.replaceAll(replacement);
                }
            }
        }

        if (titleWasCensored && config.logCensoredBooks) {
            CensorIt.LOGGER.info("Censored book title (length: {}, censored words: {})", 
                bookTitle.length(), countCensoredWords(bookTitle, config));
        }

        return result;
    }

    private String createCensoredWord(String word, BookConfig config) {
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

    public boolean containsBannedWord(String text) {
        BookConfig config = configManager.getBookConfig();
        
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

            if (pattern.matcher(text).find()) {
                return true;
            }
        }

        return false;
    }
    
    private int countCensoredWords(String text, BookConfig config) {
        int count = 0;
        for (String bannedWord : config.bannedWords) {
            if (bannedWord == null || bannedWord.trim().isEmpty()) {
                continue;
            }
            String patternString = "\\b" + Pattern.quote(bannedWord.trim()) + "\\b";
            Pattern pattern = config.caseSensitive 
                ? Pattern.compile(patternString)
                : Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
            if (pattern.matcher(text).find()) {
                count++;
            }
        }
        return count;
    }
}