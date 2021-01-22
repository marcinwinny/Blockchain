package com.marcinwinny.blockchain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {


        String filename = "blockchain.data";
        Scanner scanner = new Scanner(System.in);

        boolean importBlockchain = false;
        boolean isBlockchainValid = false;
        boolean decentralized = true;
        int howManyNewBlocks = 7;


        Blockchain blockchain;

        if(importBlockchain) {
            //import blockchain from file
            Block[] importedTable = null;
            try {
                importedTable = (Block[]) SerializationUtils.deserialize(filename);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            int blockchainSize = importedTable.length;
            blockchain = new Blockchain(blockchainSize);
            blockchain.trimNullValues(importedTable);
            isBlockchainValid = blockchain.validateBlockchain();
        }
        else{
            blockchain = new Blockchain(0);
            isBlockchainValid = true;
        }

        //check if blockchain is valid
        if(isBlockchainValid) {

            int poolSize = Runtime.getRuntime().availableProcessors();
            System.out.println("Pool size= " + poolSize);

            if(decentralized){

                //generate new blocks with M miners
                ExecutorService executorService;
                ExecutorService sendersExecutor;
                List<Callable<Boolean>> callableTasks;

                Boolean result;

                for (int i = 0; i < howManyNewBlocks; i++) {
                    blockchain.resize(blockchain.size + 1);
                    executorService = Executors.newFixedThreadPool(poolSize);
                    callableTasks = new ArrayList<>();
                    sendersExecutor = Executors.newFixedThreadPool(3);

                    for (int j = 0; j < poolSize; j++) {
                        callableTasks.add(new Miner(blockchain, j));
                    }

                    sendersExecutor.submit(new Sender(blockchain,"Josh"));
                    sendersExecutor.submit(new Sender(blockchain,"Bosh"));
                    sendersExecutor.submit(new Sender(blockchain,"Kush"));

                    result = executorService.invokeAny(callableTasks);

                    try {
                        if (!executorService.awaitTermination(500, TimeUnit.MILLISECONDS)) {
                            executorService.shutdownNow();
                        }
                    } catch (InterruptedException e) {
                        executorService.shutdownNow();
                    }

                    try {
                        if (!executorService.awaitTermination(500, TimeUnit.MILLISECONDS)) {
                            executorService.shutdownNow();
                        }
                    } catch (InterruptedException e) {
                        executorService.shutdownNow();
                    }

                    sendersExecutor.shutdownNow();

                    if(!blockchain.validateBlockchain()){
                        System.out.println("Blockchain is not valid!!!");
                        break;
                    }

                }

            }
            //just one miner
            else{
                for (int i = 0; i < howManyNewBlocks; i++) {
                    blockchain.resize(blockchain.size + 1);
                    blockchain.generateNewBlock(0);

                    if(!blockchain.validateBlockchain()){
                        System.out.println("Blockchain is not valid!!!");
                        break;
                    }
                }
            }

            //print blockchain
            //serialize data to file blockchain.data
            //save to file
//            try {
//                SerializationUtils.serialize(blockchain.table, filename);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
        else{
            System.out.println("Blockchain is not valid");
        }

        blockchain.printBlockchain();
//        System.out.println("End of Mine....");
    }




}





