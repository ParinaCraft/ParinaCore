package fi.joniaromaa.parinacorelibrary.api.nick;

import java.util.UUID;

public interface NickManager
{
	public void nick(UUID uniqueId, String username);
	public void unnick(UUID uniqueId);
	
	public void setDeadmau5Ears(UUID uniqueId, boolean ears);
	
	public boolean hasDeadmau5Ears(UUID uniqueId);
}
