package com.marcinwinny.blockchain;

import java.io.Serializable;
import java.util.ArrayList;

public class Block<T> implements Serializable {

    private final int id;
    private final long timestamp;
    private final String previousBlockHash;
    private final String blockHash;
    private final int generationTime;
    private final int magicNumber;
    private final int minerId;
    private final int difficulty;
    private static final long serialVersionUID = 7L;
    public ArrayList<Message> chat;

    public final long upperTimeBound = 10L;
    public final long lowerTimeBound = 5L;

//    public static

    public Block(int id, long timestamp, String previousBlockHash, String currentBlockHash, int magicNumber, int generationTime, int magicNumber1, int minerId, int difficulty, ArrayList<Message> chat) {
        this.id = id;
        this.timestamp = timestamp;
        this.previousBlockHash = previousBlockHash;
        this.blockHash = currentBlockHash;
        this.generationTime = generationTime;
        this.magicNumber = magicNumber;
        this.minerId = minerId;
        this.difficulty = difficulty;
        this.chat = chat;

        System.out.println("Block " + id + " was created by miner with id = " + minerId);
    }

    public int getMinerId() {
        return minerId;
    }

    public int getDifficulty() {
        return difficulty;
    }
    public int getGenerationTime() {
        return generationTime;
    }

    public int getMagicNumber() {
        return magicNumber;
    }
    public int getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public String showChat(){
        if(chat == null){
            return "Block data: no messages";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Block data:");
        chat.stream().forEach(msg -> sb.append("\n").append(msg.toString()));
        return sb.toString();
    }

    public String printBlock() {
        StringBuilder blockStringBuilder = new StringBuilder();

        String difficultyChange = "";

        if(this.getGenerationTime() > upperTimeBound){
            difficultyChange = "N was decreased to " + (this.getDifficulty() - 1);
        }
        else if(this.getGenerationTime() < upperTimeBound && this.getGenerationTime() > lowerTimeBound){
            difficultyChange = "N stays the same";
        }
        else if(this.getGenerationTime() < lowerTimeBound){
            difficultyChange = "N was increased to " + (this.getDifficulty() + 1);
        }

        if (this == null) {
            blockStringBuilder.append("null");
        } else {
            blockStringBuilder.append(
                    "Block:\n"
                    + "Created by miner # " + getMinerId()
                    + "\nId: " + getId()
                    + "\nTimestamp: " + getTimestamp()
                    + "\nMagic number: " + getMagicNumber()
                    + "\nHash of the previous block:\n" + getPreviousBlockHash()
                    + "\nHash of the block:\n" + getBlockHash()
                    + "\n" + showChat()
                    + "\nBlock was generating for " + getGenerationTime() + " second\n"
                    + difficultyChange
                    + "\n");
        }
        return blockStringBuilder.toString();
    }
}