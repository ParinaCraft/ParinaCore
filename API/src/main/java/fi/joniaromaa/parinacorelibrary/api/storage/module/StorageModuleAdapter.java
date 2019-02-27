package fi.joniaromaa.parinacorelibrary.api.storage.module;

import javax.annotation.Nonnull;

import fi.joniaromaa.parinacorelibrary.api.storage.Storage;

public abstract class StorageModuleAdapter<T extends StorageModule, U extends Storage>
{
	private U storage;
	
	public StorageModuleAdapter(@Nonnull U storage)
	{
		this.storage = storage;
	}
	
	protected @Nonnull U getStorage()
	{
		return this.storage;
	}
}
