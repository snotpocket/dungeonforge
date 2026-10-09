package dungeonforge.commands;
import dungeonforge.events.GameEvent;
import dungeonforge.items.Item;
public class DropCommand extends AbstractCommand {
    public DropCommand(GameContext ctx, String args) {super(ctx, args);}
    @Override
    protected boolean doExecute() {
        Item item = Player().findItem(args);
        if (item == null) {
            Bus().message("You are not carrying something like that.");
            return false;
        }
        Player().removeItem(item);
        Room().getFloorItems().add(item);
        Bus().publish(GameEvent.message("You drop " + item.getName() + "."));
        return true;
    }
    @Override
    public String getDescription() {return ("drop " + args).trim();}
}