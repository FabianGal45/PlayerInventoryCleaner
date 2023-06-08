package eu.ovmc;
import java.io.IOException;
import java.text.DecimalFormat;

public class PlayerInventoryCleaner {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        String directoryPath = "E:/Games/Minecraft/Servers/NewServer/9.3.0 - SERVER 1.19.4 take II/world/playerdata";
        String cmiPath = "E:/Games/Minecraft/Servers/NewServer/9.3.0 - SERVER 1.19.4 take II/plugins/CMI/cmi.sqlite.db";
        String authPath = "E:/Games/Minecraft/Servers/NewServer/9.3.0 - SERVER 1.19.4 take II/plugins/AuthMe/authme.db";
        String luckPath = "E:/Games/Minecraft/Servers/NewServer/9.3.0 - SERVER 1.19.4 take II/plugins/LuckPerms/luckperms-sqlite.db";

        // Multithreaded
//        ParallelStreamFileProcessor parallelStreamFileProcessor = new ParallelStreamFileProcessor();
//        parallelStreamFileProcessor.processFiles(directoryPath);

        //SignleThreaded
//        SingleThreadRun singleThreadRun = new SingleThreadRun(directoryPath, cmiPath, authPath, luckPath);
//        try {
//            singleThreadRun.processFiles();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //remove Effects from players
        EffectRemove effectRemove = new EffectRemove(directoryPath);
        effectRemove.removeEffectsFromAllPlayers();


        long endTime = System.currentTimeMillis();
        long totalTimeMillis = endTime - startTime;
        double totalTimeSec = (double) totalTimeMillis / 1000.0;

        DecimalFormat df = new DecimalFormat("#0.0");
        String totalTimeSeconds = df.format(totalTimeSec);

        System.out.println("#> Total time taken: " + totalTimeMillis + ", milliseconds: " + totalTimeSeconds + " seconds");
    }


}
