package fi.joniaromaa.parinacorelibrary.bukkit.config;

import org.bukkit.configuration.Configuration;

import fi.joniaromaa.parinacorelibrary.api.config.ParinaCoreConfig;
import lombok.Getter;

public class BukkitParinaCoreConfig extends ParinaCoreConfig
{
	@Getter private int serverId;
	
	@Getter private String databaseHost;
	@Getter private int databasePort;
	@Getter private String databaseUser;
	@Getter private String databasePass;
	@Getter private String databaseName;
	
	public BukkitParinaCoreConfig(Configuration file)
	{
		this.serverId = file.getInt("server.id");
		
		this.databaseHost = file.getString("database.host");
		this.databasePort = file.getInt("database.port");
		this.databaseUser = file.getString("database.user");
		this.databasePass = file.getString("database.pass");
		this.databaseName = file.getString("database.name");
	}
}
