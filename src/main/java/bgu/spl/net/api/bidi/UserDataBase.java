package bgu.spl.net.api.bidi;

import java.util.HashMap;
import java.util.LinkedList;

public class UserDataBase {
    private HashMap<String,User> UserList;
    public UserDataBase(){
        this.UserList = new HashMap<>();
    }
    public User getUser(String Username){
        return UserList.get(Username);
    }

    public synchronized boolean addUser (User toAdd){
        if(UserList.get(toAdd.getUsername())==null) {
            UserList.put(toAdd.getUsername(), toAdd);
            return true;
        }
        return false;
    }

    public LinkedList<String> getUSerList(){
        LinkedList <String> listOfUsers = new LinkedList<>();
        for(User eachUser:UserList.values()){
            listOfUsers.add(eachUser.getUsername());
        }
        return listOfUsers;
    }


}
