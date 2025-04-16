package com.frogrilla.echoes.common.block;

import com.frogrilla.echoes.common.block.entity.CrystalisationTableBlockEntity;
import com.frogrilla.echoes.common.init.EchoesBlockEntities;
import com.frogrilla.echoes.common.init.EchoesTags;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CrystalisationTableBlock extends BlockWithEntity implements BlockEntityProvider {

    public static final MapCodec<CrystalisationTableBlock> CODEC = CrystalisationTableBlock.createCodec(CrystalisationTableBlock::new);
    public CrystalisationTableBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CrystalisationTableBlockEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            ItemStack held = player.getInventory().getMainHandStack();
            CrystalisationTableBlockEntity table = (CrystalisationTableBlockEntity) world.getBlockEntity(pos);
            if (!table.empty()) {
                ItemStack shard = table.pop();
                player.giveOrDropStack(shard);
                table.markDirty();
                world.updateListeners(pos, state, state, 0);
                return ActionResult.CONSUME;
            } else if (!held.isEmpty()) {
                table.setHeldItem(held);
                held.decrement(1);
                table.markDirty();
                world.updateListeners(pos, state, state, 0);
                return ActionResult.CONSUME;
            }
        }
        return ActionResult.PASS;
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if(state.getBlock() != newState.getBlock()){
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if(blockEntity instanceof CrystalisationTableBlockEntity){
                //ItemScatterer.spawn(world, pos, ((CrystalisationTableBlockEntity)blockEntity));
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
}
