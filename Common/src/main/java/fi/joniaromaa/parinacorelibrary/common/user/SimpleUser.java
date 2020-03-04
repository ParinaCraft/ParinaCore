package fi.joniaromaa.parinacorelibrary.common.user;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;

import com.google.common.base.Preconditions;

import fi.joniaromaa.parinacorelibrary.api.user.User;
import fi.joniaromaa.parinacorelibrary.api.user.dataset.UserDataStorage;
import lombok.Getter;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.query.QueryOptions;
import net.md_5.bungee.api.ChatColor;

public class SimpleUser implements User
{
	@Getter private final int tableId;
	@Getter private final UUID uniqueId;
	@Getter private String username;
	
	private Optional<String> displayName;
	
	private Map<Class<? extends UserDataStorage>, UserDataStorage> storageSets;
	
	public SimpleUser(int tableId, UUID id, String username)
	{
		this.tableId = tableId;
		this.uniqueId = id;
		this.username = username;
		
		this.displayName = Optional.empty();
		
		this.storageSets = new HashMap<>();
	}
	
	public void setDisplayName(String displayName)
	{
		if (displayName == null || displayName.isEmpty())
		{
			this.displayName = Optional.empty();
		}
		else
		{
			Preconditions.checkArgument(displayName.length() > 16, "Display name can't be longer than 16 chars!");
			
			this.displayName = Optional.of(displayName);
		}
	}

	@Override
	public void setDataStorage(UserDataStorage dataSet)
	{
		this.storageSets.put(dataSet.getClass(), dataSet);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends UserDataStorage> Optional<T> removeDataStorage(Class<T> clazz)
	{
		return Optional.ofNullable((T)this.storageSets.remove(clazz));
	}

	@Override
	public Optional<Locale> getLocale()
	{
		return Optional.empty();
	}
	
	public String getDisplayName()
	{
		return this.displayName.orElse(this.username);
	}

	@Override
	public String getColoredDisplayName()
	{
		Optional<String> prefix = this.getPrefix();
		if (prefix.isPresent())
		{
			int colorPrefixIndex = prefix.get().lastIndexOf('');
			if (colorPrefixIndex != -1)
			{
				String color = prefix.get().substring(colorPrefixIndex, colorPrefixIndex + 2);
				
				return color + this.getDisplayName();
			}
		}
		
		return this.getDisplayName();
	}

	@Override
	public String getFormattedDisplayName()
	{
		StringBuilder stringBuilder = new StringBuilder();
		
		this.getPrefix().ifPresent((p) ->
		{
			stringBuilder.append(p);
		});
		
		stringBuilder.append(this.getDisplayName());
		
		this.getSuffix().ifPresent((s) ->
		{
			stringBuilder.append(s);
		});
		
		return stringBuilder.toString();
	}
	
	public Optional<String> getPrefix()
	{
		net.luckperms.api.model.user.User lpUser = LuckPermsProvider.get().getUserManager().getUser(this.uniqueId);
		if (lpUser != null)
		{
			String prefix = lpUser.getCachedData().getMetaData(QueryOptions.defaultContextualOptions()).getPrefix();
			if (prefix != null)
			{
				return Optional.of(ChatColor.translateAlternateColorCodes('&', prefix));
			}
		}
		
		return Optional.empty();
	}
	
	public Optional<String> getSuffix()
	{
		net.luckperms.api.model.user.User lpUser = LuckPermsProvider.get().getUserManager().getUser(this.uniqueId);
		if (lpUser != null)
		{
			String prefix = lpUser.getCachedData().getMetaData(QueryOptions.defaultContextualOptions()).getSuffix();
			if (prefix != null)
			{
				return Optional.of(ChatColor.translateAlternateColorCodes('&', prefix));
			}
		}
		
		return Optional.empty();
	}
	
	public int getWeight()
	{
		net.luckperms.api.model.user.User lpUser = LuckPermsProvider.get().getUserManager().getUser(this.uniqueId);
		if (lpUser != null)
		{
			net.luckperms.api.model.group.Group group = LuckPermsProvider.get().getGroupManager().getGroup(lpUser.getPrimaryGroup());
			if (group != null)
			{
				OptionalInt weight = group.getWeight();
				if (weight.isPresent())
				{
					return weight.getAsInt();
				}
			}
		}
		
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends UserDataStorage> Optional<T> getDataStorage(Class<T> clazz)
	{
		return Optional.ofNullable((T)this.storageSets.get(clazz));
	}
}
