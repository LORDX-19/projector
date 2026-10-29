package com.example.examplemod;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/** Built-in structures the projector can show. Add your own here. */
public final class Blueprints {
    public record Entry(BlockPos pos, BlockState state) {}

    public record Blueprint(String name, List<Entry> entries) {}

    public static final List<Blueprint> ALL = List.of(platform(), tower(), frame());

    private Blueprints() {}

    private static Blueprint platform() {
        List<Entry> list = new ArrayList<>();
        for (int x = 0; x < 5; x++) {
            for (int z = 0; z < 5; z++) {
                list.add(new Entry(new BlockPos(x, 0, z), Blocks.STONE_BRICKS.defaultBlockState()));
            }
        }
        return new Blueprint("Platform 5x5", list);
    }

    private static Blueprint tower() {
        List<Entry> list = new ArrayList<>();
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 3; x++) {
                for (int z = 0; z < 3; z++) {
                    boolean center = x == 1 && z == 1;
                    if (center && y != 5 && y != 0) continue;
                    list.add(new Entry(new BlockPos(x, y, z), Blocks.COBBLESTONE.defaultBlockState()));
                }
            }
        }
        return new Blueprint("Tower 3x3x6", list);
    }

    private static Blueprint frame() {
        List<Entry> list = new ArrayList<>();
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                for (int z = 0; z < 4; z++) {
                    int edges = (x == 0 || x == 3 ? 1 : 0) + (y == 0 || y == 3 ? 1 : 0) + (z == 0 || z == 3 ? 1 : 0);
                    if (edges >= 2) {
                        list.add(new Entry(new BlockPos(x, y, z), Blocks.OAK_PLANKS.defaultBlockState()));
                    }
                }
            }
        }
        return new Blueprint("Frame 4x4x4", list);
    }
}
