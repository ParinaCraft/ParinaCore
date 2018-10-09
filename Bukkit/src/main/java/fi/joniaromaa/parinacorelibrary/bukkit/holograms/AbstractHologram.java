package fi.joniaromaa.parinacorelibrary.bukkit.holograms;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import fi.joniaromaa.parinacorelibrary.bukkit.nms.NmsManager;

public class AbstractHologram
{
	private ArmorStand armorStand;
	
	public AbstractHologram(Location location)
	{
		this.armorStand = NmsManager.getNmsHandler().getEntityHandler().createFakeHologram(location);
	}
	
	public void setText(String text)
	{
		this.armorStand.setCustomNameVisible(text != null && text.length() > 1);
		this.armorStand.setCustomName(text != null ? text : "");
	}
	
	public int getEntityId()
	{
		return this.armorStand.getEntityId();
	}
}
