package dev.jgerard.ngsgrocerylist;

public enum GroceryItemName {
    APPLES,
    BANANAS,
    BEEF,
    BREAD,
    BUTTER,
    CARROTS,
    CHEESE,
    CHICKEN,
    EGGS,
    FISH,
    JELLY,
    MILK,
    ONIONS,
    PASTA,
    PEANUT_BUTTER,
    PEPPERS,
    PORK,
    POTATOES,
    RICE,
    TOMATOES;

    // Capitalize name and replace underscores with spaces
    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase().replace('_', ' ');
    }
}
