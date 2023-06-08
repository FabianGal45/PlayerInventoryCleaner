package eu.ovmc;

import dev.dewy.nbt.Nbt;
import dev.dewy.nbt.api.Tag;
import dev.dewy.nbt.io.CompressionType;
import dev.dewy.nbt.tags.collection.CompoundTag;
import dev.dewy.nbt.tags.collection.ListTag;
import dev.dewy.nbt.tags.primitive.StringTag;

import java.io.File;
import java.io.IOException;

public class EffectRemove {
    private String path;

    private static final Nbt NBT = new Nbt();

    public EffectRemove(String path){
        this.path=path;
    }

    public void removeEffectsFromAllPlayers(){
        File directory = new File(path);
        File[] files = directory.listFiles();

        for(File file:files){
            if(file.getName().endsWith(".dat")){
                String uuid = file.getName().substring(0, file.getName().lastIndexOf("."));//strips the ".dat"

                try {
                    CompoundTag root = NBT.fromFile(file);
                    StringTag playerName = root.getCompound("bukkit").getString("lastKnownName");
                    ListTag<Tag> activeEffects = root.getList("ActiveEffects");

                    int size = activeEffects.size();
                    activeEffects.clear();
                    System.out.println("> "+size+", Effect removed from: "+playerName + ", "+uuid);



                    //save the progress done in the NBT file
                    NBT.toFile(root, file, CompressionType.GZIP);
                } catch (IOException |NullPointerException e) {
                    System.out.println("Unable to remove effect from: "+ uuid);
                }



            }
        }
    }

}
