package fi.joniaromaa.parinacorelibrary.common.user;

import java.time.Duration;
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
	
	private AsyncLoadingCache<UUID, User> cachedUsers;
	
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
	public CompletableFuture<User> loadUser(UUID uniqueId)
	{
		return this.loadUser(uniqueId, (String)null);
	}

	@Override
	public CompletableFuture<User> loadUser(UUID uniqueId, String username)
	{
		CompletableFuture<User> user = this.plugin.getApi().getStorageManager().getStorageModule(UserStorageModule.class).loadUser(uniqueId, username);
		
		this.cachedUsers.put(uniqueId, user);
		
		return user;
	}

	private CompletableFuture<User> loadUser(UUID uniqueId, Executor executor)
	{
		CompletableFuture<User> user = this.plugin.getApi().getStorageManager().getStorageModule(UserStorageModule.class).loadUser(uniqueId, executor);
		
		this.cachedUsers.put(uniqueId, user);
		
		return user;
	}
	
	@Override
	public User getUser(UUID id)
	{
		CompletableFuture<User> user = this.cachedUsers.getIfPresent(id);
		if (user != null)
		{
			try
			{
				return user.getNow(null);
			}
			catch(CompletionException | CancellationException  e)
			{
			}
		}
		
		return null;
	}
}
