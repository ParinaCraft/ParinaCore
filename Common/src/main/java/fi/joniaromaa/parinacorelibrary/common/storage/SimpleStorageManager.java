package fi.joniaromaa.parinacorelibrary.common.storage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

	@SuppressWarnings("unchecked")
	@Override
	public <T extends StorageModule> Optional<T> addStorageModule(Class<T> module, Class<? extends StorageModuleAdapter<T, ?>> ...adapters)
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
						StorageModuleAdapter<T, ?> adapterInstance = adapter.getConstructor((Class<?>)storageType).newInstance(this.storage);
						
						this.modules.put(module, adapterInstance);
						
						return Optional.of((T)adapterInstance);
					}
					catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
					{
						throw new IllegalArgumentException(e);
					}
				}
			}
		}
		
		return Optional.empty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends StorageModule> Optional<T> getStorageModule(Class<T> baseClass)
	{
		return Optional.ofNullable((T)this.modules.get(baseClass));
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
