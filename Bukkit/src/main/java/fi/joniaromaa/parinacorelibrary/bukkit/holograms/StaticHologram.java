package fi.joniaromaa.parinacorelibrary.bukkit.holograms;

import org.bukkit.Location;

public class StaticHologram extends AbstractHologram
{
	public StaticHologram(String text, Location location)
	{
		super(location);
		
		this.setText(text);
	}
}
