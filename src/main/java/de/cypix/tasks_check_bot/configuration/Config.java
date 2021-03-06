package de.cypix.tasks_check_bot.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class Config {

    // Hier schreibst du deine Attribute hin
    public String token;
    public String channelName;
    public String sqlHost;
    public String sqlUser;
    public String sqlDatabase;
    public String sqlPassword;
    public int sqlPort;
    public String guildName;
    public boolean startAutomatically;
/*    public int WIDTH;
    public int HEIGHT;
    public double RATIO;
    public ArrayList<String> NAMES;*/

    public Config() {
        // Hier die Standardwerte falls das jeweiligen Attribut nicht in der
        // config.json enthalten ist.
        this.token = "put here your token";
        this.channelName = "put here your channelname";
        this.sqlPassword = "123456";
        this.guildName = "Name of you Guild";
        this.sqlHost = "localhost";
        this.sqlUser = "Task_Check_Bot";
        this.sqlDatabase = "Task_Check_Bot";
        this.sqlPort = 3306;
        this.startAutomatically = false;

/*        this.NAMES = new ArrayList<String>();
        this.NAMES.add("Peter");
        this.NAMES.add("Paul");*/
    }

    // DON'T TOUCH THE FOLLOWING CODE
    private static Config instance;

    public static Config getInstance() {
        if (instance == null) {
            instance = fromDefaults();
        }
        return instance;
    }

    public static void load(File file) {
        instance = fromFile(file);

        // no config file found
        if (instance == null) {
            instance = fromDefaults();
        }
    }

    public static void load(String file) {
        load(new File(file));
    }

    private static Config fromDefaults() {
        Config config = new Config();
        return config;
    }

    public void toFile(String file) {
        toFile(new File(file));
    }

    public void toFile(File file) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonConfig = gson.toJson(this);
        FileWriter writer;
        try {
            writer = new FileWriter(file);
            writer.write(jsonConfig);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Config fromFile(File configFile) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile)));
            return gson.fromJson(reader, Config.class);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}