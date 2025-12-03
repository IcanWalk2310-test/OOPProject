package game.system;

import java.io.*;

/**
 * Lightweight session tracking system.
 * Replaces the full save/load system with simple statistics tracking.
 * Perfect for short roguelike games that don't need mid-game saves.
 * 
 * Statistics file saved to: src/game/data/game_stats.dat
 */
public class SessionTracker {
    
    private static final String STATS_FILE = "src/game/data/game_stats.dat";
    
    private int gamesPlayed;
    private int wins;
    private int losses;
    private String bestProfession;
    private int highestStrength;
    private int highestAgility;
    private int highestIntelligence;
    
    public SessionTracker() {
        loadStats();
    }
    
    /**
     * Load statistics from file.
     * Creates new stats if file doesn't exist (first time playing).
     */
    public void loadStats() {
        File file = new File(STATS_FILE);
        
        if (!file.exists()) {
            // First time playing - initialize fresh stats
            resetStats();
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(STATS_FILE))) {
            gamesPlayed = Integer.parseInt(reader.readLine());
            wins = Integer.parseInt(reader.readLine());
            losses = Integer.parseInt(reader.readLine());
            bestProfession = reader.readLine();
            highestStrength = Integer.parseInt(reader.readLine());
            highestAgility = Integer.parseInt(reader.readLine());
            highestIntelligence = Integer.parseInt(reader.readLine());
        } catch (IOException | NumberFormatException e) {
            // Corrupted file - start fresh
            System.err.println("Could not load statistics (starting fresh): " + e.getMessage());
            resetStats();
        }
    }
    
    /**
     * Save statistics to file.
     */
    public void saveStats() {
        // Ensure directory exists
        File file = new File(STATS_FILE);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(STATS_FILE))) {
            writer.println(gamesPlayed);
            writer.println(wins);
            writer.println(losses);
            writer.println(bestProfession);
            writer.println(highestStrength);
            writer.println(highestAgility);
            writer.println(highestIntelligence);
        } catch (IOException e) {
            System.err.println("Could not save statistics: " + e.getMessage());
        }
    }
    
    /**
     * Record a completed game.
     */
    public void recordGame(boolean won, String profession, int finalStr, int finalAgi, int finalInt) {
        gamesPlayed++;
        
        if (won) {
            wins++;
            
            // Track personal best stats
            int totalStats = finalStr + finalAgi + finalInt;
            int bestStats = highestStrength + highestAgility + highestIntelligence;
            
            if (totalStats > bestStats) {
                highestStrength = finalStr;
                highestAgility = finalAgi;
                highestIntelligence = finalInt;
                bestProfession = profession;
            }
        } else {
            losses++;
        }
        
        saveStats();
    }
    
    /**
     * Display statistics to console.
     */
    public void printStats() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("YOUR LEGEND");
        System.out.println("=".repeat(60));
        
        if (gamesPlayed == 0) {
            System.out.println("No games played yet. Your journey begins now!");
            return;
        }
        
        System.out.println("Games Played: " + gamesPlayed);
        System.out.println("Victories: " + wins);
        System.out.println("Defeats: " + losses);
        
        if (gamesPlayed > 0) {
            double winRate = (wins * 100.0) / gamesPlayed;
            System.out.println("Win Rate: " + String.format("%.1f%%", winRate));
        }
        
        if (wins > 0) {
            System.out.println("\nBest Victorious Run:");
            System.out.println("  Profession: " + bestProfession);
            System.out.println("  Final Stats: STR=" + highestStrength + 
                             ", AGI=" + highestAgility + 
                             ", INT=" + highestIntelligence);
        }
        
        System.out.println("=".repeat(60));
    }
    
    /**
     * Reset all statistics (for testing or player request).
     */
    public void resetStats() {
        gamesPlayed = 0;
        wins = 0;
        losses = 0;
        bestProfession = "None";
        highestStrength = 0;
        highestAgility = 0;
        highestIntelligence = 0;
    }
    
    /**
     * Check if player has ever won.
     */
    public boolean hasWonBefore() {
        return wins > 0;
    }
    
    /**
     * Get motivational message based on performance.
     */
    public String getMotivationalMessage() {
        if (gamesPlayed == 0) {
            return "Welcome, hero! Your legend begins now.";
        }
        
        if (wins == 0) {
            return "Keep trying! Every defeat teaches valuable lessons.";
        }
        
        double winRate = (wins * 100.0) / gamesPlayed;
        
        if (winRate >= 75.0) {
            return "Legendary! You're mastering this challenge!";
        } else if (winRate >= 50.0) {
            return "Strong warrior! Victory is within your grasp!";
        } else if (winRate >= 25.0) {
            return "Progress! You're learning the way of battle.";
        } else {
            return "Don't give up! Every hero faces trials.";
        }
    }
    
    // ======== GETTERS ========
    
    public int getGamesPlayed() { return gamesPlayed; }
    public int getWins() { return wins; }
    public int getLosses() { return losses; }
    public String getBestProfession() { return bestProfession; }
    public int getHighestStrength() { return highestStrength; }
    public int getHighestAgility() { return highestAgility; }
    public int getHighestIntelligence() { return highestIntelligence; }
}