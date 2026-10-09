package dungeonforge.commands;

import dungeonforge.core.Combat;
import dungeonforge.core.GameWorld;
import dungeonforge.core.Player;
import dungeonforge.core.Room;
import dungeonforge.events.EventBus;

public class GameContext {
    private final GameWorld world;
    private final Player player;
    private final EventBus bus;
    private final Combat combat;
    private Room currentroom;

    private final CommandHistory history;
    private boolean running = true;
    public GameContext(GameWorld world, Player player, EventBus bus, Combat combat,  CommandHistory history ) {
        this.world = world;
        this.player = player;
        this.bus = bus;
        this.combat = combat;
        this.history = history;
        this.currentroom = world.startingRoom();
    }
    public Combat getCombat() {return combat;}
    public GameWorld getWorld() {return world;}
    public Player getPlayer() {return player;}
    public EventBus getBus() {return bus;}
    public Room getCurrentroom() {return currentroom;}
    public boolean isRunning() {return running;}
    public void setCurrentroom(Room currentroom) {this.currentroom = currentroom;}
    public void stop(boolean running) {this.running = running;}
   // public CommandHistory getHistory() {return history;}
    public void runMonsterTurns() {if (player.isAlive()) {combat.monsterTurns(player, currentroom);}}
}