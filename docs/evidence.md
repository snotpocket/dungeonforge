# Week 7 Evidence

## 1. BEFORE

```bash
mvn -q exec:java
```

Play a few turns, then answer:

**Paste five consecutive `else if` branches from `gameLoop()`:**

```java
if (verb.isEmpty()){
} else if (verb.equals("north") || verb.equals("south") || verb.equals("down")) {}
} else if (verb.equals("attack")) {}
} else if (verb.equals("look")) {}
} else if (verb.equals("take")) {
}
```

**You want to add `equip`. List every edit you would make:**
1. Main.java import dungeonforge.items.Weapon;
2. Main.java a 12th else if (verb.equals("equip")) inside gameLoop();
3. Main.java set tookaturn = true in the new branch inside #2
4. Main.java the two hardcoded help printlns lines 219-220
5. Player.java import dungeonforge.items.Weapon;
6. Player.java private Weapon equippedWeapon; + equip(Weapon) + getequippedweapon()
7. Player.java make the weapon count toward damaga
8. Player.java describe() line 59 reads the field attackpower not the getter
9. Combat.java int damage = player.getattackpower();
10. src/test/java/dungeonforge/equiptest.java
11. README-WEEK7.md and any verb list in docs

**You want `e` to mean `equip`. List every *additional* edit:**


**You want undo. Where in `gameLoop()` would the code go?** *(There is no good answer. Say
why in one sentence.)*
Undo in Game loop could go in the if else statements as another command.

## 2. AFTER — US-5.1

**Paste your new `gameLoop()` in full. It should fit in a dozen lines:**

```java
    private static void gameLoop(CommandParser parser, GameContext ctx) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        boolean running = true;
        while (ctx.isRunning() && ctx.getPlayer().isAlive()) {
            System.out.print("\n[HP " + ctx.getPlayer().getHp() + "/" + ctx.getPlayer().getMaxHp() + "] > ");
            System.out.flush();

            String line;
            try { line = in.readLine(); } catch (Exception e) { break; }
            if (line == null) break;
            Command command = parser.parse(line,ctx);
            command.execute();
        }
    }
```

**Paste the registry lines that add `drop` and its alias:**

```java
        register("drop",DropCommand::new);

```

## 3. AFTER — US-5.2

**The naive-undo failure table from Part D2 step 2:**

| | HP | XP | Gold | Monster HP |
|---|----|----|------|------------|
| Before attack | 78 | 0  | 0    | 17         |
| After attack | 76 | 0  | 0    | 7          |
| After naive undo | 76 | 0  | 0    | 17         |

**The same table after `TurnSnapshot`:**

| | HP | XP | Gold | Monster HP |
|---|----|----|------|------------|
| Before attack | 78 | 0  | 0    | 17         |
| After attack | 76 | 0  | 0    | 7          |
| After snapshot undo | 78 | 0  | 0    | 17         |

## 4. AFTER — US-5.3 and US-5.4
You have to replay backwards before if you undid the commands list forward then you would only go back 1 command in history.

**Paste a `loot` that picks up three things, then an `undo` that puts them all back:**

```

[HP 80/80] > north
  You go north.
  Ghoul Ghoullooses a shot for 2

[HP 78/80] > a
  You hit Ghoul for 10
  Ghoul Ghoullooses a shot for 2

[HP 76/80] > a
  You hit Ghoul for 10
  ACHIEVEMENT First Blood -- Defeat your first monster
  ACHIEVEMENT Exterminator -- Defeat 10 monsters
  Ghoul dies.12

[HP 76/80] > look
  [L1R1Level 1: Crypt
   "Damp Stone. Something drips in the dark, patiently"
    Iron-bound Chest:  
       -   Bone Shortswords [dmg 6] (2.0kg, 50g)
       -   Bone Shortswords [dmg 6] (2.0kg, 50g)
       -   Grave Shroud [def 3] (3.0kg, 43g)
 Exits: south,  north

[HP 76/80] > loot
  You take Bone Shortswords.
  You take Bone Shortswords.
  You take Grave Shroud.

[HP 76/80] > north
  You go north.

[HP 80/80] > south
  You go south.
  QUEST COMPLETE Clear 6 rooms

[HP 80/80] > look
  [L1R1Level 1: Crypt
   "Damp Stone. Something drips in the dark, patiently"
 Exits: south,  north

  
```

**Paste the hourglass rewinding a turn, with `inventory` before and after:**

```

[HP 80/80] > i
  You are carrying:
      - Small Healing Draught [heals 22] (0.3kg, 20g)
      - CHronomaster's Hourglass [2charges] (rewinds one turn)
      - Bone Shortswords [dmg 6] (2.0kg, 50g)
      - Bone Shortswords [dmg 6] (2.0kg, 50g)
      - Grave Shroud [def 3] (3.0kg, 43g)

[HP 80/80] > use CHronomaster's Hourglass
  Sand runs backwards. The last moment unhappens.

[HP 80/80] > i
  You are carrying nothing.
      
```

**How long did US-5.4 actually take you?** __25__ minutes

## 5. The replay

**Paste your `history` output and the first ten lines of replaying it with `--script=`:**

```
=========================================
        D U N G E O N F O R G E
  A Head First Design Patterns project
=========================================
  version 0.6.0   seed 20260829

  Type 'help' for what you can do You are carrying an hourglass -- 'use hourglass' rewinds a turn
  [L1R0Level 1: Crypt
   "Burial niches line the walls. Most are empty. Most."
 Exits: north

[script] > north
  You go north.
  Ghoul Ghoullooses a shot for 2

[script] > a
  You hit Ghoul for 10
  Ghoul Ghoullooses a shot for 2

[script] > a
  You hit Ghoul for 10
  ACHIEVEMENT First Blood -- Defeat your first monster
  Ghoul dies.12

[script] > north
  You go north.

[script] > a
  There is nothing here to attack.

[script] > look
  [L1R2Level 1: Crypt
   "Burial niches line the walls. Most are empty. Most."
 Exits: south,  north

[script] > north
  You go north.

[script] > a
  There is nothing here to attack.

[script] > look
  [L1R3Level 1: Crypt
   "Burial niches line the walls. Most are empty. Most."
 Exits: south,  north

[script] > north
  You go north.

[script] > look
  [L1R4Level 1: Crypt
   "Your footstep come back a half second too late."
 Exits: south,  north

[script] > north
  You go north.
  Bone Priest Bone Priest strikes with a censer for 3
  Wight Wightlunges at you for 4

[script] > a
  You hit Bone Priest for 10
  Bone Priest Bone Priest strikes with a censer for 3
  Wight Wightlunges at you for 4

[script] > a
  You hit Bone Priest for 10
  Bone Priest dies.12
  Wight Wightlunges at you for 4

[script] > a
  You hit Wight for 10
  Wight Wightlunges at you for 4

[script] > a
  You hit Wight for 10
  Wight dies.10

[script] > a
  There is nothing here to attack.

[script] > look
  [L1R5Level 1: Crypt
   "Burial niches line the walls. Most are empty. Most."
 Exits: south,  north

[script] > quit
  You lay down your pack.
```

## 6. Tests and CI

**`mvn test` summary:**

```
mvn test
[INFO] Scanning for projects...
[INFO] 
[INFO] ------------------< edu.redwoods.cis18:dungeonforge >-------------------
[INFO] Building DungeonForge 0.7.0
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- resources:3.4.0:resources (default-resources) @ dungeonforge ---
[INFO] Copying 3 resources from src/main/resources to target/classes
[INFO] 
[INFO] --- compiler:3.13.0:compile (default-compile) @ dungeonforge ---
[INFO] Recompiling the module because of added or removed source files.
[INFO] Compiling 60 source files with javac [debug release 21] to target/classes
[INFO] 
[INFO] --- resources:3.4.0:testResources (default-testResources) @ dungeonforge ---
[INFO] skip non existing resourceDirectory /Users/johnnycumpston/ideaprojects/dungeonforge/src/test/resources
[INFO] 
[INFO] --- compiler:3.13.0:testCompile (default-testCompile) @ dungeonforge ---
[INFO] Recompiling the module because of changed dependency.
[INFO] Compiling 5 source files with javac [debug release 21] to target/test-classes
[INFO] 
[INFO] --- surefire:3.2.5:test (default-test) @ dungeonforge ---
[INFO] Using auto detected provider org.apache.maven.surefire.junitplatform.JUnitPlatformProvider
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running dungeonforge.SkeletonTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.025 s -- in dungeonforge.SkeletonTest
[INFO] Running dungeonforge.StrategyObserverTest
[INFO] Tests run: 17, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.026 s -- in dungeonforge.StrategyObserverTest
[INFO] Running dungeonforge.FactoryTest
[INFO] Tests run: 16, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.021 s -- in dungeonforge.FactoryTest
[INFO] Running dungeonforge.CommandTest
[INFO] Tests run: 19, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.034 s -- in dungeonforge.CommandTest
[INFO] Running dungeonforge.SingletonTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.007 s -- in dungeonforge.SingletonTest
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 64, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.859 s
[INFO] Finished at: 2026-10-10T15:02:34-07:00
[INFO] ------------------------------------------------------------------------
```
replay depends on the gameconfig singleton.
**Green CI URL:**

## 7. Sprint review — one sentence

> What can the project do now that it could not do last week?

The project can add commands by creating subclasses extend abstractcommand.