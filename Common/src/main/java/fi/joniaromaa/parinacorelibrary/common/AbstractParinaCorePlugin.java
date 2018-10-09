package fi.joniaromaa.parinacorelibrary.common;

import fi.joniaromaa.parinacorelibrary.api.ParinaCore;
import fi.joniaromaa.parinacorelibrary.api.ParinaCoreApi;
import fi.joniaromaa.parinacorelibrary.api.ParinaCorePlugin;
import fi.joniaromaa.parinacorelibrary.api.config.ParinaCoreConfig;
import fi.joniaromaa.parinacorelibrary.api.language.LanguageManager;
import fi.joniaromaa.parinacorelibrary.api.storage.StorageManager;
import fi.joniaromaa.parinacorelibrary.api.user.dataset.minigames.UserMonumentWarsStatsDataStorage;
import fi.joniaromaa.parinacorelibrary.common.language.SimpleLanguageManager;
import fi.joniaromaa.parinacorelibrary.common.storage.SimpleStorageManager;
import fi.joniaromaa.parinacorelibrary.common.storage.modules.UserStorageModule;
import fi.joniaromaa.parinacorelibrary.common.storage.modules.adapters.postgres.UserStorageModulePostgresAdapter;
import fi.joniaromaa.parinacorelibrary.common.storage.types.PostgreSqlStorage;
import fi.joniaromaa.parinacorelibrary.common.user.SimpleUserManager;
import lombok.Getter;

public abstract class AbstractParinaCorePlugin implements ParinaCorePlugin, ParinaCoreApi //We are the api itself, for now
{
	@Getter private ParinaCoreConfig config;
	
	@Getter private StorageManager storageManager;
	@Getter private SimpleUserManager userManager;
	
	@Getter private LanguageManager languageManager;
	
	public void load()
	{
		this.config = this.loadConfig();
	}
	
	public void enable()
	{
		this.storageManager = new SimpleStorageManager(new PostgreSqlStorage(this.config));
		this.userManager = new SimpleUserManager(this);
		
		this.languageManager = new SimpleLanguageManager();
		
		this.addDefaultStorageModules();
		
		this.setupPlatform();
		
		ParinaCore.setApi(this);
	}
	
	@SuppressWarnings("unchecked")
	private void addDefaultStorageModules()
	{
		//Add modules
		this.storageManager.addStorageModule(UserStorageModule.class, UserStorageModulePostgresAdapter.class);
		
		//Add stuff to the modules
		UserStorageModule userStorageModule = this.storageManager.getStorageModule(UserStorageModule.class);
		userStorageModule.addUserDataStorageSet(UserMonumentWarsStatsDataStorage.class);
	}
	
	public void disable()
	{
		
	}
	
	public ParinaCoreApi getApi()
	{
		return this;
	}
	
	public abstract ParinaCoreConfig loadConfig();
	public abstract void setupPlatform();
}
