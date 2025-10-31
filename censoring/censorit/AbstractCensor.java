package net.tvc.backend.censorit;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public abstract class AbstractCensor<T> {
    private final Map<String, Pattern> patternCache = new ConcurrentHashMap<>();
    
    protected abstract T getConfig();
    protected abstract List<String> getBannedWords();
    protected abstract String getCensorCharacter();
    protected abstract boolean isCaseSensitive();
    protected abstract String getCensorMode();
    protected abstract boolean shouldLog();
    protected abstract void logCensoring(String original, String censored);

    public String censorText(String text) {
        List<String> bannedWords = getBannedWords();
        
        if (bannedWords == null || bannedWords.isEmpty() || text == null || text.trim().isEmpty()) {
            return text;
        }

        String result = text;
        boolean wasCensored = false;

        for (String bannedWord : bannedWords) {
            if (!isValidBannedWord(bannedWord)) {
                continue;
            }

            Pattern pattern = getOrCreatePattern(bannedWord.trim());
            Matcher matcher = pattern.matcher(result);
            
            if (matcher.find()) {
                wasCensored = true;
                String replacement = createCensoredWord(bannedWord, getCensorCharacter(), getCensorMode());
                result = matcher.replaceAll(replacement);
            }
        }

        if (wasCensored && shouldLog()) {
            logCensoring(text, result);
        }

        return result;
    }

    public boolean containsBannedWord(String text) {
        List<String> bannedWords = getBannedWords();
        
        if (bannedWords == null || bannedWords.isEmpty() || text == null || text.trim().isEmpty()) {
            return false;
        }

        for (String bannedWord : bannedWords) {
            if (!isValidBannedWord(bannedWord)) {
                continue;
            }

            Pattern pattern = getOrCreatePattern(bannedWord.trim());
            if (pattern.matcher(text).find()) {
                return true;
            }
        }

        return false;
    }

    private Pattern getOrCreatePattern(String bannedWord) {
        String cacheKey = bannedWord + "|" + isCaseSensitive();
        return patternCache.computeIfAbsent(cacheKey, key -> {
            String patternString = "\\b" + Pattern.quote(bannedWord) + "\\b";
            return isCaseSensitive() 
                ? Pattern.compile(patternString)
                : Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
        });
    }

    private boolean isValidBannedWord(String word) {
        return word != null && 
               !word.trim().isEmpty() && 
               word.trim().length() <= 100; // Prevent extremely long words
    }

    private String createCensoredWord(String word, String censorChar, String censorMode) {
        switch (censorMode.toLowerCase()) {
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

    public void clearPatternCache() {
        patternCache.clear();
    }
}