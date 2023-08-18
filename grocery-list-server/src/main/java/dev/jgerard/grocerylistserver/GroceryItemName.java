package dev.jgerard.grocerylistserver;

public enum GroceryItemName {
    APPLES,
    BANANAS,
    BREAD,
    BUTTER,
    CARROTS,
    PEANUT_BUTTER,
    JELLY,
    MILK,
    EGGS,
    CHEESE,
    CHICKEN,
    BEEF,
    PORK,
    FISH,
    RICE,
    PASTA,
    TOMATOES,
    POTATOES,
    ONIONS,
    PEPPERS;

    // Capitalize name and replace underscores with spaces
    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase().replace('_', ' ');
    }
}
