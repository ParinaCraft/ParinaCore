package fi.joniaromaa.parinacorelibrary.bukkit.utils;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.material.MaterialData;

public class ParticleUtils
{
	@SuppressWarnings("deprecation")
	public static void tileBreakParticle(World world, Location location, MaterialData meterialData)
	{
		world.spigot().playEffect(location, Effect.TILE_BREAK, meterialData.getItemTypeId(), meterialData.getData(), 0, 0, 0, 1, 1, 64);
	}
}
