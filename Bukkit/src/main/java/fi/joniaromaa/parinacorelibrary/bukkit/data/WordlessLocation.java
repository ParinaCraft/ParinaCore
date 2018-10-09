package fi.joniaromaa.parinacorelibrary.bukkit.data;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import lombok.Getter;

public final class WordlessLocation implements ConfigurationSerializable
{
	@Getter private final double x;
	@Getter private final double y;
	@Getter private final double z;

	@Getter private final float yaw;
	@Getter private final float pitch;
	
	public WordlessLocation(double x, double y, double z, float yaw, float pitch)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.yaw = yaw;
		this.pitch = pitch;
	}
	
	public Location toLocation(World world)
	{
		return new Location(world, this.x, this.y, this.z, this.yaw, this.pitch);
	}

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> data = new HashMap<>();
		data.put("x", this.x);
		data.put("y", this.y);
		data.put("z", this.z);
		data.put("yaw", this.yaw);
		data.put("pitch", this.pitch);
		return data;
	}
	
	public static WordlessLocation deserialize(Map<String, Object> data)
	{
		double x = Double.parseDouble(data.get("x").toString());
		double y = Double.parseDouble(data.get("y").toString());
		double z = Double.parseDouble(data.get("z").toString());
		
		float yaw = Float.parseFloat(data.get("yaw").toString());
		float pitch = Float.parseFloat(data.get("pitch").toString());
		
		return new WordlessLocation(x, y, z, yaw, pitch);
	}
}
