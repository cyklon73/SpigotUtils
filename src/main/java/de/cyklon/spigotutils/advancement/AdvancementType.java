package de.cyklon.spigotutils.advancement;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;

public sealed interface AdvancementType permits CustomAdvancement, DefaultAdvancement {

	String getKey();
	String getNamespace();
	Category getCategory();

	NamespacedKey getNamespacedKey();

	default Advancement getAdvancement() {
		return Bukkit.getAdvancement(getNamespacedKey());
	}


	static AdvancementType getAdvancementType(Advancement advancement) {
		return getAdvancementType(advancement.getKey());
	}

	static AdvancementType getAdvancementType(NamespacedKey key) {
		for (DefaultAdvancement value : DefaultAdvancement.values()) if (key.equals(value.getNamespacedKey())) return value;
		return new CustomAdvancement(key);
	}

	enum Category {
		NETHER,
		END,
		STORY,
		HUSBANDRY,
		ADVENTURE,
		CUSTOM
	}

}
