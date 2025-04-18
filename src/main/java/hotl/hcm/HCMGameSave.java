package hotl.hcm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HCMGameSave {
	private HCM plugin;
	private HCMGame game;
	
	public HCMGameSave(HCM hcm) {
		this.plugin = hcm;
		this.game = hcm.game;
	}

	public void loadDataForMatch(UUID uid) {
		File save = new File(uid.toString() + ".json");
		JsonObject match = null;

		if (!save.exists()) {
			return;
		}

		try {
			BufferedReader reader = new BufferedReader(new FileReader(save));
			match = JsonParser.parseString(reader.readLine()).getAsJsonObject();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			// Load player data
			for (Entry<String, JsonElement> entry : match.getAsJsonObject("players").entrySet()) {
				String key = entry.getKey();
				JsonElement value = entry.getValue();
				HCMPlayer p = new HCMPlayer(null);
				p.setMobKills(value.getAsJsonObject().get("kills").getAsInt());
				p.setPlayerMode(value.getAsJsonObject().get("mode").getAsInt());
				p.setKilledEnderDragon(value.getAsJsonObject().get("killedEnderDragon").getAsBoolean());
				p.setKilledWither(value.getAsJsonObject().get("killedWither").getAsBoolean());
				p.setKilledGuardian(value.getAsJsonObject().get("killedGuardian").getAsBoolean());
				p.setEnteredBastion(value.getAsJsonObject().get("enteredBastion").getAsBoolean());
				p.setEnteredEnd(value.getAsJsonObject().get("enteredEnd").getAsBoolean());
				p.setEnteredNether(value.getAsJsonObject().get("enteredNether").getAsBoolean());
				p.setCauseOfDeath(value.getAsJsonObject().get("causeOfDeath").getAsString());
				game.HCMPlayers.put(UUID.fromString(key), p);
				plugin.getLogger().info(
						"Loaded player data for " + key + ": " + value.getAsJsonObject().get("name").getAsString());
			}

			// Load match data
			game.setGameStartTime(LocalDateTime.parse(match.get("game_start_time").getAsString(),
					DateTimeFormatter.ISO_LOCAL_DATE_TIME));
			game.setGameEndTime(LocalDateTime.parse(match.get("game_end_time").getAsString(),
					DateTimeFormatter.ISO_LOCAL_DATE_TIME));
			game.setGameRunning(match.get("game_running").getAsBoolean());
			game.setGameFinished(match.get("game_finished").getAsBoolean());
			game.setGuardianIsDead(match.get("guardianIsDead").getAsBoolean());
			game.setEnderDragonIsDead(match.get("enderDragonIsDead").getAsBoolean());
			game.setWitherIsDead(match.get("witherIsDead").getAsBoolean());
			game.setMobsKilled(match.get("mobsKilled").getAsInt());
		} catch (Exception e) {
			plugin.getLogger().warning("Failed to load match data.");
			return;
		}
		plugin.getLogger().info("Successfully loaded data for world " + uid.toString());
	}

	public void saveDataForMatch(UUID uid) {
		File save = new File(uid.toString() + ".json");
		Duration duration = Duration.between(game.getGameStartTime(), LocalDateTime.now());
		if(game.isGameFinished()) {
			duration = Duration.between(game.getGameStartTime(), game.getGameEndTime());
		}

		JsonObject match = new JsonObject();
		JsonObject players = new JsonObject();
		
		match.addProperty("world_uuid", uid.toString());
		match.addProperty("game_start_time", game.getGameStartTime().toString());
		match.addProperty("game_end_time", game.getGameEndTime().toString());
		match.addProperty("game_duration",
				duration.toHours() + ":" + duration.toMinutes() % 60 + ":" + duration.getSeconds() % 60);
		match.addProperty("game_running", game.isGameRunning());
		match.addProperty("game_finished", game.isGameFinished());
		match.addProperty("guardianIsDead", game.isGuardianIsDead());
		match.addProperty("enderDragonIsDead", game.isEnderDragonIsDead());
		match.addProperty("witherIsDead", game.isWitherIsDead());
		match.addProperty("mobsKilled", game.getMobsKilled());
		
		for (HCMPlayer p : plugin.game.HCMPlayers.values()) {
			JsonObject playerData = new JsonObject();
			
			playerData.addProperty("name", p.getPlayer().getDisplayName());
			playerData.addProperty("kills", p.getMobKills());
			playerData.addProperty("mode", p.getPlayerMode());
			playerData.addProperty("killedEnderDragon", p.isKilledEnderDragon());
			playerData.addProperty("killedGuardian", p.isKilledGuardian());
			playerData.addProperty("killedWither", p.isKilledWither());
			playerData.addProperty("enteredBastion", p.isEnteredBastion());
			playerData.addProperty("enteredEnd", p.isEnteredEnd());
			playerData.addProperty("enteredNether", p.isEnteredNether());
			playerData.addProperty("causeOfDeath", p.getCauseOfDeath());
			players.add(p.getPlayer().getUniqueId().toString(), playerData);
		}

		match.add("players", players);
		if (!save.exists()) {
			try {
				save.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(save));
			writer.write(match.toString());
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		plugin.getLogger().info("Successfully saved data for world " + uid.toString());

	}

}
