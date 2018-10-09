package fi.joniaromaa.parinacorelibrary.bungee.listeners;

import fi.joniaromaa.parinacorelibrary.bungee.ParinaCoreBungeePlugin;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class BungeePlayerListener implements Listener
{
	private ParinaCoreBungeePlugin plugin;
	
	public BungeePlayerListener(ParinaCoreBungeePlugin plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onLoginEvent(LoginEvent event)
	{
		PendingConnection pendingConnection = event.getConnection();
		
		event.registerIntent(this.plugin.getLoader());
		
		this.plugin.getApi().getUserManager().loadUser(pendingConnection.getUniqueId(), pendingConnection.getName()).thenAccept((u) ->
		{
			event.completeIntent(this.plugin.getLoader());
		});
	}
	
	//Do something shit
   /* @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDisconnectEvent(PlayerDisconnectEvent event)
    {
    	ProxiedPlayer player = event.getPlayer();
    	
    	this.plugin.getApi().getUserManager().removeUser(player.getUniqueId());
    }*/
}
