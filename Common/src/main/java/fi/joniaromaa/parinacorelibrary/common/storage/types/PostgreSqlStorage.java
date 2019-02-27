package fi.joniaromaa.parinacorelibrary.common.storage.types;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import fi.joniaromaa.parinacorelibrary.api.config.DatabaseConfig;
import fi.joniaromaa.parinacorelibrary.api.storage.Storage;
import fi.joniaromaa.parinacorelibrary.api.storage.StorageType;

public class PostgreSqlStorage implements Storage
{
	private DataSource pool;
	
	public PostgreSqlStorage(DatabaseConfig config)
	{
		this(config.getDatabaseHost(), config.getDatabasePort(), config.getDatabaseUser(), config.getDatabasePass(), config.getDatabaseName());
	}
	
	public PostgreSqlStorage(String host, int port, String user, String pass, String name)
	{
		PoolProperties poolProperties = new PoolProperties();
        poolProperties.setDriverClassName("org.postgresql.Driver");
        poolProperties.setUrl("jdbc:postgresql://" + host + ":" + port + "/" + name);
        poolProperties.setUsername(user);
        poolProperties.setPassword(pass);
        poolProperties.setMinIdle(2);
        poolProperties.setMaxIdle(8);
        poolProperties.setMaxActive(8);
        poolProperties.setInitialSize(2);
        poolProperties.setMaxWait(-1);
        poolProperties.setRemoveAbandoned(true);
        poolProperties.setRemoveAbandonedTimeout(60);
        poolProperties.setTestOnBorrow(true);
        poolProperties.setValidationQuery("SELECT 1");
        poolProperties.setValidationInterval(30000);
        this.pool = new DataSource(poolProperties);
	}

	@Override
	public StorageType getType()
	{
		return StorageType.PostgreSql;
	}

	@Override
	public Connection getConnection() throws SQLException
	{
		return this.pool.getConnection();
	}
}
