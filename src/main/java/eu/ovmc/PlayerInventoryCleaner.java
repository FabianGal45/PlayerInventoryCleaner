package eu.ovmc;
import java.io.IOException;
import java.text.DecimalFormat;

public class PlayerInventoryCleaner {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        String directoryPath = "E:/Games/Minecraft/Servers/NewServer/9.1.2 - Preparation Server/world/playerdata";

        // Multithreaded
        ParallelStreamFileProcessor parallelStreamFileProcessor = new ParallelStreamFileProcessor();
        parallelStreamFileProcessor.processFiles(directoryPath);

        //SignleThreaded
//        SingleThreadRun singleThreadRun = new SingleThreadRun(directoryPath);
//        try {
//            singleThreadRun.processFiles();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        long endTime = System.currentTimeMillis();
        long totalTimeMillis = endTime - startTime;
        double totalTimeSec = (double) totalTimeMillis / 1000.0;

        DecimalFormat df = new DecimalFormat("#0.0");
        String totalTimeSeconds = df.format(totalTimeSec);

        System.out.println("Total time taken: " + totalTimeMillis + " milliseconds" + totalTimeSeconds + " seconds");
    }


}
