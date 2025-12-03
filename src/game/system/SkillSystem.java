package game.system;

import game.core.Player;
import game.core.Skill;
import game.core.Enemy;

public class SkillSystem {

    public boolean canUseSkill(Player player, Skill skill) {
        if (skill == null || player == null) return false;
        return skill.canUse(player);
    }

    public int useSkill(Player player, Skill skill, Enemy enemy) {
        if (player == null || skill == null || enemy == null) return -1;

        if (!skill.canUse(player)) return -1;

        return skill.use(player, enemy);
    }

    public void tickAllCooldowns(Player player) {
        if (player != null) player.tickAllCooldowns();
    }

    public int getSkillCooldown(Player player, Skill skill) {
        if (player == null || skill == null) return 0;

        // Read remaining cooldown from player map
        int remaining = player.getSkillCooldown(skill);
        return remaining;
    }
}