package fi.joniaromaa.parinacorelibrary.common.storage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import fi.joniaromaa.parinacorelibrary.api.storage.Storage;
import fi.joniaromaa.parinacorelibrary.api.storage.StorageManager;
import fi.joniaromaa.parinacorelibrary.api.storage.StorageType;
import fi.joniaromaa.parinacorelibrary.api.storage.module.StorageModule;
import fi.joniaromaa.parinacorelibrary.api.storage.module.StorageModuleAdapter;

public class SimpleStorageManager implements StorageManager
{
	private Storage storage;
	
	private Map<Class<? extends StorageModule>, StorageModuleAdapter<?, ?>> modules;
	
	public SimpleStorageManager(Storage storage)
	{
		this.storage = storage;
		
		this.modules = new HashMap<>();
	}

	@Override
	public <T extends StorageModule> void addStorageModule(Class<T> module, @SuppressWarnings("unchecked") Class<? extends StorageModuleAdapter<T, ?>> ...adapters)
	{
		for(Class<? extends StorageModuleAdapter<T, ?>> adapter : adapters)
		{
			Type storageType = ((ParameterizedType)adapter.getSuperclass().getGenericSuperclass()).getActualTypeArguments()[1];
			if (storageType instanceof Class<?>)
			{
				if (this.storage.getClass().isAssignableFrom((Class<?>)storageType))
				{
					try
					{
						this.modules.put(module, adapter.getConstructor((Class<?>)storageType).newInstance(this.storage));
					}
					catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
					{
						throw new IllegalArgumentException(e);
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends StorageModule> T getStorageModule(Class<T> baseClass)
	{
		StorageModuleAdapter<?, ?> storageModule = this.modules.get(baseClass);
		if (storageModule != null)
		{
			return (T)storageModule;
		}
		
		return null;
	}

	@Override
	public Storage getStorage()
	{
		return this.storage;
	}

	@Override
	public StorageType getStorageType()
	{
		return this.storage.getType();
	}
}
