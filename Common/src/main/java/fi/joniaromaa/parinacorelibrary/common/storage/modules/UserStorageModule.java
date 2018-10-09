package fi.joniaromaa.parinacorelibrary.common.storage.modules;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import fi.joniaromaa.parinacorelibrary.api.storage.module.StorageModule;
import fi.joniaromaa.parinacorelibrary.api.user.User;
import fi.joniaromaa.parinacorelibrary.api.user.dataset.UserDataStorage;

public interface UserStorageModule extends StorageModule
{
	<T extends UserDataStorage> void addUserDataStorageSet(Class<T> storageDataSet);
	
	CompletableFuture<User> loadUser(UUID uniqueId);
	CompletableFuture<User> loadUser(UUID uniqueId, String username);
	CompletableFuture<User> loadUser(UUID uniqueId, Executor executor);
	CompletableFuture<User> loadUser(UUID uniqueId, String username, Executor executor);
	
	CompletableFuture<Boolean> updateUserDataStorage(UUID uniqueId, UserDataStorage storageData);
	CompletableFuture<Boolean> updateUserDataStorageMultiple(Map<UUID, UserDataStorage> storageDataMap);
}
