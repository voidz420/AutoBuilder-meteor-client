package com.voidz.autobuilder.modules;

import com.voidz.autobuilder.AutoBuilderAddon;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class AutoBuilder extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgGrid = settings.createGroup("Build Grid");
    private final SettingGroup sgRender = settings.createGroup("Render");

    // General Settings
    private final Setting<Integer> placementSpeed = sgGeneral.add(new IntSetting.Builder()
        .name("placement-speed")
        .description("Blocks placed per tick.")
        .defaultValue(1)
        .min(1)
        .sliderRange(1, 10)
        .build()
    );

    private final Setting<Boolean> airPlace = sgGeneral.add(new BoolSetting.Builder()
        .name("air-place")
        .description("Allow placing blocks in air without support.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Double> placeRange = sgGeneral.add(new DoubleSetting.Builder()
        .name("place-range")
        .description("Range for placing blocks.")
        .defaultValue(4.5)
        .range(0, 7)
        .sliderRange(0, 7)
        .build()
    );

    private final Setting<Integer> offsetX = sgGeneral.add(new IntSetting.Builder()
        .name("offset-x")
        .description("X offset from player position.")
        .defaultValue(0)
        .sliderRange(-10, 10)
        .build()
    );

    private final Setting<Integer> offsetY = sgGeneral.add(new IntSetting.Builder()
        .name("offset-y")
        .description("Y offset from player position.")
        .defaultValue(0)
        .sliderRange(-10, 10)
        .build()
    );

    private final Setting<Integer> offsetZ = sgGeneral.add(new IntSetting.Builder()
        .name("offset-z")
        .description("Z offset from player position.")
        .defaultValue(0)
        .sliderRange(-10, 10)
        .build()
    );

    private final Setting<List<Block>> blocksToUse = sgGeneral.add(new BlockListSetting.Builder()
        .name("blocks-to-use")
        .description("Blocks to use for building.")
        .defaultValue(Blocks.OBSIDIAN)
        .build()
    );

    // 6x6 Grid Settings (36 toggles)
    private final Setting<Boolean> grid00 = sgGrid.add(new BoolSetting.Builder().name("grid-0-0").defaultValue(false).build());
    private final Setting<Boolean> grid01 = sgGrid.add(new BoolSetting.Builder().name("grid-0-1").defaultValue(false).build());
    private final Setting<Boolean> grid02 = sgGrid.add(new BoolSetting.Builder().name("grid-0-2").defaultValue(false).build());
    private final Setting<Boolean> grid03 = sgGrid.add(new BoolSetting.Builder().name("grid-0-3").defaultValue(false).build());
    private final Setting<Boolean> grid04 = sgGrid.add(new BoolSetting.Builder().name("grid-0-4").defaultValue(false).build());
    private final Setting<Boolean> grid05 = sgGrid.add(new BoolSetting.Builder().name("grid-0-5").defaultValue(false).build());

    private final Setting<Boolean> grid10 = sgGrid.add(new BoolSetting.Builder().name("grid-1-0").defaultValue(false).build());
    private final Setting<Boolean> grid11 = sgGrid.add(new BoolSetting.Builder().name("grid-1-1").defaultValue(false).build());
    private final Setting<Boolean> grid12 = sgGrid.add(new BoolSetting.Builder().name("grid-1-2").defaultValue(false).build());
    private final Setting<Boolean> grid13 = sgGrid.add(new BoolSetting.Builder().name("grid-1-3").defaultValue(false).build());
    private final Setting<Boolean> grid14 = sgGrid.add(new BoolSetting.Builder().name("grid-1-4").defaultValue(false).build());
    private final Setting<Boolean> grid15 = sgGrid.add(new BoolSetting.Builder().name("grid-1-5").defaultValue(false).build());

    private final Setting<Boolean> grid20 = sgGrid.add(new BoolSetting.Builder().name("grid-2-0").defaultValue(false).build());
    private final Setting<Boolean> grid21 = sgGrid.add(new BoolSetting.Builder().name("grid-2-1").defaultValue(false).build());
    private final Setting<Boolean> grid22 = sgGrid.add(new BoolSetting.Builder().name("grid-2-2").defaultValue(false).build());
    private final Setting<Boolean> grid23 = sgGrid.add(new BoolSetting.Builder().name("grid-2-3").defaultValue(false).build());
    private final Setting<Boolean> grid24 = sgGrid.add(new BoolSetting.Builder().name("grid-2-4").defaultValue(false).build());
    private final Setting<Boolean> grid25 = sgGrid.add(new BoolSetting.Builder().name("grid-2-5").defaultValue(false).build());

    private final Setting<Boolean> grid30 = sgGrid.add(new BoolSetting.Builder().name("grid-3-0").defaultValue(false).build());
    private final Setting<Boolean> grid31 = sgGrid.add(new BoolSetting.Builder().name("grid-3-1").defaultValue(false).build());
    private final Setting<Boolean> grid32 = sgGrid.add(new BoolSetting.Builder().name("grid-3-2").defaultValue(false).build());
    private final Setting<Boolean> grid33 = sgGrid.add(new BoolSetting.Builder().name("grid-3-3").defaultValue(false).build());
    private final Setting<Boolean> grid34 = sgGrid.add(new BoolSetting.Builder().name("grid-3-4").defaultValue(false).build());
    private final Setting<Boolean> grid35 = sgGrid.add(new BoolSetting.Builder().name("grid-3-5").defaultValue(false).build());

    private final Setting<Boolean> grid40 = sgGrid.add(new BoolSetting.Builder().name("grid-4-0").defaultValue(false).build());
    private final Setting<Boolean> grid41 = sgGrid.add(new BoolSetting.Builder().name("grid-4-1").defaultValue(false).build());
    private final Setting<Boolean> grid42 = sgGrid.add(new BoolSetting.Builder().name("grid-4-2").defaultValue(false).build());
    private final Setting<Boolean> grid43 = sgGrid.add(new BoolSetting.Builder().name("grid-4-3").defaultValue(false).build());
    private final Setting<Boolean> grid44 = sgGrid.add(new BoolSetting.Builder().name("grid-4-4").defaultValue(false).build());
    private final Setting<Boolean> grid45 = sgGrid.add(new BoolSetting.Builder().name("grid-4-5").defaultValue(false).build());

    private final Setting<Boolean> grid50 = sgGrid.add(new BoolSetting.Builder().name("grid-5-0").defaultValue(false).build());
    private final Setting<Boolean> grid51 = sgGrid.add(new BoolSetting.Builder().name("grid-5-1").defaultValue(false).build());
    private final Setting<Boolean> grid52 = sgGrid.add(new BoolSetting.Builder().name("grid-5-2").defaultValue(false).build());
    private final Setting<Boolean> grid53 = sgGrid.add(new BoolSetting.Builder().name("grid-5-3").defaultValue(false).build());
    private final Setting<Boolean> grid54 = sgGrid.add(new BoolSetting.Builder().name("grid-5-4").defaultValue(false).build());
    private final Setting<Boolean> grid55 = sgGrid.add(new BoolSetting.Builder().name("grid-5-5").defaultValue(false).build());

    // Render Settings
    private final Setting<Boolean> render = sgRender.add(new BoolSetting.Builder()
        .name("render")
        .description("Render block placement preview.")
        .defaultValue(true)
        .build()
    );

    private final Setting<ShapeMode> shapeMode = sgRender.add(new EnumSetting.Builder<ShapeMode>()
        .name("shape-mode")
        .description("How the shapes are rendered.")
        .defaultValue(ShapeMode.Both)
        .build()
    );

    private final Setting<SettingColor> sideColor = sgRender.add(new ColorSetting.Builder()
        .name("side-color")
        .description("Side color of render.")
        .defaultValue(new SettingColor(138, 43, 226, 50))
        .build()
    );

    private final Setting<SettingColor> lineColor = sgRender.add(new ColorSetting.Builder()
        .name("line-color")
        .description("Line color of render.")
        .defaultValue(new SettingColor(138, 43, 226, 255))
        .build()
    );

    private final Setting<Boolean>[][] gridSettings = new Setting[6][6];
    private int tickCounter = 0;

    public AutoBuilder() {
        super(AutoBuilderAddon.CATEGORY, "auto-builder", "Automatically builds patterns based on a 6x6 grid. Made for 2b2t.");
        initGridSettings();
    }

    private void initGridSettings() {
        gridSettings[0][0] = grid00; gridSettings[0][1] = grid01; gridSettings[0][2] = grid02;
        gridSettings[0][3] = grid03; gridSettings[0][4] = grid04; gridSettings[0][5] = grid05;
        gridSettings[1][0] = grid10; gridSettings[1][1] = grid11; gridSettings[1][2] = grid12;
        gridSettings[1][3] = grid13; gridSettings[1][4] = grid14; gridSettings[1][5] = grid15;
        gridSettings[2][0] = grid20; gridSettings[2][1] = grid21; gridSettings[2][2] = grid22;
        gridSettings[2][3] = grid23; gridSettings[2][4] = grid24; gridSettings[2][5] = grid25;
        gridSettings[3][0] = grid30; gridSettings[3][1] = grid31; gridSettings[3][2] = grid32;
        gridSettings[3][3] = grid33; gridSettings[3][4] = grid34; gridSettings[3][5] = grid35;
        gridSettings[4][0] = grid40; gridSettings[4][1] = grid41; gridSettings[4][2] = grid42;
        gridSettings[4][3] = grid43; gridSettings[4][4] = grid44; gridSettings[4][5] = grid45;
        gridSettings[5][0] = grid50; gridSettings[5][1] = grid51; gridSettings[5][2] = grid52;
        gridSettings[5][3] = grid53; gridSettings[5][4] = grid54; gridSettings[5][5] = grid55;
    }


    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (mc.player == null || mc.world == null) return;

        List<BlockPos> toPlace = getBlocksToPlace();
        int placed = 0;

        for (BlockPos pos : toPlace) {
            if (placed >= placementSpeed.get()) break;

            if (!isInRange(pos)) continue;
            if (!mc.world.getBlockState(pos).isReplaceable()) continue;

            FindItemResult block = findBlock();
            if (!block.found()) continue;

            if (airPlace.get()) {
                if (placeBlock(pos, block)) placed++;
            } else {
                if (BlockUtils.place(pos, block, true, 0, true, true)) placed++;
            }
        }
    }

    private List<BlockPos> getBlocksToPlace() {
        List<BlockPos> positions = new ArrayList<>();
        if (mc.player == null) return positions;

        BlockPos playerPos = mc.player.getBlockPos();
        int baseX = playerPos.getX() + offsetX.get() - 2;
        int baseY = playerPos.getY() + offsetY.get();
        int baseZ = playerPos.getZ() + offsetZ.get() - 2;

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                if (gridSettings[row][col].get()) {
                    positions.add(new BlockPos(baseX + col, baseY, baseZ + row));
                }
            }
        }

        return positions;
    }

    private boolean isInRange(BlockPos pos) {
        if (mc.player == null) return false;
        Vec3d playerPos = mc.player.getEyePos();
        Vec3d blockCenter = Vec3d.ofCenter(pos);
        return playerPos.distanceTo(blockCenter) <= placeRange.get();
    }

    private FindItemResult findBlock() {
        return InvUtils.findInHotbar(itemStack -> {
            if (!(itemStack.getItem() instanceof BlockItem blockItem)) return false;
            return blocksToUse.get().contains(blockItem.getBlock());
        });
    }

    private boolean placeBlock(BlockPos pos, FindItemResult block) {
        if (mc.player == null || mc.interactionManager == null || mc.world == null) return false;

        InvUtils.swap(block.slot(), false);

        mc.interactionManager.interactBlock(
            mc.player,
            Hand.MAIN_HAND,
            new net.minecraft.util.hit.BlockHitResult(
                Vec3d.ofCenter(pos),
                Direction.UP,
                pos,
                false
            )
        );

        return true;
    }

    @EventHandler
    private void onRender3D(Render3DEvent event) {
        if (!render.get()) return;

        List<BlockPos> toPlace = getBlocksToPlace();

        for (BlockPos pos : toPlace) {
            if (mc.world != null && mc.world.getBlockState(pos).isReplaceable()) {
                event.renderer.box(pos, sideColor.get(), lineColor.get(), shapeMode.get(), 0);
            }
        }
    }
}
