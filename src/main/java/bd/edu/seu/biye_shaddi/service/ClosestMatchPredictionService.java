package bd.edu.seu.biye_shaddi.service;


import bd.edu.seu.biye_shaddi.model.User;
import bd.edu.seu.biye_shaddi.repository.UserRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

//@Service
//public class ClosestMatchPredictionService {
//
//    private final ChatClient chatClient;
//    private final UserRepository userRepository;
//
//
//    public ClosestMatchPredictionService(ChatClient.Builder clientBuilder, UserRepository userRepository) {
//        this.chatClient = clientBuilder.build();
//        this.userRepository = userRepository;
//
//    }
//
//  public void predictClosestMatch() {
//    String  question= "Do you Know who is Shahriar Manzoor?";
//   ChatResponse chatResponse= chatClient.prompt().user(question).call().chatResponse();
//     System.out.println(chatResponse.getResult().getOutput().getText());
//
//
//  }
//@Bean
//public String predictClosestMatch(String question) {
//    ChatResponse response = chatClient.prompt()
//            .user(question)
//            .call()
//            .chatResponse();
//    String answer = response.getResult().getOutput().getText();
//    System.out.println("AI Response: " + answer);
//    return answer;
//
//
//}
//
//}
