package com.frogrilla.echoes.common.block.entity;

import com.frogrilla.echoes.common.init.EchoesBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class CrystalisationTableBlockEntity extends BlockEntity {
    public CrystalisationTableBlockEntity(BlockPos pos, BlockState state) {
        super(EchoesBlockEntities.CRYSTALISATION_TABLE_BLOCK_ENTITY, pos, state);
    }
}
