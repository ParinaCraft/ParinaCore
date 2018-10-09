package fi.joniaromaa.parinacorelibrary.api.config;

public interface DatabaseConfig
{
	public String getDatabaseHost();
	public int getDatabasePort();
	
	public String getDatabaseUser();
	public String getDatabasePass();
	public String getDatabaseName();
}
