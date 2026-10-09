package dungeonforge.commands;
import dungeonforge.core.Monster;
public class AttackCommand extends AbstractCommand {
    public AttackCommand(GameContext ctx, String args) {super(ctx,args);}
    @Override
    protected boolean doExecute() {
        Monster target = findTarget();
        if (target == null) {
            Bus().message(args.isBlank() ? "There is nothing here to attack." : "There is here called \"" + args + "\".");
            return false;
        }
        ctx.getCombat().playerStrikes(Player(),target);
        return true;
    }
    private Monster findTarget() {
        if (args.isBlank()) return Room().firstLivingIn();
        for (Monster m : Room().getMonsters()) {
            if (m.isAlive() && m.getName().toLowerCase().contains(args.toLowerCase())) {
                return m;
            }
        }
        return null;
    }
    @Override
    public String getDescription() {return ("attack " + args).trim();}
}