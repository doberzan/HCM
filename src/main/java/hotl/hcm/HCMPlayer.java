package hotl.hcm;


import org.bukkit.entity.Player;

import java.time.LocalDateTime;

public class HCMPlayer {
	private Player player;
	private int playerMode;
	private int mobKills;
	private int copperMined;
	private int ironMined;
	private int goldMined;
	private int diamondMined;
	private int endermanKilled;
	private int blazeKilled;
	private boolean killedEnderDragon;
	private boolean killedWither;
	private boolean killedGuardian;
	private boolean enteredBastion;
	private boolean enteredEnd;
	private boolean enteredNether;
	private LocalDateTime enteredNetherTime;
	private LocalDateTime enteredEndTime;
	private LocalDateTime timeOfDeath;
	private String causeOfDeath;
	
	// Modes: alive: 0, protection: 1, spectate: 2
	
	public HCMPlayer(Player player) 
	{
		this.setEndermanKilled(0);
		this.setBlazeKilled(0);
		this.setCopperMined(0);
		this.setIronMined(0);
		this.setGoldMined(0);
		this.setDiamondMined(0);
		this.setTimeOfDeath(LocalDateTime.of(1970, 1, 1, 0, 0, 0));
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


    public LocalDateTime getTimeOfDeath() {
        return timeOfDeath;
    }

    public void setTimeOfDeath(LocalDateTime timeOfDeath) {
        this.timeOfDeath = timeOfDeath;
    }

    public LocalDateTime getEnteredNetherTime() {
        return enteredNetherTime;
    }

    public void setEnteredNetherTime(LocalDateTime enteredNetherTime) {
        this.enteredNetherTime = enteredNetherTime;
    }

    public LocalDateTime getEnteredEndTime() {
        return enteredEndTime;
    }

    public void setEnteredEndTime(LocalDateTime enteredEndTime) {
        this.enteredEndTime = enteredEndTime;
    }

    public int getCopperMined() {
        return copperMined;
    }

    public void setCopperMined(int copperMined) {
        this.copperMined = copperMined;
    }

    public int getIronMined() {
        return ironMined;
    }

    public void setIronMined(int ironMined) {
        this.ironMined = ironMined;
    }

    public int getGoldMined() {
        return goldMined;
    }

    public void setGoldMined(int goldMined) {
        this.goldMined = goldMined;
    }

    public int getDiamondMined() {
        return diamondMined;
    }

    public void setDiamondMined(int diamondMined) {
        this.diamondMined = diamondMined;
    }

    public int getEndermanKilled() {
        return endermanKilled;
    }

    public void setEndermanKilled(int endermanKilled) {
        this.endermanKilled = endermanKilled;
    }

    public int getBlazeKilled() {
        return blazeKilled;
    }

    public void setBlazeKilled(int blazeKilled) {
        this.blazeKilled = blazeKilled;
    }
}
