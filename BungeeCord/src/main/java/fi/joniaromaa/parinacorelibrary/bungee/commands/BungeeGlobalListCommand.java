package fi.joniaromaa.parinacorelibrary.bungee.commands;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import codecrafter47.bungeetablistplus.BungeeTabListPlus;
import fi.joniaromaa.parinacorelibrary.api.user.User;
import fi.joniaromaa.parinacorelibrary.bungee.ParinaCoreBungeePlugin;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BungeeGlobalListCommand extends Command
{
	private ParinaCoreBungeePlugin plugin;
	
	public BungeeGlobalListCommand(ParinaCoreBungeePlugin plugin)
	{
		super("globallist", "parina.command.globallist.use", "glist", "gl");
		
		this.plugin = plugin;
	}

	@Override
	public void execute(CommandSender sender, String[] args)
	{
		ProxyServer.getInstance().getServers().values().stream().sorted(Comparator.comparing(ServerInfo::getName)).forEach((serverInfo) ->
        {
			if (serverInfo.canAccess(sender))
			{
				List<User> players = new ArrayList<>();
				for(ProxiedPlayer player : serverInfo.getPlayers())
				{
					if (!BungeeTabListPlus.isHidden(BungeeTabListPlus.getInstance().getConnectedPlayerManager().getPlayer(player)) || sender.hasPermission("bungeetablistplus.seevanished"))
					{
						User user = this.plugin.getApi().getUserManager().getUser(player.getUniqueId());
						if (user != null)
						{
							players.add(user);
						}
					}
				}
				
				//Skip empty servers
				if (players.isEmpty())
				{
					return;
				}
				
				ComponentBuilder componentBuilder = new ComponentBuilder(serverInfo.getName() + " (" + players.size() + "): ").color(ChatColor.AQUA);
				
				Iterator<User> users = players.stream().sorted(Comparator.comparing(User::getWeight).reversed().thenComparing(User::getDisplayName)).iterator();
				while (users.hasNext())
				{
					componentBuilder.append(TextComponent.fromLegacyText(users.next().getFormattedDisplayName()));
					
					if (users.hasNext())
					{
						componentBuilder.append(", ");
						componentBuilder.color(ChatColor.GRAY);
					}
				}
				
				sender.sendMessage(componentBuilder.create());
			}
        });
	}
}
