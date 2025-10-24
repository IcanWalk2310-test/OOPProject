package game.core;

public class Stat {
    private int str;
    private int agi;
    private int intel;

    private int hp;
    private int dmg;
    private int spd;
    private int evasion;      // percentage
    private int accuracy;     // percentage
    private int flatCooldownReduction; // flat value

    public Stat() {
        str = 5;
        agi = 5;
        intel = 5;
        recalculateStats();
    }

    private void recalculateStats() {
        hp = str * 10;
        dmg = str * 2;
        spd = agi;
        evasion = agi / 5; // +1% evasion per 5 AGI
        accuracy = 100 + (intel / 5) * 2; // +2% accuracy per 5 INT
        flatCooldownReduction = intel / 12; // -1 cooldown per 12 INT
    }

    public void increaseStr(int amount) {
        str += amount;
        recalculateStats();
    }

    public void increaseAgi(int amount) {
        agi += amount;
        recalculateStats();
    }

    public void increaseInt(int amount) {
        intel += amount;
        recalculateStats();
    }

    // --- Getters ---
    public int getStr() { return str; }
    public int getAgi() { return agi; }
    public int getInt() { return intel; }
    public int getHp() { return hp; }
    public int getDmg() { return dmg; }
    public int getSpd() { return spd; }
    public int getEvasion() { return evasion; }
    public int getAccuracy() { return accuracy; }
    public int getFlatCooldownReduction() { return flatCooldownReduction; }

    @Override
    public String toString() {
        return String.format("[STR:%d AGI:%d INT:%d | HP:%d DMG:%d SPD:%d EVA:%d%% ACC:%d%% CDR:%d]",
                str, agi, intel, hp, dmg, spd, evasion, accuracy, flatCooldownReduction);
    }
}
