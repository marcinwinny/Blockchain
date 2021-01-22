package com.marcinwinny.blockchain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Blockchain<T> implements Serializable {
    public int size;
    private Block[] table;
    private static final long serialVersionUID = 7L;
    private boolean showHash = true;
    public ArrayList<Message> chat;


    public Blockchain(int size) {
        this.size = size;
        table = new Block[size];
    }

    void resize(int newSize){
        size = newSize;
        Block[] newTable = new Block[newSize];
        for (int i = 0; i < table.length; i++) {
            newTable[i] = table[i];
        }
        table = newTable;
    }

    public synchronized void sendMessage(Message message){
        if(chat == null){
            chat = new ArrayList<>();
        }
        chat.add(message);
    }

    public String showChat(){
        if(chat == null){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Block data:");
        chat.stream().forEach(msg -> sb.append("\n").append(msg.toString()));
        return sb.toString();
    }


    public void trimNullValues(Block[] xTable){
        int countNotNull = 0;
        for (int i = 0; i < xTable.length; i++) {
            if(xTable[i] != null){
                countNotNull++;
            }
        }
        Block[] newTable = new Block[countNotNull];
        for (int i = 0; i < newTable.length; i++) {
            newTable[i] = xTable[i];
        }
        table = newTable;
    }

    boolean validateBlockchain(){
        boolean blockchainValid = true;
        for( int i = 0; i < table.length ; i++){
            if(i==0){
                if(!table[0].getPreviousBlockHash().equals("0")){
                    blockchainValid = false;
                }
            }
            else{
                if(!table[i].getPreviousBlockHash().equals(table[i-1].getBlockHash())){
                    blockchainValid = false;
                }
            }
        }
        return blockchainValid;
    }

    Boolean generateNewBlock(int minerId){
        long time0 = new Date().getTime() / 1000;
        int id = findIndex();
        long timeStamp = new Date().getTime();
        int magicNumber = 0;
        boolean isHashValid = false;
        String previousBlockHash = getPreviousHash(id);
        Random rand = new Random(); //instance of random class
        int upperbound = 100000000;
        String blockHash = "";
        String zerosString = "";
        ArrayList<Message> chatToSave = null;

        int difficulty;

        if(id == 1){
            difficulty = 0;
        }
        else{
            chatToSave = chat;
            chat = null;
            difficulty = table[id-2].getDifficulty();
            if(table[id - 2].getGenerationTime() > 60){
                if(difficulty > 0){
                    difficulty--;
                }
            }
            else if(table[id - 2].getGenerationTime() < 10){
                difficulty++;
            }
        }

        if(table[id - 1] == null) {

            for (int i = 0; i < difficulty; i++) {
                zerosString = zerosString.concat("0");
            }

            while (!isHashValid) {
                //generate random values from 0-24
                magicNumber = rand.nextInt(upperbound);
                blockHash = StringUtil.applySha256(String.valueOf(id) + String.valueOf(timeStamp) + String.valueOf(magicNumber));
//                    if (showHash) {
//                        System.out.println("Miner ID= " + minerId + ", block ID= " + id + ", difficulty= " + difficulty + ", timestamp= " + timeStamp +", hash= " + blockHash);
//                    }
                if (blockHash.substring(0, difficulty).equals(zerosString)) {
                    isHashValid = true;
                }
            }

            long time1 = new Date().getTime() / 1000;
            int generationTime = (int) (time1 - time0);

            int blockDifficulty = difficulty;
            if(table[id - 1] == null){
                table[id - 1] = new Block(id, timeStamp,previousBlockHash, blockHash, magicNumber, generationTime, magicNumber, minerId, blockDifficulty, chatToSave);
            }
            return true;
        }
        else{
            System.out.println("Block already mined...");
            return false;
        }
    }

    private String getPreviousHash(int id){
        if(id == 1){
            return String.valueOf(0);
        }
        return table[id - 2].getBlockHash();
    }


    private int findIndex(){
        int idx = -1;
        boolean idFound = false;
        int i = 0;
        while(!idFound){
            if(table[i] == null){
                idx = i + 1;
                idFound = true;
            }
            i++;
        }
        return idx;
    }

    public void printBlockchain(){
        for (int i = 0; i < table.length; i++) {
            if(!(table[i] == null)){
                System.out.println(table[i].printBlock());
            }
            else{
                break;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder tableStringBuilder = new StringBuilder();

        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) {
                tableStringBuilder.append(i + ": null");
            } else {
                tableStringBuilder.append(i + ": id=" + table[i].getId()
                                          + ", timestamp=" + table[i].getTimestamp()
                                          + ", previousHash=" + table[i].getPreviousBlockHash()
                                          + ", hash=" + table[i].getBlockHash());
            }

            if (i < table.length - 1) {
                tableStringBuilder.append("\n");
            }
        }

        return tableStringBuilder.toString();
    }
}
