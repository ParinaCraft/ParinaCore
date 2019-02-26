package fi.joniaromaa.parinacorelibrary.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.permissions.PermissionDefault;

import com.comphenix.protocol.ProtocolLibrary;

import fi.joniaromaa.parinacorelibrary.api.config.ParinaCoreConfig;
import fi.joniaromaa.parinacorelibrary.api.nick.NickManager;
import fi.joniaromaa.parinacorelibrary.bukkit.config.BukkitParinaCoreConfig;
import fi.joniaromaa.parinacorelibrary.bukkit.data.BorderVector;
import fi.joniaromaa.parinacorelibrary.bukkit.data.WordlessLocation;
import fi.joniaromaa.parinacorelibrary.bukkit.listeners.CitizensListener;
import fi.joniaromaa.parinacorelibrary.bukkit.listeners.InventoryListener;
import fi.joniaromaa.parinacorelibrary.bukkit.listeners.PlayerListener;
import fi.joniaromaa.parinacorelibrary.bukkit.listeners.protocollib.CustomPayloadExploitListener;
import fi.joniaromaa.parinacorelibrary.bukkit.listeners.protocollib.ServerPacketListenerNick;
import fi.joniaromaa.parinacorelibrary.bukkit.nick.SimpleNickManager;
import fi.joniaromaa.parinacorelibrary.bukkit.nms.NmsManager;
import fi.joniaromaa.parinacorelibrary.common.AbstractParinaCorePlugin;
import lombok.Getter;

public class ParinaCoreBukkitPlugin extends AbstractParinaCorePlugin
{
	@Getter private ParinaCoreBukkitPluginLoader loader;
	
	private SimpleNickManager nickManager;
	
	@Getter private ServerPacketListenerNick nickListener;
	
	public ParinaCoreBukkitPlugin(ParinaCoreBukkitPluginLoader loader)
	{
		this.loader = loader;
	}

	@Override
	public ParinaCoreConfig loadConfig()
	{
		this.loader.saveDefaultConfig();
		
		BukkitParinaCoreConfig config = new BukkitParinaCoreConfig(this.loader.getConfig());
		if (config.getServerId() <= 0)
		{
			throw new RuntimeException("Server id must be positive!");
		}
		
		return config;
	}
	
	@Override
	public void load()
	{
		super.load();

		NmsManager.getNmsHandler().getEntityHandler().fixEntityCount();
		NmsManager.getNmsHandler().getServerHandler().doHackyConfig();
		NmsManager.getNmsHandler().getServerHandler().disableBukkitCommands();
	}
	
	@Override
	public void setupPlatform()
	{
		try
		{
			if (this.loader.getServer().getPluginManager().getPlugin("Citizens") != null)
			{
				this.loader.getServer().getPluginManager().registerEvents(new CitizensListener(), this.loader);
			}
			
			this.nickListener = new ServerPacketListenerNick(this);
			
			ProtocolLibrary.getProtocolManager().addPacketListener(this.nickListener);
			ProtocolLibrary.getProtocolManager().addPacketListener(new CustomPayloadExploitListener(this));

			this.loader.getServer().getPluginManager().registerEvents(new PlayerListener(this), this.loader);
			this.loader.getServer().getPluginManager().registerEvents(new InventoryListener(), this.loader);
			
			NmsManager.getNmsHandler().getServerHandler().disableBukkitCommands();
			
			this.loader.getServer().getScheduler().runTask(this.loader, () -> 
			{
				NmsManager.getNmsHandler().getServerHandler().disableBukkitCommands();

				Bukkit.getServer().getPluginManager().getPermission("minecraft.command.me").setDefault(PermissionDefault.OP);
				Bukkit.getServer().getPluginManager().getPermission("minecraft.command.tell").setDefault(PermissionDefault.OP);
				Bukkit.getServer().getPluginManager().getPermission("minecraft.command").recalculatePermissibles();
				
				Bukkit.getServer().getPluginManager().getPermission("bukkit.command.help").setDefault(PermissionDefault.OP);
				Bukkit.getServer().getPluginManager().getPermission("bukkit.command.plugins").setDefault(PermissionDefault.OP);
				Bukkit.getServer().getPluginManager().getPermission("bukkit.command.version").setDefault(PermissionDefault.OP);
				Bukkit.getServer().getPluginManager().getPermission("bukkit.command").recalculatePermissibles();
			});
			
			ConfigurationSerialization.registerClass(WordlessLocation.class);
			ConfigurationSerialization.registerClass(BorderVector.class);
			
			this.nickManager = new SimpleNickManager(this);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			
			this.loader.getServer().getPluginManager().disablePlugin(this.loader); //We don't want to enable anything if we fail to run as it may cause major issues
		}
	}
	
	public BukkitParinaCoreConfig getConfig()
	{
		return (BukkitParinaCoreConfig)super.getConfig();
	}

	@Override
	public NickManager getNickManager()
	{
		return this.nickManager;
	}
}
