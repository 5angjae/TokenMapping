package main.java.com.example;

import java.util.List;

public class TokenResponse {
    public List<String> tokens;
    public List<Integer> tokenIds;

    public TokenResponse(List<String> tokens, List<Integer> tokenIds) {
        this.tokens = tokens;
        this.tokenIds = tokenIds;
    }
}