package fi.joniaromaa.parinacorelibrary.api;

import com.google.common.base.Preconditions;

import lombok.Getter;

public class ParinaCore
{
	@Getter private static ParinaCoreApi api;
	
	public static void setApi(ParinaCoreApi api)
	{
		Preconditions.checkArgument(ParinaCore.api == null, "API has been already set!");
		
		ParinaCore.api = api;
	}
}
