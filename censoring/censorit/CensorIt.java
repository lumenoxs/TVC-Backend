package net.tvc.backend.censorit;

import net.tvc.backend.censorit.config.ConfigManager;

import org.slf4j.Logger;

public class CensorIt {
	public static Logger LOGGER;
	
	private static ConfigManager configManager;
	private static ChatCensor chatCensor;
	private static AnvilCensor anvilCensor;
	private static SignCensor signCensor;
	private static BookCensor bookCensor;

	public void init(Logger LOGGER) {
		CensorIt.LOGGER = LOGGER;

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