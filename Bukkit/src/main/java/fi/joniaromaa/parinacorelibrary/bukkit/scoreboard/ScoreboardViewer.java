package fi.joniaromaa.parinacorelibrary.bukkit.scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;

import lombok.Getter;

public class ScoreboardViewer
{
	private final Plugin plugin;
	@Getter private final Player player;
	
	@Getter private Scoreboard scoreboard;
	
	private List<ScoreboardDynamicScore> scoreboardDynamicScores;
	
	public ScoreboardViewer(Plugin plugin, Player player)
	{
		this.plugin = plugin;
		this.player = player;
		
		this.scoreboardDynamicScores = new ArrayList<>();
	}
	
	public void init(Consumer<ScoreboardViewer> scoreboardSetup)
	{
		this.scoreboard = this.plugin.getServer().getScoreboardManager().getNewScoreboard();
		
		if (scoreboardSetup != null)
		{
			scoreboardSetup.accept(this);
		}
	}
	
	public void addDynamicScore(ScoreboardDynamicScore dynamicScore)
	{
		this.scoreboardDynamicScores.add(dynamicScore);
		
		dynamicScore.onTick(); //Update it instantly
	}
	
	public void onTick()
	{
		for(ScoreboardDynamicScore dynamicScore : this.scoreboardDynamicScores)
		{
			dynamicScore.onTick();
		}
	}
}
