package bd.edu.seu.biye_shaddi;


import bd.edu.seu.biye_shaddi.model.User;
import bd.edu.seu.biye_shaddi.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

@SpringBootTest
public class UserInfoTest {


    @Autowired
    private  UserService userService;


    @Test
    public void userInfo(){
        User user = new User();
        user.setAge(23);
        user.setEducation("MBA");
        user.setGender("Male");
        user.setInterests(Collections.singletonList("sports"));
        userService.saveUser(user);



    }

}
