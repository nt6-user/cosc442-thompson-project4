
package game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CombatRulesTest {

    private int maxHp;

    @BeforeEach
    void setUp() {
        
        maxHp = 100;
    }

    @AfterEach
    void tearDown() {
        maxHp = 0;
    }

    @Test
    void testZeroAmount() {
        
        int currentHp = 50;
        int amount = 0;

        int result = CombatRules.calculateNewHp(currentHp, maxHp, amount);

        assertEquals(50, result);
    }

    @Test
    void testPositiveAmountBelowMaxHp() {
        int currentHp = 50;
        int amount = 20;

        int result = CombatRules.calculateNewHp(currentHp, maxHp, amount);

        assertEquals(70, result);
    }

    @Test
    void testPositiveAmountReachesMaxHp() {
        int currentHp = 80;
        int amount = 20;

        int result = CombatRules.calculateNewHp(currentHp, maxHp, amount);

        assertEquals(100, result);
    }

    @Test
    void testHealingImmediatelyBelowMaxHpDoesNotClamp() {
        int currentHp = 79;
        int amount = 20;

        int result = CombatRules.calculateNewHp(currentHp, maxHp, amount);

        assertEquals(99, result);
    }

    @Test
    void testHealingExactlyAtMaxHpReturnsMaxHp() {
        int currentHp = 79;
        int amount = 21;

        int result = CombatRules.calculateNewHp(currentHp, maxHp, amount);

        assertEquals(100, result);
    }

    @Test
    void testHealingImmediatelyAboveMaxHpIsCapped() {
        int currentHp = 79;
        int amount = 22;

        int result = CombatRules.calculateNewHp(currentHp, maxHp, amount);

        assertEquals(100, result);
    }

    @Test
    void testPositiveAmountExceedsMaxHp() {
        int currentHp = 90;
        int amount = 20;

        int result = CombatRules.calculateNewHp(currentHp, maxHp, amount);

        assertEquals(100, result);
    }

    @Test
    void testNegativeAmountWhileAlive() {
        int currentHp = 30;
        int amount = -10;

        int result = CombatRules.calculateNewHp(currentHp, maxHp, amount);

        assertEquals(20, result);
        assertFalse(CombatRules.isDead(result));
    }

    @Test
    void testHpEqualsOneIsAlive() {
        int currentHp = 10;
        int amount = -9;

        int result = CombatRules.calculateNewHp(currentHp, maxHp, amount);

        assertEquals(1, result);
        assertFalse(CombatRules.isDead(result));
    }

    @Test
    void testHpEqualsZeroIsDead() {
        int currentHp = 10;
        int amount = -10;

        int result = CombatRules.calculateNewHp(currentHp, maxHp, amount);

        assertEquals(0, result);
        assertTrue(CombatRules.isDead(result));
    }

    @Test
    void testNegativeHpIsDead() {
        int currentHp = 10;
        int amount = -11;

        int result = CombatRules.calculateNewHp(currentHp, maxHp, amount);

        assertEquals(-1, result);
        assertTrue(CombatRules.isDead(result));
    }

    @Test
    void testIsDeadAtZero() {
        int hp = 0;

        boolean result = CombatRules.isDead(hp);

        assertTrue(result);
    }

    @Test
    void testIsDeadBelowZero() {
        int hp = -1;

        boolean result = CombatRules.isDead(hp);

        assertTrue(result);
    }

    @Test
    void testIsAliveAtOne() {
        int hp = 1;

        boolean result = CombatRules.isDead(hp);

        assertFalse(result);
    }

    @Test
    void testIsAliveAboveOne() {
        int hp = 2;

        boolean result = CombatRules.isDead(hp);

        assertFalse(result);
    }
}
