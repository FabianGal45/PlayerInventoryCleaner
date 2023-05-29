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
import dev.dewy.nbt.tags.primitive.DoubleTag;
import dev.dewy.nbt.tags.primitive.StringTag;

import java.io.*;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SingleThreadRun {
    private String path;
    private static final Nbt NBT = new Nbt();
    private final JDBC jdbc = new JDBC();
    private Connection cmiCon;
    private Connection authCon;
    private Connection luckCon;

    public SingleThreadRun(String path, String cmiPath, String authPath, String luckPath) {
        this.path = path;
        cmiCon = jdbc.connectToCMIDB(cmiPath);
        authCon = jdbc.connectToAuthDB(authPath);
        luckCon = jdbc.connectToLuckDB(luckPath);
    }

    public void processFiles() throws IOException {

        File directory = new File(path);
        File[] files = directory.listFiles();

        List<File> filteredArray = new ArrayList<>();


        System.out.println("Removing and filtering files....");
        for(File file:files){
            if(file.getName().endsWith(".dat")){
                String uuid = file.getName().substring(0, file.getName().lastIndexOf("."));//strips the ".dat"

                //remove unused accounts
                // - less than 12 hours
                // - and Have the rank null or apprentice
                ResultSet rs = jdbc.getTimeAndRank(cmiCon,uuid);
                try{
                    while(rs.next()){
                        long timePlayed = rs.getLong("TotalPlayTime");
                        String rank = rs.getString("Rank");

                        if(timePlayed<43200000 || rank.equals("Apprentice")){
//                            System.out.println("XXX> "+rank+", "+timePlayed+" > "+file.getPath());


                            Path playerDataFolder = FileSystems.getDefault().getPath(file.getParent());
                            Path worldFolder = playerDataFolder.resolveSibling("..").resolveSibling("..");
                            Path advancementsFolder = worldFolder.resolveSibling("advancements");
                            Path statsFolder = worldFolder.resolveSibling("stats");

                            Path playerDataFile = FileSystems.getDefault().getPath(file.getPath());
                            Path playerAdvancementsFile = advancementsFolder.resolve(uuid+".json");
                            Path playerStatsFile = statsFolder.resolve(uuid+".json");

//                            System.out.println("????> worldFolder: "+worldFolder+" advancements: "+advancementsFolder);
//                            System.out.println("????> advancementsFolder: "+advancementsFolder);
//                            System.out.println("????> playerDataFolder: "+playerDataFolder);
//
//                            System.out.println("????> playerDataFile: "+playerDataFile);
//                            System.out.println("????> playerAdvancementsFile: "+playerAdvancementsFile);
//                            System.out.println("????> playerStatsFile: "+playerStatsFile);

                            //delete player Data
                            System.out.println("\n----Less than 12h OR Newbie OR Apprentice ---");
                            try{
                                Files.delete(playerDataFile);
                                System.out.println("Deleted data for: "+ file.getName());
                            }catch(NoSuchFileException exception){
                                System.out.println("Failed to delete data for: "+ file.getName());
                            }

                            try{
                                Files.delete(playerAdvancementsFile);
                                System.out.println("Deleted advancements for: "+file.getName());
                            }catch(NoSuchFileException exception){
                                System.out.println("Failed to delete advancements for: "+file.getName());
                            }

                            try{
                                Files.delete(playerStatsFile);
                                System.out.println("Deleted stats for: " +file.getName());
                            }catch(NoSuchFileException exception){
                                System.out.println("Failed to delete stats for: "+file.getName());
                            }

                            //delete data from authme
                            //I have to get the name of the player in order to remove them from authme as UUId is not stored in the database
                            ResultSet usernameRs = jdbc.getUserName(cmiCon,uuid);
                            try{
                                while(usernameRs.next()){
                                    String username = usernameRs.getString("username");
                                    jdbc.removePlayerFromAuth(authCon,username);
                                }
                            }catch (SQLException e) {
                                e.printStackTrace();
                            }


                            //Delete from LuckPerms
                            //Data must be converted to SQLite
                            jdbc.removePlayerFromLuckPerm(luckCon,uuid);

                            //Delete data from CMI
                            jdbc.removePlayerData(cmiCon,uuid);
                        }
                        else{
                            filteredArray.add(file);
                        }

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
            else{
                Path playerDataFile = FileSystems.getDefault().getPath(file.getPath());
//                System.out.println("\n---Backup file/Un-usable file---");
                try{
                    Files.delete(playerDataFile);

//                    System.out.println("Deleted the file: "+file.getName());
                }catch(NoSuchFileException exception){
                    System.out.println("Could not delete the file: "+file.getName());
                }
            }

        }

        File[] filteredFiles = filteredArray.toArray(new File[0]);

        System.out.println("\n\n Updating Filtered files...");
        for(File file : filteredFiles){

            if(file.toString().endsWith(".dat")) {

//                System.out.println("Processing file: " + file.getName() + " on thread " + Thread.currentThread().getName());

                try {
                    CompoundTag root = NBT.fromFile(file);
                    CompoundTag bukkitValues = root.getCompound("BukkitValues");
                    StringTag playerName = root.getCompound("bukkit").getString("lastKnownName");
                    ListTag<Tag> inventory = root.getList("Inventory");
                    ListTag<Tag> enderChest = root.getList("EnderItems");
                    ListTag<Tag> pos = root.getList("Pos");
                    String uuid = file.getName().substring(0, file.getName().lastIndexOf("."));//strips the ".dat"

                    //clear inventory & ender chest
                    inventory.clear();
                    enderChest.clear();

                    //set balance to 0
                    try{
                        DoubleTag balance = bukkitValues.getDouble("diamondbank:balance");
                        System.out.println("> "+ playerName +" "+balance + " | "+file.getName());

                        bukkitValues.putDouble("diamondbank:balance",0); //set balance to 0

                    }catch(NullPointerException ne){
                        System.out.println("/> NO BALANCE FOUND for: "+playerName+", "+ file.getName());
                    }

                    //set position
                    DoubleTag x = new DoubleTag(-103.5);
                    DoubleTag y = new DoubleTag(-10);
                    DoubleTag z = new DoubleTag(-87.5);
                    pos.clear();
                    pos.insert(0, x);
                    pos.insert(1, y);
                    pos.insert(2, z);

                    //save the progress done in the NBT file
                    NBT.toFile(root, file, CompressionType.GZIP);


                    //remove homes form CMI database
                    jdbc.clearHomesForPlayer(cmiCon,uuid);



                } catch (IOException e) {
                    throw new RuntimeException(e);//stops the program if this exception is found
                }
            }
        }
    }


}
