package fi.joniaromaa.parinacorelibrary.bukkit.scoreboard;

import java.util.function.Consumer;

import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import lombok.Getter;

public class ScoreboardDynamicScore
{
	@Getter private final ScoreboardViewer scoreboardViewer;
	@Getter private final Objective objective;
	@Getter private final Consumer<ScoreboardDynamicScore> updater;
	@Getter private int defaultScore;
	
	private Score lastScore;
	
	public ScoreboardDynamicScore(ScoreboardViewer viewer, Objective objective,  Consumer<ScoreboardDynamicScore> updater)
	{
		this(viewer, objective, updater, 0);
	}
	
	public ScoreboardDynamicScore(ScoreboardViewer viewer, Objective objective,  Consumer<ScoreboardDynamicScore> updater, int defaultScore)
	{
		this.scoreboardViewer = viewer;
		this.objective = objective;
		this.updater = updater;
		this.defaultScore = defaultScore;
	}
	
	public void set(String value)
	{
		this.set(value, this.defaultScore);
	}
	
	public void set(String value, int score)
	{
		if (this.lastScore != null)
		{
			if (!this.lastScore.getEntry().equals(value))
			{
				this.scoreboardViewer.getScoreboard().resetScores(this.lastScore.getEntry());
			}
			else
			{
				return; //No updates
			}
		}
		
		this.lastScore = this.objective.getScore(value);
		this.lastScore.setScore(score);
	}
	
	public void onTick()
	{
		this.updater.accept(this);
	}
}
