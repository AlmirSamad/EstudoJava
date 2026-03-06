package com.almir.screenmatch.service;


import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
// 1. Adicionado o import da interface
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service
public class ConsultaGemini {

    @Value("${GEMINI_APIKEY}")
    private String apikey;




    public  String obterTraducao(String texto){
        ChatLanguageModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(System.getenv("GEMINI_APIKEY"))
                .modelName("gemini-3-flash-preview")
                .logRequestsAndResponses(true)
                .temperature(0.7)
                .build();
        String prompt = """
                Traduza o texto a seguir para o português do Brasil.
                Responda APENAS com a tradução direta, sem introduções e sem listar opções.

                Texto: %s
                """.formatted(texto);

        try {
            return model.generate(prompt);
        } catch (Exception e) {
            System.out.println("Erro na tradução, mas a série será salva mesmo assim. Motivo: " + e.getMessage());
            return texto;
        }
    }
}
