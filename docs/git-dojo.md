# Git Dojo — my recovery notes

> Part D of Lab 2. For each drill: the command(s) you ran, **one sentence in your own
> words** on what it did, and one on when you would reach for it again.
>
> Graded on the sentences, not the commands. Commands can be copied; understanding cannot.

## The three trees — in my own words

| Tree | What lives here |
|---|-----------------|
| Working Directory | The main folder that contains the files to work with.|
| Staging Area (Index) | The files that will be added to a commit.|
| HEAD | The file containing the last commit to the local repository|

---

## Drill 1 — Committed to `main` by accident

**Commands I ran:**
```bash
git switch main
echo "oops" > accident.txt
git commit -m "feat: work that should have been on a branch"
git switch -c fix/rescued-work # branch now points at your commit
git switch main
git reset --hard origin/main # main returns to where the remote is
```
**What it did:**
The commands added files to the main branch on accident and then created a new branch to save existing work and then switched back to the main branch and then reset the main branch to that of origin.
**When I would use it again:**
When I accidentally commited to the main branch.
---

## Drill 2 — Wrong commit message / forgot a file

**Commands I ran:**
```bash
echo "x" > note.txt && git add note.txt && git commit -m "asdf"
git commit --amend -m "docs: add note file"
```
**What it did:**
The commands  created a file and changed the commit message after it was already commited
**Why you must not do this to a commit you already pushed:**
using git commit --amend will overwrite commit history of the last commit pushed which can make it harder to see certain commit history.
---

## Drill 3 — Committed a file that should be ignored

**Commands I ran:**
```bash
mkdir -p target && echo "junk" > target/Main.class
git add -f target/Main.class && git commit -m "chore: oops, committed build output"
git rm -r --cached target # stop tracking, keep the local files
echo "target/" >> .gitignore
git add .gitignore && git commit -m "chore: untrack build output and ignore target/"
```
**What it did:**
The commands added a java class file to the main branch and then removed the file from the main branch using another commit
**Why adding it to `.gitignore` alone was not enough:**
because the -f git command flag was used which ignored the .gitignore file.
---

## Drill 4 — Merge conflict

**Commands I ran:**
```bash
git switch main
git switch -c feature/a
printf '# DungeonForge - branch A title\n' > README.md
git commit -am "docs: title from branch A"
git switch main
git switch -c feature/b
printf '# DungeonForge - branch B title\n' > README.md
git commit -am "docs: title from branch B"
git switch main
git merge feature/a # clean
git merge feature/b # CONFLICT
```
**In the conflict markers, which side was "mine"?**
feature/a
**What it did:**
The commands created two branches and then changed readme.md on the two branches and then merged feature/a first and then tried to merge feature/b which caused a merge conflict
**How I would back out of a merge I regretted starting:**
run the command git merge --abort.
---

## Drill 5 — "I destroyed everything"

**Commands I ran:**
```bash
git log --oneline # note the current hash
git reset --hard HEAD~3 # nuke the last three commits
git log --oneline # gone
git reflog # every position HEAD has held
git reset --hard <hash-from-before>
```
**What `git reflog` showed me:**
It showed me the a log of commits for the last 90 days.
**One sentence on why this changes how nervous I should be about Git:**
Because it can allow me to revert the git project to a previous commit from before the local copy is destroyed.
---

## Stretch — Drill 6 (detached HEAD, interactive rebase)

**Notes:**
It allows to change a branch to a different branch.
---

## The one command I want to remember from today

git reflog