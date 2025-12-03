package game.core;

import java.io.Serializable;
import java.util.UUID;

public class Skill implements Serializable {

    private final String id; // unique immutable ID
    private final String name;
    private final Profession allowedProfession;
    private final int baseDamage;
    private final int baseCooldown;

    public Skill(String name, Profession allowedProfession, int baseDamage, int baseCooldown) {
        this.id = UUID.randomUUID().toString(); // generate unique ID
        this.name = name;
        this.allowedProfession = allowedProfession;
        this.baseDamage = baseDamage;
        this.baseCooldown = baseCooldown;
    }

    // ======== USAGE LOGIC =========

    public boolean canUse(Player player) {
        if (player == null) return false;

        if (allowedProfession != null && player.getProfession() != allowedProfession)
            return false;

        return player.getSkillCooldown(this) == 0;
    }

    public int use(Player player, Enemy enemy) {
        if (!canUse(player)) return 0;

        int damage = baseDamage + player.getStats().getStrength();
        enemy.takeDamage(damage);

        if (baseCooldown > 0) {
            int cdr = player.getStats().getCooldownReduction();
            int finalCooldown = Math.max(1, baseCooldown - cdr);
            player.setSkillCooldown(this, finalCooldown);
        } else {
            player.setSkillCooldown(this, 0);
        }

        return damage;
    }

    // ======== GETTERS ========

    public String getId() { return id; }
    public String getName() { return name; }
    public int getBaseDamage() { return baseDamage; }
    public int getBaseCooldown() { return baseCooldown; }
}