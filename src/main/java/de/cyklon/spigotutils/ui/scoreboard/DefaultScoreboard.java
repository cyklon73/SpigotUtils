package de.cyklon.spigotutils.ui.scoreboard;

import de.cyklon.spigotutils.tuple.Pair;
import de.cyklon.spigotutils.version.MinecraftVersion;
import de.cyklon.spigotutils.version.Version;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_20_R1.CraftOfflinePlayer;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

abstract class DefaultScoreboard<T> implements ScoreboardUI<T> {

	static final Collection<ScoreboardUI<?>> SCOREBOARDS = new ArrayList<>();

	private static final String NM_PACKAGE = "net.minecraft";
	private static final String OBC_PACKAGE = "org.bukkit.craftbukkit";
	private static final String NMS_PACKAGE = NM_PACKAGE + ".server";

	private static final String VERSION_NAME = Bukkit.getServer().getClass().getPackage().getName().substring(OBC_PACKAGE.length() + 1);

	private static final MethodType VOID_METHOD_TYPE = MethodType.methodType(void.class);
	private static final boolean NMS_REPACKAGED = optionalClass(NM_PACKAGE + ".network.protocol.Packet").isPresent();

	private static volatile Object theUnsafe;


	private static final Map<Class<?>, Field[]> PACKETS = new HashMap<>(8);
	protected static final String[] COLOR_CODES = Arrays.stream(ChatColor.values())
			.map(Object::toString)
			.toArray(String[]::new);
	private static final int LIMIT = COLOR_CODES.length-10;

	//Current Server version
	protected static final MinecraftVersion VERSION;


	//Packets
	private static final PacketConstructor PACKET_SB_OBJ;
	private static final PacketConstructor PACKET_SB_DISPLAY_OBJ;
	private static final PacketConstructor PACKET_SB_SCORE;
	private static final PacketConstructor PACKET_SB_TEAM;
	private static final PacketConstructor PACKET_SB_SERIALIZABLE_TEAM;

	// Packets and components
	private static final Class<?> CHAT_COMPONENT_CLASS;
	private static final Class<?> CHAT_FORMAT_ENUM;
	private static final Object RESET_FORMATTING;
	private static final MethodHandle PLAYER_CONNECTION;
	private static final MethodHandle SEND_PACKET;
	private static final MethodHandle PLAYER_GET_HANDLE;

	// enums
	private static final Class<?> DISPLAY_SLOT_TYPE;
	private static final Class<?> ENUM_SB_HEALTH_DISPLAY;
	private static final Class<?> ENUM_SB_ACTION;
	private static final Object SIDEBAR_DISPLAY_SLOT;
	private static final Object ENUM_SB_HEALTH_DISPLAY_INTEGER;
	private static final Object ENUM_SB_ACTION_CHANGE;
	private static final Object ENUM_SB_ACTION_REMOVE;


	static {
		try {
			MethodHandles.Lookup lookup = MethodHandles.lookup();

			VERSION = Version.getVersion(Bukkit.getServer());

			String gameProtocolPackage = "network.protocol.game";
			Class<?> craftPlayerClass = obcClass("entity.CraftPlayer");
			Class<?> entityPlayerClass = nmsClass("server.level", "EntityPlayer");
			Class<?> playerConnectionClass = nmsClass("server.network", "PlayerConnection");
			Class<?> packetClass = nmsClass("network.protocol", "Packet");
			Class<?> packetSbObjClass = nmsClass(gameProtocolPackage, "PacketPlayOutScoreboardObjective");
			Class<?> packetSbDisplayObjClass = nmsClass(gameProtocolPackage, "PacketPlayOutScoreboardDisplayObjective");
			Class<?> packetSbScoreClass = nmsClass(gameProtocolPackage, "PacketPlayOutScoreboardScore");
			Class<?> packetSbTeamClass = nmsClass(gameProtocolPackage, "PacketPlayOutScoreboardTeam");
			Class<?> sbTeamClass = MinecraftVersion.v1_17.isHigherOrEqual(VERSION)
					? innerClass(packetSbTeamClass, innerClass -> !innerClass.isEnum()) : null;
			Field playerConnectionField = Arrays.stream(entityPlayerClass.getFields())
					.filter(field -> field.getType().isAssignableFrom(playerConnectionClass))
					.findFirst().orElseThrow(NoSuchFieldException::new);
			Method sendPacketMethod = Stream.concat(
							Arrays.stream(playerConnectionClass.getSuperclass().getMethods()),
							Arrays.stream(playerConnectionClass.getMethods())
					)
					.filter(m -> m.getParameterCount() == 1 && m.getParameterTypes()[0] == packetClass)
					.findFirst().orElseThrow(NoSuchMethodException::new);

			Optional<Class<?>> displaySlotEnum = nmsOptionalClass("world.scores", "DisplaySlot");
			CHAT_COMPONENT_CLASS = nmsClass("network.chat", "IChatBaseComponent");
			CHAT_FORMAT_ENUM = nmsClass(null, "EnumChatFormat");
			DISPLAY_SLOT_TYPE = displaySlotEnum.orElse(int.class);
			RESET_FORMATTING = enumValueOf(CHAT_FORMAT_ENUM, "RESET", 21);
			SIDEBAR_DISPLAY_SLOT = displaySlotEnum.isPresent() ? enumValueOf(DISPLAY_SLOT_TYPE, "SIDEBAR", 1) : 1;
			PLAYER_GET_HANDLE = lookup.findVirtual(craftPlayerClass, "getHandle", MethodType.methodType(entityPlayerClass));
			PLAYER_CONNECTION = lookup.unreflectGetter(playerConnectionField);
			SEND_PACKET = lookup.unreflect(sendPacketMethod);
			PACKET_SB_OBJ = findPacketConstructor(packetSbObjClass, lookup);
			PACKET_SB_DISPLAY_OBJ = findPacketConstructor(packetSbDisplayObjClass, lookup);
			PACKET_SB_SCORE = findPacketConstructor(packetSbScoreClass, lookup);
			PACKET_SB_TEAM = findPacketConstructor(packetSbTeamClass, lookup);
			PACKET_SB_SERIALIZABLE_TEAM = sbTeamClass == null ? null : findPacketConstructor(sbTeamClass, lookup);

			for (Class<?> clazz : Arrays.asList(packetSbObjClass, packetSbDisplayObjClass, packetSbScoreClass, packetSbTeamClass, sbTeamClass)) {
				if (clazz == null) {
					continue;
				}
				Field[] fields = Arrays.stream(clazz.getDeclaredFields())
						.filter(field -> !Modifier.isStatic(field.getModifiers()))
						.toArray(Field[]::new);
				for (Field field : fields) {
					field.setAccessible(true);
				}
				PACKETS.put(clazz, fields);
			}

			if (MinecraftVersion.v1_8.isHigherOrEqual(VERSION)) {
				String enumSbActionClass = MinecraftVersion.v1_13.isHigherOrEqual(VERSION)
						? "ScoreboardServer$Action"
						: "PacketPlayOutScoreboardScore$EnumScoreboardAction";
				ENUM_SB_HEALTH_DISPLAY = nmsClass("world.scores.criteria", "IScoreboardCriteria$EnumScoreboardHealthDisplay");
				ENUM_SB_ACTION = nmsClass("server", enumSbActionClass);
				ENUM_SB_HEALTH_DISPLAY_INTEGER = enumValueOf(ENUM_SB_HEALTH_DISPLAY, "INTEGER", 0);
				ENUM_SB_ACTION_CHANGE = enumValueOf(ENUM_SB_ACTION, "CHANGE", 0);
				ENUM_SB_ACTION_REMOVE = enumValueOf(ENUM_SB_ACTION, "REMOVE", 1);
			} else {
				ENUM_SB_HEALTH_DISPLAY = null;
				ENUM_SB_ACTION = null;
				ENUM_SB_HEALTH_DISPLAY_INTEGER = null;
				ENUM_SB_ACTION_CHANGE = null;
				ENUM_SB_ACTION_REMOVE = null;
			}
		} catch (Throwable t) {
			throw new ExceptionInInitializerError(t);
		}
	}


	private final String id;
	private final List<Player> players;

	private final Map<Integer, Pair<T, Integer>> lines = new HashMap<>();
	private Map<Integer, Pair<T, Integer>> newLines = null;
	private T title = emptyLine();

	private boolean deleted = false;

	protected DefaultScoreboard(@NotNull Player... players) {
		this.players = new ArrayList<>(List.of(players));
		this.id = "scui-" + Long.toHexString(System.currentTimeMillis());
		try {
			sendPacket(objectivePacket(ObjectiveMode.CREATE));
			sendPacket(displayObjectivePacket());
		} catch (Throwable t) {
			throw new RuntimeException("Unable to create scoreboard", t);
		}
		SCOREBOARDS.add(this);
	}

	protected boolean addPlayer(Player player) {
		if (contains(player)) return false;
		players.add(player);
		return true;
	}

	protected void show(Player player) {
        System.out.println("show to Player: " + player.getName() + " (" + player.getUniqueId() + ")");
		try {
			sendPacket(player, objectivePacket(ObjectiveMode.CREATE));
			sendPacket(player, displayObjectivePacket());

			Map<Integer, Pair<T, Integer>> topLines = getTopEntries(this.lines, LIMIT);

			System.out.println("toplines:");
			System.out.println("s: " + topLines.size());
			System.out.println(topLines);
			int i = 0;
			for (Integer score : topLines.keySet()) {
				int i1 = topLines.get(score).second();
				if (i1==-1) i1 = i;
				i++;
				sendPacket(scorePacket(score, ScoreboardAction.CHANGE, i1));
				sendPacket(teamPacket(score, TeamMode.CREATE, i1));
			}

			for (Integer score : topLines.keySet()) {
				sendLineChange(score, topLines.get(score).second());
			}
		} catch (Throwable t) {
			throw new RuntimeException("Unable to show for player '" + player.getName() + "'", t);
		}
	}

	protected boolean removePlayer(Player player) {
		if (!contains(player)) return false;
		players.remove(player);
		return true;
	}

	protected void hide(Player player) {
		try {
			for (Integer score : lines.keySet()) {
				int i = lines.get(score).second();
				if (i!=-1) sendPacket(player, teamPacket(score, TeamMode.REMOVE, i));
			}
			sendPacket(player, objectivePacket(ObjectiveMode.REMOVE));
		} catch (Throwable t) {
			throw new RuntimeException("Unable to hide for player '" + player.getName() + "'", t);
		}
	}

	protected List<Player> getPlayers() {
		return players;
	}

	@Override
	public void setTitle(T title) {
		if (this.title.equals(title)) return;
		this.title = title;
		try {
			sendPacket(objectivePacket(ObjectiveMode.UPDATE));
		} catch (Throwable t) {
			throw new RuntimeException("Unable to update scoreboard title", t);
		}
	}

	@Override
	public T getTitle() {
		return this.title;
	}

	@Override
	public void setLine(int score, T text) {
		try {
			if (newLines==null) newLines = new HashMap<>(lines);
			newLines.put(score, new Pair<>(text, -1));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setEmptyLine(int score) {
		setLine(score, emptyLine());
	}

	@Override
	public T getLine(int score) {
		return lines.get(score).first();
	}

	@Override
	public List<T> getLines() {
		return new ArrayList<>(lines.values()).stream().map(Pair::first).toList();
	}

	@Override
	public void removeLine(int score) {
		if (newLines==null) newLines = new HashMap<>(lines);
		newLines.remove(score);
	}

	@Override
	public void clearLines() {
		newLines = new HashMap<>();
	}

	@Override
	public void update() {
		if (newLines!=null) {
			updateLines(newLines);
			newLines = null;
		}
	}

	protected synchronized void updateLines(Map<Integer, Pair<T, Integer>> lines) {
		Objects.requireNonNull(lines, "lines");

		Map<Integer, Pair<T, Integer>> oldLines = new HashMap<>(this.lines);
		this.lines.clear();
		this.lines.putAll(lines);

		Map<Integer, Pair<T, Integer>> topLines = getTopEntries(this.lines, LIMIT);

		try {
			for (Integer score : oldLines.keySet()) {
				if (!topLines.containsKey(score)) {
					int i = oldLines.get(score).second();
					if (i==-1) i = 0;
					sendPacket(teamPacket(score, TeamMode.REMOVE, i));
					sendPacket(scorePacket(score, ScoreboardAction.REMOVE, i));
				}
			}
			int i = 0;
			for (Integer score : topLines.keySet()) {
				if (!oldLines.containsKey(score)) {
					this.lines.put(score, new Pair<>(topLines.get(score).first(), i));
					sendPacket(scorePacket(score, ScoreboardAction.CHANGE, i));
					sendPacket(teamPacket(score, TeamMode.CREATE, i++));
				}
			}

			for (Integer score : topLines.keySet()) {
				if (!topLines.get(score).equals(oldLines.get(score))) sendLineChange(score, this.lines.get(score).second());
			}
		} catch (Throwable t) {
			throw new RuntimeException("Unable to update scoreboard lines", t);
		}
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public boolean isDeleted() {
		return deleted;
	}

	@Override
	public int size() {
		return lines.size();
	}

	@Override
	public void delete() {
		try {
			for (Integer score : lines.keySet()) {
				int i = lines.get(score).second();
				if (i!=-1) sendPacket(teamPacket(score, TeamMode.REMOVE, i));
			}
			sendPacket(objectivePacket(ObjectiveMode.REMOVE));
			SCOREBOARDS.remove(this);
			this.deleted = true;
		} catch (Throwable t) {
			throw new RuntimeException("Unable to delete scoreboard", t);
		}
	}

	protected abstract void sendLineChange(int score, int index) throws Throwable;
	protected abstract Object toMinecraftComponent(T line) throws Throwable;
	protected abstract String serializeLine(T line);
	protected abstract T emptyLine();

	private Object objectivePacket(ObjectiveMode mode) throws Throwable {
		Object packet = PACKET_SB_OBJ.invoke();

		setField(packet, String.class, this.id);
		setField(packet, int.class, mode.ordinal());

		if (mode != ObjectiveMode.REMOVE) {
			setComponentField(packet, this.title, 1);

			if (MinecraftVersion.v1_8.isHigherOrEqual(VERSION)) {
				setField(packet, ENUM_SB_HEALTH_DISPLAY, ENUM_SB_HEALTH_DISPLAY_INTEGER);
			}
		} else if (VERSION.name().contains("v1_7")) {
			setField(packet, String.class, "", 1);
		}
		return packet;
	}

	private Object displayObjectivePacket() throws Throwable {
		Object packet = PACKET_SB_DISPLAY_OBJ.invoke();

		setField(packet, DISPLAY_SLOT_TYPE, SIDEBAR_DISPLAY_SLOT); // Position
		setField(packet, String.class, this.id); // Score Name
		return packet;
	}

	private Object scorePacket(int score, ScoreboardAction action, int index) throws Throwable {
		Object packet = PACKET_SB_SCORE.invoke();

		setField(packet, String.class, COLOR_CODES[index], 0); // Player Name

		if (MinecraftVersion.v1_8.isHigherOrEqual(VERSION)) {
			Object enumAction = action == ScoreboardAction.REMOVE
					? ENUM_SB_ACTION_REMOVE : ENUM_SB_ACTION_CHANGE;
			setField(packet, ENUM_SB_ACTION, enumAction);
		} else {
			setField(packet, int.class, action.ordinal(), 1); // Action
		}

		if (action == ScoreboardAction.CHANGE) {
			setField(packet, String.class, this.id, 1); // Objective Name
			setField(packet, int.class, score); // Score
		}

		return packet;
	}

	private Object teamPacket(int score, TeamMode mode, int index) throws Throwable {
		return teamPacket(score, mode, index, null, null);
	}

	protected Object teamPacket(int score, TeamMode mode, int index, T prefix, T suffix)
			throws Throwable {
		if (mode == TeamMode.ADD_PLAYERS || mode == TeamMode.REMOVE_PLAYERS) {
			throw new UnsupportedOperationException();
		}

		Object packet = PACKET_SB_TEAM.invoke();

		setField(packet, String.class, this.id + ':' + score); // Team name
		setField(packet, int.class, mode.ordinal(), VERSION.name().contains("v1_8") ? 1 : 0); // Update mode

		if (mode == TeamMode.REMOVE) return packet;

		if (MinecraftVersion.v1_17.isHigherOrEqual(VERSION)) {
			Object team = PACKET_SB_SERIALIZABLE_TEAM.invoke();
			// Since the packet is initialized with null values, we need to change more things.
			setComponentField(team, null, 0); // Display name
			setField(team, CHAT_FORMAT_ENUM, RESET_FORMATTING); // Color
			setComponentField(team, prefix, 1); // Prefix
			setComponentField(team, suffix, 2); // Suffix
			setField(team, String.class, "always", 0); // Visibility
			setField(team, String.class, "always", 1); // Collisions
			setField(packet, Optional.class, Optional.of(team));
		} else {
			setComponentField(packet, prefix, 2); // Prefix
			setComponentField(packet, suffix, 3); // Suffix
			setField(packet, String.class, "always", 4); // Visibility for 1.8+
			setField(packet, String.class, "always", 5); // Collisions for 1.9+
		}

		if (mode == TeamMode.CREATE) {
			setField(packet, Collection.class, Collections.singletonList(COLOR_CODES[index])); // Players in the team
		}

		return packet;
	}

	protected boolean contains(Player player) {
		return contains(player.getUniqueId());
	}

	protected boolean contains(UUID uuid) {
		for (Player player : players) if (player.getUniqueId().equals(uuid)) return true;
		return false;
	}

	protected void sendPacket(Object packet) throws Throwable {
		if (this.deleted) {
			throw new IllegalStateException("This Scoreboard is deleted");
		}
		for (Player player : players) {
			sendPacket(player, packet);
		}
	}

	private void sendPacket(Player player, Object packet) throws Throwable {
		if (this.deleted) {
			throw new IllegalStateException("This Scoreboard is deleted");
		}
		if (player.isOnline()) {
			Object entityPlayer = PLAYER_GET_HANDLE.invoke(player);
			Object playerConnection = PLAYER_CONNECTION.invoke(entityPlayer);
			SEND_PACKET.invoke(playerConnection, packet);
		}
	}

	private void setField(Object object, Class<?> fieldType, Object value)
			throws ReflectiveOperationException {
		setField(object, fieldType, value, 0);
	}

	private void setField(Object packet, Class<?> fieldType, Object value, int count)
			throws ReflectiveOperationException {
		int i = 0;
		for (Field field : PACKETS.get(packet.getClass())) {
			if (field.getType() == fieldType && count == i++) {
				field.set(packet, value);
			}
		}
	}

	private void setComponentField(Object packet, T value, int count) throws Throwable {
		if (!MinecraftVersion.v1_13.isHigherOrEqual(VERSION)) {
			String line = value != null ? serializeLine(value) : "";
			setField(packet, String.class, line, count);
			return;
		}

		int i = 0;
		for (Field field : PACKETS.get(packet.getClass())) {
			if ((field.getType() == String.class || field.getType() == CHAT_COMPONENT_CLASS) && count == i++) {
				field.set(packet, toMinecraftComponent(value));
			}
		}
	}

	private Map<Integer, Pair<T, Integer>> getTopEntries(Map<Integer, Pair<T, Integer>> map, int limit) {
		List<Map.Entry<Integer, Pair<T, Integer>>> entryList = new ArrayList<>(map.entrySet());
		entryList.sort((e1, e2) -> e2.getKey().compareTo(e1.getKey()));
		if (entryList.size()>limit) entryList = entryList.subList(0, limit);
		return entryList.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private enum ObjectiveMode {
		CREATE, REMOVE, UPDATE
	}

	protected enum TeamMode {
		CREATE, REMOVE, UPDATE, ADD_PLAYERS, REMOVE_PLAYERS
	}

	private enum ScoreboardAction {
		CHANGE, REMOVE
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ScoreboardUI<?> scui) return scui.getId().equals(getId());
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "ScoreboardUI{id=%s}".formatted(getId());
	}

	private static String nmsClassName(String post1_17package, String className) {
		if (NMS_REPACKAGED) {
			String classPackage = post1_17package == null ? NM_PACKAGE : NM_PACKAGE + '.' + post1_17package;
			return classPackage + '.' + className;
		}
		return NMS_PACKAGE + '.' + VERSION_NAME + '.' + className;
	}

	private static Class<?> nmsClass(String post1_17package, String className) throws ClassNotFoundException {
		return Class.forName(nmsClassName(post1_17package, className));
	}

	private static Optional<Class<?>> nmsOptionalClass(String post1_17package, String className) {
		return optionalClass(nmsClassName(post1_17package, className));
	}

	private static String obcClassName(String className) {
		return OBC_PACKAGE + '.' + VERSION_NAME + '.' + className;
	}

	protected static Class<?> obcClass(String className) throws ClassNotFoundException {
		return Class.forName(obcClassName(className));
	}

	private static Optional<Class<?>> optionalClass(String className) {
		try {
			return Optional.of(Class.forName(className));
		} catch (ClassNotFoundException e) {
			return Optional.empty();
		}
	}

	private static Object enumValueOf(Class<?> enumClass, String enumName) {
		return Enum.valueOf(enumClass.asSubclass(Enum.class), enumName);
	}

	private static Object enumValueOf(Class<?> enumClass, String enumName, int fallbackOrdinal) {
		try {
			return enumValueOf(enumClass, enumName);
		} catch (IllegalArgumentException e) {
			Object[] constants = enumClass.getEnumConstants();
			if (constants.length > fallbackOrdinal) {
				return constants[fallbackOrdinal];
			}
			throw e;
		}
	}

	private static Class<?> innerClass(Class<?> parentClass, Predicate<Class<?>> classPredicate) throws ClassNotFoundException {
		for (Class<?> innerClass : parentClass.getDeclaredClasses()) {
			if (classPredicate.test(innerClass)) {
				return innerClass;
			}
		}
		throw new ClassNotFoundException("No class in " + parentClass.getCanonicalName() + " matches the predicate.");
	}

	private static PacketConstructor findPacketConstructor(Class<?> packetClass, MethodHandles.Lookup lookup) throws Exception {
		try {
			MethodHandle constructor = lookup.findConstructor(packetClass, VOID_METHOD_TYPE);
			return constructor::invoke;
		} catch (NoSuchMethodException | IllegalAccessException e) {
			// try below with Unsafe
		}

		if (theUnsafe == null) {
			synchronized (DefaultScoreboard.class) {
				if (theUnsafe == null) {
					Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
					Field theUnsafeField = unsafeClass.getDeclaredField("theUnsafe");
					theUnsafeField.setAccessible(true);
					theUnsafe = theUnsafeField.get(null);
				}
			}
		}

		MethodType allocateMethodType = MethodType.methodType(Object.class, Class.class);
		MethodHandle allocateMethod = lookup.findVirtual(theUnsafe.getClass(), "allocateInstance", allocateMethodType);
		return () -> allocateMethod.invoke(theUnsafe, packetClass);
	}

	@FunctionalInterface
	protected interface PacketConstructor {
		Object invoke() throws Throwable;
	}
}
