package eu.ovmc;


//import net.querz.io.seekable.SeekableFile;
//import net.querz.mca.Chunk;
//import net.querz.mca.MCAFile;
//import net.querz.mca.MCAFileHandle;
//import net.querz.mca.MCCFileHandler;
//import net.querz.nbt.*;
//import org.jglrxavpok.hephaistos.nbt.NBT;


import dev.dewy.nbt.Nbt;
import dev.dewy.nbt.api.Tag;
import dev.dewy.nbt.io.CompressionType;
import dev.dewy.nbt.tags.collection.CompoundTag;
import dev.dewy.nbt.tags.collection.ListTag;

import java.io.*;

public class SingleThreadRun {
    private String path;

    private static final Nbt NBT = new Nbt();
    public SingleThreadRun(String path) {
        this.path = path;
    }

    public void processFiles() throws IOException {

        File directory = new File(path);
        File[] files = directory.listFiles();

        for(File file : files){
            if(file.toString().endsWith(".dat")){
                System.out.println("Processing: " + file.getName());

                CompoundTag clone = NBT.fromFile(file);
                System.out.println("Food: " + clone.getInt("foodLevel"));

                ListTag<Tag> inventory = clone.getList("Inventory");

                System.out.println("size before: "+ inventory.size());
                inventory.clear();
                System.out.println("size after: " +inventory.size());

                NBT.toFile(clone, file, CompressionType.GZIP);

//                Tag tag = NBTUtil.read(file);
//                CompoundTag compoundTag = (CompoundTag) tag;
//                System.out.println("Food: " +compoundTag.getInt("foodLevel"));
//
//                ListTag inventory = compoundTag.getList("Inventory");
//
//                int itemCount = 0;
//
//                for(Tag iTag: inventory){
//                    CompoundTag iCTag = (CompoundTag) iTag;
//                    itemCount++;
//                }
//
//                System.out.println("itemCount: "+ itemCount);
////                inventory.clear();
//                System.out.println("size: " +inventory.size());
//
//
//
//                NBTUtil.write(file,tag);

            }
        }
    }


}
