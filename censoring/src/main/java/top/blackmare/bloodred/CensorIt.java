package top.blackmare.bloodred;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.blackmare.bloodred.config.ConfigManager;

public class CensorIt implements ModInitializer {
	public static final String MOD_ID = "censorit";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	
	private static ConfigManager configManager;
	private static ChatCensor chatCensor;
	private static AnvilCensor anvilCensor;
	private static SignCensor signCensor;
	private static BookCensor bookCensor;

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing CensorIt mod...");
		
		// Initialize config and censors
		configManager = new ConfigManager();
		chatCensor = new ChatCensor(configManager);
		anvilCensor = new AnvilCensor(configManager);
		signCensor = new SignCensor(configManager);
		bookCensor = new BookCensor(configManager);
		
		LOGGER.info("CensorIt mod initialized successfully!");
		LOGGER.info("Loaded {} banned words from chat config", configManager.getConfig().bannedWords.size());
		LOGGER.info("Loaded {} banned words from anvil config", configManager.getAnvilConfig().bannedWords.size());
		LOGGER.info("Loaded {} banned words from sign config", configManager.getSignConfig().bannedWords.size());
		LOGGER.info("Loaded {} banned words from book config", configManager.getBookConfig().bannedWords.size());
	}
	
	public static ConfigManager getConfigManager() {
		return configManager;
	}
	
	public static ChatCensor getChatCensor() {
		return chatCensor;
	}
	
	public static AnvilCensor getAnvilCensor() {
		return anvilCensor;
	}
	
	public static SignCensor getSignCensor() {
		return signCensor;
	}
	
	public static BookCensor getBookCensor() {
		return bookCensor;
	}
}