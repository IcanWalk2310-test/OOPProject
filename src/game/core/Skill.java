package game.core;

public class Skill {
    private String name;
    private int power;
    private int baseCooldown;
    private int currentCooldown;
    private SkillType type;

    public enum SkillType {
        NORMAL,
        SKILL,
        ULTIMATE
    }

    public Skill(String name, int power, int baseCooldown, SkillType type) {
        this.name = name;
        this.power = power;
        this.baseCooldown = baseCooldown;
        this.currentCooldown = 0;
        this.type = type;
    }

    public boolean isUsable() {
        return currentCooldown == 0;
    }

    public void use(Stat userStats, Stat targetStats) {
        if (!isUsable()) {
            System.out.println(name + " is on cooldown for " + currentCooldown + " more turns!");
            return;
        }

        int damage = userStats.getDmg() + power;
        System.out.println("Used " + name + "! It dealt " + damage + " damage.");
        currentCooldown = Math.max(0, baseCooldown - userStats.getFlatCooldownReduction());
    }

    public void reduceCooldown() {
        if (currentCooldown > 0) currentCooldown--;
    }

    // --- Getters ---
    public String getName() { return name; }
    public int getPower() { return power; }
    public int getBaseCooldown() { return baseCooldown; }
    public int getCurrentCooldown() { return currentCooldown; }
    public SkillType getType() { return type; }

    @Override
    public String toString() {
        return String.format("%s (Power: %d, Cooldown: %d, Type: %s)",
                name, power, baseCooldown, type);
    }
}
