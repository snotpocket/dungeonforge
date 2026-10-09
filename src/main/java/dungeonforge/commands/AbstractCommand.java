package dungeonforge.commands;
import dungeonforge.core.Player;
import dungeonforge.core.Room;
import dungeonforge.events.EventBus;
public abstract class AbstractCommand implements Command {
    protected GameContext ctx;
    protected final String args;
    private boolean actuallyRan;
    public AbstractCommand(GameContext ctx, String args) {
        this.ctx = ctx;
        this.args = args == null ? "" : args.trim();
    }
    @Override
    public final void execute() {
        actuallyRan = doExecute();
        if (endsTurn()) {
            ctx.runMonsterTurns();
        }
    }
    protected abstract boolean doExecute();
    @Override
    public void undo() {
    }
    @Override
    public String getDescription() {
        return "";
    }
    public Player Player() {return ctx.getPlayer();}
    public EventBus Bus() {return ctx.getBus();}
    public Room Room() {return ctx.getCurrentroom();}
}