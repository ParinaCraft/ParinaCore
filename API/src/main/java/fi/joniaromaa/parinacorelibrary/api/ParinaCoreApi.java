package fi.joniaromaa.parinacorelibrary.api;

import fi.joniaromaa.parinacorelibrary.api.config.ParinaCoreConfig;
import fi.joniaromaa.parinacorelibrary.api.language.LanguageManager;
import fi.joniaromaa.parinacorelibrary.api.nick.NickManager;
import fi.joniaromaa.parinacorelibrary.api.storage.StorageManager;
import fi.joniaromaa.parinacorelibrary.api.user.UserManager;

public interface ParinaCoreApi
{
	public ParinaCoreConfig getConfig();
	
	public StorageManager getStorageManager();
	public UserManager getUserManager();
	
	public NickManager getNickManager();
	
	public LanguageManager getLanguageManager();
}
