package com.example.writerspace.model;

public class prompt {
    private String prompt;
    private String promptid;

    public prompt(String prompt, String hashtag, String promptid) {
        this.prompt = prompt;
        this.promptid = promptid;
    }

    public prompt() {
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getPromptid() {
        return promptid;
    }

    public void setPromptid(String promptid) {
        this.promptid = promptid;
    }
}
