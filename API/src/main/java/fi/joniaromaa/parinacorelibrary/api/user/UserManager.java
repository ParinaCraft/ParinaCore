package fi.joniaromaa.parinacorelibrary.api.user;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface UserManager
{
	public CompletableFuture<Optional<User>> loadUser(@Nonnull UUID uuid);
	public CompletableFuture<Optional<User>> loadUser(@Nonnull UUID uuid, @Nullable String username);
	
	public Optional<User> getUser(@Nonnull UUID uniqueId);
}
