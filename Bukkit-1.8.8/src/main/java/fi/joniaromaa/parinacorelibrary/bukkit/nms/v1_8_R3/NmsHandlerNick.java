package fi.joniaromaa.parinacorelibrary.bukkit.nms.v1_8_R3;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.ScheduledPacket;
import com.comphenix.protocol.wrappers.EnumWrappers.NativeGameMode;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;

import fi.joniaromaa.parinacorelibrary.bukkit.ParinaCoreBukkitPlugin;
import fi.joniaromaa.parinacorelibrary.bukkit.nick.NickEntry;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;

public class NmsHandlerNick implements fi.joniaromaa.parinacorelibrary.bukkit.nms.handlers.NmsHandlerNick
{
	@Override
	public void handleDeadmau5(ParinaCoreBukkitPlugin plugin, Map<UUID, Map<Integer, NickEntry>> nickEntryByEntity, PacketEvent event)
	{
		Player player = event.getPlayer();
		PacketContainer packet = event.getPacket();
		
		PacketType type = event.getPacketType();
		if (type.equals(PacketType.Play.Server.NAMED_ENTITY_SPAWN))
		{
			UUID uniqueId = packet.getUUIDs().read(0);
			if (plugin.getNickManager().hasDeadmau5Ears(uniqueId))
			{
				Player other = plugin.getLoader().getServer().getPlayer(uniqueId);
				if (other != null)
				{
					int entityId = packet.getIntegers().read(0);
					
					Map<Integer, NickEntry> entities = nickEntryByEntity.computeIfAbsent(player.getUniqueId(), (k) -> new HashMap<>());
					NickEntry nickEntry = entities.computeIfAbsent(entityId, (k) -> new NickEntry(uniqueId));
					nickEntry.getDeadmau5NameTagEntites().add(-1);
					nickEntry.getDeadmau5NameTagEntites().add(-2);
					
					EntityPlayer otherNms = ((CraftPlayer)other).getHandle();
					GameProfile gameProfile = otherNms.getProfile();

					WrappedGameProfile deadmau5GameProfile = new WrappedGameProfile(gameProfile.getId(), "deadmau5");
					gameProfile.getProperties().asMap().forEach((k, v) -> v.forEach((p) -> deadmau5GameProfile.getProperties().put(k, WrappedSignedProperty.fromHandle(p))));
					
					PlayerInfoData deadmau5PlayerInfo = new PlayerInfoData(deadmau5GameProfile, 0, NativeGameMode.NOT_SET, WrappedChatComponent.fromText(player.getName()));
					
					PacketContainer addDeadmau5Packet = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
					addDeadmau5Packet.getPlayerInfoAction().write(0, PlayerInfoAction.ADD_PLAYER);
					addDeadmau5Packet.getPlayerInfoDataLists().write(0, Lists.newArrayList(deadmau5PlayerInfo));
					
					PacketContainer deadmau5TeamPacket = new PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM);
					deadmau5TeamPacket.getStrings().write(0, "deadmau5-ears"); //Team name
					deadmau5TeamPacket.getStrings().write(1, "deadmau5-ears"); //Team display name
					deadmau5TeamPacket.getStrings().write(2, ""); //Team prefix
					deadmau5TeamPacket.getStrings().write(3, ""); //Team suffix
					deadmau5TeamPacket.getStrings().write(4, "never"); //Name tag visiblity
					
					deadmau5TeamPacket.getIntegers().write(0, -1); //Color
					deadmau5TeamPacket.getIntegers().write(1, 0); //Type
					deadmau5TeamPacket.getIntegers().write(2, 1); //Pack option data
					
					deadmau5TeamPacket.getSpecificModifier(Collection.class).write(0, Lists.newArrayList("deadmau5"));
					
					PacketContainer spawnSlime = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
					spawnSlime.getIntegers().write(0, -1); //Entity id
					spawnSlime.getIntegers().write(1, 55); //Entity type
					
					spawnSlime.getIntegers().write(2, (int)(other.getLocation().getX() * 32)); //Loc X
					spawnSlime.getIntegers().write(3, (int)(other.getLocation().getY() * 32)); //Loc Y
					spawnSlime.getIntegers().write(4, (int)(other.getLocation().getZ() * 32)); //Loc Z
					
					WrappedDataWatcher slimeDataWatcher = new WrappedDataWatcher();
					slimeDataWatcher.setObject(0, (byte)(1 << 5));
					slimeDataWatcher.setObject(16, (byte)1);
					
					spawnSlime.getDataWatcherModifier().write(0, slimeDataWatcher);
					
					PacketContainer attachSlime = new PacketContainer(PacketType.Play.Server.ATTACH_ENTITY);
					attachSlime.getIntegers().write(0, 0); //Type
					attachSlime.getIntegers().write(1, -1); //Entity id
					attachSlime.getIntegers().write(2, other.getEntityId()); //Vehicle id
					
					PacketContainer spawnArmorStand = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);
					spawnArmorStand.getIntegers().write(0, -2); //Entity id
					
					spawnArmorStand.getIntegers().write(1, (int)(other.getLocation().getX() * 32)); //Loc X
					spawnArmorStand.getIntegers().write(2, (int)(other.getLocation().getY() * 32) + 16); //Loc Y
					spawnArmorStand.getIntegers().write(3, (int)(other.getLocation().getZ() * 32)); //Loc Z
					
					spawnArmorStand.getIntegers().write(9, 78); //Entity type
					
					PacketContainer entityMetaArmorStand = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
					entityMetaArmorStand.getIntegers().write(0, -2); //Entity id
					
					entityMetaArmorStand.getWatchableCollectionModifier().write(0, Lists.newArrayList(new WrappedWatchableObject(2, other.getName()),
							new WrappedWatchableObject(3, (byte)1),
							new WrappedWatchableObject(0, (byte)(1 << 5)),
							new WrappedWatchableObject(10, (byte)16)));
					
					PacketContainer attachArmorStand = new PacketContainer(PacketType.Play.Server.ATTACH_ENTITY);
					attachArmorStand.getIntegers().write(0, 0); //Type
					attachArmorStand.getIntegers().write(1, -2); //Entity id
					attachArmorStand.getIntegers().write(2, -1); //Vehicle id
					
					try
					{
						ProtocolLibrary.getProtocolManager().sendServerPacket(player, addDeadmau5Packet, false);
						ProtocolLibrary.getProtocolManager().sendServerPacket(player, deadmau5TeamPacket, false);
						
						ProtocolLibrary.getProtocolManager().sendServerPacket(player, spawnSlime, false);
						
						ProtocolLibrary.getProtocolManager().sendServerPacket(player, spawnArmorStand, false);
						ProtocolLibrary.getProtocolManager().sendServerPacket(player, entityMetaArmorStand, false);
						
						event.schedule(new ScheduledPacket(attachSlime, player, false));
						event.schedule(new ScheduledPacket(attachArmorStand, player, false));
					}
					catch (InvocationTargetException e)
					{
						e.printStackTrace();
					}
					finally
					{
						event.schedule(new ScheduledPacket(PacketContainer.fromPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, otherNms)), player, false));
					}
				}
			}
		}
		else if (type.equals(PacketType.Play.Server.ENTITY_DESTROY))
		{
			Map<Integer, NickEntry> nickEntries = nickEntryByEntity.get(player.getUniqueId());
			if (nickEntries != null)
			{
				int[] entityIds = packet.getIntegerArrays().read(0);
				for(int i = 0; i < entityIds.length; i++)
				{
					int entityId = entityIds[i];
					
					NickEntry entry = nickEntries.get(entityId);
					if (entry != null && entry.getDeadmau5NameTagEntites().size() > 0)
					{
						int[] deadmausNameTagEntities = new int[entry.getDeadmau5NameTagEntites().size()];
						for(int j = 0; j < entry.getDeadmau5NameTagEntites().size(); j++)
						{
							deadmausNameTagEntities[j] = entry.getDeadmau5NameTagEntites().get(j);
						}
						
						entry.getDeadmau5NameTagEntites().clear();
						
						try
						{
							ProtocolLibrary.getProtocolManager().sendServerPacket(player, PacketContainer.fromPacket(new PacketPlayOutEntityDestroy(deadmausNameTagEntities)), false);
						}
						catch (InvocationTargetException e)
						{
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}
