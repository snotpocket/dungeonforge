package dungeonforge.factory;

import dungeonforge.config.RandomSource;
import dungeonforge.core.Monster;
import dungeonforge.items.*;

public class CryptThemeKit implements ThemeKit {
    private final MonsterFactory factory;
    private static final String[] FLAVOURS = {
            "Damp Stone. Something drips in the dark, patiently",
            "Burial niches line the walls. Most are empty. Most.",
            "The air tastes of old dust and older grief.",
            "Your footstep come back a half second too late."
    };
    public CryptThemeKit(MonsterFactory factory) {
        this.factory = factory;
    }
    @Override
    public String themeName() {
        return "Crypt";
    }

    @Override
    public Monster createMonster(int depth) {
        String id = RandomSource.getInstance().pick(factory.idsForTheme("crypt"));
        return factory.create(id, depth);
    }

    @Override
    public Monster createBoss(int depth) {
        return factory.create("bone_tyrant",depth);
    }

    @Override
    public Item createLoot(int depth) {
        switch (RandomSource.getInstance().nextInt(4)) {
            case 0: return new Weapon("Bone Shortswords",2.0,40 + depth*10,5+depth);
            case 1: return new Armor("Grave Shroud",3.0,35 + depth*8,2+depth);
            case 2: return new Potion("Vile of Still water",0.4,25,18 + depth);
            default: return new Treasure("Bone Charm",0.2,60 + depth*15);
        }

    }

    @Override
    public String createRoomFlavor() {
        return RandomSource.getInstance().pick(FLAVOURS);
    }
}
