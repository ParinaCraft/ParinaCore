package fi.joniaromaa.parinacorelibrary.common.storage.modules.adapters.postgres;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import fi.joniaromaa.parinacorelibrary.api.user.User;
import fi.joniaromaa.parinacorelibrary.api.user.dataset.UserDataStorage;
import fi.joniaromaa.parinacorelibrary.common.storage.data.StorageDataSetInfo;
import fi.joniaromaa.parinacorelibrary.common.storage.modules.adapters.UserStorageModuleAdapter;
import fi.joniaromaa.parinacorelibrary.common.storage.types.PostgreSqlStorage;
import fi.joniaromaa.parinacorelibrary.common.user.SimpleUser;

public class UserStorageModulePostgresAdapter extends UserStorageModuleAdapter<PostgreSqlStorage>
{
	public UserStorageModulePostgresAdapter(PostgreSqlStorage storage)
	{
		super(storage);
	}
	
	//TODO: We need to implement effient caching for dynamic queries and take advantage of prepared statements
	
	@Override
	protected Optional<User> loadUser0(UUID uniqueId, String username)
	{
		StringBuilder stringBuilder = new StringBuilder("SELECT * FROM (SELECT u.id, u.uuid, u.username");
		for(StorageDataSetInfo dataSet : this.getUserDataStorageSetInfo())
		{
			for(String getter : dataSet.getGetterFields().keySet())
			{
				stringBuilder.append(", ")
					.append(dataSet.getUniqueName())
					.append('.')
					.append(getter)
					.append(" AS ")
					.append("getter_")
					.append(dataSet.getUniqueName())
					.append('_')
					.append(getter);
			}
			
			for(String rank : dataSet.getRankFields().keySet())
			{
				stringBuilder.append(", ROW_NUMBER() OVER(ORDER BY ")
					.append(dataSet.getUniqueName())
					.append('.')
					.append(rank)
					.append(" DESC NULLS LAST)::integer AS ")
					.append("rank_")
					.append(dataSet.getUniqueName())
					.append('_')
					.append(rank);
			}
		}
		
		stringBuilder.append(" FROM base.users u ");
		
		for(StorageDataSetInfo dataSet : this.getUserDataStorageSetInfo())
		{
			stringBuilder.append("LEFT JOIN ")
				.append(dataSet.getSchemeName())
				.append('.')
				.append(dataSet.getTableName())
				.append(' ')
				.append(dataSet.getUniqueName())
				.append(" ON ")
				.append(dataSet.getUniqueName())
				.append(".id = u.id ");
		}
		
		stringBuilder.append(") AS d WHERE d.uuid = '")
			.append(uniqueId)
			.append("' LIMIT 1");

		try (Connection connection = this.getStorage().getConnection(); Statement statement = connection.createStatement())
		{
			if (username != null)
			{
				try(PreparedStatement createUserStatement = connection.prepareStatement("WITH new_values (uuid, username) AS (VALUES('" + uniqueId + "'::uuid, ?)), upsert AS(UPDATE base.users u SET username = nv.username FROM new_values nv WHERE u.uuid = nv.uuid RETURNING u.*) INSERT INTO base.users (uuid, username) SELECT uuid, username FROM new_values WHERE NOT EXISTS (SELECT 1 FROM upsert up WHERE up.uuid = new_values.uuid)"))
				{
					createUserStatement.setString(1, username);
					
					createUserStatement.execute();
				}
			}
			
			try (ResultSet resultSet = statement.executeQuery(stringBuilder.toString()))
			{
				if (resultSet.next())
				{
					int id = resultSet.getInt("id");
					UUID sqlUniqueId = UUID.fromString(resultSet.getString("uuid"));
					String sqlUsername = resultSet.getString("username");
					
					SimpleUser user = new SimpleUser(id, sqlUniqueId, sqlUsername);
					
					for(StorageDataSetInfo dataSet : this.getUserDataStorageSetInfo())
					{
						try
						{
							UserDataStorage dataSetContainer = (UserDataStorage)dataSet.getDataSetClass().newInstance();
							for(Entry<String, Field> getter : dataSet.getGetterFields().entrySet())
							{
								String fieldName = getter.getKey();
								Field field = getter.getValue();
								
								Object fieldValue = resultSet.getObject("getter_" + dataSet.getUniqueName() + '_' + fieldName);
								if (fieldValue != null)
								{
									field.set(dataSetContainer, fieldValue);
								}
							}
							
							for(Entry<String, Field> rank : dataSet.getRankFields().entrySet())
							{
								String fieldName = rank.getKey();
								Field field = rank.getValue();
								
								Object fieldValue = resultSet.getObject("rank_" + dataSet.getUniqueName() + '_' + fieldName);
								if (fieldValue != null)
								{
									field.set(dataSetContainer, fieldValue);
								}
							}
							
							user.setDataStorage(dataSetContainer);
						}
						catch (InstantiationException | IllegalAccessException e)
						{
							e.printStackTrace();
						}
					}
					
					return Optional.of(user);
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return Optional.empty(); //Not found, failed to load, something
	}
	
	private String buildIncrementUpdaterQuery(UUID uniqueId, Object storageData)
	{
		StorageDataSetInfo dataSetInfo = new StorageDataSetInfo(storageData.getClass());
		
		StringBuilder stringBuilder = new StringBuilder("INSERT INTO ")
				.append(dataSetInfo.getSchemeName())
				.append('.')
				.append(dataSetInfo.getTableName())
				.append("(id");
		
		for(String field : dataSetInfo.getIncrementUpdaterFields().keySet())
		{
			stringBuilder.append(", ")
				.append(field);
		}
		
		stringBuilder.append(") SELECT id");
		for(Field field : dataSetInfo.getIncrementUpdaterFields().values())
		{
			try
			{
				stringBuilder.append(", '" + field.get(storageData)).append("'");
			}
			catch (IllegalArgumentException | IllegalAccessException e)
			{
				e.printStackTrace();
				
				return null;
			}
		}
		
		stringBuilder.append(" FROM base.users WHERE uuid = '")
			.append(uniqueId)
			.append("' LIMIT 1 ON CONFLICT(id) DO UPDATE SET ");
		
		boolean first = true;
		for(Entry<String, Field> field : dataSetInfo.getIncrementUpdaterFields().entrySet())
		{
			if (first)
			{
				first = false;
			}
			else
			{
				stringBuilder.append(", ");
			}
			
			stringBuilder.append(field.getKey())
				.append(" = ")
				.append(dataSetInfo.getTableName())
				.append('.')
				.append(field.getKey())
				.append(" + EXCLUDED.")
				.append(field.getKey());
		}
		
		return stringBuilder.toString();
	}

	@Override
	public CompletableFuture<Boolean> updateUserDataStorage(UUID uniqueId, UserDataStorage storageData)
	{
		return CompletableFuture.supplyAsync(() ->
		{
			try (Connection connection = this.getStorage().getConnection(); Statement statement = connection.createStatement())
			{
				if (statement.executeUpdate(this.buildIncrementUpdaterQuery(uniqueId, storageData)) != 1)
				{
					return false;
				}
				
				return true;
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				
				return false;
			}
		});
	}
	
	public boolean updateUserDataStorageSet(UUID uniqueId, String... storageData)
	{
		return false;
	}

	@Override
	public CompletableFuture<Boolean> updateUserDataStorageMultiple(Map<UUID, UserDataStorage> storageDataMap)
	{
		if (storageDataMap.size() <= 0)
		{
			return CompletableFuture.completedFuture(true); //True?
		}
		else if (storageDataMap.size() == 1) //Dont use transaction
		{
			Entry<UUID, UserDataStorage> entry = storageDataMap.entrySet().iterator().next();
			
			return this.updateUserDataStorage(entry.getKey(), entry.getValue());
		}
		else
		{
			return CompletableFuture.supplyAsync(() ->
			{
				try (Connection connection = this.getStorage().getConnection(); Statement statement = connection.createStatement())
				{
					connection.setAutoCommit(false);
					
					try
					{
						for(Entry<UUID, UserDataStorage> entry : storageDataMap.entrySet())
						{
							if (statement.executeUpdate(this.buildIncrementUpdaterQuery(entry.getKey(), entry.getValue())) != 1)
							{
								connection.rollback();
								
								return false;
							}
						}
						
						connection.commit();
						
						return true;
					}
					catch(Exception e)
					{
						connection.rollback();
						
						e.printStackTrace();
						
						return false;
					}
					finally
					{
						connection.setAutoCommit(true);
					}
				}
				catch (SQLException e)
				{
					e.printStackTrace();
					
					return false;
				}
			});
		}
	}
}
