package fi.joniaromaa.parinacorelibrary.bungee;

import net.md_5.bungee.api.plugin.Plugin;

public class ParinaCoreBungeePluginLoader extends Plugin
{
	private ParinaCoreBungeePlugin plugin;
	
	public ParinaCoreBungeePluginLoader()
	{
		this.plugin = new ParinaCoreBungeePlugin(this);
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
