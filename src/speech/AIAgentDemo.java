package speech;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import speech.util.JsonObject;
import speech.util.TimeUtil;

public class AIAgentDemo {
	
    private static final String API_KEY = "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";
	
	public static void main(String[] args) {
       // ChatModel model = OpenAiChatModel.builder().apiKey(API_KEY).modelName(OpenAiChatModelName.GPT_4).build();

       // String answer = model.chat("can mallard ducks fly?");

		JsonObject time = TimeUtil.getCurrentTime();
	//	System.out.println(time);

	}
}
