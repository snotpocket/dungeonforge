# The Undo Clinic — Lab 7, Part D

> Week 3: refuse a pattern. Week 4: tell three lookalikes apart. Week 5: two directions of
> decoupling. **This week: discover that a pattern gives you a hook, not an answer.**

## D1 — Measure the if/else · 6 pts

**Before you delete anything**, on `main`:

| Question | Your answer |
|---|---|
| Lines in `Main.gameLoop()` | |
| Number of `else if` branches | |
| To add "equip", which file(s) and how many places? | |
| To add alias "e" for "equip", how many *additional* places? | |
| Which line of `gameLoop()` would you attach undo to? | |

That last row has no answer, and saying so clearly is worth full marks.

**Now finish the refactor and measure again:**

| Question | Your answer |
|---|---|
| Lines in the new `gameLoop()` | |
| `else if` branches remaining | |
| To add "equip": files created, files modified | |
| To add alias "e": how many places? | |

## D2 — The naive undo, and why it fails · 12 pts 🔑

**Do these steps in order. Do not skip step 1 — the failure is the lesson.**
because it only reverses the monster hp.

### Step 1 — build the obvious version

Give `AttackCommand` a naive undo: a single field remembering the target's hit points before
the blow, restored on `undo()`. No snapshot, no other state.

```java
private int targetHpBefore;      // the "obvious" approach
```

### Step 2 — break it, on purpose

Write down `HP`, `XP` and `Gold` from `status`. Attack something that survives and hits back.
Check `status` again. Now `undo`, and check `status` a third time.

| | HP | XP | Gold | Monster HP |
|---|---|---|---|---|
| Before attack | | | | |
| After attack | | | | |
| After naive undo | | | | |

**Which values did NOT come back?**

**Why not? Answer in terms of what happened during that one turn**, not in terms of code:


### Step 3 — list everything one turn can change

Before you write the fix, enumerate it. Your undo has to reverse all of it.

- [ ] the target monster's hit points
- [ ] 
- [ ] 
- [ ] 
- [ ] 
- [ ] 

> Two of these are easy to miss. One involves Week 5's strategy swap; the other involves a
> monster that isn't in the room any more.

### Step 4 — fix it, then answer this

Build `TurnSnapshot` and make `undo()` restore it. Re-run the table from step 2 and confirm
everything returns.

**Now the question worth the marks.** The Command pattern promised "supports undoable
operations." **Did Command actually give you undo?** Answer carefully — there's a real
distinction here, and it's the most useful thing in this week.


## D3 — The capabilities you didn't ask for · 5 pts

The pattern's definition lists four things. You wanted one.

**Record a session.** Play for a dozen commands, run `history`, and paste the log:

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
  Ghoul dies.12

[HP 76/80] > nortj
  You cannot 'nortj' here. Type 'help' for what you can do.

[HP 76/80] > north
  You go north.

[HP 80/80] > a
  There is nothing here to attack.

[HP 80/80] > look
  [L1R2Level 1: Crypt
   "Burial niches line the walls. Most are empty. Most."
 Exits: south,  north

[HP 80/80] > north
  You go north.

[HP 80/80] > a
  There is nothing here to attack.

[HP 80/80] > look
  [L1R3Level 1: Crypt
   "Burial niches line the walls. Most are empty. Most."
 Exits: south,  north

[HP 80/80] > north
  You go north.

[HP 80/80] > look
  [L1R4Level 1: Crypt
   "Your footstep come back a half second too late."
 Exits: south,  north

[HP 80/80] > nirth
  You cannot 'nirth' here. Type 'help' for what you can do.

[HP 80/80] > north
  You go north.
  Bone Priest Bone Priest strikes with a censer for 3
  Wight Wightlunges at you for 4

[HP 73/80] > a
  You hit Bone Priest for 10
  Bone Priest Bone Priest strikes with a censer for 3
  Wight Wightlunges at you for 4

[HP 66/80] > a
  You hit Bone Priest for 10
  Bone Priest dies.12
  Wight Wightlunges at you for 4

[HP 62/80] > a
  You hit Wight for 10
  Wight Wightlunges at you for 4

[HP 58/80] > a
  You hit Wight for 10
  Wight dies.10

[HP 58/80] > a
  There is nothing here to attack.

[HP 58/80] > look
  [L1R5Level 1: Crypt
   "Burial niches line the walls. Most are empty. Most."
 Exits: south,  north

[HP 58/80] > quit
```

**Save those lines to a file and replay them:**

```bash
mvn -q exec:java -Dexec.args="--script=mysession.txt"
```
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
**Did you get the same game? Why does that depend on something you built in Week 3?**


**Finally:** name a **fifth** capability that falls out of actions-as-objects which the
pattern's definition doesn't mention. *(Hint: think about the optional GUI track, or about
anything that isn't a keyboard.)*


## D4 — One honest question · 2 pts

`AbstractCommand.execute()` is `final` and defines a fixed order: snapshot, act, monster
turns. Subclasses fill in one step.

**You have seen this shape before this week, in a pattern you already know. Which one, and
where?** *(You built something structurally identical in Week 4.)*
Abstractcommand.execute() is similar to the roompopulator.populate class


**And anything still unclear:**

