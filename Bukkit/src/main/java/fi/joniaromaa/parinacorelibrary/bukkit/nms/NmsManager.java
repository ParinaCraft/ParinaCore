package fi.joniaromaa.parinacorelibrary.bukkit.nms;

import fi.joniaromaa.parinacorelibrary.bukkit.nms.handlers.NsmHandler;

public class NmsManager
{
	private static NsmHandler nmsHandler;
	
	static
	{
		try
		{
			Class<?> clazz = Class.forName("fi.joniaromaa.parinacorelibrary.bukkit.nms.v1_8_R3.NsmHandler");
			
			NmsManager.nmsHandler = (NsmHandler)clazz.newInstance();
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException e)
		{
			
		}
	}
	
	public static NsmHandler getNmsHandler()
	{
		return NmsManager.nmsHandler;
	}
}
