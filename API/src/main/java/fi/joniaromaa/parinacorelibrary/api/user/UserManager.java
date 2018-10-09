package fi.joniaromaa.parinacorelibrary.api.user;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface UserManager
{
	public CompletableFuture<User> loadUser(UUID uuid);
	public CompletableFuture<User> loadUser(UUID uuid, String username);
	
	public User getUser(UUID uniqueId);
}
