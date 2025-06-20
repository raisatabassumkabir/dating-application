package bd.edu.seu.biye_shaddi;


import bd.edu.seu.biye_shaddi.model.TalkRequest;
import bd.edu.seu.biye_shaddi.model.User;
import bd.edu.seu.biye_shaddi.service.TalkRequestService;
import bd.edu.seu.biye_shaddi.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

@SpringBootTest
public class UserInfoTest {


    @Autowired
    private  UserService userService;
    @Autowired
    private TalkRequestService talkRequestService;


    @Test
    public void userInfo(){
        User user = new User();
        user.setAge(23);
        user.setEmailId("email@email.com");
        user.setEducation("MBA");
        user.setGender("Male");
        user.setInterests(Collections.singletonList("sports"));
        userService.saveUser(user);



    }
    @Test
    public void talkRequest(){
        TalkRequest talkRequest = new TalkRequest();
        talkRequest.setToEmailId("jane@gmail.com");
        talkRequest.setFromEmailId("abul@gmail.com");
        talkRequest.setId("1");

       talkRequestService.sendTalkRequest(talkRequest.getFromEmailId(), talkRequest.getToEmailId());
    }

}
