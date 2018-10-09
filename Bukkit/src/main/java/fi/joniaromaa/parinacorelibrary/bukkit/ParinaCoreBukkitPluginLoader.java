package fi.joniaromaa.parinacorelibrary.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

public class ParinaCoreBukkitPluginLoader extends JavaPlugin
{
	private ParinaCoreBukkitPlugin plugin;
	
	public ParinaCoreBukkitPluginLoader()
	{
		this.plugin = new ParinaCoreBukkitPlugin(this);
	}
	
	@Override
	public void onLoad()
	{
		this.plugin.load();
	}
	
	@Override
	public void onEnable()
	{
		this.plugin.enable();
	}
	
	@Override
	public void onDisable()
	{
		this.plugin.disable();
	}
}
