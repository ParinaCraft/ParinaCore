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
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Contexts;
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
			stringBuilder.append(p).append(' '); //Add space to keep prefix and display name seprate
		});
		
		stringBuilder.append(this.getDisplayName());
		
		this.getSuffix().ifPresent((s) ->
		{
			stringBuilder.append(' ').append(s); //Add space to keep prefix and display name seprate
		});
		
		return stringBuilder.toString();
	}
	
	public Optional<String> getPrefix()
	{
		me.lucko.luckperms.api.User lpUser = LuckPerms.getApi().getUser(this.uniqueId);
		if (lpUser != null)
		{
			String prefix = lpUser.getCachedData().getMetaData(Contexts.global()).getPrefix();
			if (prefix != null)
			{
				return Optional.of(ChatColor.translateAlternateColorCodes('&', prefix));
			}
		}
		
		return Optional.empty();
	}
	
	public Optional<String> getSuffix()
	{
		me.lucko.luckperms.api.User lpUser = LuckPerms.getApi().getUser(this.uniqueId);
		if (lpUser != null)
		{
			String prefix = lpUser.getCachedData().getMetaData(Contexts.global()).getSuffix();
			if (prefix != null)
			{
				return Optional.of(ChatColor.translateAlternateColorCodes('&', prefix));
			}
		}
		
		return Optional.empty();
	}
	
	public int getWeight()
	{
		me.lucko.luckperms.api.User lpUser = LuckPerms.getApi().getUser(this.uniqueId);
		if (lpUser != null)
		{
			me.lucko.luckperms.api.Group group = LuckPerms.getApi().getGroup(lpUser.getPrimaryGroup());
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
