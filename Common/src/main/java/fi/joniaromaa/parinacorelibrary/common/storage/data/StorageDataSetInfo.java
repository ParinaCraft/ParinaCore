package fi.joniaromaa.parinacorelibrary.common.storage.data;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import fi.joniaromaa.parinacorelibrary.api.storage.dataset.StorageDataGetter;
import fi.joniaromaa.parinacorelibrary.api.storage.dataset.StorageDataRank;
import fi.joniaromaa.parinacorelibrary.api.storage.dataset.StorageDataSet;
import fi.joniaromaa.parinacorelibrary.api.storage.dataset.StorageDataUpdateType;
import fi.joniaromaa.parinacorelibrary.api.storage.dataset.StorageDataUpdater;
import lombok.Getter;

public class StorageDataSetInfo
{
	@Getter private final Class<?> dataSetClass;
	@Getter private final String schemeName;
	@Getter private final String tableName;
	
	private Map<String, Field> getterFields;
	private Map<String, Field> incrementUpdaterFields;
	private Map<String, Field> rankFields;
	
	public StorageDataSetInfo(Class<?> clazz)
	{
		this.dataSetClass = clazz;
		
		this.getterFields = new LinkedHashMap<>();
		this.incrementUpdaterFields = new LinkedHashMap<>();
		this.rankFields = new LinkedHashMap<>();

		StorageDataSet storageSet = clazz.getAnnotation(StorageDataSet.class);
		if (storageSet != null)
		{
			this.schemeName = storageSet.schema();
			this.tableName = storageSet.table();
			
			for (Field field : clazz.getDeclaredFields())
			{
				field.setAccessible(true);
				
				StorageDataGetter getter = field.getAnnotation(StorageDataGetter.class);
				if (getter != null)
				{
					this.getterFields.put(getter.value(), field);
				}
				
				StorageDataUpdater updater = field.getAnnotation(StorageDataUpdater.class);
				if (updater != null)
				{
					if (updater.type() == StorageDataUpdateType.INCREMENT)
					{
						this.incrementUpdaterFields.put(updater.value(), field);
					}
				}
				
				StorageDataRank rank = field.getAnnotation(StorageDataRank.class);
				if (rank != null)
				{
					this.rankFields.put(rank.value(), field);
				}
			}
		}
		else
		{
			this.schemeName = null;
			this.tableName = null; //Invalid
		}
	}
	
	public boolean isValid()
	{
		return this.tableName != null && !this.tableName.isEmpty();
	}
	
	public Map<String, Field> getGetterFields()
	{
		return Collections.unmodifiableMap(this.getterFields);
	}
	
	public Map<String, Field> getIncrementUpdaterFields()
	{
		return Collections.unmodifiableMap(this.incrementUpdaterFields);
	}
	
	public Map<String, Field> getRankFields()
	{
		return Collections.unmodifiableMap(this.rankFields);
	}
	
	public String getUniqueName()
	{
		return this.schemeName + "_" + this.tableName;
	}
}
