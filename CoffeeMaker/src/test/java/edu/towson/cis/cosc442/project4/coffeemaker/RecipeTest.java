
package edu.towson.cis.cosc442.project4.coffeemaker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecipeTest {

    private Recipe recipe;

    @BeforeEach
    void setUp() {
        recipe = new Recipe();
    }

    @Test
    void newRecipeHasDefaultValues() {
        assertNull(recipe.getName());
        assertEquals(0, recipe.getPrice());
        assertEquals(0, recipe.getAmtCoffee());
        assertEquals(0, recipe.getAmtMilk());
        assertEquals(0, recipe.getAmtSugar());
        assertEquals(0, recipe.getAmtChocolate());
    }

    @Test
    void setNameStoresName() {
        recipe.setName("Latte");
        assertEquals("Latte", recipe.getName());
    }

    @Test
    void setNameAcceptsNull() {
        recipe.setName("Latte");
        recipe.setName(null);
        assertNull(recipe.getName());
    }

    @Test
    void setPriceAcceptsZero() {
        recipe.setPrice(0);
        assertEquals(0, recipe.getPrice());
    }

    @Test
    void setPriceAcceptsPositiveValue() {
        recipe.setPrice(10);
        assertEquals(10, recipe.getPrice());
    }

    @Test
    void setPriceRejectsNegativeValue() {
        recipe.setPrice(-1);
        assertEquals(0, recipe.getPrice());
    }

    @Test
    void setPriceRejectsNegativeBoundaryValue() {
        recipe.setPrice(-100);
        assertEquals(0, recipe.getPrice());
    }

    @Test
    void setAmtCoffeeAcceptsPositiveValue() {
        recipe.setAmtCoffee(5);
        assertEquals(5, recipe.getAmtCoffee());
    }

    @Test
    void setAmtCoffeeRejectsNegativeValue() {
        recipe.setAmtCoffee(-1);
        assertEquals(0, recipe.getAmtCoffee());
    }

    @Test
    void setAmtMilkAcceptsPositiveValue() {
        recipe.setAmtMilk(5);
        assertEquals(5, recipe.getAmtMilk());
    }

    @Test
    void setAmtMilkRejectsNegativeValue() {
        recipe.setAmtMilk(-1);
        assertEquals(0, recipe.getAmtMilk());
    }

    @Test
    void setAmtSugarAcceptsPositiveValue() {
        recipe.setAmtSugar(5);
        assertEquals(5, recipe.getAmtSugar());
    }

    @Test
    void setAmtSugarRejectsNegativeValue() {
        recipe.setAmtSugar(-1);
        assertEquals(0, recipe.getAmtSugar());
    }

    @Test
    void setAmtChocolateAcceptsPositiveValue() {
        recipe.setAmtChocolate(5);
        assertEquals(5, recipe.getAmtChocolate());
    }

    @Test
    void setAmtChocolateRejectsNegativeValue() {
        recipe.setAmtChocolate(-1);
        assertEquals(0, recipe.getAmtChocolate());
    }

    @Test
    void equalsReturnsTrueForMatchingNames() {
        Recipe other = new Recipe();

        recipe.setName("Latte");
        other.setName("Latte");

        assertTrue(recipe.equals(other));
    }

    @Test
    void equalsReturnsFalseForDifferentNames() {
        Recipe other = new Recipe();

        recipe.setName("Latte");
        other.setName("Mocha");

        assertFalse(recipe.equals(other));
    }

    @Test
    void equalsReturnsFalseWhenThisNameIsNull() {
        Recipe other = new Recipe();
        other.setName("Latte");

        assertFalse(recipe.equals(other));
    }

    @Test
    void equalsReturnsFalseWhenOtherNameIsNull() {
        recipe.setName("Latte");
        Recipe other = new Recipe();

        assertFalse(recipe.equals(other));
    }

    @Test
    void toStringReturnsRecipeName() {
        recipe.setName("Latte");
        assertEquals("Latte", recipe.toString());
    }

    @Test
    void toStringReturnsNullWhenNameIsNull() {
        assertNull(recipe.toString());
    }

    @Test
    void setAmtCoffeeAcceptsZero() {
        recipe.setAmtCoffee(0);
        assertEquals(0, recipe.getAmtCoffee());
    }

    @Test
    void setAmtMilkAcceptsZero() {
        recipe.setAmtMilk(0);
        assertEquals(0, recipe.getAmtMilk());
    }

    @Test
    void setAmtSugarAcceptsZero() {
        recipe.setAmtSugar(0);
        assertEquals(0, recipe.getAmtSugar());
    }

    @Test
    void setAmtChocolateAcceptsZero() {
        recipe.setAmtChocolate(0);
        assertEquals(0, recipe.getAmtChocolate());
    }

}

