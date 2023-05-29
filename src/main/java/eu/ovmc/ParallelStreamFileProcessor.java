package eu.ovmc;


import dev.dewy.nbt.Nbt;
import dev.dewy.nbt.api.Tag;
import dev.dewy.nbt.io.CompressionType;
import dev.dewy.nbt.tags.collection.CompoundTag;
import dev.dewy.nbt.tags.collection.ListTag;
import dev.dewy.nbt.tags.primitive.DoubleTag;
import dev.dewy.nbt.tags.primitive.StringTag;

import javax.swing.plaf.ComponentUI;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;


public class ParallelStreamFileProcessor {
    private AtomicInteger processedFileCount = new AtomicInteger(0);
    private static final Nbt NBT = new Nbt();

    public void processFiles(String directoryPath) {
        File directory = new File(directoryPath);

        try (Stream<File> files = Files.walk(directory.toPath())
                .filter(Files::isRegularFile)
                .map(java.nio.file.Path::toFile)
                .parallel()) { // invoke the parallel method to process files in parallel
            files.forEach(this::processFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Processed " + processedFileCount.get() + " files");
    }

    private void processFile(File file) {
        // process file logic goes here
//        System.out.println("Processing file: " + file.getName() + " on thread " + Thread.currentThread().getName());

        if(file.toString().endsWith(".dat")) {
            try {
                CompoundTag root = NBT.fromFile(file);
                ListTag<Tag> inventory = root.getList("Inventory");
//                inventory.clear();

                ListTag<Tag> enderChest = root.getList("EnderItems");
//                enderChest.clear();

                //set balance to 0
                CompoundTag bukkitValues = root.getCompound("BukkitValues");
                DoubleTag balance = bukkitValues.getDouble("diamondbank:balance");
                StringTag playerName = root.getCompound("bukkit").getString("lastKnownName");
                System.out.println("> "+ playerName +" "+balance);


                //set position



                //remove unused accounts

                NBT.toFile(root, file, CompressionType.GZIP);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        processedFileCount.incrementAndGet();
    }
}
