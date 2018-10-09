package fi.joniaromaa.parinacorelibrary.bukkit.nms.handlers;

import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

public interface NmsHandlerEntity
{
	public void fixEntityCount();
	public void fireworkExplodeEffect(Location location, FireworkEffect ...effects);
	public ArmorStand createFakeHologram(Location location);
	public void setEntityDataWatcher(Entity entity, int id, Object value);
}
