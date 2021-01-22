package com.marcinwinny.blockchain;

import java.util.concurrent.Callable;

public class Miner implements Callable<Boolean> {

    Blockchain blockchain;
    int id;

    public Miner(Blockchain blockchain, int id) {
        this.blockchain = blockchain;
        this.id = id;

        System.out.println("Miner with id=" + id + " is online!");
    }

    @Override
    public Boolean call(){
        return blockchain.generateNewBlock(id);
    }

}
