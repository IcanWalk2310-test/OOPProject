package game.core;

import java.io.Serializable;

public class Stat implements Serializable {

    // ===== BALANCED STAT CONSTANTS =====
    private static final int BASE_HP = 60;
    private static final int HP_PER_STRENGTH = 3;  // balanced for +60â€“100 STR builds

    // Evasion & accuracy now use diminishing returns to prevent broken scaling
    private static final double EVASION_SCALER = 1.8;
    private static final double ACCURACY_SCALER = 2.2;
    private static final int BASE_ACCURACY = 75;

    // CDR safely scales with sqrt so it can't break cooldowns
    private static final double CDR_SCALER = 0.5;

    // Speed impacts turn order, Etheria-like feeling
    private static final double SPEED_SCALER = 1.5;

    private int strength;
    private int agility;
    private int intelligence;

    private int maxHp;
    private int hp;

    private int evasion;
    private int accuracy;
    private int cooldownReduction;
    private int speed;

    public Stat(int strength, int agility, int intelligence) {
        this.strength = Math.max(0, strength);
        this.agility = Math.max(0, agility);
        this.intelligence = Math.max(0, intelligence);
        calculateDerivedStats();
        this.hp = maxHp;
    }

    // ===== NEW BALANCED FORMULAS =====
    private void calculateDerivedStats() {
        this.maxHp = BASE_HP + strength * HP_PER_STRENGTH;

        // Diminishing returns â€” no infinite dodge builds
        this.evasion = (int)(Math.sqrt(agility) * EVASION_SCALER);

        // Accuracy soft cap â€” INT matters but does not hit 150%
        this.accuracy = BASE_ACCURACY + (int)(Math.sqrt(intelligence) * ACCURACY_SCALER);

     // Cooldown reduction uses sqrt scaling â€” cannot break cooldowns
     // Base INT (20) gives 0 CDR, only training INT increases CDR
     int intelligenceAboveBase = Math.max(0, intelligence - 20);
     this.cooldownReduction = (int)(Math.sqrt(intelligenceAboveBase) * CDR_SCALER);

        // Faster turn order for AGI builds
        this.speed = (int)(Math.sqrt(agility) * SPEED_SCALER);
    }

    public void increaseStrength(int amount) {
        if (amount == 0) return;

        int oldMaxHp = maxHp;
        strength = Math.max(0, strength + amount);
        calculateDerivedStats();

        int hpGain = maxHp - oldMaxHp;
        hp = Math.max(0, Math.min(maxHp, hp + hpGain));
    }

    public void increaseAgility(int amount) {
        if (amount == 0) return;
        agility = Math.max(0, agility + amount);
        calculateDerivedStats();
    }

    public void increaseIntelligence(int amount) {
        if (amount == 0) return;
        intelligence = Math.max(0, intelligence + amount);
        calculateDerivedStats();
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public void takeDamage(int damage) {
        if (damage < 0) return;
        hp = Math.max(0, hp - damage);
    }

    public void fullHeal() {
        hp = maxHp;
    }

    // ===== GETTERS =====
    public int getStrength() { return strength; }
    public int getAgility() { return agility; }
    public int getIntelligence() { return intelligence; }

    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getEvasion() { return evasion; }
    public int getAccuracy() { return accuracy; }
    public int getCooldownReduction() { return cooldownReduction; }
    public int getSpeed() { return speed; }
}