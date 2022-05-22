package util;

import android.app.Application;

public class AppAPI extends Application {
    private String username;
    private String userId;
    private static AppAPI instance;

    public static AppAPI getInstance() {
        if(instance == null){
            instance = new AppAPI();
        }
        return instance;
    }


    public AppAPI(){}


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userID) {
        this.userId = userID;
    }

}
