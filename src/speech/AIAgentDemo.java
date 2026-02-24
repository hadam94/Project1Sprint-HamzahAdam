package speech;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;

public class AIAgentDemo {
	
    private static final String API_KEY = "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";
	
	public static void main(String[] args) {
        ChatModel model = OpenAiChatModel.builder().apiKey(API_KEY).modelName(OpenAiChatModelName.GPT_5_1).build();

        String answer = model.chat("can mallard ducks fly?");

        System.out.println(answer);

	}
}
