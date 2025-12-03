package game.test;

import game.system.GameplaySystem;
import game.core.Profession;

public class MainGame {
    
    public static void main(String[] args) {
        // Simple test to verify GameplaySystem works
        GameplaySystem game = new GameplaySystem();
        
        // Test initialization
        boolean initialized = game.initialize("TestPlayer", Profession.WARRIOR);
        
        if (initialized) {
            System.out.println("✓ GameplaySystem initialized successfully");
            System.out.println("✓ Player: " + game.getPlayer().getName());
            System.out.println("✓ Profession: " + game.getPlayer().getProfession());
            System.out.println("✓ Skills loaded: " + game.getPlayerSkills().length);
            System.out.println("✓ Enemies loaded: " + game.getEnemies().length);
            System.out.println("\nSystem test passed! Use ConsoleUI.java for gameplay.");
        } else {
            System.out.println("✗ Failed to initialize GameplaySystem");
        }
    }
}