package fi.joniaromaa.parinacorelibrary.bukkit.nick;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import fi.joniaromaa.parinacorelibrary.api.nick.NickManager;
import fi.joniaromaa.parinacorelibrary.bukkit.ParinaCoreBukkitPlugin;

public class SimpleNickManager implements NickManager
{
	private final ParinaCoreBukkitPlugin plugin;
	
	private Map<UUID, Boolean> deadmau5Ears;
	
	public SimpleNickManager(ParinaCoreBukkitPlugin plugin)
	{
		this.plugin = plugin;
		
		this.deadmau5Ears = new HashMap<>();
		this.deadmau5Ears.put(UUID.fromString("8c0d9cc3-c4fc-4b2b-b945-3726f6a6b3fb"), true);
	}
	
	@Override
	public void nick(UUID uniqueId, String username)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void unnick(UUID uniqueId)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void setDeadmau5Ears(UUID uniqueId, boolean ears)
	{
		this.deadmau5Ears.put(uniqueId, ears);
	}

	@Override
	public boolean hasDeadmau5Ears(UUID uniqueId)
	{
		return this.deadmau5Ears.getOrDefault(uniqueId, false);
	}
}
