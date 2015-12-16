package com.epam.cdpSelenium.businessObject.users;

public class UserCreator {

    //Static Factory for User creation
    public static User createUser1(){
        return new User("gmbdaily001", "gmbdaily001mially");
    };

    public static User createUser2(){
        return new User("gmbdaily002", "gmbdaily002mially");
    };

    public static User createUserWithCredentials(String username, String password){
        return new User(username, password);
    }
}

