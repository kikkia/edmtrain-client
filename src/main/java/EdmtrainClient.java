import com.sun.istack.internal.NotNull;
import exceptions.APIException;
import models.Event;
import models.Location;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import utils.ParseUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EdmtrainClient {

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

        public EdmtrainClient build() {
            return new EdmtrainClient(this);
        }
    }

    public class EventQuery {
        private Map<String, String> args;
        private DateTimeFormatter formatter;

        public EventQuery() {
            args = new HashMap<>();
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        }

        public EventQuery withEventName(@NotNull String name) {
            this.args.put("eventName", name);
            return this;
        }

        public EventQuery withArtistIds(@NotNull List<Integer> artistIds) {
            List<String> idStrings = artistIds.stream().map(i -> "" + i).collect(Collectors.toList());
            this.args.put("artistIds", String.join(",", idStrings));
            return this;
        }

        public EventQuery withVenueIds(@NotNull List<Integer> venueIds) {
            List<String> idStrings = venueIds.stream().map(i -> "" + i).collect(Collectors.toList());
            this.args.put("venueIds", String.join(",", idStrings));
            return this;
        }

        public EventQuery withLocationIds(@NotNull List<Integer> locationIds) {
            List<String> idStrings = locationIds.stream().map(i -> "" + i).collect(Collectors.toList());
            this.args.put("locationIds", String.join(",", idStrings));
            return this;
        }

        public EventQuery withStartDate(@NotNull LocalDate startDate) {
            this.args.put("startDate", startDate.format(formatter));
            return this;
        }

        public EventQuery withEndDate(@NotNull LocalDate endDate) {
            this.args.put("endDate", endDate.format(formatter));
            return this;
        }

        public EventQuery withCreatedStartDate(@NotNull LocalDate createdStartDate) {
            this.args.put("createdStartDate", createdStartDate.format(formatter));
            return this;
        }

        public EventQuery withCreatedEndDate(@NotNull LocalDate createdEndDate) {
            this.args.put("createdEndDate", createdEndDate.format(formatter));
            return this;
        }

        public EventQuery isFestival(boolean isFestival) {
            this.args.put("festivalInd", String.valueOf(isFestival));
            return this;
        }

        public EventQuery includeElectronicGenre(boolean includeElectronicGenre) {
            this.args.put("includeElectronicGenreInd", String.valueOf(includeElectronicGenre));
            return this;
        }

        public EventQuery includeOtherGenre(boolean includeOtherGenre) {
            this.args.put("includeOtherGenreInd", String.valueOf(includeOtherGenre));
            return this;
        }

        public List<Event> get() throws APIException {
            String url = buildUrl(this.args);
            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpGet get = new HttpGet(url);
                HttpResponse response = client.execute(get);
                String json = IOUtils.toString(response.getEntity().getContent());
                JSONObject respJson = new JSONObject(json);

                return ParseUtils.parseEventsFromResponse(respJson);
            } catch (IOException e) {
                throw new APIException("Something went wrong getting a connection to the api: " + e.getMessage());
            }
        }
    }

    public class LocationQuery {
        private Map<String, String> args;

        public LocationQuery() {
            args = new HashMap<>();
        }

        public LocationQuery withState(String state) {
            args.put("state", state);
            return this;
        }

        public LocationQuery withCity(String city) {
            args.put("city", city);
            return this;
        }

        public List<Location> get() throws APIException {
            String url = buildUrl(this.args);

            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpGet get = new HttpGet(url);
                HttpResponse response = client.execute(get);
                String json = IOUtils.toString(response.getEntity().getContent());
                JSONObject respJson = new JSONObject(json);

                return ParseUtils.parseLocationsFromResponse(respJson);
            } catch (IOException e) {
                throw new APIException("Something went wrong getting a connection to the api: " + e.getMessage());
            }
        }
    }

    private String buildUrl(Map<String, String> args) {
        String baseUrl = "https://edmtrain.com/api/";
        StringBuilder url = new StringBuilder(baseUrl + "events?");
        for (Map.Entry<String, String> e : args.entrySet()) {
            url.append(e.getKey()).append("=").append(e.getValue()).append("&");
        }
        return url.append("client=").append(token).toString();
    }
}
