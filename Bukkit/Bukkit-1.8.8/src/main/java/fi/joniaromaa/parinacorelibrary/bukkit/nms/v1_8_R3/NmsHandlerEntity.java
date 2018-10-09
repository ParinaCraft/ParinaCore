package fi.joniaromaa.parinacorelibrary.bukkit.nms.v1_8_R3;

import java.lang.reflect.Field;

import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.EntityFireworks;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_8_R3.WorldServer;

public class NmsHandlerEntity implements fi.joniaromaa.parinacorelibrary.bukkit.nms.handlers.NmsHandlerEntity
{
	@Override
	public void fixEntityCount()
	{
		try
		{
			Field entityCountField = Entity.class.getDeclaredField("entityCount");
			entityCountField.setAccessible(true);
			
			 //On empty worlds player might get entity id 0 which makes some things bug the hell out, one example is, bow
			if (entityCountField.getInt(null) <= 0)	
			{
				entityCountField.setInt(null, 1);
			}
		}
		catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void fireworkExplodeEffect(Location location, FireworkEffect ...effects)
	{
		WorldServer worldNms = ((CraftWorld)location.getWorld()).getHandle();
		EntityFireworks fireworkNms = new EntityFireworks(worldNms);
		fireworkNms.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		
		Firework firework = (Firework)fireworkNms.getBukkitEntity();
		
		//Use bukkit APIs to modify it
		FireworkMeta meta = firework.getFireworkMeta();
		meta.addEffects(effects);
		meta.setPower(0);
		
		firework.setFireworkMeta(meta);
		
		worldNms.getMinecraftServer().getPlayerList().sendPacketNearby(location.getX(), location.getY(), location.getZ(), 64, worldNms.dimension, new PacketPlayOutSpawnEntity(fireworkNms, 76));
		worldNms.getMinecraftServer().getPlayerList().sendPacketNearby(location.getX(), location.getY(), location.getZ(), 64, worldNms.dimension, new PacketPlayOutEntityMetadata(fireworkNms.getId(), fireworkNms.getDataWatcher(), true));
		worldNms.getMinecraftServer().getPlayerList().sendPacketNearby(location.getX(), location.getY(), location.getZ(), 64, worldNms.dimension, new PacketPlayOutEntityStatus(fireworkNms, (byte) 17));
		worldNms.getMinecraftServer().getPlayerList().sendPacketNearby(location.getX(), location.getY(), location.getZ(), 64, worldNms.dimension, new PacketPlayOutEntityDestroy(fireworkNms.getId()));
	}

	@Override
	public ArmorStand createFakeHologram(Location location)
	{
		WorldServer worldServer = ((CraftWorld)location.getWorld()).getHandle();
		
		EntityArmorStand armorStand = new EntityArmorStand(worldServer, location.getX(), location.getY(), location.getZ());
		armorStand.ae = location.getChunk().getX();
		armorStand.ag = location.getChunk().getZ();
		
		ArmorStand armorStandBukkit = (ArmorStand)armorStand.getBukkitEntity();
		armorStandBukkit.setGravity(false);
		armorStandBukkit.setMarker(true);
		armorStandBukkit.setVisible(false);
		
		worldServer.tracker.track(armorStand);
		
		return armorStandBukkit;
	}

	@Override
	public void setEntityDataWatcher(org.bukkit.entity.Entity entity, int id, Object value)
	{
		((CraftEntity)entity).getHandle().getDataWatcher().watch(id, value);
	}
}
