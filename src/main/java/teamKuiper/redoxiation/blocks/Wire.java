package teamKuiper.redoxiation.blocks;


import codechicken.lib.packet.PacketCustom;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import teamKuiper.redoxiation.PacketHandler;
import teamKuiper.redoxiation.Redoxiation;
import teamKuiper.redoxiation.blocks.tileentity.TileWire;
import teamKuiper.redoxiation.multipart.WirePart;

public class Wire extends BlockContainer {

    String name = "wire";

    public Wire() {
        super(Material.rock);
        setBlockName(Redoxiation.MODID + "." + name);
        setBlockTextureName(Redoxiation.MODID + ":" + name);
        setCreativeTab(Redoxiation.tabRedoxiation);
        setHarvestLevel("pickaxe", 2);
        setHardness(2.0F);
        setResistance(10.0F);
    }

    public TileEntity createNewTileEntity(World world, int meta) {
        return  new TileWire();
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
		return side;
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        if(world.isRemote)
            new PacketCustom(PacketHandler.channel, 1).sendToServer();
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side) {
        ForgeDirection dir = ForgeDirection.getOrientation(side).getOpposite();
        return world.isSideSolid(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, dir.getOpposite());
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.isSideSolid(x - 1, y, z, ForgeDirection.EAST) ||
                world.isSideSolid(x + 1, y, z, ForgeDirection.WEST) ||
                world.isSideSolid(x, y, z - 1, ForgeDirection.SOUTH) ||
                world.isSideSolid(x, y, z + 1, ForgeDirection.NORTH) ||
                world.isSideSolid(x, y - 1, z, ForgeDirection.UP) ||
                world.isSideSolid(x, y + 1, z, ForgeDirection.DOWN);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if (!this.canPlaceBlockAt(world, x, y, z)) {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
        } else {
            int l = world.getBlockMetadata(x, y, z);
            if (!canPlaceBlockOnSide(world, x, y, z, l)) {
                this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                world.setBlockToAir(x, y, z);
            }
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        int l = world.getBlockMetadata(x, y, z);
        if (l == 0) {
            this.setBlockBounds(0.3125f, 0.875f, 0.3125f, 0.6875f, 1.0f, 0.6875f);
        } else if (l == 1) {
            this.setBlockBounds(0.3125f, 0.0f, 0.3125f, 0.6875f, 0.125f, 0.6875f);
        } else if (l == 2) {
            this.setBlockBounds(0.3125f, 0.3125f, 0.875f, 0.6875f, 0.6875f, 1.0f);
        } else if (l == 3) {
            this.setBlockBounds(0.3125f, 0.3125f, 0.0f, 0.6875f, 0.6875f, 0.125f);
        } else if (l == 4) {
            this.setBlockBounds(0.875f, 0.3125f, 0.3125f, 1.0f, 0.6875f, 0.6875f);
        } else if (l == 5) {
            this.setBlockBounds(0.0f, 0.3125f, 0.3125f, 0.125f, 0.6875f, 0.6875f);
        }
    }

}
