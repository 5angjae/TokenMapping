package main.java.com.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.util.*;

public class TokenHandler extends HttpServlet {
    private final Gson gson = new Gson();
    private final Map<String, Integer> wordToId = new HashMap<>();

    public TokenHandler() {
        loadTokenMap();
    }

    private void loadTokenMap() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("tokens.txt"), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split("#");
                if (parts.length == 2) {
                    wordToId.put(parts[0], Integer.parseInt(parts[1]));
                }
            }
        } catch (IOException e) {
            System.err.println("토큰 파일 로딩 실패: " + e.getMessage());
        }
    }

    private List<String> tokenize(String sentence) {
    	String cleaned = sentence.replaceAll("[^a-zA-Z가-힣0-9\\s]", "");
        return Arrays.asList(cleaned.trim().split("\\s+"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            BufferedReader reader = req.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            TokenRequest input = gson.fromJson(sb.toString(), TokenRequest.class);

            List<String> tokens = tokenize(input.sentence);
            List<Integer> ids = new ArrayList<>();
            for (String token : tokens) {
                ids.add(wordToId.getOrDefault(token, 0));
            }

            TokenResponse response = new TokenResponse(tokens, ids);
            resp.setContentType("application/json");
            resp.setStatus(200);
            resp.getWriter().write(gson.toJson(response));
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write("{\\\"error\\\": \\\"" + e.getMessage().replace("\"", "'") + "\\\"}");
        }
    }
}