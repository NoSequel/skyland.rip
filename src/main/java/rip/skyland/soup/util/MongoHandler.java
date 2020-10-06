package rip.skyland.soup.util;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import rip.skyland.soup.SoupPlugin;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MongoHandler {

    @Getter
    private static MongoCollection soupProfiles;

    private String username, password, host, database;
    private int port;

    private MongoClient client;

    public MongoHandler(String username, String password, String host, String database, int port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.database = database;
        this.port = port;
    }

    public void setup() {
        if (password.equalsIgnoreCase("")) {
            client = new MongoClient(host, port);
            System.out.println("true");
        } else {
            System.out.println("false");
            List<MongoCredential> credentials = new ArrayList<>();
            credentials.add(MongoCredential.createCredential(username, database, password.toCharArray()));

            client = new MongoClient(new ServerAddress(host, port), credentials);
        }

        soupProfiles = client.getDatabase(SoupPlugin.getInstance().getConfig().getString("MONGO.MONGO_DATABASE")).getCollection("profiles");
    }
}
