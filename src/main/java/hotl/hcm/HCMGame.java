package hotl.hcm;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

public class HCMGame {

	public HashMap<UUID, HCMPlayer> HCMPlayers;

	private LocalDateTime gameStartTime;
	private LocalDateTime gameEndTime;

	private boolean gameRunning;
	private boolean gameFinished;

	private boolean guardianIsDead;
	private boolean enderDragonIsDead;
	private boolean witherIsDead;
	private int mobsKilled;

	private HCM hcm;

	public HCMGame(HCM hcm) {
		this.hcm = hcm;
		HCMPlayers = new HashMap<UUID,HCMPlayer>();
		gameStartTime = LocalDateTime.now();
		gameEndTime = LocalDateTime.now();
		gameRunning = false;
		gameFinished = false;
		guardianIsDead = false;
		enderDragonIsDead = false;
		witherIsDead = false;
	}

	public LocalDateTime getGameStartTime() {
		return gameStartTime;
	}

	public void setGameStartTime(LocalDateTime gameStartTime) {
		this.gameStartTime = gameStartTime;
	}

	public LocalDateTime getGameEndTime() {
		return gameEndTime;
	}

	public void setGameEndTime(LocalDateTime gameEndTime) {
		this.gameEndTime = gameEndTime;
	}

	public boolean isGameRunning() {
		return gameRunning;
	}

	public void setGameRunning(boolean gameRunning) {
		this.gameRunning = gameRunning;
	}

	public boolean isGameFinished() {
		return gameFinished;
	}

	public void setGameFinished(boolean gameFinished) {
		this.gameFinished = gameFinished;
	}

	public boolean isGuardianIsDead() {
		return guardianIsDead;
	}

	public void setGuardianIsDead(boolean guardianIsDead) {
		this.guardianIsDead = guardianIsDead;
	}

	public int getMobsKilled() {
		return mobsKilled;
	}

	public void setMobsKilled(int mobsKilled) {
		this.mobsKilled = mobsKilled;
	}

	public boolean isEnderDragonIsDead() {
		return enderDragonIsDead;
	}

	public void setEnderDragonIsDead(boolean enderDragonIsDead) {
		this.enderDragonIsDead = enderDragonIsDead;
	}

	public boolean isWitherIsDead() {
		return witherIsDead;
	}

	public void setWitherIsDead(boolean witherIsDead) {
		this.witherIsDead = witherIsDead;
	}

	public void resetGame() {
		setGameRunning(false);
		setGameStartTime(LocalDateTime.now());
		setGameEndTime(LocalDateTime.now());
		setGameFinished(false);
		setMobsKilled(0);
		setWitherIsDead(false);
		setGuardianIsDead(false);
		setEnderDragonIsDead(false);

		// Reset player data
		HCMPlayers.clear();
		for (Player p : Bukkit.getOnlinePlayers())
		{
			HCMPlayers.put(p.getUniqueId(), new HCMPlayer(p));
		}
	}
}
