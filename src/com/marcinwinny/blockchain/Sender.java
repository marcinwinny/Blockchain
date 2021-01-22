package com.marcinwinny.blockchain;

public class Sender implements Runnable {
    private Blockchain blockchain;
    private String name;

    String randomMessage0 = "I love Barcelona";
    String randomMessage1 = "Have you visited Rome";
    String randomMessage2 = "One night in Paris xoxo";
    String randomMessage3 = "Go off you bum";
    String randomMessage4 = "Tooooookyoooooo";
    String randomMessage5 = "Voyage, voyage!!!";

    public Sender(Blockchain blockchain, String name) {
        this.blockchain = blockchain;
        this.name = name;
    }

    private Message sendRandomMessage() {
        int max = 5;
        int min = 0;
        int choose = (int) (Math.floor(Math.random() * (max - min + 1)) + min);
        String text;

        switch (choose){
            case 0:
                text = randomMessage0;
                break;
            case 1:
                text = randomMessage1;
                break;
            case 2:
                text = randomMessage2;
                break;
            case 3:
                text = randomMessage3;
                break;
            case 4:
                text = randomMessage4;
                break;
            case 5:
                text = randomMessage5;
                break;
            default:
                text = "Empty message";
                break;
        }
        return new Message(name, text);
    }


    @Override
    public void run() {
        blockchain.sendMessage(sendRandomMessage());
    }
}
