package fi.joniaromaa.parinacorelibrary.bukkit.scoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ScoreboardManager
{
	private final Plugin plugin;
	private Consumer<ScoreboardViewer> scoreboardSetup;
	
	private Map<UUID, ScoreboardViewer> scoreboards;
	
	public ScoreboardManager(Plugin plugin, Consumer<ScoreboardViewer> scoreboardSetup)
	{
		this.plugin = plugin;
		this.scoreboardSetup = scoreboardSetup;
		
		this.scoreboards = new HashMap<>();
	}
	
	public void addPlayer(Player player)
	{
		ScoreboardViewer viewer = new ScoreboardViewer(this.plugin, player);
		viewer.init(this.scoreboardSetup);
		
		this.scoreboards.put(player.getUniqueId(), viewer);
		
		player.setScoreboard(viewer.getScoreboard());
	}
	
	public void onTick()
	{
		for(ScoreboardViewer viewer : this.scoreboards.values())
		{
			viewer.onTick();
		}
	}
	
	public void clear()
	{
		this.scoreboards.clear();
	}
	
	public void removePlayer(Player player)
	{
		this.removePlayer(player.getUniqueId());
	}
	
	public void removePlayer(UUID uuid)
	{
		this.scoreboards.remove(uuid);
	}
	
	public ScoreboardViewer getPlayer(Player player)
	{
		return this.getPlayer(player.getUniqueId());
	}
	
	public ScoreboardViewer getPlayer(UUID uuid)
	{
		return this.scoreboards.get(uuid);
	}
}
