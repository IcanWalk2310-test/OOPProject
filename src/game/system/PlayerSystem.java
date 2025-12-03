package game.system;

import game.core.Player;
import game.core.Profession;
import game.core.Stat;

public class PlayerSystem {

    private static final int DEFAULT_STRENGTH = 20;
    private static final int DEFAULT_AGILITY = 20;
    private static final int DEFAULT_INTELLIGENCE = 20;

    private Player player;

    /**
     * Creates a new player ONLY if one does not already exist.
     * Returns the existing player otherwise.
     */
    public Player createPlayer(String name, Profession profession) {
        if (player == null) {
            Stat initialStats = new Stat(DEFAULT_STRENGTH, DEFAULT_AGILITY, DEFAULT_INTELLIGENCE);
            player = new Player(name, profession, initialStats);
        }
        return player;
    }

    /**
     * Creates a new player with custom initial stats.
     * Only creates if one does not already exist.
     */
    public Player createPlayer(String name, Profession profession, int strength, int agility, int intelligence) {
        if (player == null) {
            Stat initialStats = new Stat(strength, agility, intelligence);
            player = new Player(name, profession, initialStats);
        }
        return player;
    }

    /**
     * Gets the active player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets (or replaces) the saved player.
     * Used by SaveLoadSystem.
     */
    public void setPlayer(Player loadedPlayer) {
        this.player = loadedPlayer;
    }

    /**
     * Checks if a player exists.
     */
    public boolean hasPlayer() {
        return player != null;
    }

    /**
     * Resets the player (clears current player).
     * Used for starting a new game.
     */
    public void reset() {
        this.player = null;
    }
}