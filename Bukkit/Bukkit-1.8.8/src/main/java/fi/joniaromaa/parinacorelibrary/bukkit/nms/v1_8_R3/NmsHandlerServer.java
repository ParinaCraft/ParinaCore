package fi.joniaromaa.parinacorelibrary.bukkit.nms.v1_8_R3;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.defaults.HelpCommand;
import org.bukkit.command.defaults.PluginsCommand;
import org.bukkit.command.defaults.ReloadCommand;
import org.bukkit.command.defaults.VersionCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.help.HelpYamlReader;
import org.bukkit.craftbukkit.v1_8_R3.help.SimpleHelpMap;
import org.spigotmc.SpigotConfig;

import com.google.common.collect.Lists;

import net.minecraft.server.v1_8_R3.MinecraftServer;

public class NmsHandlerServer implements fi.joniaromaa.parinacorelibrary.bukkit.nms.handlers.NmsHandlerServer
{
	@Override
	public void doHackyConfig()
	{
		//Edit the config itself
		MinecraftServer mcServer = ((CraftServer)Bukkit.getServer()).getServer();
		mcServer.getPropertyManager().setProperty("online-mode", "false"); //BungeeCord
		mcServer.getPropertyManager().setProperty("player-idle-timeout", "0");
		mcServer.getPropertyManager().setProperty("announce-player-achievements", "false");
		mcServer.getPropertyManager().setProperty("enable-command-block", "false"); //Add special case for this? By default false should be forced
		mcServer.getPropertyManager().setProperty("broadcast-rcon-to-ops", "false");
		mcServer.getPropertyManager().setProperty("broadcast-console-to-ops", "false");
		mcServer.getPropertyManager().setProperty("snooper-enabled", "false");
		mcServer.getPropertyManager().setProperty("enable-query", "false");
		mcServer.getPropertyManager().setProperty("enable-rcon", "false");
		mcServer.getPropertyManager().setProperty("hardcore", "false");
		mcServer.getPropertyManager().savePropertiesFile(); //Save so these aren't that "silent"
		
		//Change some variables as they are not read directly from the config file
		mcServer.setOnlineMode(false);
		mcServer.setIdleTimeout(0);
		
		try
		{
			//Access the field that holds bukkit.yml
			Field bukkitConfigField = CraftServer.class.getDeclaredField("configuration");
			bukkitConfigField.setAccessible(true);
			
			YamlConfiguration bukkitConfig = (YamlConfiguration)bukkitConfigField.get(Bukkit.getServer());
			bukkitConfig.set("settings.query-plugins", false); //Blocks query returning plugins
			bukkitConfig.set("settings.plugin-profiling", false); //Dont profile plugins as it takes CPU time
			
			//Save the changes to bukkit.yml
			Method bukkitConfigSaveMethod = CraftServer.class.getDeclaredMethod("saveConfig");
			bukkitConfigSaveMethod.setAccessible(true);
			bukkitConfigSaveMethod.invoke(Bukkit.getServer());
		}
		catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			//Disable timings
			Method pluginManagerUseTimingsMethod = Bukkit.getServer().getPluginManager().getClass().getDeclaredMethod("useTimings", boolean.class);
			pluginManagerUseTimingsMethod.setAccessible(true);
			pluginManagerUseTimingsMethod.invoke(Bukkit.getServer().getPluginManager(), false);
		}
		catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
		{
			e.printStackTrace();
		}

		SpigotConfig.config.set("settings.bungeecord", true); //BungeeCord
		SpigotConfig.config.set("settings.player-shuffle", 20); //Helps with the "gambling"
		
		SpigotConfig.config.set("commands.tab-complete", 0);
		SpigotConfig.config.set("commands.silent-commandblock-console", false);
		SpigotConfig.config.set("commands.log", true);
		SpigotConfig.config.set("commands.spam-exclusions", new ArrayList<>());

		SpigotConfig.config.set("settings.save-user-cache-on-stop-only", true); //Dont waste IO on this while running
		SpigotConfig.config.set("stats.forced-stats.achievement.openInventory", 1); //Prevents the anonying "open inventory" achievement not going away
		
		SpigotConfig.config.set("world-settings.default.random-light-updates", false);
		
		try
		{
			//Save spigot.yml
			Field spigotConfigFile = SpigotConfig.class.getDeclaredField("CONFIG_FILE");
			spigotConfigFile.setAccessible(true);
			SpigotConfig.config.save((File)spigotConfigFile.get(null));
		}
		catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | IOException e)
		{
			e.printStackTrace();
		}
		
		//Update the fields
		SpigotConfig.bungee = true;
		SpigotConfig.playerShuffle = 20;
		
		SpigotConfig.tabComplete = 0;
		SpigotConfig.silentCommandBlocks = false;
		SpigotConfig.logCommands = true;
		SpigotConfig.spamExclusions.clear();

		SpigotConfig.saveUserCacheOnStopOnly = true;
		SpigotConfig.forcedStats.put("achievement.openInventory", 1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void disableBukkitCommands()
	{
		try
		{
			Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
			knownCommandsField.setAccessible(true);
			
			Map<String, Command> knownCommands = (Map<String, Command>)knownCommandsField.get(((CraftServer)Bukkit.getServer()).getCommandMap());
			this.removeCommand(knownCommands, VersionCommand.class);
			this.removeCommand(knownCommands, ReloadCommand.class);
			this.removeCommand(knownCommands, PluginsCommand.class);
			this.removeCommand(knownCommands, HelpCommand.class);
			
			this.renameCommandMod(knownCommands, "bukkit", "parina");
			this.renameCommandMod(knownCommands, "spigot", "parina");
		}
		catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			Field helpYamlReaderField = SimpleHelpMap.class.getDeclaredField("yaml");
			helpYamlReaderField.setAccessible(true);
			
			HelpYamlReader helpYamlReader = (HelpYamlReader)helpYamlReaderField.get(Bukkit.getServer().getHelpMap());
			
			Field helpYamlField = HelpYamlReader.class.getDeclaredField("helpYaml");
			helpYamlField.setAccessible(true);
			
			YamlConfiguration helpYaml = (YamlConfiguration)helpYamlField.get(helpYamlReader);
			helpYaml.set("ignore-plugins", Lists.newArrayList("All"));
			helpYaml.save(new File("help.yml"));
		}
		catch (NoSuchFieldException | IOException | IllegalArgumentException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
		
		Bukkit.getServer().getHelpMap().clear();
	}

	private void removeCommand(Map<String, Command> knownCommands, Class<? extends Command> commandClass)
	{
		Iterator<Command> iterator = knownCommands.values().iterator();
		while (iterator.hasNext())
		{
			Command command = iterator.next();
			if (command.getClass().equals(commandClass))
			{
				command.unregister(((CraftServer)Bukkit.getServer()).getCommandMap());
				
				iterator.remove();
			}
		}
	}
	
	private void renameCommandMod(Map<String, Command> knownCommands, String oldMod, String newMod)
	{
		List<String> toRename = new ArrayList<>();
		for(String key : knownCommands.keySet())
		{
			if (key.startsWith(oldMod + ":"))
			{
				toRename.add(key);
			}
		}
		
		for(String rename : toRename)
		{
			String[] split = rename.split(":");
			
			knownCommands.put(newMod + ":" + split[1], knownCommands.remove(rename));
		}
	}
}
