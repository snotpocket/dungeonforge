package dungeonforge.commands;
import dungeonforge.events.GameEvent;
import dungeonforge.items.Hourglass;
import dungeonforge.items.Item;
import dungeonforge.items.Potion;
public class UseItemCommand extends AbstractCommand {
    private boolean rewound = false;
    public UseItemCommand(GameContext ctx, String args) {super(ctx, args);}
    @Override
    protected boolean doExecute() {
        Item item = Player().findItem(args);
        if (item == null) {
            Bus().message("You have nothing like that to use.");
            return false;
        }
        if (item instanceof Hourglass hourglass) {
            if (!hourglass.hasCharges()) {
                Bus().message("The hourglass is spent. The sand will not run again.");
                return false;
            }
            hourglass.spendCharge();
            rewound = true;
            Bus().message("Sand runs backwards. The last moment unhappens.");
            if (!ctx.getHistory().undoLast()) {
                Bus().message("...but there was nothing to take back.");
            }
            return false;
        }
        if (item instanceof Potion potion) {
            Player().heal(potion.getHealAmount());
            Player().removeItem(item);
            Bus().publish(GameEvent.message("You drink " + potion.getName() + ". HP" + Player().getHp() +
                     " / " + Player().getMaxHp()));
            return true;
        }
        Bus().message("You cannot think of a use for " + item.getName() + " right now.");
        return false;
    }
    @Override
    public String getDescription() {return ("use " + args).trim();}
}