package net.tropicraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.tropicraft.core.common.block.tileentity.TileEntityFactory;

public class BlockVolcano extends Block implements ITileEntityProvider {

	public BlockVolcano() {
		super(Material.ROCK);
		this.setBlockUnbreakable();
		this.setResistance(Integer.MAX_VALUE);
		this.setCreativeTab(null);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return TileEntityFactory.getVolcanoTE();
	}

}
