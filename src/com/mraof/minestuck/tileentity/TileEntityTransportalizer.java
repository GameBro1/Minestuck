package com.mraof.minestuck.tileentity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;

import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.Location;
import com.mraof.minestuck.util.Teleport;

public class TileEntityTransportalizer extends TileEntity
{
	public static HashMap<String, Location> transportalizers = new HashMap<String, Location>();
	private static Random rand = new Random();
	String id = "";
	private String destId = "";
	
	@Override
	public void validate()
	{
		super.validate();
		if(!world.isRemote)
		{
			if(id.isEmpty())
				id = getUnusedId();
			put(id, new Location(this.pos, world.provider.getDimension()));
		}
	}
	
	@Override
	public void invalidate()
	{
		super.invalidate();
		if(!world.isRemote)
		{
			Location location = transportalizers.get(id);
			if(location.equals(new Location(this.pos, this.world.provider.getDimension())))
				transportalizers.remove(id);
		}
	}
	
	public String getUnusedId()
	{
		String unusedId = "";
		do
		{
			for(int i = 0; i < 4; i++)
			{
				unusedId += (char) (rand.nextInt(26) + 'A');
			}
		} 
		while(transportalizers.containsKey(unusedId));

		return unusedId;
	}

	public static void put(String key, Location location)
	{
		transportalizers.put(key, location);
	}
	
	public void teleport(Entity entity)
	{
		Location location = transportalizers.get(this.destId);
		if(location != null && location.pos.getY() != -1)
		{
			WorldServer world = entity.getServer().worldServerForDimension(location.dim);
			TileEntityTransportalizer destTransportalizer = (TileEntityTransportalizer) world.getTileEntity(location.pos);
			if(destTransportalizer == null)
			{
				Debug.warn("Invalid transportalizer in map: " + this.destId + " at " + location);
				transportalizers.remove(this.destId);
				this.destId = "";
				return;
			}
			
			IBlockState block0 = world.getBlockState(location.pos.up());
			IBlockState block1 = world.getBlockState(location.pos.up(2));
			if(block0.getMaterial().blocksMovement() || block1.getMaterial().blocksMovement())
			{
				entity.timeUntilPortal = entity.getPortalCooldown();
				if(entity instanceof EntityPlayerMP)
					((EntityPlayerMP) entity).sendStatusMessage(new TextComponentTranslation("message.transportalizer.destinationBlocked"));
				return;
			}
			
			Teleport.teleportEntity(entity, location.dim, null, destTransportalizer.pos.getX() + 0.5, destTransportalizer.pos.getY() + 0.6, destTransportalizer.pos.getZ() + 0.5);
			entity.timeUntilPortal = entity.getPortalCooldown();
		}
	}
	
	public static void saveTransportalizers(NBTTagCompound tagCompound)
	{
		NBTTagCompound transportalizerTagCompound = new NBTTagCompound();
		Iterator<Map.Entry<String, Location>> it = transportalizers.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry<String, Location> entry = it.next();
			Location location = entry.getValue();
			NBTTagCompound locationTag = new NBTTagCompound();
			locationTag.setInteger("x", location.pos.getX());
			locationTag.setInteger("y", location.pos.getY());
			locationTag.setInteger("z", location.pos.getZ());
			locationTag.setInteger("dim", location.dim);
			transportalizerTagCompound.setTag(entry.getKey(), locationTag);
		}
		tagCompound.setTag("transportalizers", transportalizerTagCompound);
	}
	
	public static void loadTransportalizers(NBTTagCompound tagCompound)
	{
		for(Object id : tagCompound.getKeySet())
		{
			NBTTagCompound locationTag = tagCompound.getCompoundTag((String)id);
			put((String)id, new Location(locationTag.getInteger("x"), locationTag.getInteger("y"), locationTag.getInteger("z"), locationTag.getInteger("dim")));
		}
	}
	
	public String getId()
	{
		return id;
	}

	public String getDestId()
	{
		return destId;
	}

	public void setDestId(String destId)
	{
		this.destId = destId;
		IBlockState state = world.getBlockState(pos);
		this.markDirty();
		world.notifyBlockUpdate(pos, state, state, 0);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		this.destId = tagCompound.getString("destId");
		this.id = tagCompound.getString("idString");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		
		tagCompound.setString("idString", id);
		tagCompound.setString("destId", destId);
		
		return tagCompound;
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
	{
		return this.writeToNBT(new NBTTagCompound());
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(this.pos, 2, this.writeToNBT(new NBTTagCompound()));
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) 
	{
		this.readFromNBT(pkt.getNbtCompound());
	}
	
}