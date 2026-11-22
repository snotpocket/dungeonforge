git diff --stat HEAD~1          # on the us-9.1 commit
diff src/main/java/dungeonforge/legacy/LegacyBestiary.java  src/main/java/dungeonforge/legacy/LegacyBestiary.java
diff src/main/java/dungeonforge/factory/MonsterFactory.java src/main/java/dungeonforge/factory/MonsterFactory.java
python3 scripts/codeonly.py src/main/java/dungeonforge/factory/MonsterFactory.java \
  | grep -ciE 'bestiary|csv|MOB_ID|HIT_DICE'
