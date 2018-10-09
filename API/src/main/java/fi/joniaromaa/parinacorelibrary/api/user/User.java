package fi.joniaromaa.parinacorelibrary.api.user;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import fi.joniaromaa.parinacorelibrary.api.user.dataset.UserDataStorage;

public interface User
{
	public void setDisplayName(@Nullable String displayName);
	
	public void setDataStorage(UserDataStorage dataSet);
	
	public <T extends UserDataStorage> T removeDataStorage(Class<T> clazz);
	
	public @Nonnull UUID getUniqueId();
	public @Nonnull String getUsername();
	
	public Optional<Locale> getLocale();
	
	public @Nonnull String getDisplayName();
	public @Nonnull String getColoredDisplayName();
	public @Nonnull String getFormattedDisplayName();
	
	public Optional<String> getPrefix();
	public Optional<String> getSuffix();
	
	public int getWeight();
	
	public <T extends UserDataStorage> T getDataStorage(Class<T> clazz);
}
