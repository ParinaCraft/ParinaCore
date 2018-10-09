package fi.joniaromaa.parinacorelibrary.bukkit.utils;

public final class LocationUtils
{
	public static boolean outsideChunkCoords(int x, int z, int minX, int minZ, int maxX, int maxZ)
	{
		int topX = (minX < maxX ? maxX : minX);
		int bottomX = (minX > maxX ? maxX : minX);
		 
		int topZ = (minZ < maxZ ? maxZ : minZ);
		int bottomZ = (minZ > maxZ ? maxZ : minZ);
		
		if(x > topX || x < bottomX || z > topZ || z < bottomZ)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
