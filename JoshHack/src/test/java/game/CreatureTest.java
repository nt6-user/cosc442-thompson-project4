
package game;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        new CreatureAi(creature);

        creature.x = 1;
        creature.y = 1;
        creature.z = 0;
    }

    @Test
    void testMoveByMovesCreatureAndConsumesFood() {
        int startingFood = creature.food();

        creature.moveBy(1, 0, 0);

        assertEquals(2, creature.x);
        assertEquals(1, creature.y);
        assertEquals(startingFood - 1, creature.food());
    }

    @Test
    void testMoveByIntoWallDoesNotMove() {
        Tile[][][] tiles = new Tile[3][3][1];

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                tiles[x][y][0] = Tile.FLOOR;
            }
        }
        tiles[2][1][0] = Tile.WALL;

        World wallWorld = new World(tiles);
        Creature wallCreature = new Creature(wallWorld, '@', Color.GRAY, "josh", 100, 10, 5);
        new CreatureAi(wallCreature);
        wallCreature.x = 1;
        wallCreature.y = 1;
        wallCreature.z = 0;

        wallCreature.moveBy(1, 0, 0);

        assertEquals(1, wallCreature.x);
        assertEquals(1, wallCreature.y);
    }

    @Test
    void testMoveByCanTravelBetweenStairLevels() {
        Tile[][][] tiles = new Tile[3][3][2];

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                tiles[x][y][0] = Tile.FLOOR;
                tiles[x][y][1] = Tile.FLOOR;
            }
        }
        tiles[1][1][0] = Tile.STAIRS_DOWN;
        tiles[1][1][1] = Tile.STAIRS_UP;

        World stairWorld = new World(tiles);
        Creature stairCreature = new Creature(stairWorld, '@', Color.GRAY, "josh", 100, 10, 5);
        new CreatureAi(stairCreature);
        stairCreature.x = 1;
        stairCreature.y = 1;
        stairCreature.z = 0;

        stairCreature.moveBy(0, 0, 1);

        assertEquals(1, stairCreature.z);

        stairCreature.moveBy(0, 0, -1);

        assertEquals(0, stairCreature.z);
    }

    @Test
    void testMoveByZeroVectorDoesNotConsumeFoodOrMove() {
        int startingFood = creature.food();

        creature.moveBy(0, 0, 0);

        assertEquals(1, creature.x);
        assertEquals(1, creature.y);
        assertEquals(0, creature.z);
        assertEquals(startingFood, creature.food());
    }

    @Test
    void testMeleeAttackDamagesCreatureAndKillsAtZeroHp() {
        world.add(creature);

        Creature target = new Creature(world, 'r', Color.RED, "rat", 10, 10, 10);
        new CreatureAi(target);
        target.x = 2;
        target.y = 1;
        target.z = 0;
        target.modifyHp(-9, "setup damage");
        world.add(target);

        creature.meleeAttack(target);

        assertEquals(0, target.hp());
        assertNull(world.creature(2, 1, 0));
        assertEquals("josh", creature.name());
        assertEquals(29, creature.xp());
        assertEquals(2, creature.level());
    }

    @Test
    void testMeleeAttackAgainstHighDefenseStillDealsOneDamage() {
        world.add(creature);

        Creature target = new Creature(world, 'g', Color.GREEN, "guard", 100, 10, 100);
        new CreatureAi(target);
        target.x = 2;
        target.y = 1;
        target.z = 0;
        world.add(target);

        creature.meleeAttack(target);

        assertEquals(99, target.hp());
    }

    @Test
    void testEquipWeaponAndArmorModifyCombatValues() {
        Item sword = new Item('s', Color.WHITE, "Sword", "Sword");
        sword.modifyAttackValue(5);
        creature.inventory().add(sword);

        creature.equip(sword);

        assertEquals(sword, creature.weapon());
        assertEquals(15, creature.attackValue());
        assertEquals(5, creature.defenseValue());

        Item armor = new Item('[', Color.WHITE, "Armor", "Armor");
        armor.modifyDefenseValue(4);
        creature.inventory().add(armor);

        creature.equip(armor);

        assertEquals(armor, creature.armor());
        assertEquals(9, creature.defenseValue());
    }

    @Test
    void testModifyXpGainsALevelAndAppliesALevelUpBonus() {
        int startingMaxHp = creature.maxHp();
        int startingMaxMana = creature.maxMana();

        creature.modifyXp(26);

        assertEquals(26, creature.xp());
        assertEquals(2, creature.level());
        assertTrue(creature.maxHp() >= startingMaxHp);
        assertTrue(creature.maxMana() >= startingMaxMana);
    }

    @Test
    void testModifyManaStaysWithinZeroAndMaxMana() {
        creature.modifyMana(-100);
        assertEquals(0, creature.mana());

        creature.modifyMana(100);
        assertEquals(creature.maxMana(), creature.mana());
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

    @Test
    void testModifyHpDeathLeavesCorpse() {
        world.add(creature);
        new CreatureAi(creature);

        int amount = -100;

        creature.modifyHp(amount, "test death");

        assertEquals("josh corpse", world.item(1, 1, 0).name());
    }

}
