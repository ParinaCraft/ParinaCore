package fi.joniaromaa.parinacorelibrary.bukkit.nick;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;

public class NickEntry
{
	@Getter private final UUID uniqueId;
	
	@Getter private List<Integer> deadmau5NameTagEntites;
	
	public NickEntry(UUID uniqueId)
	{
		this.uniqueId = uniqueId;
		
		this.deadmau5NameTagEntites = new ArrayList<>();
	}
}
