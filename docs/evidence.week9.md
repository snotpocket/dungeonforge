# Week 9 Evidence

Fill this in as you go. Parts 0–2 and Part 5 are graded on the *measurement*, not on the code.

---

## 0. The Sprint 6 carry-over (US-6.7)

Before any Decorator work. `docs/sprint-07-plan.md` has the story.

```bash
grep -n 'isRunning\|!running' src/main/java/dungeonforge/Main.java \
                              src/main/java/dungeonforge/commands/GameContext.java
```

**Paste the output. How many places spell out the game-over rule?** ______

**Now open `GameOverState.java`. It lists four verbs. Try to reach one in play. What happens,
and why?**


**One sentence: `running` and `GameOverState` both mean "the game is over". Do they? Say what
each one actually means.**


---

## 1. BEFORE — count the problem

```bash
ls src/main/java/dungeonforge/items/ | grep -E 'Weapon|Enchant'
grep -rln "damage + 4" src/main/java/dungeonforge/items/
grep -rho "instanceof" src/main/java/dungeonforge/items/*.java | wc -l
grep -c "return null" src/main/java/dungeonforge/items/EnchantmentForge.java
wc -l src/main/java/dungeonforge/items/EnchantmentForge.java
```

| | Count |
|---|-------|
| Weapon/enchantment classes | 8     |
| Files containing the fire damage bonus | 4     |
| `instanceof` tests in `items/` | 34    |
| `return null` paths in `EnchantmentForge` | 1     |
| Lines in `EnchantmentForge` | 108   |

**Open `FlamingFrostVampiricWeapon.java`. Which of its methods contain arithmetic that also
appears, word for word, in another file?**
getDamage() is the method and getValue()

**You want a fourth enchantment, `keen`. Before you look anything up: how many new classes
would this package need?** ___15___

*(Write it down before reading on.)* For N enchantments the package needs 2^N − 1 classes.
N = 3 is 7. N = 4 is 15. N = 5 is 31.

---

## 2. BEFORE — reproduce all four defects

You start carrying a **Notched Shortsword**, so this works on turn one. Run
`mvn -q exec:java` and type exactly this:

```
equip shortsword
status
enchant shortsword with flaming
status
inventory
equip shortsword
status
enchant shortsword with flaming
enchant shortsword with keen
quit
```

**(a) Paste the two `status` lines from before and after the first `enchant`:**

```
  Delver  HP 80/80  ATK 15  DEF 3  Gold 0  XP 0  Carry 60.0kg  wielding Notched Shortsword [dmg 5] (2.5kg, 25g)
  The Notched Shortsword is now Flaming Notched Shortsword [dmg 9] (2.5kg, 85g)

```

**Did your damage change? Explain in one sentence why not.**

The damage did change because it changed to a different class.
**(b) Paste the refusal from the second `enchant ... with flaming`:**

```
  That weapon cannot take another flaming enchantment.
```

**Is there anything wrong with what you asked for? Yes or no — then say what the refusal was
really reporting.**
You can't put the same enchantment twice.

**(c) Paste the response to `enchant ... with keen`:**

```
  No such enchantment: keen
```

**(d) Open `EnchantmentForge.baseDamage()`. In one sentence: why does this method exist at all?
What would have to be true for it to be unnecessary?**
to get the base damage for the enchantment. It would have to be rewritten to take base damage elsewhere.

**(e) Classify all four.** Two of these are the *design*; one is an ordinary *bug* that would
exist in any design. Which is which, and how do you know?

| Defect | Design, or ordinary bug? | How you can tell                                          |
|---|--------------------------|-----------------------------------------------------------|
| (a) stale hand | Design                   | Because of how it is designed                             |
| (b) no double enchantment | Design                   | Because there is no class with a double enchantment       |
| (c) no fourth enchantment | Design                   | because there is no class files for the forth enchantment |
| (d) base-value recovery | bug                      | Because it changes it.                                    |

---

## 3. AFTER — US-7.3, the receipt

**Paste `git diff --stat HEAD~1` for the commit that added the fourth enchantment:**

```
 docs/decorator-clinic.md                                  |   1 +
 src/main/java/dungeonforge/commands/EnchantCommand.java   |  29 ++++++----------
 src/main/java/dungeonforge/commands/TurnSnapshot.java     |   8 ++---
 src/main/java/dungeonforge/items/EnchantmentForge.java    | 118 -----------------------------------------------------------------
 src/main/java/dungeonforge/items/EnchantmentRegistry.java |  23 +++++++++++++
 src/main/java/dungeonforge/items/KeenWeapon.java          |  17 ++++++++++
 src/test/java/dungeonforge/DecoratorTest.java             | 193 ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 7 files changed, 249 insertions(+), 140 deletions(-)
```

**New files:** ___1___  **Modified files:** ___1___  **Lines added to modified files:** ___1___

**Now re-run the Part 1 counts on the finished package:**

| | Before | After |
|---|--------|-------|
| Weapon/enchantment classes | 8      | 7     |
| Files containing the fire bonus | 4      | 1     |
| `instanceof` in `items/` | 34     | 0     |
| `return null` paths in the selection class | 13     | 0     |
| Lines in the selection class | 108    | 33    |

---

## 4. AFTER — US-7.4, order

**Paste your two order tests, and their four numbers:**

```java
    @Test
    void orderChangesTheResultForMultiplyingDecorators() {
        int keenThenFire = new FlamingWeapon(new KeenWeapon(sword())).getDamage();  // 12 + 4
        int fireThenKeen = new KeenWeapon(new FlamingWeapon(sword())).getDamage();  // round(12*1.5)

        assertEquals(16, keenThenFire);
        assertEquals(18, fireThenKeen);
        assertNotEquals(keenThenFire, fireThenKeen,
                "if these were equal, order would not matter and this test would be pointless");
    }

    @Test
    void orderChangesHowMuchAVampiricWeaponDrinks() {
        assertEquals(3, new VampiricWeapon(new FlamingWeapon(sword())).lifesteal()); // (8+4)/4
        assertEquals(2, new FlamingWeapon(new VampiricWeapon(sword())).lifesteal()); // 8/4
    }
```

| Stack | Result        |
|---|---------------|
| `Flaming(Keen(sword))` | dmg ___16___  |
| `Keen(Flaming(sword))` | dmg ___18___  |
| `Vampiric(Flaming(sword))` | drain ___3___ |
| `Flaming(Vampiric(sword))` | drain ___2___ |

**One paragraph. Why is this correct behaviour rather than a bug?** *(There is one idea to
state, and everything else follows from it.)*
It is correct behavior because of order of operations being different. It likely calculates it differently.

---

## 5. The payoff — what did NOT change

```bash
git diff --stat main -- src/main/java/dungeonforge/core/
```

**Which files in `core/` changed during this entire sprint?** ___25___

**`Combat.playerStrikes()` computes the damage the player deals and the hit points they drink
back. You added four enchantments this week, two of which change damage and one of which
changes healing. How many lines of `Combat.java` did you edit?** ___0___

**Explain that number** in two or three sentences, using the phrase *programs to the
interface* and naming which interface.

Because everything is from the interface from the decorator pattern. which can add new items without modifying the combat class.
---

## 6. The honest accounting

**Of the four defects in Part 2, which one did Decorator fix, and which one did you fix?**


**Quote the lines that fixed the one Decorator did not.**

```java
        wasEquipped = (player().getEquippedWeapon() == w);
        if (wasEquipped) {player().equip(after);}

```

**Would those lines have worked before the refactor?** ___yes___

---

## 7. Retro line

**One sentence on something Decorator gave you, and one on something it did not.**
