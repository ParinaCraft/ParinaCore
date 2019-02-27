package fi.joniaromaa.parinacorelibrary.api.storage;

import java.util.Optional;

import javax.annotation.Nonnull;

import fi.joniaromaa.parinacorelibrary.api.storage.module.StorageModule;
import fi.joniaromaa.parinacorelibrary.api.storage.module.StorageModuleAdapter;

public interface StorageManager
{
	public <T extends StorageModule> Optional<T> addStorageModule(@Nonnull Class<T> module, @SuppressWarnings("unchecked") @Nonnull Class<? extends StorageModuleAdapter<T, ?>> ...adapters);
	public <T extends StorageModule> Optional<T> getStorageModule(@Nonnull Class<T> baseClass);
	
	public @Nonnull Storage getStorage();
	public @Nonnull StorageType getStorageType();
}
