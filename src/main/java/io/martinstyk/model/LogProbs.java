package io.martinstyk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.Valid;
import java.util.List;

@Serdeable
public class LogProbs {

    @JsonProperty("content")
    @Valid
    private List<TokenLogProb> content;

    public LogProbs() {}

    public LogProbs(List<TokenLogProb> content) {
        this.content = content;
    }

    public List<TokenLogProb> getContent() {
        return content;
    }

    public void setContent(List<TokenLogProb> content) {
        this.content = content;
    }

    @Serdeable
    public static class TokenLogProb {
        @JsonProperty("token")
        private String token;

        @JsonProperty("logprob")
        private Double logprob;

        @JsonProperty("bytes")
        private List<Integer> bytes;

        @JsonProperty("top_logprobs")
        @Valid
        private List<TopLogProb> topLogprobs;

        public TokenLogProb() {}

        public TokenLogProb(String token, Double logprob) {
            this.token = token;
            this.logprob = logprob;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Double getLogprob() {
            return logprob;
        }

        public void setLogprob(Double logprob) {
            this.logprob = logprob;
        }

        public List<Integer> getBytes() {
            return bytes;
        }

        public void setBytes(List<Integer> bytes) {
            this.bytes = bytes;
        }

        public List<TopLogProb> getTopLogprobs() {
            return topLogprobs;
        }

        public void setTopLogprobs(List<TopLogProb> topLogprobs) {
            this.topLogprobs = topLogprobs;
        }
    }

    @Serdeable
    public static class TopLogProb {
        @JsonProperty("token")
        private String token;

        @JsonProperty("logprob")
        private Double logprob;

        @JsonProperty("bytes")
        private List<Integer> bytes;

        public TopLogProb() {}

        public TopLogProb(String token, Double logprob) {
            this.token = token;
            this.logprob = logprob;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Double getLogprob() {
            return logprob;
        }

        public void setLogprob(Double logprob) {
            this.logprob = logprob;
        }

        public List<Integer> getBytes() {
            return bytes;
        }

        public void setBytes(List<Integer> bytes) {
            this.bytes = bytes;
        }
    }
}
