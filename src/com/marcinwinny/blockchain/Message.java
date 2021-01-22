package com.marcinwinny.blockchain;

 class Message {
     String login;
     String text;
     String signature;
     String publicKey;
     int id;


     public Message(String login, String text) {
         this.login = login;
         this.text = text;
     }

     public String getAuthor() {
         return login;
     }

     public String getText() {
         return text;
     }

     @Override
     public String toString(){
         return login + ": " + text;
     }
 }
