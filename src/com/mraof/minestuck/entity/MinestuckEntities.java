package com.mraof.minestuck.entity;

import com.mraof.minestuck.entity.item.EntityCrewPoster;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.carapacian.EntityBlackBishop;
import com.mraof.minestuck.entity.carapacian.EntityBlackPawn;
import com.mraof.minestuck.entity.carapacian.EntityBlackRook;
import com.mraof.minestuck.entity.carapacian.EntityWhiteBishop;
import com.mraof.minestuck.entity.carapacian.EntityWhitePawn;
import com.mraof.minestuck.entity.carapacian.EntityWhiteRook;
import com.mraof.minestuck.entity.consort.EntityIguana;
import com.mraof.minestuck.entity.consort.EntityNakagator;
import com.mraof.minestuck.entity.consort.EntitySalamander;
import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.entity.item.EntityMetalBoat;
import com.mraof.minestuck.entity.item.EntityVitalityGel;
import com.mraof.minestuck.entity.underling.EntityBasilisk;
import com.mraof.minestuck.entity.underling.EntityGiclops;
import com.mraof.minestuck.entity.underling.EntityImp;
import com.mraof.minestuck.entity.underling.EntityOgre;
import com.mraof.minestuck.entity.underling.EntityWyrm;

public final class MinestuckEntities
{
	public static int currentEntityIdOffset = 0;

	public static void registerEntities()
	{
		//register entities
		registerEntity(EntitySalamander.class, "Salamander");
		registerEntity(EntityNakagator.class, "Nakagator");
		registerEntity(EntityIguana.class, "Iguana");
		registerEntity(EntityImp.class, "Imp");
		registerEntity(EntityOgre.class, "Ogre");
		registerEntity(EntityBasilisk.class, "Basilisk");
		registerEntity(EntityGiclops.class, "Giclops");
		registerEntity(EntityWyrm.class, "Wyrm");
		registerEntity(EntityBlackPawn.class, "DersitePawn");
		registerEntity(EntityWhitePawn.class, "ProspitianPawn");
		registerEntity(EntityBlackBishop.class, "DersiteBishop");
		registerEntity(EntityWhiteBishop.class, "ProspitianBishop");
		registerEntity(EntityBlackRook.class, "DersiteRook");
		registerEntity(EntityWhiteRook.class, "ProspitianRook");
		registerEntity(EntityDecoy.class, "PlayerDecoy");
		registerEntity(EntityMetalBoat.class, "MetalBoat");
		registerEntity(EntityGrist.class, "Grist", 512, 1, true);
		registerEntity(EntityVitalityGel.class, "VitalityGel", 512, 1, true);
		registerEntity(EntityCrewPoster.class, "MidnightCrewPoster");
	}

	//registers entity with forge and minecraft, and increases currentEntityIdOffset by one in order to prevent id collision
	public static void registerEntity(Class<? extends Entity> entityClass, String name)
	{
		registerEntity(entityClass, name, 80, 3, true);
	}

	public static void registerEntity(Class<? extends Entity> entityClass, String name, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates)
	{
		EntityRegistry.registerModEntity(entityClass, name, currentEntityIdOffset, Minestuck.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
		currentEntityIdOffset++;
	}

}
