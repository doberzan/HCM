package hotl.hcm;


import org.bukkit.entity.Player;

public class HCMPlayer {
	private Player player;
	private int playerMode;
	private int mobKills;
	private boolean killedEnderDragon;
	private boolean killedWither;
	private boolean killedGuardian;
	private boolean enteredBastion;
	private boolean enteredEnd;
	private boolean enteredNether;
	private String causeOfDeath;
	
	// Modes: alive: 0, protection: 1, spectate: 2
	
	public HCMPlayer(Player player) 
	{
		this.setCauseOfDeath("");
		this.setMobKills(0);
		this.setPlayerMode(0);
		this.setPlayer(player);
		this.setKilledEnderDragon(false);
		this.setKilledGuardian(false);
		this.setKilledWither(false);
		this.setEnteredBastion(false);
		this.setEnteredEnd(false);
		this.setEnteredNether(false);
	}
	
	public int getMobKills() {
		return mobKills;
	}
	public void setMobKills(int mobKills) {
		this.mobKills = mobKills;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getPlayerMode() {
		return playerMode;
	}

	public void setPlayerMode(int playerMode) {
		this.playerMode = playerMode;
	}

	public boolean isKilledEnderDragon() {
		return killedEnderDragon;
	}

	public void setKilledEnderDragon(boolean killedEnderDragon) {
		this.killedEnderDragon = killedEnderDragon;
	}

	public boolean isKilledWither() {
		return killedWither;
	}

	public void setKilledWither(boolean killedWither) {
		this.killedWither = killedWither;
	}

	public boolean isKilledGuardian() {
		return killedGuardian;
	}

	public void setKilledGuardian(boolean killedGuardian) {
		this.killedGuardian = killedGuardian;
	}

	public boolean isEnteredBastion() {
		return enteredBastion;
	}

	public void setEnteredBastion(boolean enteredBastion) {
		this.enteredBastion = enteredBastion;
	}

	public boolean isEnteredEnd() {
		return enteredEnd;
	}

	public void setEnteredEnd(boolean enteredEnd) {
		this.enteredEnd = enteredEnd;
	}

	public boolean isEnteredNether() {
		return enteredNether;
	}

	public void setEnteredNether(boolean enteredNether) {
		this.enteredNether = enteredNether;
	}

	public String getCauseOfDeath() {
		return causeOfDeath;
	}

	public void setCauseOfDeath(String causeOfDeath) {
		this.causeOfDeath = causeOfDeath;
	}


}
