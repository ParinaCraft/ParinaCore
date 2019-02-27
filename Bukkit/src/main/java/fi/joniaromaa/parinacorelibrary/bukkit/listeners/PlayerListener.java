package fi.joniaromaa.parinacorelibrary.bukkit.listeners;

import java.util.concurrent.TimeUnit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fi.joniaromaa.parinacorelibrary.api.user.User;
import fi.joniaromaa.parinacorelibrary.bukkit.ParinaCoreBukkitPlugin;

public class PlayerListener implements Listener
{
	private ParinaCoreBukkitPlugin plugin;
	
	public PlayerListener(ParinaCoreBukkitPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event)
	{
		AsyncPlayerPreLoginEvent.Result result = event.getLoginResult();
		if (result == AsyncPlayerPreLoginEvent.Result.ALLOWED)
		{
			try
			{
				User user = this.plugin.getUserManager().loadUser(event.getUniqueId(), event.getName()).get(10, TimeUnit.SECONDS).orElse(null);
				if (user == null)
				{
					event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Failed to load user data");
				}
			}
			catch (Throwable e)
			{
				e.printStackTrace();
				
				event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Exception while trying to load user data");
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerLoginEvent(PlayerLoginEvent event)
	{
		
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoinEvent(PlayerJoinEvent event)
	{
		event.setJoinMessage(null);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuitEvent(PlayerQuitEvent event)
	{
		event.setQuitMessage(null);
		
		this.plugin.getNickListener().onQuit(event.getPlayer());
	}
}
