package de.cyklon.spigotutils.advancement;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;

public enum AdvancementType {


    //NETHER
    NETHER(Category.NETHER, "root"),
    WHO_IS_CUTTING_ONIONS(Category.NETHER, "obtain_crying_obsidian"),
    OH_SHINY(Category.NETHER, "distract_piglin"),
    A_FURIOUS_COCKTAIL(Category.NETHER, "all_potions"),
    BRING_HOME_THE_BEACON(Category.NETHER, "create_beacon"),
    LOCAL_BREWERY(Category.NETHER, "brew_potion"),
    HOT_TOURIST_DESTINATIONS(Category.NETHER, "explore_nether"),
    THIS_BOAT_HAS_LEGS(Category.NETHER, "ride_strider"),
    HOW_DID_WE_GET_HERE(Category.NETHER, "all_effects"),
    SPOOKY_SCARY_SKELETON(Category.NETHER, "get_wither_skull"),
    INTO_FIRE(Category.NETHER, "obtain_blaze_rod"),
    WAR_PIGS(Category.NETHER, "loot_bastion"),
    NOT_QUITE_NINE_LIVES(Category.NETHER, "charge_respawn_anchor"),
    RETURN_TO_SENDER(Category.NETHER, "return_to_sender"),
    THOSE_WERE_THE_DAYS(Category.NETHER, "find_bastion"),
    FEELS_LIKE_HOME(Category.NETHER, "ride_strider_in_overworld_lava"),
    HIDDEN_IN_THE_DEPTHS(Category.NETHER, "obtain_ancient_debris"),
    BEACONATOR(Category.NETHER, "create_full_beacon"),
    WITHERING_HEIGHTS(Category.NETHER, "summon_wither"),
    SUBSPACE_BUBBLE(Category.NETHER, "fast_travel"),
    COUNTRY_LODE_TAKE_ME_HOME(Category.NETHER, "use_lodestone"),
    UNEASY_ALLIANCE(Category.NETHER, "uneasy_alliance"),
    A_TERRIBLE_FORTRESS(Category.NETHER, "find_fortress"),
    COVER_ME_IN_DEBRIS(Category.NETHER, "netherite_armor"),


    //END
    THE_END(Category.END, "root"),
    GREAT_VIEW_FROM_UP_HERE(Category.END, "levitate"),
    FREE_THE_END(Category.END, "kill_dragon"),
    THE_NEXT_GENERATION(Category.END, "dragon_egg"),
    THE_CITY_AT_THE_END_OF_THE_GAME(Category.END, "find_end_city"),
    REMOTE_GETAWAY(Category.END, "enter_end_gateway"),
    THE_END_AGAIN(Category.END, "respawn_dragon"),
    SKYS_THE_LIMIT(Category.END, "elytra"),
    YOU_NEED_A_MINT(Category.END, "dragon_breath"),


    //STORY
    SUIT_UP(Category.STORY, "obtain_armor"),
    HOT_STUFF(Category.STORY, "lava_bucket"),
    NOT_TODAY_THANK_YOU(Category.STORY, "deflect_arrow"),
    ISNT_IT_IRON_PICK(Category.STORY, "iron_tools"),
    STONE_AGE(Category.STORY, "mine_stone"),
    WE_NEED_TO_GO_DEEPER(Category.STORY, "enter_the_nether"),
    GETTING_AN_UPGRADE(Category.STORY, "upgrade_tools"),
    ZOMBIE_DOCTOR(Category.STORY, "cure_zombie_villager"),
    ICE_BUCKET_CHALLENGE(Category.STORY, "form_obsidian"),
    ACQUIRE_HARDWARE(Category.STORY, "smelt_iron"),
    COVER_ME_WITH_DIAMONDS(Category.STORY, "shiny_gear"),
    ENCHANTER(Category.STORY, "enchant_item"),
    EYE_SPY(Category.STORY, "follow_ender_eye"),
    DIAMONDS(Category.STORY, "mine_diamond"),
    MINECRAFT(Category.STORY, "root"),
    THE_END_(Category.STORY, "enter_the_end"),


    //HUSBANDRY
    YOUVE_GOT_A_FRIEND_IN_ME(Category.HUSBANDRY, "allay_deliver_item_to_player"),
    WITH_OUR_POWERS_COMBINED(Category.HUSBANDRY, "froglights"),
    WHATEVER_FLOATS_YOUR_GOAT(Category.HUSBANDRY, "ride_a_boat_with_a_goat"),
    BEST_FRIENDS_FOREVER(Category.HUSBANDRY, "tame_an_animal"),
    GLOW_AND_BEHOLD(Category.HUSBANDRY, "make_a_sign_glow"),
    WHEN_THE_SQUAD_HOPS_INTO_TOWN(Category.HUSBANDRY, "leash_all_frog_variants"),
    FISHY_BUSINESS(Category.HUSBANDRY, "fishy_business"),
    TWO_BY_TWO(Category.HUSBANDRY, "bred_all_animals"),
    TACTICAL_FISHING(Category.HUSBANDRY, "tactical_fishing"),
    LITTLE_SNIFFS(Category.HUSBANDRY, "feed_snifflet"),
    TOTAL_BEELOCATION(Category.HUSBANDRY, "silk_touch_nest"),
    BUKKIT_BUKKIT(Category.HUSBANDRY, "tadpole_in_a_bucket"),
    WAX_OFF(Category.HUSBANDRY, "wax_off"),
    SMELLS_INTERESTING(Category.HUSBANDRY, "obtain_sniffer_egg"),
    SERIOUS_DEDICATION(Category.HUSBANDRY, "obtain_netherite_hoe"),
    PLANTING_THE_PAST(Category.HUSBANDRY, "plant_any_sniffer_seed"),
    A_SEEDY_PLACE(Category.HUSBANDRY, "plant_seed"),
    THE_CUTEST_PREDATOR(Category.HUSBANDRY, "axolotl_in_a_bucket"),
    BIRTHDAY_SONG(Category.HUSBANDRY, "allay_deliver_cake_to_note_block"),
    WAX_ON(Category.HUSBANDRY, "wax_on"),
    A_BALANCED_DIET(Category.HUSBANDRY, "balanced_diet"),
    HUSBANDRY(Category.HUSBANDRY, "root"),
    BEE_OUR_GUEST(Category.HUSBANDRY, "safely_harvest_honey"),
    THE_HEALING_POWER_OF_FRIENDSHIP(Category.HUSBANDRY, "kill_axolotl_target"),
    THE_PARROTS_AND_THE_BATS(Category.HUSBANDRY, "breed_an_animal"),
    A_COMPLETE_CATALOGUE(Category.HUSBANDRY, "complete_catalogue"),


    //ADVENTURE
    SMITHING_WITH_STYLE(Category.ADVENTURE, "trim_with_all_exclusive_armor_patterns"),
    VERY_VERY_FRIGHTENING(Category.ADVENTURE, "very_very_frightening"),
    SURGE_PROTECTOR(Category.ADVENTURE, "lightning_rod_with_villager_no_fire"),
    CAREFUL_RESTORATION(Category.ADVENTURE, "craft_decorated_pot_using_only_sherds"),
    IT_SPREADS(Category.ADVENTURE, "kill_mob_near_sculk_catalyst"),
    CAVES_AND_CLIFFS(Category.ADVENTURE, "fall_from_world_height"),
    SNIPER_DUEL(Category.ADVENTURE, "sniper_duel"),
    BULLSEYE(Category.ADVENTURE, "bullseye"),
    TWO_BIRDS_ONE_ARROW(Category.ADVENTURE, "two_birds_one_arrow"),
    WHOS_THE_PILLAGER_NOW(Category.ADVENTURE, "whos_the_pillager_now"),
    LIGHT_AS_A_RABBIT(Category.ADVENTURE, "walk_on_powder_snow_with_leather_boots"),
    RESPECTING_THE_REMNANTS(Category.ADVENTURE, "salvage_sherd"),
    TAKE_AIM(Category.ADVENTURE, "shoot_arrow"),
    ARBALISTIC(Category.ADVENTURE, "arbalistic"),
    HIRED_HELP(Category.ADVENTURE, "summon_iron_golem"),
    SNEAK_100(Category.ADVENTURE, "avoid_vibration"),
    SWEET_DREAMS(Category.ADVENTURE, "sleep_in_bed"),
    ADVENTURE(Category.ADVENTURE, "root"),
    MONSTERS_HUNTED(Category.ADVENTURE, "kill_all_mobs"),
    VOLUNTARY_EXILE(Category.ADVENTURE, "voluntary_exile"),
    IS_IT_A_BIRD(Category.ADVENTURE, "spyglass_at_parrot"),
    POSTMORTAL(Category.ADVENTURE, "totem_of_undying"),
    MONSTER_HUNTER(Category.ADVENTURE, "kill_a_mob"),
    ADVENTURING_TIME(Category.ADVENTURE, "adventuring_time"),
    IS_IT_A_PLANE(Category.ADVENTURE, "spyglass_at_dragon"),
    STAR_TRADER(Category.ADVENTURE, "trade_at_world_height"),
    SOUND_OF_MUSIC(Category.ADVENTURE, "play_jukebox_in_meadows"),
    HERO_OF_THE_VILLAGE(Category.ADVENTURE, "hero_of_the_village"),
    THE_POWER_OF_BOOKS(Category.ADVENTURE, "read_power_of_chiseled_bookshelf"),
    WHAT_A_DEAL(Category.ADVENTURE, "trade"),
    IS_IT_A_BALLOON(Category.ADVENTURE, "spyglass_at_ghast"),
    CRAFTING_A_NEW_LOOK(Category.ADVENTURE, "trim_with_any_armor_pattern"),
    A_THROWAWAY_JOKE(Category.ADVENTURE, "throw_trident"),
    STICKY_SITUATION(Category.ADVENTURE, "honey_block_slide"),
    OL_BETSY(Category.ADVENTURE, "ol_betsy");


    private final Category category;
    private final String key;

    AdvancementType(Category category, String key) {
        this.category = category;
        this.key = key;
    }

    public Category getCategory() {
        return category;
    }

    public String getKey() {
        return key;
    }

    public NamespacedKey getNamespacedKey() {
        return NamespacedKey.minecraft(String.format("%s/%s", category.name().toLowerCase(), key));
    }

    public Advancement getAdvancement() {
        return Bukkit.getAdvancement(getNamespacedKey());
    }

    public static enum Category {
        NETHER,
        END,
        STORY,
        HUSBANDRY,
        ADVENTURE
    }
}
