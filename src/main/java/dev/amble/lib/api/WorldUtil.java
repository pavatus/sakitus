package dev.amble.lib.api;

import net.minecraft.block.BlockState;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

import dev.amble.lib.data.DirectedGlobalPos;
import dev.amble.lib.util.ServerLifecycleHooks;

public class WorldUtil {
    private static final int SAFE_RADIUS = 3;

    public static DirectedGlobalPos locateSafe(DirectedGlobalPos cached,
                                               GroundSearch vSearch, boolean hSearch) {
        ServerWorld world = ServerLifecycleHooks.get().getWorld(cached.getDimension());
        BlockPos pos = cached.getPos();

        if (isSafe(world, pos))
            return cached;

        if (hSearch) {
            BlockPos temp = findSafeXZ(world, pos, SAFE_RADIUS);

            if (temp != null)
                return cached.pos(temp);
        }

        int x = pos.getX();
        int z = pos.getZ();

        int y = switch (vSearch) {
            case CEILING -> findSafeTopY(world, pos);
            case FLOOR -> findSafeBottomY(world, pos);
            case MEDIAN -> findSafeMedianY(world, pos);
            case NONE -> pos.getY();
        };

        return cached.pos(x, y, z);
    }

    private static BlockPos findSafeXZ(ServerWorld world, BlockPos original, int radius) {
        BlockPos.Mutable pos = original.mutableCopy();

        int minX = pos.getX() - radius;
        int maxX = pos.getX() + radius;

        int minZ = pos.getZ() - radius;
        int maxZ = pos.getZ() + radius;

        for (int x = minX; x < maxX; x++) {
            for (int z = minZ; z < maxZ; z++) {
                pos.setX(x).setZ(z);

                if (isSafe(world, pos))
                    return pos;
            }
        }

        return null;
    }

    private static int findSafeMedianY(ServerWorld world, BlockPos pos) {
        BlockPos upCursor = pos;
        BlockState floorUp = world.getBlockState(upCursor.down());
        BlockState curUp = world.getBlockState(upCursor);
        BlockState aboveUp = world.getBlockState(upCursor.up());

        BlockPos downCursor = pos;
        BlockState floorDown = world.getBlockState(downCursor.down());
        BlockState curDown = world.getBlockState(downCursor);
        BlockState aboveDown = world.getBlockState(downCursor.up());

        while (true) {
            boolean canGoUp = upCursor.getY() < world.getTopY();
            boolean canGoDown = downCursor.getY() > world.getBottomY();

            if (!canGoUp && !canGoDown)
                return pos.getY();

            if (canGoUp) {
                if (isSafe(floorUp, curUp, aboveUp))
                    return upCursor.getY() - 1;

                upCursor = upCursor.up();

                floorUp = curUp;
                curUp = aboveUp;
                aboveUp = world.getBlockState(upCursor);
            }

            if (canGoDown) {
                if (isSafe(floorDown, curDown, aboveDown))
                    return downCursor.getY() + 1;

                downCursor = downCursor.down();

                curDown = aboveDown;
                aboveDown = floorDown;
                floorDown = world.getBlockState(downCursor);
            }
        }
    }

    private static int findSafeBottomY(ServerWorld world, BlockPos pos) {
        BlockPos cursor = pos.withY(world.getBottomY() + 2);

        BlockState floor = world.getBlockState(cursor.down());
        BlockState current = world.getBlockState(cursor);
        BlockState above = world.getBlockState(cursor.up());

        while (true) {
            if (cursor.getY() > world.getTopY())
                return pos.getY();

            if (isSafe(floor, current, above))
                return cursor.getY() - 1;

            cursor = cursor.up();

            floor = current;
            current = above;
            above = world.getBlockState(cursor);
        }
    }

    private static int findSafeTopY(ServerWorld world, BlockPos pos) {
        int x = pos.getX();
        int z = pos.getZ();

        return world.getChunk(ChunkSectionPos.getSectionCoord(x), ChunkSectionPos.getSectionCoord(z))
                .sampleHeightmap(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x & 15, z & 15) + 1;
    }

    private static boolean isSafe(BlockState floor, BlockState block1, BlockState block2) {
        return isFloor(floor) && !block1.blocksMovement() && !block2.blocksMovement();
    }

    private static boolean isSafe(BlockState block1, BlockState block2) {
        return !block1.blocksMovement() && !block2.blocksMovement();
    }

    private static boolean isFloor(BlockState floor) {
        return floor.blocksMovement();
    }

    private static boolean isSafe(World world, BlockPos pos) {
        BlockState floor = world.getBlockState(pos.down());

        if (!isFloor(floor))
            return false;

        BlockState curUp = world.getBlockState(pos);
        BlockState aboveUp = world.getBlockState(pos.up());

        return isSafe(curUp, aboveUp);
    }

    public enum GroundSearch implements StringIdentifiable {
        NONE {
            @Override
            public GroundSearch next() {
                return FLOOR;
            }
        },
        FLOOR {
            @Override
            public GroundSearch next() {
                return CEILING;
            }
        },
        CEILING {
            @Override
            public GroundSearch next() {
                return MEDIAN;
            }
        },
        MEDIAN {
            @Override
            public GroundSearch next() {
                return NONE;
            }
        };

        @Override
        public String asString() {
            return toString();
        }

        public abstract GroundSearch next();
    }

    public static Text worldText(RegistryKey<World> key) {
        return Text.translatableWithFallback(key.getValue().toTranslationKey("dimension"), fakeTranslate(key));
    }

    private static String fakeTranslate(RegistryKey<World> id) {
        return fakeTranslate(id.getValue());
    }

    private static String fakeTranslate(Identifier id) {
        return fakeTranslate(id.getPath());
    }

    private static String fakeTranslate(String path) {
        // Split the string into words
        String[] words = path.split("_");

        // Capitalize the first letter of each word
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
        }

        // Join the words back together with spaces
        return String.join(" ", words);
    }
}