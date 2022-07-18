package cls.android.simplecar;


import cls.android.simplecar.models.User;

import static cls.android.simplecar.Application.getContext;

public class UserRepository {
    private static UserRepository instance;
    SaveDataTool saveDataTool = new SaveDataTool(getContext());


    public static UserRepository getInstance() {
        if (instance == null){
            instance = new UserRepository();
        }
        return instance;
    }

    public void saveUser(User parseUser) {
        saveDataTool.saveUser(parseUser);
    }
    public User getUser(){
        return saveDataTool.getUser();
    }
}
