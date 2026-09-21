package dungeonforge.behavior;

import dungeonforge.core.Entity;

public class Action {
    public enum Type {ATTACK,RANGED_ATTACK,FLEE,HEAL_ALLY,WAIT}
    private final Type type;
    private final Entity target;
    private final String flavour;
    public Action(Type type,Entity target, String flavour) {
        this.type = type;
        this.target = target;
        this.flavour = flavour;
    }
    public static Action wait(String flavour) {return new Action(Type.WAIT,null,flavour);}
    public Type getType() {return type;}
    public Entity getTarget() {return target;}
    public String getFlavour() {return flavour;}
}