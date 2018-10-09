package fi.joniaromaa.parinacorelibrary.bukkit.utils;

import org.bukkit.World;

public final class WorldUtils
{
	public static void loadChunksBetween(World world, int minX, int minZ, int maxX, int maxZ)
	{
		int bottomX = (minX > maxX ? maxX : minX);
		int topX = (minX < maxX ? maxX : minX);

		int bottomZ = (minZ > maxZ ? maxZ : minZ);
		int topZ = (minZ < maxZ ? maxZ : minZ);
		
		for(int i = bottomX; i <= topX; i++)
		{
			for(int j = bottomZ; j <= topZ; j++)
			{
				world.loadChunk(i, j, false);
			}
		}
	}
	
	public static void unloadChunksBetween(World world, int minX, int minZ, int maxX, int maxZ)
	{
		int bottomX = (minX > maxX ? maxX : minX);
		int topX = (minX < maxX ? maxX : minX);

		int bottomZ = (minZ > maxZ ? maxZ : minZ);
		int topZ = (minZ < maxZ ? maxZ : minZ);
		
		for(int i = bottomX; i <= topX; i++)
		{
			for(int j = bottomZ; j <= topZ; j++)
			{
				world.unloadChunk(i, j, false, false);
			}
		}
	}
}
