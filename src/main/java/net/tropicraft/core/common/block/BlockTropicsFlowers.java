package net.tropicraft.core.common.block;

import java.util.List;

import net.minecraft.block.BlockBush;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.common.enums.TropicraftFlowers;

public class BlockTropicsFlowers extends BlockBush implements ITropicraftBlock {

    public static final PropertyEnum<TropicraftFlowers> VARIANT = PropertyEnum.create("variant", TropicraftFlowers.class);
    
    /** Flower names */
    public String[] names;
    
    @Override
    protected BlockStateContainer createBlockState() {
    	return new BlockStateContainer(this, VARIANT);
    }
    
    @Override
    public String getStateName(IBlockState state) {
        return ((TropicraftFlowers) state.getValue(VARIANT)).getName();
    }
	
	public BlockTropicsFlowers(String[] names) {
		super(Material.PLANTS, MapColor.GREEN);
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, TropicraftFlowers.COMMELINA_DIFFUSA));
		this.names = names;
	}
	
    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {        
        for (int i = 0; i < names.length; i++) {
        	list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, TropicraftFlowers.values()[meta]);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return ((TropicraftFlowers) state.getValue(VARIANT)).ordinal();
    }
    
    @Override
    public int damageDropped(IBlockState state) {
        return this.getMetaFromState(state);
    }

	@Override
	public IProperty[] getProperties() {
		return new IProperty[] {VARIANT};
	}

}