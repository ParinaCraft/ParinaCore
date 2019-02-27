package fi.joniaromaa.parinacorelibrary.api.storage;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Nonnull;

public interface Storage
{
	public @Nonnull StorageType getType();
	
	public @Nonnull Connection getConnection() throws SQLException;
}
