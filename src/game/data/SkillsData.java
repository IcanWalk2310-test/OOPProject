package game.data;

import game.core.Skill;
import game.core.Profession;

public class SkillsData {


    public static Skill[] getSkillsForProfession(Profession profession) {
        return switch (profession) {
            case WARRIOR -> new Skill[] {
                    new Skill("Strike", Profession.WARRIOR, 10, 0),           // Basic attack
                    new Skill("Slash", Profession.WARRIOR, 20, 2),            // Normal skill
                    new Skill("Berserker Rage", Profession.WARRIOR, 35, 3)    // Ultimate
            };
            case MAGE -> new Skill[] {
                    new Skill("Magic Bolt", Profession.MAGE, 8, 0),           // Basic attack
                    new Skill("Fireball", Profession.MAGE, 25, 3),            // Normal skill
                    new Skill("Meteor Strike", Profession.MAGE, 50, 5)        // Ultimate
            };
            case ROGUE -> new Skill[] {
                    new Skill("Quick Stab", Profession.ROGUE, 12, 0),         // Basic attack
                    new Skill("Backstab", Profession.ROGUE, 22, 2),           // Normal skill
                    new Skill("Assassinate", Profession.ROGUE, 40, 3)         // Ultimate
            };
        };
    }
}