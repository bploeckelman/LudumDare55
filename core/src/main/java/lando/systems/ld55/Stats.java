package lando.systems.ld55;

public class Stats {
    // TODO - add stuff here that should be tracked through the game
    public static int enemyUnitsKilled;
    public static int playerUnitsKilled;

    public static void reset() {
        enemyUnitsKilled = 0;
        playerUnitsKilled = 0;
    }
}
