# Week 13 Evidence

Fill this in as you go. Parts 0–3 and Part 5 are graded on the **measurement**, not the prose.

**Every counting command pipes through `scripts/codeonly.py`.** The reference solution's own
javadoc contains the strings `getChest()` and `private Chest chest`. Grep the code, not the
commentary.

---

## 0. The Sprint 9 carry-over (US-9.4)

Open `events/DamageMeter.java` and find where its warning level comes from.

**Paste that line.** ______

**`playerStartingHp` is a constant in `config.json`. In one sentence: what is the meter supposed
to be asking, and who is it asking instead?**


**Now try to write a test that proves it is wrong.**

**What stops you?** ______

**One sentence: why did this defect survive four sprints?**


---

## 1. BEFORE — count the files

```bash
for f in $(find src/main/java -name '*.java'); do
  python3 scripts/codeonly.py "$f" | grep -qE 'getChest\(\)|hasChest\(\)|setChest\(' && echo "$f"
done
```

**List them:** ______
```bash
for f in $(find src/main/java -name '*.java'); do
  python3 scripts/codeonly.py "$f" | grep -qE 'getChest\(\)|hasChest\(\)|setChest\(' && echo "$f"
done
src/main/java/dungeonforge/core/Room.java
src/main/java/dungeonforge/core/GameWorld.java
src/main/java/dungeonforge/commands/CommandParser.java
src/main/java/dungeonforge/commands/TurnSnapshot.java
src/main/java/dungeonforge/commands/TakeCommand.java
src/main/java/dungeonforge/factory/RoomPopulator.java
```

**How many?** ___6___

**Now the sentence. Name the count and say what it is a count OF.** *(Two points. "Six files use
getChest" is not it — six files know *something*, and naming that thing is the answer.)*

> __________________________________Six files know that a room keeps items in exactly two places__________________________________

**And one more count:**

```bash
grep -rln "carriedWeight\|totalWeight" src/main/java/
```

**Result:** ___60kg___

**Every `Item` has declared `getWeight()` since Week 1. In one sentence, what does that result
mean about the number on your status line?**


---

## 2. BEFORE — the two halves

Open `commands/TakeCommand.java`. Put `takeFromFloor()` and `takeFromChest()` side by side.

**Paste both:**

```java
    private Item takeFromFloor() {
        for (Item i : room().getFloorItems()) {
            if (matches(i)) { room().getFloorItems().remove(i); return i; }
        }
        return null;
    }

    private Item takeFromChest() {
        if (!room().hasChest()) return null;
        for (Item i : room().getChest().getContents()) {
            if (matches(i)) { room().getChest().getContents().remove(i); return i; }
        }
        return null;
    }
```

**Ignoring the collection each one walks, how many lines actually differ?** ___2___

**Now play the game.** `mvn -q exec:java`, then: go north, kill whatever is there, `look`,
`take <the chest's name>`.

**Paste those four lines of output:**

```
mvn exec:java
[INFO] Scanning for projects...
[INFO] 
[INFO] ------------------< edu.redwoods.cis18:dungeonforge >-------------------
[INFO] Building DungeonForge 0.13.0
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- exec:3.2.0:java (default-cli) @ dungeonforge ---
=========================================
        D U N G E O N F O R G E
  A Head First Design Patterns project
=========================================
  version 0.13.0   seed 20260818

  Type 'help' for what you can do. You are carrying an hourglass -- 'use hourglass' rewinds a turn.
  [L1R0] Level 1: Crypt
  "Burial niches line the walls. Most are empty. Most."
  Exits: north
 [sources] bestiary.csv (0.4 export): 10 blueprints
n

  You go north.
  Barrow Wight is now hunting.
  Combat! 1 hostile(s). Try 'attack'.
a

  You hit Barrow Wight for 10.
  Barrow Wight hits you for 5.
  ACHIEVEMENT: Cornered -- Force an enemy to change tactics
  Barrow Wight changes tactics: aggressive -> aggressive.
  Barrow Wight is now enraged.
a

  You hit Barrow Wight for 10.
  ACHIEVEMENT: First Blood -- Defeat your first monster
  Barrow Wight dies. +14 XP.
  The room falls quiet.
l

  [L1R1] Level 1: Crypt
  "Damp stone. Something drips in the dark, patiently."
  Iron-bound Chest:
    - Vial of Still Water [heals 21] (0.4kg, 25g)
    - Vampiric Bone Shortsword [dmg 6] [drain 1] (2.0kg, 140g
    - Grave Shroud [def 3] (3.0kg, 43g)
  Exits: south, north
take Iron-bound

  There is nothing like that here.

```

**The game listed a chest, let you take something out of it, and then said it does not exist.
Which of those three statements is false?**

That the chest doesn't exist is false. It clearly exists with look.

**Open `items/Chest.java` and read the comment at the top. When was it written?** ___Week 4___

---

## 3. BEFORE — the counting

```bash
C="python3 scripts/codeonly.py"
$C src/main/java/dungeonforge/core/Room.java | grep -cE 'List<Item> floorItems|private Chest chest'
$C src/main/java/dungeonforge/commands/TakeCommand.java | grep -cE 'private Item takeFrom'
$C src/main/java/dungeonforge/items/Chest.java | grep -c 'implements Item'
```

|                                           | Before    | After                   |
|-------------------------------------------|-----------|-------------------------|
| files that know a room has two containers | 6         | 0                       |
| collections a `Room` keeps items in       | 2         | 1                       |
| search methods in `TakeCommand`           | 2         | 0                       |
| `Chest` is an `Item` (yes/no)             | No        | Yes                     |
| files that sum item weight                | 0         | 2 (Player, TakeCommand) |
| `take <chest>` works (yes/no)             | no        | yes                     |
| `totalLoot()` after dropping two items    | unchanged | +2                      |

*(After column at the end of the sprint. **Nothing in it may be a guess.**)*

---

## 4. The gap US-10.2 leaves on purpose

Finish US-10.2, then play the game again and try `take <the chest's name>`.

**What happened?** ___Still couldn't pick the chest up___

**Complete this sentence:**

> A chest is an `Item` now and I still cannot pick one up, because _______________The rooms are the tree and are not the iterator because were never offering the chest to be pick up._______________


**Which story owns that, and which of its acceptance criteria?** ______

---

## 5. AFTER — the receipt

### The deletion

```bash
ls src/main/java/dungeonforge/core/RoomContentsIterator.java
grep -rl 'RoomContentsIterator' src/
mvn test
```

**Paste all three.**

```
ls src/main/java/dungeonforge/core/RoomContentsIterator.java
src/main/java/dungeonforge/core/RoomContentsIterator.java
bash-3.2$ grep -rl 'RoomContentsIterator' src/
src/main/java/dungeonforge/core/RoomContentsIterator.java
bash-3.2$ mvn test
[INFO] Scanning for projects...
[INFO] 
[INFO] ------------------< edu.redwoods.cis18:dungeonforge >-------------------
[INFO] Building DungeonForge 0.13.0
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- resources:3.4.0:resources (default-resources) @ dungeonforge ---
[INFO] Copying 4 resources from src/main/resources to target/classes
[INFO] 
[INFO] --- compiler:3.13.0:compile (default-compile) @ dungeonforge ---
[INFO] Nothing to compile - all classes are up to date.
[INFO] 
[INFO] --- resources:3.4.0:testResources (default-testResources) @ dungeonforge ---
[INFO] skip non existing resourceDirectory /Users/johnnycumpston/ideaprojects/dungeonforge/src/test/resources
[INFO] 
[INFO] --- compiler:3.13.0:testCompile (default-testCompile) @ dungeonforge ---
[INFO] Nothing to compile - all classes are up to date.
[INFO] 
[INFO] --- surefire:3.2.5:test (default-test) @ dungeonforge ---
[INFO] Using auto detected provider org.apache.maven.surefire.junitplatform.JUnitPlatformProvider
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running dungeonforge.SkeletonTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.025 s -- in dungeonforge.SkeletonTest
[INFO] Running dungeonforge.TemplateMethodTest
[INFO] Tests run: 19, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.041 s -- in dungeonforge.TemplateMethodTest
[INFO] Running dungeonforge.StateTest
[INFO] Tests run: 29, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.044 s -- in dungeonforge.StateTest
[INFO] Running dungeonforge.StrategyObserverTest
[INFO] Tests run: 17, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.019 s -- in dungeonforge.StrategyObserverTest
[INFO] Running dungeonforge.CarryOverTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.008 s -- in dungeonforge.CarryOverTest
[INFO] Running dungeonforge.FactoryTest
[INFO] Tests run: 16, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.015 s -- in dungeonforge.FactoryTest
[INFO] Running dungeonforge.CommandTest
[INFO] Tests run: 19, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.023 s -- in dungeonforge.CommandTest
[INFO] Running dungeonforge.IteratorCompositeTest
[INFO] Tests run: 24, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.025 s -- in dungeonforge.IteratorCompositeTest
[INFO] Running dungeonforge.SingletonTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.006 s -- in dungeonforge.SingletonTest
[INFO] Running dungeonforge.DecoratorTest
[INFO] Tests run: 25, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.031 s -- in dungeonforge.DecoratorTest
[INFO] Running dungeonforge.AdapterFacadeTest
[INFO] Tests run: 19, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.029 s -- in dungeonforge.AdapterFacadeTest
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 183, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.113 s
[INFO] Finished at: 2026-11-21T21:47:56-08:00
[INFO] ------------------------------------------------------------------------
```

**US-10.1's tests were written against a class that no longer exists, and they still pass. In one
sentence: what were they actually asserting?**


### The bug nobody reported

**Read the version of `totalLoot()` you started the week with. What did it count?** ______

**What did it NOT count?** ______

**Why was that invisible?** ______

**Paste your before-and-after measurement** — `totalLoot()` with nothing on the floor, then after
dropping two items.

```

```

**One sentence, and be careful with it: did Iterator FIND this bug?** *(There is a wrong answer
that sounds good.)*


---

## 6. AFTER — what an iterator cannot do

**After US-10.1, how many files still knew a room had two containers?** ______

**Name them, and for each one say why `Room.contents()` could not serve it:**

| File | Why not |
|------|---------|
|      |         |
|      |         |
|      |         |

**One of those three still reads the room's collections directly in your finished code. Which,
and is that a failure to finish the refactor? Answer yes or no and defend it.**


**For the other two: what fixed them — a better iterator, or something else?**


---

## 7. AFTER — the honest accounting

### Composite's trade

**Head First puts `add()` on the Component and lets leaves throw (p. 364). You did not.**

**Name one thing a caller cannot write because of that choice.** ______

**Name one thing that cannot go wrong at run time because of it.** ______

**Which would you choose for a library used by strangers? Is that the same answer you gave for
this codebase?**


### The two walks

|               | walks          | means                                                         |
|---------------|----------------|---------------------------------------------------------------|
| `take <name>` | the whole tree | adds that specific thing to the players inventory             |
| `loot`        | the top level  | picks everything in that room to add to the players inventory |

**One sentence: what goes wrong if `loot` walks the tree?**


### Leaves

**How many Leaf classes did you change this week?** ______

**One sentence on what that number says about the pattern.**


### Retro line

| Pattern   | Gave | Took |
|-----------|------|------|
| Iterator  |      |      |
| Composite |      |      |

**And: the composite walk yields a container before its contents. Name the player-facing bug
that comes back if you reverse it.**


