package fi.joniaromaa.parinacorelibrary.api.language;

import java.util.Locale;

import javax.annotation.Nonnull;

public interface LanguageManager
{
	public void addTranslation(@Nonnull Locale locale, @Nonnull String area, @Nonnull String key, @Nonnull String value);
	
	public @Nonnull String getTranslation(@Nonnull Locale locale, @Nonnull String area, @Nonnull String key, Object ...params);
}
