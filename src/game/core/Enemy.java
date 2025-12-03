package game.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Enemy implements Serializable {

    private final String name;
    private final Stat stats;
    private final Skill[] skills;

    private final Map<String, Integer> skillCooldowns;

    public Enemy(String name, Stat stats) {
        this(name, stats, null);
    }

    public Enemy(String name, Stat stats, Skill[] skills) {
        this.name = name;
        this.stats = stats;
        this.skills = skills;
        this.skillCooldowns = new HashMap<>();
    }

    public void takeDamage(int damage) {
        stats.takeDamage(damage);
    }

    public boolean isDead() {
        return stats.isDead();
    }

    public int getSkillCooldown(Skill skill) {
        if (skill == null) return 0;
        return skillCooldowns.getOrDefault(skill.getName(), 0);
    }

    public void setSkillCooldown(Skill skill, int cooldown) {
        if (skill == null) return;

        String key = skill.getName();
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

    public String getName() { return name; }
    public Stat getStats() { return stats; }
    public Skill[] getSkills() { return skills; }
    public boolean hasSkills() { return skills != null && skills.length > 0; }
}