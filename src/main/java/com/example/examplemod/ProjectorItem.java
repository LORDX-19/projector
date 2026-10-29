package com.example.examplemod;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

/**
 * Right-click a block: place the hologram there (facing the way you look).
 * Right-click air: next structure.
 * Shift + right-click: remove the hologram.
 */
public class ProjectorItem extends Item {
    public ProjectorItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        Player player = ctx.getPlayer();
        if (level.isClientSide && player != null) {
            if (player.isShiftKeyDown()) {
                ProjectorState.clear();
                player.displayClientMessage(Component.literal("Projector: hologram removed"), true);
            } else {
                ProjectorState.place(ctx.getClickedPos().relative(ctx.getClickedFace()), ctx.getHorizontalDirection());
                player.displayClientMessage(
                        Component.literal("Projector: " + Blueprints.ALL.get(ProjectorState.index).name()), true);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide) {
            if (player.isShiftKeyDown()) {
                ProjectorState.clear();
                player.displayClientMessage(Component.literal("Projector: hologram removed"), true);
            } else {
                ProjectorState.next();
                player.displayClientMessage(
                        Component.literal("Projector: " + Blueprints.ALL.get(ProjectorState.index).name()), true);
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
