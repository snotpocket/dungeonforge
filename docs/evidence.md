# Week 8 Evidence

## 1. BEFORE — the two problems

### Problem A: mode checks scattered across commands

```bash
grep -rn "hasLivingMonsters" src/main/java/dungeonforge/commands/
```

**Paste it. How many command classes re-implement the same check?**

```
 % grep -rn "hasLivingMonsters" src/main/java/dungeonforge/commands/
src/main/java/dungeonforge/commands/CommandParser.java:172:        if (room.hasLivingMonsters()) {
src/main/java/dungeonforge/commands/DropCommand.java:21:        if (room().hasLivingMonsters()) {
src/main/java/dungeonforge/commands/TakeCommand.java:13:        if (room().hasLivingMonsters()) {
src/main/java/dungeonforge/commands/MoveCommand.java:19:        if (room().hasLivingMonsters()) {
```

**Play a turn: start a fight, then type `north`. Paste what happens:**

```
        D U N G E O N F O R G E
  A Head First Design Patterns project
=========================================
  version 0.7.0   seed 20260818

  Type 'help' for what you can do. You are carrying an hourglass -- 'use hourglass' rewinds a turn.
  [L1R0] Level 1: Crypt
  "Burial niches line the walls. Most are empty. Most."
  Exits: north

[HP 80/80] > north

  You go north.
  ACHIEVEMENT: Cornered -- Force an enemy to change tactics
  Ghoul changes tactics: idle -> hunting.
  Ghoul hits you for 2.

[HP 78/80] > a

  You hit Ghoul for 10.
  Ghoul changes tactics: hunting -> enraged.
  Ghoul hits you for 3.

[HP 75/80] > north

  You cannot walk away with enemies still standing.

[HP 75/80] > 
```

**Now type `help` during that fight. Does it list `north`?** __yes__
**Should it?**

No
### Problem B: the monster lifecycle as a string plus conditionals

```bash
grep -n "getStateName" src/main/java/dungeonforge/core/Combat.java
```

**Paste it. Which three methods branch on the state name?**

```

```

**Answer before you refactor:**

1. To add a "stunned" state, which methods must you edit?

2. Reading `updateMonsterState()`, which states can follow `enraged`? How long did it take you
   to be sure?

3. What stops `setStateName("dancing")` from compiling?


## 2. AFTER — US-6.1 and US-6.2

**Paste `help` in Exploring and in Combat, side by side:**

```
  [Exploring] you can: north, south, look, take, loot, drop, use, inventory, pack, status, quests, history, undo, help, quit
  [Combat] you can: attack, look, use, inventory, status, history, undo, help, quit

```

**Paste an attempt to move during combat:**

```
  You cannot do that in Combat mode .
 Available here: attack, look, use, inventory, status, history, undo, help, quit

```

**Paste the grep proving the guards are gone:**

```bash
grep -rn "hasLivingMonsters" src/main/java/dungeonforge/commands/
```

```
grep -rn "hasLivingMonsters" src/main/java/dungeonforge/commands/

src/main/java/dungeonforge/commands/GameContext.java:52:        if (currentRoom.hasLivingMonsters()) transitionTo(new CombatState());
src/main/java/dungeonforge/commands/CommandParser.java:176:        if (room.hasLivingMonsters()) {
```

**Paste entering and leaving the pack, and the prompt in each mode:**

```

[Exploring | HP 76/80] > pack

   --- Your pack --- ('back' to close)
[Pack | HP 76/80] > back 


[Exploring | HP 76/80] > 
```

## 3. AFTER — US-6.3

**Paste the grep proving `Combat` no longer knows any state name:**

```bash
grep -n "getStateName\|\"enraged\"\|\"idle\"" src/main/java/dungeonforge/core/Combat.java
```

```
grep -n "getStateName\|\"enraged\"\|\"idle\"" src/main/java/dungeonforge/core/Combat.java


```

**Paste a fight showing a monster moving through its lifecycle:**

```

[Exploring | HP 80/80] > n

  You go north.
  Ghoul is now  hunting.
  Combat: 1 hostiles(s). Try 'attack'.

[Combat | HP 80/80] > a

  You hit Ghoul for 10.
  Ghoul hits you for 2.
  Ghoul hits you for 2.
  ACHIEVEMENT: Cornered -- Force an enemy to change tactics
  Ghoul changes tactics: dungeonforge.behavior.RangedStrategy@23dcda80 -> aggressive.
  Ghoul is now  enraged.

[Combat | HP 76/80] > a

  You hit Ghoul for 10.
  ACHIEVEMENT: First Blood -- Defeat your first monster
  Ghoul dies. +12 XP.
```

## 4. AFTER — US-6.4

**Paste the test output (or a demo) showing a healer that becomes aggressive when enraged:**

```
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
[INFO] Nothing to compile - all classes are up to date.
[INFO] 
[INFO] --- resources:3.4.0:testResources (default-testResources) @ dungeonforge ---
[INFO] skip non existing resourceDirectory /Users/johnnycumpston/ideaprojects/dungeonforge/src/test/resources
[INFO] 
[INFO] --- compiler:3.13.0:testCompile (default-testCompile) @ dungeonforge ---
[INFO] Recompiling the module because of changed source code.
[INFO] Compiling 6 source files with javac [debug release 21] to target/test-classes
[INFO] 
[INFO] --- surefire:3.2.5:test (default-test) @ dungeonforge ---
[INFO] Using auto detected provider org.apache.maven.surefire.junitplatform.JUnitPlatformProvider
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running dungeonforge.SkeletonTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.025 s -- in dungeonforge.SkeletonTest
[INFO] Running dungeonforge.StateTest
[INFO] Tests run: 22, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.046 s -- in dungeonforge.StateTest
[INFO] Running dungeonforge.StrategyObserverTest
[INFO] Tests run: 17, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.019 s -- in dungeonforge.StrategyObserverTest
[INFO] Running dungeonforge.FactoryTest
[INFO] Tests run: 16, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.012 s -- in dungeonforge.FactoryTest
[INFO] Running dungeonforge.CommandTest
[INFO] Tests run: 19, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.028 s -- in dungeonforge.CommandTest
[INFO] Running dungeonforge.SingletonTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.007 s -- in dungeonforge.SingletonTest
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 86, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.605 s
[INFO] Finished at: 2026-10-17T19:03:13-07:00
[INFO] ------------------------------------------------------------------------
```

**In one sentence: which one changed the other, and could it have gone the other way?**
The State change changed the strategy and we proved in unit tests that it couldn't go the other way (i.e strategy > state).

## 5. Tests and CI

**`mvn test` summary:**

```
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
[INFO] Nothing to compile - all classes are up to date.
[INFO] 
[INFO] --- resources:3.4.0:testResources (default-testResources) @ dungeonforge ---
[INFO] skip non existing resourceDirectory /Users/johnnycumpston/ideaprojects/dungeonforge/src/test/resources
[INFO] 
[INFO] --- compiler:3.13.0:testCompile (default-testCompile) @ dungeonforge ---
[INFO] Recompiling the module because of changed source code.
[INFO] Compiling 6 source files with javac [debug release 21] to target/test-classes
[INFO] 
[INFO] --- surefire:3.2.5:test (default-test) @ dungeonforge ---
[INFO] Using auto detected provider org.apache.maven.surefire.junitplatform.JUnitPlatformProvider
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running dungeonforge.SkeletonTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.025 s -- in dungeonforge.SkeletonTest
[INFO] Running dungeonforge.StateTest
[INFO] Tests run: 22, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.046 s -- in dungeonforge.StateTest
[INFO] Running dungeonforge.StrategyObserverTest
[INFO] Tests run: 17, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.019 s -- in dungeonforge.StrategyObserverTest
[INFO] Running dungeonforge.FactoryTest
[INFO] Tests run: 16, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.012 s -- in dungeonforge.FactoryTest
[INFO] Running dungeonforge.CommandTest
[INFO] Tests run: 19, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.028 s -- in dungeonforge.CommandTest
[INFO] Running dungeonforge.SingletonTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.007 s -- in dungeonforge.SingletonTest
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 86, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.605 s
[INFO] Finished at: 2026-10-17T19:03:13-07:00
[INFO] ------------------------------------------------------------------------
```

**Green CI URL:**

## 6. Sprint review — one sentence

> What can the project do now that it could not do last week?

The project can now have states that limit the number of commands and it has a couple more commands.