package fi.joniaromaa.parinacorelibrary.api.user.dataset.minigames;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import fi.joniaromaa.parinacorelibrary.api.storage.dataset.StorageDataGetter;
import fi.joniaromaa.parinacorelibrary.api.storage.dataset.StorageDataRank;
import fi.joniaromaa.parinacorelibrary.api.storage.dataset.StorageDataSet;
import fi.joniaromaa.parinacorelibrary.api.user.dataset.UserDataStorage;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

@StorageDataSet(schema = "monument_wars", table = "user_stats")
public class UserMonumentWarsStatsDataStorage implements UserDataStorage
{
	@StorageDataGetter("total_exp")
	@Getter private int totalExp;
	
	@StorageDataGetter("kills")
	@Getter private int kills;
	@StorageDataGetter("final_kills")
	@Getter private int finalKills;
	
	@StorageDataGetter("deaths")
	@Getter private int deaths;
	@StorageDataGetter("final_deaths")
	@Getter private int finalDeaths;
	
	@StorageDataGetter("plays")
	@Getter private int plays;
	@StorageDataGetter("wins")
	@Getter private int wins;
	
	@StorageDataGetter("monuments_destroyed")
	@Getter private int monumentsDestroyed;
	
	@StorageDataRank("final_kills")
	@Getter private int finalKillsRank;
	
	@StorageDataRank("wins")
	@Getter private int winsRank;
	
	@StorageDataRank("total_exp")
	@Getter private int totalExpRank;
	
	public int getLevel()
	{
		int level = 1;
		int expLeft = this.getTotalExp();
		
		int nextLevel = this.getNextLevelExpRequirement(level);
		while (expLeft >= nextLevel)
		{
			expLeft -= nextLevel;
			level++;
		}
		
		return level;
	}
	
	public int getNextLevelExpRequirement(int currentLevel)
	{
		return (int)Math.max(1, Math.ceil(currentLevel / 5D)) * 500;
	}
	
	public @Nonnull String getPrefix()
	{
		Map<String, Integer> ranks = new HashMap<String, Integer>(3);
		ranks.put("Wins", this.getWinsRank());
		ranks.put("Final Kills", this.getFinalKillsRank());
		ranks.put("✮", this.getTotalExpRank());
		
		Entry<String, Integer> ranking = ranks.entrySet().stream().min((o1, o2) -> Integer.compare(o1.getValue(), o2.getValue())).get();
		
		StringBuilder stringBuilder = new StringBuilder();
		if (ranking.getValue() <= 10)
		{
			stringBuilder.append(ChatColor.GOLD)
				.append("[#")
				.append(ranking.getValue())
				.append(' ')
				.append(ranking.getKey())
				.append(']');
		}
		else
		{
			stringBuilder.append(ChatColor.GREEN)
				.append("[✮")
				.append(this.getLevel())
				.append("]");
		}
		
		return stringBuilder.toString();
	}
}
