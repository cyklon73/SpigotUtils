package de.cyklon.spigotutils.persistence;

import com.google.common.base.Preconditions;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTCompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_20_R1.persistence.CraftPersistentDataAdapterContext;
import org.bukkit.craftbukkit.v1_20_R1.persistence.CraftPersistentDataContainer;
import org.bukkit.craftbukkit.v1_20_R1.persistence.CraftPersistentDataTypeRegistry;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;

public class PersistentDataFile implements PersistentDataContainer {

    private final Map<String, NBTBase> customDataTags;
    private final CraftPersistentDataTypeRegistry registry;
    private final CraftPersistentDataAdapterContext adapterContext;
    private final File file;

    public PersistentDataFile(File file) {
        Preconditions.checkArgument(file != null, "The File cannot be null");
        this.customDataTags = new HashMap<>();
        this.registry = new CraftPersistentDataTypeRegistry();
        this.adapterContext = new CraftPersistentDataAdapterContext(this.registry);
        this.file = file;
        try {
            file.createNewFile();
            try(FileInputStream fis = new FileInputStream(file)) {
                byte[] data = fis.readAllBytes();
                if (data.length!=0) readFromBytes(data, false);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T, Z> void set(NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
        Preconditions.checkArgument(key != null, "The NamespacedKey key cannot be null");
        Preconditions.checkArgument(type != null, "The provided type cannot be null");
        Preconditions.checkArgument(value != null, "The provided value cannot be null");
        this.customDataTags.put(key.toString(), this.registry.wrap(type.getPrimitiveType(), type.toPrimitive(value, this.adapterContext)));
        save();
    }

    public <T, Z> boolean has(NamespacedKey key, PersistentDataType<T, Z> type) {
        Preconditions.checkArgument(key != null, "The NamespacedKey key cannot be null");
        Preconditions.checkArgument(type != null, "The provided type cannot be null");
        NBTBase value = (NBTBase)this.customDataTags.get(key.toString());
        return value == null ? false : this.registry.isInstanceOf(type.getPrimitiveType(), value);
    }

    public <T, Z> Z get(NamespacedKey key, PersistentDataType<T, Z> type) {
        Preconditions.checkArgument(key != null, "The NamespacedKey key cannot be null");
        Preconditions.checkArgument(type != null, "The provided type cannot be null");
        NBTBase value = (NBTBase)this.customDataTags.get(key.toString());
        return value == null ? null : type.fromPrimitive(this.registry.extract(type.getPrimitiveType(), value), this.adapterContext);
    }

    public <T, Z> Z getOrDefault(NamespacedKey key, PersistentDataType<T, Z> type, Z defaultValue) {
        Z z = this.get(key, type);
        return z != null ? z : defaultValue;
    }

    public Set<NamespacedKey> getKeys() {
        Set<NamespacedKey> keys = new HashSet<>();
        this.customDataTags.keySet().forEach((key) -> {
            String[] keyData = key.split(":", 2);
            if (keyData.length == 2) {
                keys.add(new NamespacedKey(keyData[0], keyData[1]));
            }

        });
        return keys;
    }

    public void remove(NamespacedKey key) {
        Preconditions.checkArgument(key != null, "The NamespacedKey key cannot be null");
        this.customDataTags.remove(key.toString());
        save();
    }

    public boolean isEmpty() {
        return this.customDataTags.isEmpty();
    }

    public @NotNull PersistentDataAdapterContext getAdapterContext() {
        return this.adapterContext;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof CraftPersistentDataContainer)) {
            return false;
        } else {
            Map<String, NBTBase> theirRawMap = ((CraftPersistentDataContainer)obj).getRaw();
            return Objects.equals(this.customDataTags, theirRawMap);
        }
    }

    public int hashCode() {
        int hashCode = 3;
        hashCode += this.customDataTags.hashCode();
        return hashCode;
    }

    public boolean has(NamespacedKey key) {
        Preconditions.checkArgument(key != null, "The provided key for the custom value was null");
        return this.customDataTags.containsKey(key.toString());
    }

    private void save() {
        try(FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(serializeToBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] serializeToBytes() throws IOException {
        NBTTagCompound root = new NBTTagCompound();

        for (Map.Entry<String, NBTBase> entry : this.customDataTags.entrySet()) {
            root.a(entry.getKey(), entry.getValue());
        }
        ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
        DataOutputStream dataOutput = new DataOutputStream(byteArrayOutput);

        byte[] var4;
        try {
            NBTCompressedStreamTools.a(root, (OutputStream) dataOutput);
            var4 = byteArrayOutput.toByteArray();
        } catch (Throwable var7) {
            try {
                dataOutput.close();
            } catch (Throwable var6) {
                var7.addSuppressed(var6);
            }

            throw var7;
        }

        dataOutput.close();
        return var4;
    }

    public void readFromBytes(byte[] bytes, boolean clear) throws IOException {
        if (clear) this.customDataTags.clear();

        DataInputStream dataInput = new DataInputStream(new ByteArrayInputStream(bytes));

        try {
            NBTTagCompound compound = NBTCompressedStreamTools.a((InputStream) dataInput);
            for (String key : compound.e()) {
                this.customDataTags.put(key, compound.c(key));
            }
        } catch (Throwable var7) {
            try {
                dataInput.close();
            } catch (Throwable var6) {
                var7.addSuppressed(var6);
            }

            throw var7;
        }

        dataInput.close();
    }
}
