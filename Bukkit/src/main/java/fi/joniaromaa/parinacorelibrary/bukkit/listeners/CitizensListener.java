package fi.joniaromaa.parinacorelibrary.bukkit.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import fi.joniaromaa.parinacorelibrary.bukkit.nms.NmsManager;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.npc.NPC;

public class CitizensListener implements Listener
{
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onNPCSpawnEvent(NPCSpawnEvent event)
	{
		NPC npc = event.getNPC();
		if (npc != null)
		{
			Entity npcEntity = npc.getEntity();
			if (npcEntity instanceof Player)
			{
				NmsManager.getNmsHandler().getEntityHandler().setEntityDataWatcher(npcEntity, 10, Byte.valueOf((byte)127)); //Show skin layers
			}
		}
	}
}
