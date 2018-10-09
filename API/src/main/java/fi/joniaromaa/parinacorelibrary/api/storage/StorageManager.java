package fi.joniaromaa.parinacorelibrary.api.storage;

import fi.joniaromaa.parinacorelibrary.api.storage.module.StorageModule;
import fi.joniaromaa.parinacorelibrary.api.storage.module.StorageModuleAdapter;

public interface StorageManager
{
	public <T extends StorageModule> void addStorageModule(Class<T> module, @SuppressWarnings("unchecked") Class<? extends StorageModuleAdapter<T, ?>> ...adapters);
	public <T extends StorageModule> T getStorageModule(Class<T> baseClass);
	
	public Storage getStorage();
	public StorageType getStorageType();
}
