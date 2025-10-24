package game.core;

public class Enemy {
    private String name;
    private Stat stats;

    public Enemy(String name, int str, int agi, int intel) {
        this.name = name;
        this.stats = new Stat();
        for (int i = 0; i < str - 5; i++) stats.increaseStr(1);
        for (int i = 0; i < agi - 5; i++) stats.increaseAgi(1);
        for (int i = 0; i < intel - 5; i++) stats.increaseInt(1);
    }

    public void attack(Player player) {
        int damage = stats.getDmg();
        System.out.println(name + " attacks " + player.getName() + " for " + damage + " damage!");
    }

    public String getName() { return name; }
    public Stat getStats() { return stats; }

    @Override
    public String toString() {
        return String.format("Enemy{name='%s', stats=%s}", name, stats);
    }
}
