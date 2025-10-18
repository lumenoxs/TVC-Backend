package top.blackmare.bloodred.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import top.blackmare.bloodred.CensorIt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigManager {
    private static final String CONFIG_DIR = "config/CensorIt";
    private static final String CHAT_CONFIG_FILE = "chat.conf";
    private static final String ANVIL_CONFIG_FILE = "anvil.conf";
    private static final String SIGN_CONFIG_FILE = "sign.conf";
    private static final String BOOK_CONFIG_FILE = "book.conf";
    private static final Path CHAT_CONFIG_PATH = Paths.get(CONFIG_DIR, CHAT_CONFIG_FILE);
    private static final Path ANVIL_CONFIG_PATH = Paths.get(CONFIG_DIR, ANVIL_CONFIG_FILE);
    private static final Path SIGN_CONFIG_PATH = Paths.get(CONFIG_DIR, SIGN_CONFIG_FILE);
    private static final Path BOOK_CONFIG_PATH = Paths.get(CONFIG_DIR, BOOK_CONFIG_FILE);
    
    private ChatConfig config;
    private AnvilConfig anvilConfig;
    private SignConfig signConfig;
    private BookConfig bookConfig;

    public ConfigManager() {
        loadConfig();
        loadAnvilConfig();
        loadSignConfig();
        loadBookConfig();
    }

    public void loadConfig() {
        try {
            Files.createDirectories(Paths.get(CONFIG_DIR));
            File configFile = CHAT_CONFIG_PATH.toFile();
            
            if (!configFile.exists()) {
                CensorIt.LOGGER.info("Creating default chat config file at {}", CHAT_CONFIG_PATH);
                config = new ChatConfig();
                saveConfig();
            } else {
                try {
                    Config hoconConfig = ConfigFactory.parseFile(configFile);
                    config = loadChatConfigFromHocon(hoconConfig);
                    CensorIt.LOGGER.info("Loaded chat config with {} banned words", config.bannedWords.size());
                } catch (Exception e) {
                    CensorIt.LOGGER.error("Failed to load chat config, using defaults", e);
                    config = new ChatConfig();
                    saveConfig();
                }
            }
        } catch (IOException e) {
            CensorIt.LOGGER.error("Failed to create config directory, using default chat config", e);
            config = new ChatConfig();
        }
    }

    public void loadAnvilConfig() {
        try {
            Files.createDirectories(Paths.get(CONFIG_DIR));
            File configFile = ANVIL_CONFIG_PATH.toFile();
            
            if (!configFile.exists()) {
                CensorIt.LOGGER.info("Creating default anvil config file at {}", ANVIL_CONFIG_PATH);
                anvilConfig = new AnvilConfig();
                saveAnvilConfig();
            } else {
                try {
                    Config hoconConfig = ConfigFactory.parseFile(configFile);
                    anvilConfig = loadAnvilConfigFromHocon(hoconConfig);
                    CensorIt.LOGGER.info("Loaded anvil config with {} banned words", anvilConfig.bannedWords.size());
                } catch (Exception e) {
                    CensorIt.LOGGER.error("Failed to load anvil config, using defaults", e);
                    anvilConfig = new AnvilConfig();
                    saveAnvilConfig();
                }
            }
        } catch (IOException e) {
            CensorIt.LOGGER.error("Failed to create config directory, using default anvil config", e);
            anvilConfig = new AnvilConfig();
        }
    }

    public void loadSignConfig() {
        try {
            Files.createDirectories(Paths.get(CONFIG_DIR));
            File configFile = SIGN_CONFIG_PATH.toFile();
            
            if (!configFile.exists()) {
                CensorIt.LOGGER.info("Creating default sign config file at {}", SIGN_CONFIG_PATH);
                signConfig = new SignConfig();
                saveSignConfig();
            } else {
                try {
                    Config hoconConfig = ConfigFactory.parseFile(configFile);
                    signConfig = loadSignConfigFromHocon(hoconConfig);
                    CensorIt.LOGGER.info("Loaded sign config with {} banned words", signConfig.bannedWords.size());
                } catch (Exception e) {
                    CensorIt.LOGGER.error("Failed to load sign config, using defaults", e);
                    signConfig = new SignConfig();
                    saveSignConfig();
                }
            }
        } catch (IOException e) {
            CensorIt.LOGGER.error("Failed to create config directory, using default sign config", e);
            signConfig = new SignConfig();
        }
    }

    public void loadBookConfig() {
        try {
            Files.createDirectories(Paths.get(CONFIG_DIR));
            File configFile = BOOK_CONFIG_PATH.toFile();
            
            if (!configFile.exists()) {
                CensorIt.LOGGER.info("Creating default book config file at {}", BOOK_CONFIG_PATH);
                bookConfig = new BookConfig();
                saveBookConfig();
            } else {
                try {
                    Config hoconConfig = ConfigFactory.parseFile(configFile);
                    bookConfig = loadBookConfigFromHocon(hoconConfig);
                    CensorIt.LOGGER.info("Loaded book config with {} banned words", bookConfig.bannedWords.size());
                } catch (Exception e) {
                    CensorIt.LOGGER.error("Failed to load book config, using defaults", e);
                    bookConfig = new BookConfig();
                    saveBookConfig();
                }
            }
        } catch (IOException e) {
            CensorIt.LOGGER.error("Failed to create config directory, using default book config", e);
            bookConfig = new BookConfig();
        }
    }

    public void saveConfig() {
        try {
            Files.createDirectories(Paths.get(CONFIG_DIR));
            String hoconContent = createChatConfigHocon(config);
            
            try (FileWriter writer = new FileWriter(CHAT_CONFIG_PATH.toFile())) {
                writer.write(hoconContent);
                CensorIt.LOGGER.info("Saved chat config to {}", CHAT_CONFIG_PATH);
            }
        } catch (IOException e) {
            CensorIt.LOGGER.error("Failed to save chat config", e);
        }
    }

    public void saveAnvilConfig() {
        try {
            Files.createDirectories(Paths.get(CONFIG_DIR));
            String hoconContent = createAnvilConfigHocon(anvilConfig);
            
            try (FileWriter writer = new FileWriter(ANVIL_CONFIG_PATH.toFile())) {
                writer.write(hoconContent);
                CensorIt.LOGGER.info("Saved anvil config to {}", ANVIL_CONFIG_PATH);
            }
        } catch (IOException e) {
            CensorIt.LOGGER.error("Failed to save anvil config", e);
        }
    }

    public void saveSignConfig() {
        try {
            Files.createDirectories(Paths.get(CONFIG_DIR));
            String hoconContent = createSignConfigHocon(signConfig);
            
            try (FileWriter writer = new FileWriter(SIGN_CONFIG_PATH.toFile())) {
                writer.write(hoconContent);
                CensorIt.LOGGER.info("Saved sign config to {}", SIGN_CONFIG_PATH);
            }
        } catch (IOException e) {
            CensorIt.LOGGER.error("Failed to save sign config", e);
        }
    }

    public void saveBookConfig() {
        try {
            Files.createDirectories(Paths.get(CONFIG_DIR));
            String hoconContent = createBookConfigHocon(bookConfig);
            
            try (FileWriter writer = new FileWriter(BOOK_CONFIG_PATH.toFile())) {
                writer.write(hoconContent);
                CensorIt.LOGGER.info("Saved book config to {}", BOOK_CONFIG_PATH);
            }
        } catch (IOException e) {
            CensorIt.LOGGER.error("Failed to save book config", e);
        }
    }

    public ChatConfig getConfig() {
        return config;
    }

    public AnvilConfig getAnvilConfig() {
        return anvilConfig;
    }

    public SignConfig getSignConfig() {
        return signConfig;
    }
    
    private BookConfig loadBookConfigFromHocon(Config config) {
        BookConfig bookConfig = new BookConfig();
        
        if (config.hasPath("bannedWords")) {
            bookConfig.bannedWords = validateBannedWords(config.getStringList("bannedWords"));
        }
        if (config.hasPath("censorCharacter")) {
            bookConfig.censorCharacter = config.getString("censorCharacter");
        }
        if (config.hasPath("caseSensitive")) {
            bookConfig.caseSensitive = config.getBoolean("caseSensitive");
        }
        if (config.hasPath("logCensoredBooks")) {
            bookConfig.logCensoredBooks = config.getBoolean("logCensoredBooks");
        }
        if (config.hasPath("censorMode")) {
            String mode = config.getString("censorMode");
            bookConfig.censorMode = isValidCensorMode(mode) ? mode : "partial";
        }
        if (config.hasPath("preventBookEdit")) {
            bookConfig.preventBookEdit = config.getBoolean("preventBookEdit");
        }
        if (config.hasPath("preventBookSigning")) {
            bookConfig.preventBookSigning = config.getBoolean("preventBookSigning");
        }
        if (config.hasPath("defaultReplacementText")) {
            bookConfig.defaultReplacementText = config.getString("defaultReplacementText");
        }
        if (config.hasPath("defaultBookTitle")) {
            bookConfig.defaultBookTitle = config.getString("defaultBookTitle");
        }
        
        return bookConfig;
    }

    public BookConfig getBookConfig() {
        return bookConfig;
    }

    public void reloadConfig() {
        loadConfig();
        loadAnvilConfig();
        loadSignConfig();
        loadBookConfig();
    }
    
    private List<String> validateBannedWords(List<String> words) {
        return words.stream()
            .filter(word -> word != null && !word.trim().isEmpty())
            .map(String::trim)
            .filter(word -> word.length() <= 100) // Prevent extremely long words
            .distinct() // Remove duplicates
            .collect(java.util.stream.Collectors.toList());
    }
    
    private boolean isValidCensorMode(String mode) {
        return mode != null && 
               (mode.equalsIgnoreCase("full") || 
                mode.equalsIgnoreCase("partial") || 
                mode.equalsIgnoreCase("first_last"));
    }
    
    // HOCON loading methods
    private ChatConfig loadChatConfigFromHocon(Config config) {
        ChatConfig chatConfig = new ChatConfig();
        
        if (config.hasPath("bannedWords")) {
            chatConfig.bannedWords = validateBannedWords(config.getStringList("bannedWords"));
        }
        if (config.hasPath("censorCharacter")) {
            chatConfig.censorCharacter = config.getString("censorCharacter");
        }
        if (config.hasPath("caseSensitive")) {
            chatConfig.caseSensitive = config.getBoolean("caseSensitive");
        }
        if (config.hasPath("logCensoredMessages")) {
            chatConfig.logCensoredMessages = config.getBoolean("logCensoredMessages");
        }
        if (config.hasPath("censorMode")) {
            String mode = config.getString("censorMode");
            chatConfig.censorMode = isValidCensorMode(mode) ? mode : "partial";
        }
        
        return chatConfig;
    }
    
    private AnvilConfig loadAnvilConfigFromHocon(Config config) {
        AnvilConfig anvilConfig = new AnvilConfig();
        
        if (config.hasPath("bannedWords")) {
            anvilConfig.bannedWords = validateBannedWords(config.getStringList("bannedWords"));
        }
        if (config.hasPath("censorCharacter")) {
            anvilConfig.censorCharacter = config.getString("censorCharacter");
        }
        if (config.hasPath("caseSensitive")) {
            anvilConfig.caseSensitive = config.getBoolean("caseSensitive");
        }
        if (config.hasPath("logCensoredItems")) {
            anvilConfig.logCensoredItems = config.getBoolean("logCensoredItems");
        }
        if (config.hasPath("censorMode")) {
            String mode = config.getString("censorMode");
            anvilConfig.censorMode = isValidCensorMode(mode) ? mode : "partial";
        }
        if (config.hasPath("preventRename")) {
            anvilConfig.preventRename = config.getBoolean("preventRename");
        }
        if (config.hasPath("defaultReplacementName")) {
            anvilConfig.defaultReplacementName = config.getString("defaultReplacementName");
        }
        
        return anvilConfig;
    }
    
    private SignConfig loadSignConfigFromHocon(Config config) {
        SignConfig signConfig = new SignConfig();
        
        if (config.hasPath("bannedWords")) {
            signConfig.bannedWords = validateBannedWords(config.getStringList("bannedWords"));
        }
        if (config.hasPath("censorCharacter")) {
            signConfig.censorCharacter = config.getString("censorCharacter");
        }
        if (config.hasPath("caseSensitive")) {
            signConfig.caseSensitive = config.getBoolean("caseSensitive");
        }
        if (config.hasPath("logCensoredSigns")) {
            signConfig.logCensoredSigns = config.getBoolean("logCensoredSigns");
        }
        if (config.hasPath("censorMode")) {
            String mode = config.getString("censorMode");
            signConfig.censorMode = isValidCensorMode(mode) ? mode : "partial";
        }
        if (config.hasPath("preventSignEdit")) {
            signConfig.preventSignEdit = config.getBoolean("preventSignEdit");
        }
        if (config.hasPath("defaultReplacementText")) {
            signConfig.defaultReplacementText = config.getString("defaultReplacementText");
        }
        
        return signConfig;
    }
    
    // HOCON creation methods
    private String createChatConfigHocon(ChatConfig config) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Chat censoring configuration\n");
        sb.append("# censorMode options: 'full' (****), 'partial' (b**d), 'first_last' (b**d)\n\n");
        
        sb.append("bannedWords = [\n");
        for (int i = 0; i < config.bannedWords.size(); i++) {
            sb.append("  \"").append(config.bannedWords.get(i)).append("\"");
            if (i < config.bannedWords.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]\n\n");
        
        sb.append("# Character used for censoring\n");
        sb.append("censorCharacter = \"").append(config.censorCharacter).append("\"\n\n");
        
        sb.append("# Whether word matching is case-sensitive\n");
        sb.append("caseSensitive = ").append(config.caseSensitive).append("\n\n");
        
        sb.append("# Whether to log censored messages to server console\n");
        sb.append("logCensoredMessages = ").append(config.logCensoredMessages).append("\n\n");
        
        sb.append("# How to censor words: 'full', 'partial', or 'first_last'\n");
        sb.append("censorMode = \"").append(config.censorMode).append("\"\n");
        
        return sb.toString();
    }
    
    private String createAnvilConfigHocon(AnvilConfig config) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Anvil item renaming censoring configuration\n");
        sb.append("# Set preventRename=true to block renaming entirely when banned words detected\n\n");
        
        sb.append("bannedWords = [\n");
        for (int i = 0; i < config.bannedWords.size(); i++) {
            sb.append("  \"").append(config.bannedWords.get(i)).append("\"");
            if (i < config.bannedWords.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]\n\n");
        
        sb.append("# Character used for censoring\n");
        sb.append("censorCharacter = \"").append(config.censorCharacter).append("\"\n\n");
        
        sb.append("# Whether word matching is case-sensitive\n");
        sb.append("caseSensitive = ").append(config.caseSensitive).append("\n\n");
        
        sb.append("# Whether to log censored items to server console\n");
        sb.append("logCensoredItems = ").append(config.logCensoredItems).append("\n\n");
        
        sb.append("# How to censor words: 'full', 'partial', or 'first_last'\n");
        sb.append("censorMode = \"").append(config.censorMode).append("\"\n\n");
        
        sb.append("# If true, prevents renaming entirely when banned word is detected\n");
        sb.append("preventRename = ").append(config.preventRename).append("\n\n");
        
        sb.append("# Default name to use when preventing rename\n");
        sb.append("defaultReplacementName = \"").append(config.defaultReplacementName).append("\"\n");
        
        return sb.toString();
    }
    
    private String createSignConfigHocon(SignConfig config) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Sign and hanging sign censoring configuration\n");
        sb.append("# Set preventSignEdit=true to block sign editing entirely when banned words detected\n\n");
        
        sb.append("bannedWords = [\n");
        for (int i = 0; i < config.bannedWords.size(); i++) {
            sb.append("  \"").append(config.bannedWords.get(i)).append("\"");
            if (i < config.bannedWords.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]\n\n");
        
        sb.append("# Character used for censoring\n");
        sb.append("censorCharacter = \"").append(config.censorCharacter).append("\"\n\n");
        
        sb.append("# Whether word matching is case-sensitive\n");
        sb.append("caseSensitive = ").append(config.caseSensitive).append("\n\n");
        
        sb.append("# Whether to log censored signs to server console\n");
        sb.append("logCensoredSigns = ").append(config.logCensoredSigns).append("\n\n");
        
        sb.append("# How to censor words: 'full', 'partial', or 'first_last'\n");
        sb.append("censorMode = \"").append(config.censorMode).append("\"\n\n");
        
        sb.append("# If true, prevents sign editing entirely when banned word is detected\n");
        sb.append("preventSignEdit = ").append(config.preventSignEdit).append("\n\n");
        
        sb.append("# Default text to use when preventing sign edit\n");
        sb.append("defaultReplacementText = \"").append(config.defaultReplacementText).append("\"\n");
        
        return sb.toString();
    }
    
    private String createBookConfigHocon(BookConfig config) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Book and Book & Quill censoring configuration\n");
        sb.append("# Set preventBookEdit=true to block book editing entirely when banned words detected\n");
        sb.append("# Set preventBookSigning=true to block book signing when banned words detected\n\n");
        
        sb.append("bannedWords = [\n");
        for (int i = 0; i < config.bannedWords.size(); i++) {
            sb.append("  \"").append(config.bannedWords.get(i)).append("\"");
            if (i < config.bannedWords.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]\n\n");
        
        sb.append("# Character used for censoring\n");
        sb.append("censorCharacter = \"").append(config.censorCharacter).append("\"\n\n");
        
        sb.append("# Whether word matching is case-sensitive\n");
        sb.append("caseSensitive = ").append(config.caseSensitive).append("\n\n");
        
        sb.append("# Whether to log censored books to server console\n");
        sb.append("logCensoredBooks = ").append(config.logCensoredBooks).append("\n\n");
        
        sb.append("# How to censor words: 'full', 'partial', or 'first_last'\n");
        sb.append("censorMode = \"").append(config.censorMode).append("\"\n\n");
        
        sb.append("# If true, prevents book editing entirely when banned word is detected\n");
        sb.append("preventBookEdit = ").append(config.preventBookEdit).append("\n\n");
        
        sb.append("# If true, prevents book signing when banned words detected\n");
        sb.append("preventBookSigning = ").append(config.preventBookSigning).append("\n\n");
        
        sb.append("# Default text to use when preventing book edit\n");
        sb.append("defaultReplacementText = \"").append(config.defaultReplacementText).append("\"\n\n");
        
        sb.append("# Default title when preventing book signing\n");
        sb.append("defaultBookTitle = \"").append(config.defaultBookTitle).append("\"\n");
        
        return sb.toString();
    }
}