package hotl.hcm.listeners;

import hotl.hcm.HCM;
import hotl.hcm.HCMPlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBreakListener implements Listener {

    private HCM hcm;

    public BlockBreakListener (HCM hcm)
    {
        this.hcm = hcm;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        ItemStack tool = player.getInventory().getItemInMainHand();

        // Check if game is not running or is finished
        if (!hcm.game.isGameRunning() || hcm.game.isGameFinished())
        {
            return;
        }

        // Return if the block does not drop ore (wrong tool)
        if (!block.isPreferredTool(tool)) {
            return;
        }

        HCMPlayer hcmPlayer = hcm.game.HCMPlayers.get(player.getUniqueId());

        // Player broke copper ore
        if(block.getType().equals(Material.COPPER_ORE)|| block.getType().equals(Material.DEEPSLATE_COPPER_ORE))
        {
            hcmPlayer.setCopperMined(hcmPlayer.getCopperMined()+1);
        }

        // Player broke iron ore
        if(block.getType().equals(Material.IRON_ORE)|| block.getType().equals(Material.DEEPSLATE_IRON_ORE))
        {
            hcmPlayer.setIronMined(hcmPlayer.getIronMined()+1);
        }

        // Player broke gold ore
        if(block.getType().equals(Material.GOLD_ORE) || block.getType().equals(Material.DEEPSLATE_GOLD_ORE) || block.getType().equals(Material.NETHER_GOLD_ORE))
        {
            hcmPlayer.setGoldMined(hcmPlayer.getGoldMined()+1);
        }

        // Player broke diamond ore
        if(block.getType().equals(Material.DIAMOND_ORE) || block.getType().equals(Material.DEEPSLATE_DIAMOND_ORE))
        {
            hcmPlayer.setDiamondMined(hcmPlayer.getDiamondMined()+1);
        }
    }
}
