package game.core;

public class Effect {
    private String name;
    private String description;
    private int duration;
    private EffectType type;
    private int value;
    private StatModifier statModifier;

    public enum EffectType {
        BUFF,
        DEBUFF
    }

    public enum StatModifier {
        STR,
        AGI,
        INT
    }

    public Effect(String name, String description, int duration, EffectType type, StatModifier statModifier, int value) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.type = type;
        this.statModifier = statModifier;
        this.value = value;
    }

    public void apply(Player player) {
        Stat stat = player.getStats();
        int actualValue = (type == EffectType.BUFF) ? value : -value;

        switch (statModifier) {
            case STR: stat.increaseStr(actualValue); break;
            case AGI: stat.increaseAgi(actualValue); break;
            case INT: stat.increaseInt(actualValue); break;
        }

        System.out.println(player.getName() + " gained effect: " + name +
                " (" + description + ", " + actualValue + " " + statModifier + " for " + duration + " turns)");
    }

    public void remove(Player player) {
        Stat stat = player.getStats();
        int actualValue = (type == EffectType.BUFF) ? -value : value;

        switch (statModifier) {
            case STR: stat.increaseStr(actualValue); break;
            case AGI: stat.increaseAgi(actualValue); break;
            case INT: stat.increaseInt(actualValue); break;
        }

        System.out.println(name + " has expired for " + player.getName() + ".");
    }

    public void tick(Player player) {
        if (duration > 0) {
            duration--;
            if (duration == 0) remove(player);
        }
    }

    public boolean isExpired() {
        return duration <= 0;
    }

    public String getName() { return name; }
    public int getDuration() { return duration; }
    public EffectType getType() { return type; }
    public StatModifier getStatModifier() { return statModifier; }
    public int getValue() { return value; }

    @Override
    public String toString() {
        return String.format("%s (%s %s %+d for %d turns)", 
                             name, type, statModifier, value, duration);
    }
}
