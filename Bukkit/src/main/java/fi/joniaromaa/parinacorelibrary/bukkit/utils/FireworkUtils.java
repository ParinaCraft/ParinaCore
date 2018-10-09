package fi.joniaromaa.parinacorelibrary.bukkit.utils;

import org.bukkit.FireworkEffect;
import org.bukkit.Location;

import fi.joniaromaa.parinacorelibrary.bukkit.nms.NmsManager;

public class FireworkUtils
{
	public static void fireworkExplodeEffect(Location location, FireworkEffect ...effects)
	{
		NmsManager.getNmsHandler().getEntityHandler().fireworkExplodeEffect(location, effects);
	}
}
