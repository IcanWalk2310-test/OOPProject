package game.core;

public class Item {
    private String name;
    private String description;
    private ItemType type;
    private int value;
    private Effect effect;

    public enum ItemType {
        HEALING,
        STAT_BOOST,
        EFFECT_ITEM
    }

    public Item(String name, String description, ItemType type, int value) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.value = value;
    }

    public Item(String name, String description, Effect effect) {
        this.name = name;
        this.description = description;
        this.type = ItemType.EFFECT_ITEM;
        this.value = 0;
        this.effect = effect;
    }

    public void use(Player player) {
        switch (type) {
            case HEALING:
                System.out.println(player.getName() + " used " + name + " and healed " + value + " HP!");
                break;
            case STAT_BOOST:
                player.getStats().increaseStr(value);
                System.out.println(player.getName() + " used " + name + " and gained +" + value + " STR!");
                break;
            case EFFECT_ITEM:
                if (effect != null) {
                    effect.apply(player);
                    player.addEffect(effect);
                }
                break;
        }
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public ItemType getType() { return type; }
    public int getValue() { return value; }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, type);
    }
}
