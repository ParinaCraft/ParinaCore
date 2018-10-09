package fi.joniaromaa.parinacorelibrary.common.storage.modules.adapters;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.apache.commons.io.IOUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fi.joniaromaa.parinacorelibrary.api.storage.module.StorageModuleAdapter;
import fi.joniaromaa.parinacorelibrary.api.user.User;
import fi.joniaromaa.parinacorelibrary.api.user.dataset.UserDataStorage;
import fi.joniaromaa.parinacorelibrary.common.storage.data.StorageDataSetInfo;
import fi.joniaromaa.parinacorelibrary.common.storage.modules.UserStorageModule;
import fi.joniaromaa.parinacorelibrary.common.storage.types.PostgreSqlStorage;

public abstract class UserStorageModuleAdapter extends StorageModuleAdapter<UserStorageModule, PostgreSqlStorage> implements UserStorageModule
{
	private static final JsonParser jsonParser = new JsonParser();
	
	private List<StorageDataSetInfo> userDataStorageSetInfo;
	
	public UserStorageModuleAdapter(PostgreSqlStorage storage)
	{
		super(storage);
		
		this.userDataStorageSetInfo = new ArrayList<>();
	}

	@Override
	public <T extends UserDataStorage> void addUserDataStorageSet(Class<T> storageDataSet)
	{
		StorageDataSetInfo info = new StorageDataSetInfo(storageDataSet);
		if (info.isValid())
		{
			this.userDataStorageSetInfo.add(info);
		}
	}

	@Override
	public CompletableFuture<User> loadUser(UUID uniqueId)
	{
		return CompletableFuture.supplyAsync(() -> this.figureOutUsername(uniqueId)).thenApply((u) -> this.loadUser0(uniqueId, u));
	}

	@Override
	public CompletableFuture<User> loadUser(UUID uniqueId, String username)
	{
		return CompletableFuture.supplyAsync(() -> this.loadUser0(uniqueId, username));
	}

	@Override
	public CompletableFuture<User> loadUser(UUID uniqueId, Executor executor)
	{
		return CompletableFuture.supplyAsync(() -> this.figureOutUsername(uniqueId), executor).thenApply((u) -> this.loadUser0(uniqueId, u));
	}
	
	@Override
	public CompletableFuture<User> loadUser(UUID uniqueId, String username, Executor executor)
	{
		return CompletableFuture.supplyAsync(() -> this.loadUser0(uniqueId, username), executor);
	}
	
	private String figureOutUsername(UUID uniqueId)
	{
		try
		{
			String json = IOUtils.toString(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uniqueId.toString().replace("-", "")).openConnection().getInputStream(), StandardCharsets.UTF_8);
			if (json != null && json.length() > 0)
			{
				JsonElement profile = UserStorageModuleAdapter.jsonParser.parse(json);
				if (profile.isJsonObject())
				{
					JsonObject profileObject = profile.getAsJsonObject();
					if (profileObject.has("name"))
					{
						return profileObject.get("name").getAsString();
					}
				}
			}
		}
		catch (IOException e)
		{
		}
	
		return "Unknown";
	}
	
	protected List<StorageDataSetInfo> getUserDataStorageSetInfo()
	{
		return Collections.unmodifiableList(this.userDataStorageSetInfo);
	}
	
	protected abstract User loadUser0(UUID uniqueId, String username);
}
