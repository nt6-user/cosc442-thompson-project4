package game;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WorldTest {

    private World world;

    @BeforeEach
    void setUp() {
        Tile[][][] tiles = new Tile[3][3][2];

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                tiles[x][y][0] = Tile.FLOOR;
                tiles[x][y][1] = Tile.FLOOR;
            }
        }

        world = new World(tiles);
    }

    @Test
    void testDimensionsMatchTileArray() {
        assertEquals(3, world.width());
        assertEquals(3, world.height());
        assertEquals(2, world.depth());
    }

    @Test
    void testTileReturnsFloorAtValidMinimumAndMaximumCoordinates() {
        assertEquals(Tile.FLOOR, world.tile(0, 0, 0));
        assertEquals(Tile.FLOOR, world.tile(2, 2, 1));
    }

    @Test
    void testTileReturnsBoundsOutsideEveryCoordinateRange() {
        assertEquals(Tile.BOUNDS, world.tile(-1, 0, 0));
        assertEquals(Tile.BOUNDS, world.tile(3, 0, 0));
        assertEquals(Tile.BOUNDS, world.tile(0, -1, 0));
        assertEquals(Tile.BOUNDS, world.tile(0, 3, 0));
        assertEquals(Tile.BOUNDS, world.tile(0, 0, -1));
        assertEquals(Tile.BOUNDS, world.tile(0, 0, 2));
    }

    @Test
    void testDigChangesDiggableWallToFloor() {
        Tile[][][] tiles = new Tile[2][2][1];
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                tiles[x][y][0] = Tile.WALL;
            }
        }
        World wallWorld = new World(tiles);

        wallWorld.dig(0, 0, 0);

        assertEquals(Tile.FLOOR, wallWorld.tile(0, 0, 0));
    }

    @Test
    void testAddAndRemoveCreatureUpdatesLookup() {
        Creature creature = new Creature(world, '@', Color.GRAY, "josh", 100, 10, 5);
        creature.x = 1;
        creature.y = 1;
        creature.z = 0;

        world.add(creature);

        assertSame(creature, world.creature(1, 1, 0));

        world.remove(creature);

        assertNull(world.creature(1, 1, 0));
    }

    @Test
    void testCreatureLookupReturnsNullForMissingEntity() {
        assertNull(world.creature(0, 0, 0));
        assertNull(world.creature(2, 2, 1));
    }

    @Test
    void testAddAtEmptyLocationPlacesCreatureOnGround() {
        Creature creature = new Creature(world, 'r', Color.RED, "rat", 10, 3, 2);

        world.addAtEmptyLocation(creature, 0);

        assertTrue(world.tile(creature.x, creature.y, creature.z).isGround());
        assertSame(creature, world.creature(creature.x, creature.y, creature.z));
    }

    @Test
    void testAddAtEmptyLocationMovesPastOccupiedCell() {
        Creature first = new Creature(world, 'r', Color.RED, "rat", 10, 3, 2);
        first.x = 0;
        first.y = 0;
        first.z = 0;
        world.add(first);

        Creature second = new Creature(world, 'g', Color.GREEN, "goblin", 10, 3, 2);
        world.addAtEmptyLocation(second, 0);

        assertFalse(second.x == 0 && second.y == 0);
        assertSame(second, world.creature(second.x, second.y, second.z));
    }

    @Test
    void testAddAndRemoveItemAtCoordinates() {
        Item sword = new Item('s', Color.WHITE, "Sword", "Sword");

        world.addAtEmptyLocation(sword, 1);

        int swordX = itemLocationX(sword);
        int swordY = itemLocationY(sword);
        assertSame(sword, world.item(swordX, swordY, 1));

        world.remove(swordX, swordY, 1);

        assertNull(world.item(swordX, swordY, 1));
    }

    @Test
    void testRemoveItemRemovesItFromWorldWithoutRemovingOtherItems() {
        Item first = new Item('s', Color.WHITE, "Sword", "Sword");
        Item second = new Item('p', Color.BLUE, "Potion", "Potion");
        world.addAtEmptyLocation(first, 0);
        world.addAtEmptyLocation(second, 1);

        world.remove(first);

        assertEquals(second, findItem(second));
        assertNull(findItem(first));
    }

    @Test
    void testAddAtEmptySpacePlacesItemAtRequestedLocation() {
        Item potion = new Item('p', Color.BLUE, "Potion", "Potion");

        assertTrue(world.addAtEmptySpace(potion, 1, 1, 0));
        assertSame(potion, world.item(1, 1, 0));
    }

    @Test
    void testAddAtEmptySpaceFallsBackToNearestOpenLocation() {
        Item existing = new Item('s', Color.WHITE, "Sword", "Sword");
        Item incoming = new Item('p', Color.BLUE, "Potion", "Potion");
        world.addAtEmptyLocation(existing, 0);

        int existingX = itemLocationX(existing);
        int existingY = itemLocationY(existing);

        assertTrue(world.addAtEmptySpace(incoming, existingX, existingY, 0));
        assertSame(incoming, world.item(itemLocationX(incoming), itemLocationY(incoming), 0));
        assertFalse(itemLocationX(incoming) == existingX && itemLocationY(incoming) == existingY);
    }

    @Test
    void testAddAtEmptySpaceWithNullItemReturnsTrue() {
        assertTrue(world.addAtEmptySpace(null, 1, 1, 0));
        assertNull(world.item(1, 1, 0));
    }

    private int swordLocationX(Item item) {
        return itemLocationX(item);
    }

    private int swordLocationY(Item item) {
        return itemLocationY(item);
    }

    private int itemLocationX(Item item) {
        for (int x = 0; x < world.width(); x++) {
            for (int y = 0; y < world.height(); y++) {
                for (int z = 0; z < world.depth(); z++) {
                    if (world.item(x, y, z) == item) {
                        return x;
                    }
                }
            }
        }
        return -1;
    }

    private int itemLocationY(Item item) {
        for (int x = 0; x < world.width(); x++) {
            for (int y = 0; y < world.height(); y++) {
                for (int z = 0; z < world.depth(); z++) {
                    if (world.item(x, y, z) == item) {
                        return y;
                    }
                }
            }
        }
        return -1;
    }

    private Item findItem(Item item) {
        for (int x = 0; x < world.width(); x++) {
            for (int y = 0; y < world.height(); y++) {
                for (int z = 0; z < world.depth(); z++) {
                    if (world.item(x, y, z) == item) {
                        return item;
                    }
                }
            }
        }
        return null;
    }
}
