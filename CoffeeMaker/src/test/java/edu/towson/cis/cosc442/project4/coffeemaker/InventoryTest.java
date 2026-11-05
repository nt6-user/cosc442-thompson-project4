
package edu.towson.cis.cosc442.project4.coffeemaker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InventoryTest {

    private Inventory inventory;

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
    }

    @Test
    void constructorInitializesAllIngredientsTo15() {
        assertEquals(15, inventory.getCoffee());
        assertEquals(15, inventory.getMilk());
        assertEquals(15, inventory.getSugar());
        assertEquals(15, inventory.getChocolate());
    }

    @Test
    void setCoffeeAcceptsZero() {
        inventory.setCoffee(0);
        assertEquals(0, inventory.getCoffee());
    }

    @Test
    void setCoffeeAcceptsPositiveValue() {
        inventory.setCoffee(20);
        assertEquals(20, inventory.getCoffee());
    }

    @Test
    void setCoffeeRejectsNegativeValue() {
        inventory.setCoffee(-1);
        assertEquals(0, inventory.getCoffee());
    }

    @Test
    void setMilkAcceptsZero() {
        inventory.setMilk(0);
        assertEquals(0, inventory.getMilk());
    }

    @Test
    void setMilkAcceptsPositiveValue() {
        inventory.setMilk(20);
        assertEquals(20, inventory.getMilk());
    }

    @Test
    void setMilkRejectsNegativeValue() {
        inventory.setMilk(-1);
        assertEquals(0, inventory.getMilk());
    }

    @Test
    void setSugarAcceptsZero() {
        inventory.setSugar(0);
        assertEquals(0, inventory.getSugar());
    }

    @Test
    void setSugarAcceptsPositiveValue() {
        inventory.setSugar(20);
        assertEquals(20, inventory.getSugar());
    }

    @Test
    void setSugarRejectsNegativeValue() {
        inventory.setSugar(-1);
        assertEquals(0, inventory.getSugar());
    }

    @Test
    void setChocolateAcceptsZero() {
        inventory.setChocolate(0);
        assertEquals(0, inventory.getChocolate());
    }

    @Test
    void setChocolateAcceptsPositiveValue() {
        inventory.setChocolate(20);
        assertEquals(20, inventory.getChocolate());
    }

    @Test
    void setChocolateRejectsNegativeValue() {
        inventory.setChocolate(-1);
        assertEquals(0, inventory.getChocolate());
    }

    @Test
    void enoughIngredientsReturnsTrueWhenAllIngredientsAvailable() {
        Recipe recipe = new Recipe();

        recipe.setAmtCoffee(10);
        recipe.setAmtMilk(10);
        recipe.setAmtSugar(10);
        recipe.setAmtChocolate(10);

        assertTrue(inventory.enoughIngredients(recipe));
    }

    @Test
    void enoughIngredientsReturnsTrueAtExactBoundary() {
        Recipe recipe = new Recipe();

        recipe.setAmtCoffee(15);
        recipe.setAmtMilk(15);
        recipe.setAmtSugar(15);
        recipe.setAmtChocolate(15);

        assertTrue(inventory.enoughIngredients(recipe));
    }

    @Test
    void enoughIngredientsReturnsFalseWhenCoffeeInsufficient() {
        Recipe recipe = new Recipe();
        recipe.setAmtCoffee(16);

        assertFalse(inventory.enoughIngredients(recipe));
    }

    @Test
    void enoughIngredientsReturnsFalseWhenMilkInsufficient() {
        Recipe recipe = new Recipe();
        recipe.setAmtMilk(16);

        assertFalse(inventory.enoughIngredients(recipe));
    }

    @Test
    void enoughIngredientsReturnsFalseWhenSugarInsufficient() {
        Recipe recipe = new Recipe();
        recipe.setAmtSugar(16);

        assertFalse(inventory.enoughIngredients(recipe));
    }

    @Test
    void enoughIngredientsReturnsFalseWhenChocolateInsufficient() {
        Recipe recipe = new Recipe();
        recipe.setAmtChocolate(16);

        assertFalse(inventory.enoughIngredients(recipe));
    }

    @Test
    void enoughIngredientsReturnsTrueForZeroIngredientRecipe() {
        Recipe recipe = new Recipe();

        assertTrue(inventory.enoughIngredients(recipe));
    }

    @Test
    void toStringReturnsFormattedInventory() {
        String expected =
                "Coffee: 15\n" +
                "Milk: 15\n" +
                "Sugar: 15\n" +
                "Chocolate: 15\n";

        assertEquals(expected, inventory.toString());
    }

    @Test
    void enoughIngredientsReturnsFalseWhenAllIngredientsInsufficient() {
        Recipe recipe = new Recipe();

        recipe.setAmtCoffee(16);
        recipe.setAmtMilk(16);
        recipe.setAmtSugar(16);
        recipe.setAmtChocolate(16);

        assertFalse(inventory.enoughIngredients(recipe));
    }

    @Test
    void enoughIngredientsReturnsFalseWhenMultipleIngredientsInsufficient() {
        Recipe recipe = new Recipe();

        recipe.setAmtCoffee(16);
        recipe.setAmtMilk(16);

        assertFalse(inventory.enoughIngredients(recipe));
    }

    @Test
    void enoughIngredientsReturnsFalseWhenSugarAndChocolateInsufficient() {
        Recipe recipe = new Recipe();

        recipe.setAmtSugar(16);
        recipe.setAmtChocolate(16);

        assertFalse(inventory.enoughIngredients(recipe));
    }
}
