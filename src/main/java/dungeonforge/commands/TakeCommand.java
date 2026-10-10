package dungeonforge.commands;
import dungeonforge.events.GameEvent;
import dungeonforge.items.Item;
public class TakeCommand extends AbstractCommand {
    public TakeCommand(GameContext ctx, String args) {super(ctx, args);}
    @Override
    protected boolean doExecute() {
        if (Room().hasLivingMonsters()) {
            Bus().message("Not while something is still moving in here.");
            return false;
        }
        Item found = takeFromFloor();
        if (found == null) {
            found = takeFromChest();
        }
        if (found == null) {
            Bus().message("There is nothing like that here.");
            return false;
        }
        Player().addItem(found);
        Bus().publish(GameEvent.message("You take " + found.getName() + "."));
        return true;
    }
    private Item takeFromFloor() {
        for (Item i : Room().getFloorItems()) {
            if (matches(i)) {Room().getFloorItems().remove(i);
                return i;
            }
        }
        return null;
    }
    private Item takeFromChest() {
        if (Room().getChest() == null || Room().getChest().getContents().isEmpty()) {return null;}
        for (Item i : Room().getChest().getContents()) {
            if (matches(i)) {
                Room().getChest().getContents().remove(i);
                return i;
            }
        }
        return null;
    }
    private boolean matches(Item i) {
        return args.isBlank() || i.getName().toLowerCase().contains(args.toLowerCase());
    }
    @Override
    public String getDescription() {return ("take " + args).trim();}
}
