package cls.android.simplecar;


import android.content.Context;

import cls.android.simplecar.models.User;

import static cls.android.simplecar.Application.getContext;

public class UserRepository {
    private static UserRepository instance;
    private final SaveDataTool saveDataTool;

    public UserRepository(Context context) {
        saveDataTool = new SaveDataTool(context);

    }


    public static UserRepository getInstance(Context context) {
        if (instance == null){
            instance = new UserRepository(context);
        }
        return instance;
    }

    public void saveUser(User parseUser) {
        saveDataTool.saveUser(parseUser);
    }
    public User getUser(){
        User user = saveDataTool.getUser();

        if (user == null) {
            user = new User();
            saveDataTool.saveUser(user);
        }
        return user;
    }
}
