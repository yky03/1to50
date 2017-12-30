package ex.view.yky.a1to50;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by yky on 2017-12-17.
 */

@IgnoreExtraProperties
public class User {

    public String username;
    public String record;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String record) {
        this.username = username;
        this.record = record;
    }

}
