package game.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Player implements Serializable {

    private final String name;
    private final Profession profession;
    private final Stat stats;

    private final Map<String, Integer> skillCooldowns; // key = skill ID

    public Player(String name, Profession profession, Stat stats) {
        this.name = name;
        this.profession = profession;
        this.stats = stats;
        this.skillCooldowns = new HashMap<>();
    }

    // ======== COMBAT METHODS ========

    public void takeDamage(int damage) {
        stats.takeDamage(damage);
    }

    public boolean isDead() {
        return stats.isDead();
    }

    // ======== SKILL COOLDOWN MANAGEMENT ========

    public int getSkillCooldown(Skill skill) {
        if (skill == null) return 0;
        return skillCooldowns.getOrDefault(skill.getId(), 0);
    }

    public void setSkillCooldown(Skill skill, int cooldown) {
        if (skill == null) return;
        String key = skill.getId();
        if (cooldown <= 0) {
            skillCooldowns.remove(key);
        } else {
            skillCooldowns.put(key, cooldown);
        }
    }

    public void tickAllCooldowns() {
        skillCooldowns.replaceAll((key, cd) -> Math.max(0, cd - 1));
        skillCooldowns.entrySet().removeIf(entry -> entry.getValue() <= 0);
    }

    public void resetAllCooldowns() {
        skillCooldowns.clear();
    }

    // ======== GETTERS ========

    public String getName() { return name; }
    public Profession getProfession() { return profession; }
    public Stat getStats() { return stats; }
}