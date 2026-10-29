package com.example.examplemod;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

/** Draws outlines for blocks that are still missing (cyan) or blocked by another block (red). */
@EventBusSubscriber(modid = ExampleMod.MODID, value = Dist.CLIENT)
public class ProjectorRenderer {

    @SubscribeEvent
    public static void onRender(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;
        if (!ProjectorState.active) return;

        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        if (level == null || mc.player == null) return;
        if (!(mc.player.getMainHandItem().getItem() instanceof ProjectorItem)
                && !(mc.player.getOffhandItem().getItem() instanceof ProjectorItem)) {
            return;
        }

        Blueprints.Blueprint bp = Blueprints.ALL.get(ProjectorState.index);
        PoseStack ps = event.getPoseStack();
        Vec3 cam = event.getCamera().getPosition();
        MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
        VertexConsumer vc = buffers.getBuffer(RenderType.lines());

        ps.pushPose();
        ps.translate(-cam.x, -cam.y, -cam.z);
        double e = 0.003;
        for (Blueprints.Entry entry : bp.entries()) {
            BlockPos p = ProjectorState.anchor.offset(entry.pos().rotate(ProjectorState.rotation));
            BlockState expected = entry.state();
            BlockState actual = level.getBlockState(p);
            if (actual.getBlock() == expected.getBlock()) continue; // already built correctly
            boolean blocked = !actual.isAir();
            float r = blocked ? 1.0f : 0.2f;
            float g = blocked ? 0.2f : 0.9f;
            float b = blocked ? 0.2f : 1.0f;
            LevelRenderer.renderLineBox(ps, vc,
                    p.getX() + e, p.getY() + e, p.getZ() + e,
                    p.getX() + 1 - e, p.getY() + 1 - e, p.getZ() + 1 - e,
                    r, g, b, 1.0f);
        }
        ps.popPose();
        buffers.endBatch(RenderType.lines());
    }
}
