package game.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Player {
    private String name;
    private Stat stats;
    private List<Skill> skills;
    private List<Item> inventory;
    private List<Effect> activeEffects;

    public Player(String name) {
        this.name = name;
        this.stats = new Stat();
        this.skills = new ArrayList<>();
        this.inventory = new ArrayList<>();
        this.activeEffects = new ArrayList<>();
    }

    // --- Training actions ---
    public void trainStr() {
        stats.increaseStr(1);
        System.out.println(name + " trained Strength! HP and DMG increased.");
    }

    public void trainAgi() {
        stats.increaseAgi(1);
        System.out.println(name + " trained Agility! SPD and Evasion increased.");
    }

    public void trainInt() {
        stats.increaseInt(1);
        System.out.println(name + " trained Intellect! Accuracy and cooldown reduction increased.");
    }

    // --- Inventory handling ---
    public void addItem(Item item) {
        inventory.add(item);
    }

    public void useItem(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                item.use(this);
                return;
            }
        }
        System.out.println("Item not found: " + itemName);
    }

    // --- Skill management ---
    public void addSkill(Skill skill) {
        skills.add(skill);
    }

    public List<Skill> getSkills() {
        return skills;
    }

    // --- Effect system ---
    public void addEffect(Effect effect) {
        activeEffects.add(effect);
    }

    public void updateEffects() {
        Iterator<Effect> iterator = activeEffects.iterator();
        while (iterator.hasNext()) {
            Effect effect = iterator.next();
            effect.tick(this);
            if (effect.isExpired()) {
                iterator.remove();
            }
        }
    }

    public List<Effect> getActiveEffects() {
        return activeEffects;
    }

    // --- Getters ---
    public String getName() { return name; }
    public Stat getStats() { return stats; }

    @Override
    public String toString() {
        return String.format("Player{name='%s', stats=%s, effects=%d}", 
                             name, stats, activeEffects.size());
    }
}
