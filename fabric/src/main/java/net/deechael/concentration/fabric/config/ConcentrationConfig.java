package net.deechael.concentration.fabric.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class ConcentrationConfig {
	private static final Path configFile = FabricLoader.getInstance().getConfigDir().resolve("concentration.json");

	private ConcentrationConfig() {}

	private static ConcentrationConfig INSTANCE = null;

	public static ConcentrationConfig getInstance() {
		if (INSTANCE == null) {
			Gson gson = new Gson();
			try (FileReader reader = new FileReader(configFile.toFile())) {
				INSTANCE = gson.fromJson(reader, ConcentrationConfig.class);
			} catch (IOException ignored) {
			}
			if (INSTANCE == null) {
				INSTANCE = new ConcentrationConfig();
				INSTANCE.save();
			}
		}
		return INSTANCE;
	}

	public boolean customized = false;
	public boolean related = false;
	public int x = 0;
	public int y = 0;
	public int width = 800;
	public int height = 600;


	public void save() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try (FileWriter writer = new FileWriter(configFile.toFile())) {
			gson.toJson(this, ConcentrationConfig.class, writer);
		} catch (IOException ignored) {
		}
	}

}
