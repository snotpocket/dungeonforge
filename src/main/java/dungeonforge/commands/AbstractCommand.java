package dungeonforge.commands;
import dungeonforge.core.Player;
import dungeonforge.core.Room;
import dungeonforge.events.EventBus;
public abstract class AbstractCommand implements Command {
    protected GameContext ctx;
    protected final String args;
    private boolean actuallyRan;
    private TurnSnapshot before;
    public AbstractCommand(GameContext ctx, String args) {
        this.ctx = ctx;
        this.args = args == null ? "" : args.trim();
    }
    @Override
    public final void execute() {
        if (endsTurn()) {
            before = TurnSnapshot.capture(ctx.getPlayer(), ctx.getCurrentroom());
        }
        actuallyRan = doExecute();
        if (!actuallyRan) {
            before = null;
            return;
        }
        if (endsTurn()) {
            ctx.runMonsterTurns();
        }
    }
    protected abstract boolean doExecute();
    @Override
    public void undo() {
        if (before != null) {
            before.restore();
            ctx.setCurrentroom(before.getRoom());
        }
    }
    @Override
    public String getDescription() {
        return "";
    }
    public boolean isUndoable() {return actuallyRan;}
    public boolean didRun() {return actuallyRan;}

    public Player Player() {return ctx.getPlayer();}
    public EventBus Bus() {return ctx.getBus();}
    public Room Room() {return ctx.getCurrentroom();}
}