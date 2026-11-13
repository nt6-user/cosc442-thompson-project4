
package game;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreatureTest {

    private World world;
    private Creature creature;

    @BeforeEach
    void setUp() {
        Tile[][][] tiles = new Tile[3][3][1];

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                tiles[x][y][0] = Tile.FLOOR;
            }
        }

        world = new World(tiles);
        creature = new Creature(world, '@', Color.GRAY, "josh", 100, 10, 5);

        creature.x = 1;
        creature.y = 1;
        creature.z = 0;
    }

    @Test
    void testModifyHpDamageWhileAlive() {
        int startingHp = creature.hp();
        int amount = -20;

        creature.modifyHp(amount, "test damage");

        assertEquals(80, creature.hp());
        assertEquals("test damage", creature.causeOfDeath());
    }

    @Test
    void testModifyHpHealingDoesNotExceedMaxHp() {
        creature.modifyHp(-20, "setup damage");
        int amount = 40;

        creature.modifyHp(amount, "test healing");

        assertEquals(100, creature.hp());
    }

    @Test
    void testModifyHpDeathAtZero() {
        int amount = -100;

        creature.modifyHp(amount, "test death");

        assertEquals(0, creature.hp());
        assertEquals("test death", creature.causeOfDeath());
    }
}
