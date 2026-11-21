package game;

import java.awt.Color;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PathFinderTest {

    private World world;
    private Creature creature;
    private PathFinder pathFinder;

    @BeforeEach
    void setUp() {
        world = createWorld(5, 5, 1);
        creature = new Creature(world, '@', Color.GRAY, "josh", 100, 10, 5);
        creature.x = 0;
        creature.y = 0;
        creature.z = 0;
        world.add(creature);
        new CreatureAi(creature);
        pathFinder = new PathFinder();
    }

    @Test
    void testFindsPathAcrossOpenWorld() {
        Point start = new Point(0, 0, 0);
        Point end = new Point(4, 4, 0);

        ArrayList<Point> path = pathFinder.findPath(creature, start, end, 100);

        assertNotNull(path);
        assertEquals(end, path.get(path.size() - 1));
        assertValidPath(start, path);
    }

    @Test
    void testReturnsEmptyPathWhenStartEqualsDestination() {
        Point start = new Point(2, 2, 0);

        ArrayList<Point> path = pathFinder.findPath(creature, start, start, 100);

        assertNotNull(path);
        assertTrue(path.isEmpty());
    }

    @Test
    void testCanFindPathAlongWorldBoundary() {
        Point start = new Point(0, 0, 0);
        Point end = new Point(0, 4, 0);

        ArrayList<Point> path = pathFinder.findPath(creature, start, end, 100);

        assertNotNull(path);
        assertEquals(end, path.get(path.size() - 1));
        assertValidPath(start, path);
    }

    @Test
    void testReturnsNullWhenDestinationIsSeparatedByCompleteWall() {
        for (int y = 0; y < 5; y++) {
            worldTile(2, y, 0, Tile.WALL);
        }
        Point start = new Point(1, 2, 0);
        Point end = new Point(3, 2, 0);
        creature.x = 1;
        creature.y = 2;

        ArrayList<Point> path = pathFinder.findPath(creature, start, end, 100);

        assertNull(path);
    }

    @Test
    void testReturnsNullWhenSearchLimitIsZero() {
        ArrayList<Point> path = pathFinder.findPath(
            creature,
            new Point(0, 0, 0),
            new Point(4, 4, 0),
            0
        );

        assertNull(path);
    }

    @Test
    void testSearchLimitCanStopBeforeDestination() {
        ArrayList<Point> path = pathFinder.findPath(
            creature,
            new Point(0, 0, 0),
            new Point(4, 4, 0),
            1
        );

        assertNull(path);
    }

    @Test
    void testReusesFinderForIndependentSearches() {
        ArrayList<Point> first = pathFinder.findPath(
            creature,
            new Point(0, 0, 0),
            new Point(4, 4, 0),
            100
        );
        ArrayList<Point> second = pathFinder.findPath(
            creature,
            new Point(4, 4, 0),
            new Point(0, 0, 0),
            100
        );

        assertNotNull(first);
        assertNotNull(second);
        assertEquals(new Point(4, 4, 0), first.get(first.size() - 1));
        assertEquals(new Point(0, 0, 0), second.get(second.size() - 1));
    }

    private World createWorld(int width, int height, int depth) {
        Tile[][][] tiles = new Tile[width][height][depth];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    tiles[x][y][z] = Tile.FLOOR;
                }
            }
        }
        return new World(tiles);
    }

    private void worldTile(int x, int y, int z, Tile tile) {
        Tile[][][] tiles = getTiles(world);
        tiles[x][y][z] = tile;
    }

    private Tile[][][] getTiles(World targetWorld) {
        return reflectTiles(targetWorld);
    }

    private Tile[][][] reflectTiles(World targetWorld) {
        try {
            java.lang.reflect.Field field = World.class.getDeclaredField("tiles");
            field.setAccessible(true);
            return (Tile[][][]) field.get(targetWorld);
        } catch (ReflectiveOperationException exception) {
            throw new AssertionError("Unable to access World tiles for test setup", exception);
        }
    }

    private void assertValidPath(Point start, ArrayList<Point> path) {
        Point previous = start;
        for (Point point : path) {
            assertTrue(point.x >= 0 && point.x < world.width());
            assertTrue(point.y >= 0 && point.y < world.height());
            assertTrue(point.z >= 0 && point.z < world.depth());
            assertEquals(previous.z, point.z);
            assertTrue(Math.abs(point.x - previous.x) <= 1);
            assertTrue(Math.abs(point.y - previous.y) <= 1);
            previous = point;
        }
    }
}
