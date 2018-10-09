package fi.joniaromaa.parinacorelibrary.common.language;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import fi.joniaromaa.parinacorelibrary.api.language.LanguageManager;

public class SimpleLanguageManager implements LanguageManager
{
	private static final Locale DEFAULT = Locale.forLanguageTag("fi-FI");
	
	private Map<Locale, Map<String, Map<String, String>>> locales;
	
	public SimpleLanguageManager()
	{
		this.locales = new HashMap<>();
	}
	
	@Override
	public void addTranslation(Locale locale, String area, String key, String value)
	{
		Map<String, Map<String, String>> areas = this.locales.get(locale);
		if (areas == null)
		{
			this.locales.put(locale, areas = new HashMap<>());
		}
		
		Map<String, String> translations = areas.get(area);
		if (translations == null)
		{
			areas.put(area, translations = new HashMap<>());
		}
		
		translations.put(key, value);
	}

	@Override
	public String getTranslation(Locale locale, String area, String key, Object... params)
	{
		String translation = this.getRawTranslation(locale, area, key);
		if (translation == null)
		{
			if (!SimpleLanguageManager.DEFAULT.equals(locale))
			{
				translation = this.getRawTranslation(SimpleLanguageManager.DEFAULT, area, key);
			}
		}
		
		if (translation != null)
		{
			return MessageFormat.format(translation, params);
		}
		
		return locale.toLanguageTag() + "." + area + "." + key;
	}
	
	private String getRawTranslation(Locale locale, String area, String key)
	{
		Map<String, Map<String, String>> areas = this.locales.get(locale);
		if (areas != null)
		{
			Map<String, String> translations = areas.get(area);
			if (translations != null)
			{
				return translations.get(key);
			}
		}
		
		return null;
	}
}
