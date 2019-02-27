package fi.joniaromaa.parinacorelibrary.api.nick;

import java.util.UUID;

import javax.annotation.Nonnull;

public interface NickManager
{
	public void nick(@Nonnull UUID uniqueId, @Nonnull String username);
	public void unnick(@Nonnull UUID uniqueId);
	
	public void setDeadmau5Ears(@Nonnull UUID uniqueId, boolean ears);
	
	public boolean hasDeadmau5Ears(@Nonnull UUID uniqueId);
}
