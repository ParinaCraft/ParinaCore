package fi.joniaromaa.parinacorelibrary.common.user;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;

import fi.joniaromaa.parinacorelibrary.api.ParinaCorePlugin;
import fi.joniaromaa.parinacorelibrary.api.user.User;
import fi.joniaromaa.parinacorelibrary.api.user.UserManager;
import fi.joniaromaa.parinacorelibrary.common.storage.modules.UserStorageModule;

public class SimpleUserManager implements UserManager
{
	private ParinaCorePlugin plugin;
	
	private AsyncLoadingCache<UUID, Optional<User>> cachedUsers;
	
	public SimpleUserManager(ParinaCorePlugin plugin)
	{
		this.plugin = plugin;

		this.cachedUsers = Caffeine.newBuilder()
				.maximumSize(1000)
				.expireAfterAccess(Duration.ofMinutes(60))
				.refreshAfterWrite(Duration.ofMinutes(15))
				.buildAsync((k, e) ->
				{
					return this.loadUser(k, e);
				});
	}
	
	@Override
	public CompletableFuture<Optional<User>> loadUser(UUID uniqueId)
	{
		return this.loadUser(uniqueId, (String)null);
	}

	@Override
	public CompletableFuture<Optional<User>> loadUser(UUID uniqueId, String username)
	{
		UserStorageModule userStorageModule = this.plugin.getApi().getStorageManager().getStorageModule(UserStorageModule.class).orElseThrow(() -> new RuntimeException("User storage moduel is missing"));
		
		CompletableFuture<Optional<User>> user = userStorageModule.loadUser(uniqueId, username);
		
		this.cachedUsers.put(uniqueId, user);
		
		return user;
	}

	private CompletableFuture<Optional<User>> loadUser(UUID uniqueId, Executor executor)
	{
		UserStorageModule userStorageModule = this.plugin.getApi().getStorageManager().getStorageModule(UserStorageModule.class).orElseThrow(() -> new RuntimeException("User storage moduel is missing"));
		
		CompletableFuture<Optional<User>> user = userStorageModule.loadUser(uniqueId, executor);
		
		this.cachedUsers.put(uniqueId, user);
		
		return user;
	}
	
	@Override
	public Optional<User> getUser(UUID id)
	{
		CompletableFuture<Optional<User>> user = this.cachedUsers.getIfPresent(id);
		if (user != null)
		{
			try
			{
				return user.getNow(Optional.empty());
			}
			catch(CompletionException | CancellationException e)
			{
			}
		}
		
		return Optional.empty();
	}
}
