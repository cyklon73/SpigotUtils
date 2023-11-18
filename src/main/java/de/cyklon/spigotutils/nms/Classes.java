package de.cyklon.spigotutils.nms;

import static de.cyklon.spigotutils.nms.NMSReflection.nmsClass;
import static de.cyklon.spigotutils.nms.NMSReflection.obcClass;

public final class Classes {

	public static final class org {
		public static final class bukkit {
			public static final class craftbukkit {
				public static final class inventory {
					public static final Class<?> CraftItemStack;

					static {
						try {
							CraftItemStack = obcClass("inventory.CraftItemStack");
						} catch (Exception e) {
							throw new ExceptionInInitializerError(e);
						}
					}
				}

				public static final class entity {
					public static final Class<?> CraftPlayer;

					static {
						try {
							CraftPlayer = obcClass("entity.CraftPlayer");;
						} catch (Exception e) {
							throw new ExceptionInInitializerError(e);
						}
					}
				}
			}
		}
	}

	public static final class net {
		public static final class minecraft {
			public static final class server {
				public static final class level {
					public static final Class<?> EntityPlayer;

					static {
						try {
							EntityPlayer = nmsClass("server.level", "EntityPlayer");
						} catch (Exception e) {
							throw new ExceptionInInitializerError(e);
						}
					}
				}

				public static final class network {
					public static final Class<?> PlayerConnection;

					static {
						try {
							PlayerConnection = nmsClass("server.network", "PlayerConnection");
						} catch (Exception e) {
							throw new ExceptionInInitializerError(e);
						}
					}
				}
			}

			public static final class network {
				public static final class protocol {
					public static final Class<?> Packet;

					static {
						try {
							Packet = nmsClass("network.protocol", "Packet");
						} catch (Exception e) {
							throw new ExceptionInInitializerError(e);
						}
					}

					public static final class game {
						public static final Class<?> PacketPlayOutScoreboardObjective;
						public static final Class<?> PacketPlayOutScoreboardDisplayObjective;
						public static final Class<?> PacketPlayOutScoreboardScore;
						public static final Class<?> PacketPlayOutScoreboardTeam;

						static {
							try {
								String gameProtocolPackage = "network.protocol.game";
								PacketPlayOutScoreboardObjective = nmsClass(gameProtocolPackage, "PacketPlayOutScoreboardObjective");
								PacketPlayOutScoreboardDisplayObjective = nmsClass(gameProtocolPackage, "PacketPlayOutScoreboardDisplayObjective");
								PacketPlayOutScoreboardScore = nmsClass(gameProtocolPackage, "PacketPlayOutScoreboardScore");
								PacketPlayOutScoreboardTeam = nmsClass(gameProtocolPackage, "PacketPlayOutScoreboardTeam");
							} catch (Exception e) {
								throw new ExceptionInInitializerError(e);
							}
						}
					}
				}
			}
			public static final class world {
				public static final class item {
					public static final Class<?> ItemStack;

					static {
						try {
							ItemStack = nmsClass("world.item", "ItemStack");
						} catch (Exception e) {
							throw new ExceptionInInitializerError(e);
						}
					}
				}
			}
			public static final class nbt {
				public static final Class<?> NBTTagCompound;

				static {
					try {
						NBTTagCompound = nmsClass("nbt", "NBTTagCompound");
					} catch (Exception e) {
						throw new ExceptionInInitializerError(e);
					}
				}
			}
		}
	}

	static {
		try {

		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

}
