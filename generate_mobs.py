import json
import re
import os

with open("parsed_mobs.json", "r") as f:
    mobs_desc = json.load(f)

categories = {
    "ocean": ["AbyssalHunter", "DeepOneSorcerer", "LuminousJellyfish", "CoralGuardian", "IslandPirate", "SandCrab", "DrownedKnight", "TideCaller", "PearlOyster", "LaguneShark", "VolcanoSpirit", "LavaGull"],
    "mountains": ["CrystalGolem", "ShardWyvern", "CloudEagle", "PlateauWarden", "LavaStalker", "ObsidianColossus"],
    "forests": ["AncientTreeSpirit", "ForestTroll", "CinderHound", "AshWalker", "SaltGolem", "MiragePhantom", "SporeSpreader", "FungalBehemoth", "PineNymph", "ShadowStalker", "GroveSpecter", "WickerConstruct", "GoldenFox", "HarvestGolem", "PandaMonk", "BambooNinja", "CanopyMoth", "TwilightDruid"],
    "ice": ["GlacialWraith", "IceTitan", "AuroraShaMan", "TundraWolf", "FrozenMariner", "IceWhale"],
    "exotic": ["SkyDrake", "IslePhoenix", "StoneGolem", "PetrifiedWarden", "VineStrangler", "ChasmSerpent"],
    "swamps": ["BayouWitch", "VoodooSkeleton", "BogCrocodile", "GlowFrog", "WillOWisp", "MoorBanshee"],
    "deserts": ["DuneScorpion", "SandWraith", "CanyonHawk", "RedSandElemental", "AshPhoenix", "MagmaWorm"],
    "caves": ["CrystalMimic", "GeodeGuardian", "MoltenElemental", "InfernoDrake", "EchoBat", "ChamberColossus", "GlowMushroomCrab", "SporeQueen", "CinderSprite", "LavaFallTitan"],
    "nether": ["NetherBloom", "WarpedPiglin", "SoulLibrarian", "ArchivistWreath", "BasaltBrute", "LavaDjinn"],
    "end": ["EndCrawler", "ChorusReaper", "VoidSentinel", "OblivionBoss", "SpireGargoyle", "EndKnight"],
    "mystic": ["StarFragment", "CelestialGuardian", "DreamWeaver", "Nightmare", "RuneGuardian", "AncientSeer", "EclipsePhantom", "SolarWraith", "OasisDjinn", "MirageHydra", "MoonElemental", "LunarWolf", "ThunderCallerWyvern", "StormGolem", "SporeVine", "JungleStalker", "RuinsCurator", "ColossalStatue"],
    "waterfall": ["CaveHermit", "WaterfallSpirit", "MossGolem"]
}

bosses = ["ObsidianColossus", "InfernoDrake", "IceTitan", "FungalBehemoth", "ChamberColossus", "SporeQueen", "LavaFallTitan", "OblivionBoss", "MirageHydra", "SolarWraith", "ShardWyvern", "TideCaller", "DeepOneSorcerer", "PlateauWarden", "IslePhoenix"]
world_bosses = ["ObsidianColossus", "OblivionBoss"]

biome_mapping = {
    "ocean": "DEEP_OCEAN",
    "mountains": "MOUNTAINS",
    "forests": "FOREST",
    "ice": "ICE_SPIKES",
    "exotic": "JUNGLE",
    "swamps": "SWAMP",
    "deserts": "DESERT",
    "caves": "DRIPSTONE_CAVES",
    "nether": "NETHER_WASTES",
    "end": "THE_END",
    "mystic": "DARK_FOREST",
    "waterfall": "RIVER"
}

drops_mapping = {
    "ocean": ["PRISMARINE_SHARD 1-3 0.8", "PRISMARINE_CRYSTALS 1-2 0.5", "COD 1 0.9", "TROPICAL_FISH 1 0.5", "NAUTILUS_SHELL 1 0.1"],
    "mountains": ["AMETHYST_SHARD 1-2 0.6", "STONE 1-4 0.9", "IRON_INGOT 1 0.3", "COAL 1-3 0.7"],
    "forests": ["OAK_LOG 1-3 0.8", "MOSS_BLOCK 1-2 0.5", "BONE 1-2 0.7", "ROTTEN_FLESH 1-3 0.8", "FEATHER 1-2 0.6"],
    "ice": ["PACKED_ICE 1-2 0.6", "BLUE_ICE 1 0.3", "SNOWBALL 2-4 0.9", "LEATHER 1 0.5"],
    "exotic": ["AMETHYST_SHARD 1 0.5", "GOLD_NUGGET 2-5 0.6"],
    "swamps": ["SLIMEBALL 1-2 0.7", "CLAY_BALL 1-3 0.8", "SPIDER_EYE 1 0.4", "FERMENTED_SPIDER_EYE 1 0.2"],
    "deserts": ["SAND 2-4 0.9", "SANDSTONE 1-2 0.7", "BONE 1-3 0.8", "GOLD_NUGGET 2-5 0.6"],
    "caves": ["STONE 2-4 0.9", "IRON_INGOT 1-2 0.4", "COAL 1-3 0.8", "GLOW_INK_SAC 1 0.5"],
    "nether": ["BLAZE_ROD 1 0.4", "NETHER_BRICK 1-3 0.7", "GOLD_INGOT 1 0.3", "GHAST_TEAR 1 0.1"],
    "end": ["ENDER_PEARL 1 0.5", "CHORUS_FRUIT 1-2 0.6", "PURPUR_BLOCK 1 0.4"],
    "mystic": ["AMETHYST_SHARD 1-2 0.6", "ECHO_SHARD 1 0.1", "GLOWSTONE_DUST 1-3 0.7", "EXPERIENCE_BOTTLE 1 0.3"],
    "waterfall": ["STONE 1-3 0.9", "PRISMARINE_SHARD 1 0.5"]
}

boss_drops = ["DIAMOND_BLOCK 1 0.05", "DIAMOND 1-2 0.3", "GOLD_INGOT 2-5 0.8"]

def get_base_type(mob, desc):
    desc_l = desc.lower()
    name_l = mob.lower()

    if "wraith" in name_l or "geist" in desc_l or "phantom" in name_l or "phantom" in desc_l: return "PHANTOM"
    if "golem" in name_l or "golem" in desc_l or "stein" in desc_l or "statue" in desc_l or "statue" in name_l: return "IRON_GOLEM"
    if "dragon" in desc_l or "drache" in desc_l or "wyvern" in name_l:
        return "ENDER_DRAGON" if "boss" in desc_l else "PHANTOM"
    if "wolf" in name_l or "hund" in desc_l or "hound" in name_l: return "WOLF"
    if "wasser" in desc_l or "tiefsee" in desc_l or "shark" in name_l: return "DROWNED"
    if "pilz" in desc_l or "spore" in name_l or "fungal" in name_l: return "CREEPER"
    if "hex" in desc_l or "witch" in name_l or "magier" in desc_l or "seer" in name_l or "druid" in name_l: return "WITCH"
    if "skelett" in desc_l or "skeleton" in name_l or "untot" in desc_l: return "SKELETON"
    if "spinn" in desc_l or "spider" in name_l or "crawler" in name_l: return "CAVE_SPIDER"
    if "krabb" in desc_l or "crab" in name_l or "panzer" in desc_l: return "SHULKER"
    if "hai" in desc_l or "fisch" in desc_l or "jellyfish" in name_l: return "GUARDIAN"
    if "boden" in desc_l or "bohr" in desc_l or "worm" in name_l: return "SILVERFISH"
    if "riesig" in desc_l or "boss" in desc_l or "colossus" in name_l or "titan" in name_l: return "IRON_GOLEM"
    if "flieg" in desc_l or "vogel" in desc_l or "hawk" in name_l or "gull" in name_l or "moth" in name_l: return "PHANTOM"
    if "konstrukt" in desc_l or "holz" in desc_l or "construct" in name_l: return "IRON_GOLEM"
    if "djinn" in name_l or "elemental" in name_l: return "BLAZE"

    return "ZOMBIE"

def get_stats(mob, desc):
    desc_l = desc.lower()
    name_l = mob.lower()

    # Scale
    hp = 100
    damage = 5
    speed = 0.25

    if mob in world_bosses or "weltenboss" in desc_l:
        hp = 25000
        damage = 25
    elif mob in bosses or "boss" in desc_l:
        hp = 5000
        damage = 20
    elif "elite" in desc_l or "miniboss" in desc_l:
        hp = 300
        damage = 12
    elif "passiv" in desc_l or "npc" in desc_l:
        hp = 30
        damage = 0
    else:
        hp = 80
        damage = 5

    if "langsam" in desc_l or "golem" in name_l or "panzer" in desc_l or "crab" in name_l:
        speed = 0.18
    elif "schnell" in desc_l or "ninja" in name_l or "stalker" in name_l:
        speed = 0.40
    elif "wraith" in name_l or "phantom" in name_l or "sehr schnell" in desc_l:
        speed = 0.55

    return hp, damage, speed

def get_skills(mob, desc):
    desc_l = desc.lower()
    name_l = mob.lower()

    skills = []
    if "vergift" in desc_l or "gift" in desc_l or "spore" in name_l:
        skills.append("  - potion{type=POISON;duration=100;amplifier=1} @Target ~onAttack")
    if "slow" in desc_l or "einfrier" in desc_l or "eis" in desc_l or "ice" in name_l:
        skills.append("  - potion{type=SLOWNESS;duration=80;amplifier=2} @Target ~onAttack")
    if "teleport" in desc_l:
        skills.append("  - teleport{} @Self ~onDamaged{healthPercent<50}")
    if "unsichtbar" in desc_l or "tarn" in desc_l or "camouflage" in desc_l:
        skills.append("  - potion{type=INVISIBILITY;duration=200;amplifier=0} @Self ~onSpawn")
        skills.append("  - potion{type=INVISIBILITY;duration=200;amplifier=0} @Self ~onTimer:200")
    if "heilt" in desc_l:
        skills.append("  - heal{amount=5} @Self ~onTimer:60")
    if "knockback" in desc_l:
        skills.append("  - setvar{var=caster.noKnockback;value=true} @Self ~onSpawn")
    if "aoe" in desc_l or "schrei" in desc_l:
        skills.append("  - damage{amount=8;radius=5} @PIR{r=5} ~onTimer:120")
    if "beschwört" in desc_l or "summon" in desc_l:
        skills.append("  - summon{type=ZOMBIE;amount=2;radius=3} @Self ~onDamaged{healthPercent<30}")
    if "sturzflug" in desc_l or "drache" in desc_l or "hawk" in name_l:
        skills.append("  - projectile{type=ARROW;v=2.5;onTick=[potion{type=SLOWNESS;d=40} @Target]} @Target ~onTimer:80")
    if "stiehlt" in desc_l or "xp" in desc_l:
        skills.append("  - setvar{var=target.xp;value=-50} @Target ~onAttack")
    if "wirft" in desc_l or "brocken" in desc_l:
        skills.append("  - projectile{type=SNOWBALL;v=1.5;damage=8} @Target ~onTimer:100")
    if "lava" in desc_l and "pfade" in desc_l:
        skills.append("  - blockwave{material=MAGMA_BLOCK;radius=2;duration=100} @Self ~onTimer:60")
    if "nachts" in desc_l and "aktiv" in desc_l:
        skills.append("  - message{m='<&7>Der Mob verschwindet im Tageslicht...'} @Self ~onTimer:20")
        skills.append("  - remove{} @Self onConditions[isDay=true] ~onTimer:20")
    if "rudel" in desc_l or "gruppe" in desc_l:
        skills.append("  - summon{type=self;amount=2;radius=10} @Self ~onFirstSpawn")
    if "spiegel" in desc_l or "reflektiert" in desc_l:
        skills.append("  - damage{amount=3} @Attacker ~onDamaged")
    if "belebt" in desc_l or "ersteht" in desc_l:
        skills.append("  - heal{amount=full} @Self ~onDeath")
        skills.append("  - message{m='<&c>Er ersteht wieder!'} @World ~onDeath")

    if len(skills) < 2:
        skills.append("  - damage{amount=2} @Target ~onAttack")
        skills.append("  - effect:particles{p=crit;a=10} @Self ~onDamaged")

    return skills

for cat, mobs in categories.items():
    with open(f"plugins/MythicMobs/Mobs/geoworld_{cat}.yml", "w") as f:
        f.write(f"# GeoWorld MythicMobs – {cat.capitalize()}\n")
        f.write("# Generiert aus docs/CUSTOM_BIOMES.md\n")
        f.write(f"# Ablageort: plugins/MythicMobs/Mobs/geoworld_{cat}.yml\n\n")

        for mob in mobs:
            desc = mobs_desc.get(mob, "")

            mtype = get_base_type(mob, desc)
            hp, dmg, speed = get_stats(mob, desc)
            silent = "true" if ("statue" in mob.lower() or "schlafend" in desc.lower()) else "false"

            f.write(f"{mob}:\n")
            f.write(f"  Type: {mtype}\n")
            f.write(f"  Display: '<&6>{mob}'\n")
            f.write(f"  Health: {hp}\n")
            f.write(f"  Damage: {dmg}\n")
            f.write(f"  Armor: 0\n")
            f.write(f"  Options:\n")
            f.write(f"    MovementSpeed: {speed}\n")
            f.write(f"    Despawn: false\n")
            f.write(f"    PreventOtherDrops: true\n")
            f.write(f"    PreventItemPickup: true\n")
            f.write(f"    Silent: {silent}\n")
            f.write(f"  Faction: GeoWorld\n")

            f.write("  Skills:\n")
            for skill in get_skills(mob, desc):
                f.write(f"{skill}\n")

            f.write("  Drops:\n")
            drops = drops_mapping.get(cat, ["STONE 1 1"])
            for d in drops:
                f.write(f"    - {d}\n")
            if mob in bosses:
                for d in boss_drops:
                    f.write(f"    - {d}\n")

            f.write("  LevelModifiers:\n")
            f.write("    Health: 0.5\n")
            f.write("    Damage: 0.25\n")

            if mob in bosses:
                f.write("  BossBar:\n")
                f.write("    Enabled: true\n")
                f.write(f"    Title: '<&4>{mob}'\n")
                f.write("    Range: 64\n")
                f.write("    Color: RED\n")
                f.write("    Style: SEGMENTED_10\n")

            f.write("\n")

        f.write("RandomSpawning:\n")
        for mob in mobs:
            f.write(f"  {mob}:\n")
            f.write(f"    Enabled: false\n")
            f.write(f"    Worlds: world\n")
            f.write(f"    Biomes: {biome_mapping.get(cat, 'PLAINS')}\n")
            f.write(f"    Chance: 0.05\n")
            f.write(f"    MaxMobsPerChunk: 2\n")
