package fi.joniaromaa.parinacorelibrary.api.storage;

import lombok.Getter;

public enum StorageType
{
	PostgreSql("postgre");
	
	@Getter private String name;
	
	private StorageType(String name)
	{
		this.name = name;
	}
}
