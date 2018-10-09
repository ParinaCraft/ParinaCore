package fi.joniaromaa.parinacorelibrary.api.language;

import java.util.Locale;

import javax.annotation.Nullable;

public interface LanguageManager
{
	public void addTranslation(Locale locale, String area, String key, String value);
	
	public String getTranslation(@Nullable Locale locale, String area, String key, Object ...params);
}
