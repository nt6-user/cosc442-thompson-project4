
package edu.towson.cis.cosc442.project4.coffeemaker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoffeeMakerTests {

    private CoffeeMaker coffeeMaker;
    private Recipe recipe;

    @BeforeEach
    void setUp() {
        coffeeMaker = new CoffeeMaker();
        recipe = new Recipe();
        recipe.setName("Latte");
        recipe.setPrice(5);
        recipe.setAmtCoffee(2);
        recipe.setAmtMilk(3);
        recipe.setAmtSugar(1);
        recipe.setAmtChocolate(2);
    }

    @Test
    void constructorCreatesFourEmptyRecipeSlots() {
        Recipe[] recipes = coffeeMaker.getRecipes();

        assertEquals(4, recipes.length);

        for (Recipe r : recipes) {
            assertNull(r.getName());
        }
    }

    @Test
    void addRecipeAddsRecipeSuccessfully() {
        assertTrue(coffeeMaker.addRecipe(recipe));
        assertEquals("Latte",
                coffeeMaker.getRecipeForName("Latte").getName());
    }

    @Test
    void addRecipeRejectsDuplicateRecipe() {
        assertTrue(coffeeMaker.addRecipe(recipe));
        assertFalse(coffeeMaker.addRecipe(recipe));
    }

    @Test
    void addRecipeRejectsRecipeWhenFull() {
        for (int i = 0; i < 4; i++) {
            Recipe r = new Recipe();
            r.setName("Recipe" + i);

            assertTrue(coffeeMaker.addRecipe(r));
        }

        Recipe extra = new Recipe();
        extra.setName("Extra");

        assertFalse(coffeeMaker.addRecipe(extra));
    }

    @Test
    void deleteRecipeReturnsFalseForNull() {
        assertFalse(coffeeMaker.deleteRecipe(null));
    }

    @Test
    void deleteRecipeReturnsFalseForUnknownRecipe() {
        assertFalse(coffeeMaker.deleteRecipe(recipe));
    }

    @Test
    void deleteRecipeReturnsTrueForExistingRecipe() {
        coffeeMaker.addRecipe(recipe);

        assertTrue(coffeeMaker.deleteRecipe(recipe));
    }

    @Test
    void getRecipeForNameReturnsMatchingRecipe() {
        coffeeMaker.addRecipe(recipe);

        assertEquals("Latte",
                coffeeMaker.getRecipeForName("Latte").getName());
    }

    @Test
    void getRecipeForNameReturnsEmptyRecipeForUnknownName() {
        Recipe result = coffeeMaker.getRecipeForName("Unknown");

        assertNull(result.getName());
    }

    @Test
    void checkInventoryReturnsInventory() {
        Inventory inventory = coffeeMaker.checkInventory();

        assertNotNull(inventory);
        assertEquals(15, inventory.getCoffee());
    }

    @Test
    void addInventoryAcceptsNonnegativeAmounts() {
        Inventory inventory = coffeeMaker.checkInventory();

        int initialCoffee = inventory.getCoffee();
        int initialMilk = inventory.getMilk();
        int initialSugar = inventory.getSugar();
        int initialChocolate = inventory.getChocolate();

        assertTrue(coffeeMaker.addInventory(1, 2, 3, 4));

        Inventory updatedInventory = coffeeMaker.checkInventory();

        assertEquals(initialCoffee + 1, updatedInventory.getCoffee());
        assertEquals(initialMilk + 2, updatedInventory.getMilk());
        assertEquals(initialSugar + 3, updatedInventory.getSugar());
        assertEquals(initialChocolate + 4, updatedInventory.getChocolate());
    }

    @Test
    void addInventoryRejectsNegativeCoffee() {
        assertFalse(coffeeMaker.addInventory(-1, 0, 0, 0));
    }

    @Test
    void addInventoryRejectsNegativeMilk() {
        assertFalse(coffeeMaker.addInventory(0, -1, 0, 0));
    }

    @Test
    void addInventoryRejectsNegativeSugar() {
        assertFalse(coffeeMaker.addInventory(0, 0, -1, 0));
    }

    @Test
    void addInventoryRejectsNegativeChocolate() {
        assertFalse(coffeeMaker.addInventory(0, 0, 0, -1));
    }

    @Test
    void makeCoffeeReturnsChangeWhenPaidExactly() {
        int change = coffeeMaker.makeCoffee(recipe, 5);

        assertEquals(0, change);
    }

    @Test
    void makeCoffeeReturnsChangeWhenOverpaid() {
        int change = coffeeMaker.makeCoffee(recipe, 10);

        assertEquals(5, change);
    }

    @Test
    void makeCoffeeReturnsFullPaymentWhenUnderpaid() {
        int change = coffeeMaker.makeCoffee(recipe, 4);

        assertEquals(4, change);
    }

    @Test
    void makeCoffeeReturnsPaymentWhenIngredientsInsufficient() {
        coffeeMaker.checkInventory().setMilk(0);

        int change = coffeeMaker.makeCoffee(recipe, 10);

        assertEquals(10, change);
    }

    @Test
    void makeCoffeeUsesIngredientsWhenSuccessful() {
        Inventory inventory = coffeeMaker.checkInventory();

        int initialCoffee = inventory.getCoffee();
        int initialMilk = inventory.getMilk();
        int initialSugar = inventory.getSugar();
        int initialChocolate = inventory.getChocolate();

        coffeeMaker.makeCoffee(recipe, 5);

        assertEquals(initialCoffee + 2, inventory.getCoffee());
        assertEquals(initialMilk - 3, inventory.getMilk());
        assertEquals(initialSugar - 1, inventory.getSugar());
        assertEquals(initialChocolate - 2, inventory.getChocolate());
    }

    @Test
    void editRecipeReturnsFalseWhenOldRecipeDoesNotExist() {
        Recipe oldRecipe = new Recipe();
        oldRecipe.setName("Missing");

        Recipe newRecipe = new Recipe();
        newRecipe.setName("New");

        assertFalse(coffeeMaker.editRecipe(oldRecipe, newRecipe));
    }

    @Test
    void editRecipeReturnsTrueWhenNewRecipeMatchesExistingRecipe() {
        Recipe existing = new Recipe();
        existing.setName("Latte");

        Recipe replacement = new Recipe();
        replacement.setName("Latte");

        coffeeMaker.addRecipe(existing);

        assertTrue(coffeeMaker.editRecipe(existing, replacement));
    }

    @Test
    void makeCoffeeReturnsPaymentWhenUnderpaid() {
        int payment = 4;

        assertEquals(payment, coffeeMaker.makeCoffee(recipe, payment));
    }

    @Test
    void makeCoffeeReturnsPaymentWhenUnderpaidAndIngredientsInsufficient() {
        coffeeMaker.checkInventory().setMilk(0);

        int payment = 4;

        assertEquals(payment, coffeeMaker.makeCoffee(recipe, payment));
    }

    @Test
    void getRecipeForNameReturnsEmptyRecipeWhenNameIsNull() {
        Recipe result = coffeeMaker.getRecipeForName(null);

        assertNull(result.getName());
    }

    @Test
    void addRecipeAcceptsRecipeWithNullName() {
        Recipe unnamed = new Recipe();

        assertTrue(coffeeMaker.addRecipe(unnamed));
    }

        @Test
    void addInventoryAcceptsZeroCoffee() {
        assertTrue(coffeeMaker.addInventory(0, 1, 1, 1));
    }

    @Test
    void addInventoryAcceptsZeroMilk() {
        assertTrue(coffeeMaker.addInventory(1, 0, 1, 1));
    }

    @Test
    void addInventoryAcceptsZeroSugar() {
        assertTrue(coffeeMaker.addInventory(1, 1, 0, 1));
    }

    @Test
    void addInventoryAcceptsZeroChocolate() {
        assertTrue(coffeeMaker.addInventory(1, 1, 1, 0));
    }

}
