package fi.joniaromaa.parinacorelibrary.api.storage.module;

import fi.joniaromaa.parinacorelibrary.api.storage.Storage;

public abstract class StorageModuleAdapter<U extends StorageModule, T extends Storage>
{
	private T storage;
	
	public StorageModuleAdapter(T storage)
	{
		this.storage = storage;
	}
	
	protected T getStorage()
	{
		return this.storage;
	}
}
