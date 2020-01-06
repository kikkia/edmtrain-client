public class EdmtrainClient {

    private final String baseUrl = "https://edmtrain.com/api/";
    private final String token;

    public EdmtrainClient(Builder builder) {
        this.token = builder.token;
    }




    public static class Builder {
        private String token;

        public Builder() {
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
