package com.example.examplemod;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;

/** Client-side state of the hologram. Contains no client-only imports so it is safe in common code. */
public final class ProjectorState {
    public static boolean active = false;
    public static BlockPos anchor = BlockPos.ZERO;
    public static Rotation rotation = Rotation.NONE;
    public static int index = 0;

    private ProjectorState() {}

    public static void place(BlockPos pos, Direction facing) {
        anchor = pos;
        rotation = switch (facing) {
            case EAST -> Rotation.CLOCKWISE_90;
            case SOUTH -> Rotation.CLOCKWISE_180;
            case WEST -> Rotation.COUNTERCLOCKWISE_90;
            default -> Rotation.NONE;
        };
        active = true;
    }

    public static void clear() {
        active = false;
    }

    public static void next() {
        index = (index + 1) % Blueprints.ALL.size();
    }
}
