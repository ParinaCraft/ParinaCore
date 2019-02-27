package fi.joniaromaa.parinacorelibrary.common.storage.modules;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import fi.joniaromaa.parinacorelibrary.api.storage.module.StorageModule;
import fi.joniaromaa.parinacorelibrary.api.user.User;
import fi.joniaromaa.parinacorelibrary.api.user.dataset.UserDataStorage;

public interface UserStorageModule extends StorageModule
{
	<T extends UserDataStorage> void addUserDataStorageSet(@Nonnull Class<T> storageDataSet);
	
	CompletableFuture<Optional<User>> loadUser(@Nonnull UUID uniqueId);
	CompletableFuture<Optional<User>> loadUser(@Nonnull UUID uniqueId, @Nonnull String username);
	CompletableFuture<Optional<User>> loadUser(@Nonnull UUID uniqueId, @Nullable Executor executor);
	CompletableFuture<Optional<User>> loadUser(@Nonnull UUID uniqueId, @Nullable String username, @Nonnull Executor executor);
	
	CompletableFuture<Boolean> updateUserDataStorage(@Nonnull UUID uniqueId, UserDataStorage storageData);
	CompletableFuture<Boolean> updateUserDataStorageMultiple(@Nonnull Map<UUID, UserDataStorage> storageDataMap);
}
