package fi.joniaromaa.parinacorelibrary.bukkit.holograms;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import fi.joniaromaa.parinacorelibrary.bukkit.holograms.dynamic.DynamicHologram;

public class HologramManager
{
	private List<StaticHologram> holograms;
	private List<DynamicHologram> dynamicHolograms;
	
	public HologramManager()
	{
		this.holograms = new ArrayList<>();
		this.dynamicHolograms = new ArrayList<>();
	}
	
	public void addStaticHologram(String text, Location location)
	{
		this.holograms.add(new StaticHologram(text, location));
	}
	
	public void addTickableHologram(DynamicHologram tickableHologram)
	{
		this.dynamicHolograms.add(tickableHologram);
		
		tickableHologram.tick(); //Tick instantly
	}
	
	public void tick()
	{
		for(DynamicHologram tickableHologram : this.dynamicHolograms)
		{
			tickableHologram.tick();
		}
	}
}
