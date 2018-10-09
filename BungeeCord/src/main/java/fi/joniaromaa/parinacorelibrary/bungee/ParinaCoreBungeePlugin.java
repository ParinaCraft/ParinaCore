package fi.joniaromaa.parinacorelibrary.bungee;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import fi.joniaromaa.parinacorelibrary.api.config.ParinaCoreConfig;
import fi.joniaromaa.parinacorelibrary.api.nick.NickManager;
import fi.joniaromaa.parinacorelibrary.bungee.commands.BungeeGlobalListCommand;
import fi.joniaromaa.parinacorelibrary.bungee.config.BungeeParinaCoreConfig;
import fi.joniaromaa.parinacorelibrary.bungee.listeners.BungeePlayerListener;
import fi.joniaromaa.parinacorelibrary.common.AbstractParinaCorePlugin;
import lombok.Getter;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class ParinaCoreBungeePlugin extends AbstractParinaCorePlugin
{
	@Getter private ParinaCoreBungeePluginLoader loader;
	
	public ParinaCoreBungeePlugin(ParinaCoreBungeePluginLoader loader)
	{
		this.loader = loader;
	}

	@Override
	public ParinaCoreConfig loadConfig()
	{
		if (!this.loader.getDataFolder().exists())
		{
			this.loader.getDataFolder().mkdir();
		}

        File file = new File(this.loader.getDataFolder(), "config.yml");
        if (!file.exists())
        {
            try (InputStream in = this.loader.getResourceAsStream("bungee_config.yml"))
            {
                Files.copy(in, file.toPath());
            }
            catch (IOException e)
            {
				e.printStackTrace();
			}
        }
        
		try
		{
			Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
			
			ParinaCoreConfig config = new BungeeParinaCoreConfig(configuration);
			
			return config;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void setupPlatform() 
	{
		this.loader.getProxy().getPluginManager().registerCommand(this.loader, new BungeeGlobalListCommand(this));
		
		this.loader.getProxy().getPluginManager().registerListener(this.loader, new BungeePlayerListener(this));
	}

	@Override
	public NickManager getNickManager()
	{
		throw new UnsupportedOperationException();
	}
}
