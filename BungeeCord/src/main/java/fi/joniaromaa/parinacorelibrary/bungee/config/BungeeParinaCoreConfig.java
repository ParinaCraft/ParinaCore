package fi.joniaromaa.parinacorelibrary.bungee.config;

import fi.joniaromaa.parinacorelibrary.api.config.ParinaCoreConfig;
import lombok.Getter;
import net.md_5.bungee.config.Configuration;

public class BungeeParinaCoreConfig extends ParinaCoreConfig
{
	@Getter private String databaseHost;
	@Getter private int databasePort;
	@Getter private String databaseUser;
	@Getter private String databasePass;
	@Getter private String databaseName;
	
	public BungeeParinaCoreConfig(Configuration file)
	{
		this.databaseHost = file.getString("database.host");
		this.databasePort = file.getInt("database.port");
		this.databaseUser = file.getString("database.user");
		this.databasePass = file.getString("database.pass");
		this.databaseName = file.getString("database.name");
	}
}
