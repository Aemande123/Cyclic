/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.registry;
import com.lothrazar.cyclicmagic.component.cyclicwand.PacketSpellBuildSize;
import com.lothrazar.cyclicmagic.component.cyclicwand.PacketSpellFromServer;
import com.lothrazar.cyclicmagic.component.cyclicwand.PacketSpellShiftLeft;
import com.lothrazar.cyclicmagic.component.cyclicwand.PacketSpellShiftRight;
import com.lothrazar.cyclicmagic.component.cyclicwand.PacketWandGui;
import com.lothrazar.cyclicmagic.component.enderbook.PacketDeleteWaypoint;
import com.lothrazar.cyclicmagic.component.enderbook.PacketNewButton;
import com.lothrazar.cyclicmagic.component.enderbook.PacketWarpButton;
import com.lothrazar.cyclicmagic.component.merchant.PacketSyncVillagerToClient;
import com.lothrazar.cyclicmagic.component.merchant.PacketSyncVillagerToServer;
import com.lothrazar.cyclicmagic.component.merchant.PacketVillagerTrade;
import com.lothrazar.cyclicmagic.component.password.PacketTilePassword;
import com.lothrazar.cyclicmagic.component.pattern.PacketTilePatternSwap;
import com.lothrazar.cyclicmagic.component.playerext.PacketOpenExtendedInventory;
import com.lothrazar.cyclicmagic.component.playerext.PacketOpenFakeWorkbench;
import com.lothrazar.cyclicmagic.component.playerext.PacketOpenNormalInventory;
import com.lothrazar.cyclicmagic.component.playerext.PacketSyncExtendedInventory;
import com.lothrazar.cyclicmagic.component.pylonexp.PacketTilePylon;
import com.lothrazar.cyclicmagic.component.terrariabuttons.PacketDepositContainerToPlayer;
import com.lothrazar.cyclicmagic.component.terrariabuttons.PacketDepositPlayerToNearby;
import com.lothrazar.cyclicmagic.component.terrariabuttons.PacketQuickStack;
import com.lothrazar.cyclicmagic.component.terrariabuttons.PacketRestockContainerToPlayer;
import com.lothrazar.cyclicmagic.component.vector.PacketTileVector;
import com.lothrazar.cyclicmagic.fluid.PacketFluidSync;
import com.lothrazar.cyclicmagic.net.PacketChestSack;
import com.lothrazar.cyclicmagic.net.PacketGuiShortOverride;
import com.lothrazar.cyclicmagic.net.PacketItemToggle;
import com.lothrazar.cyclicmagic.net.PacketMoveBlock;
import com.lothrazar.cyclicmagic.net.PacketMovePlayerColumn;
import com.lothrazar.cyclicmagic.net.PacketMovePlayerHotbar;
import com.lothrazar.cyclicmagic.net.PacketParticleAtPosition;
import com.lothrazar.cyclicmagic.net.PacketPlayerFalldamage;
import com.lothrazar.cyclicmagic.net.PacketRandomize;
import com.lothrazar.cyclicmagic.net.PacketScythe;
import com.lothrazar.cyclicmagic.net.PacketSleepClient;
import com.lothrazar.cyclicmagic.net.PacketSound;
import com.lothrazar.cyclicmagic.net.PacketSwapBlock;
import com.lothrazar.cyclicmagic.net.PacketSwapPlayerHotbar;
import com.lothrazar.cyclicmagic.net.PacketSyncPlayerData;
import com.lothrazar.cyclicmagic.net.PacketSyncPlayerFlying;
import com.lothrazar.cyclicmagic.net.PacketSyncPlayerHealth;
import com.lothrazar.cyclicmagic.net.PacketTileFuelDisplay;
import com.lothrazar.cyclicmagic.net.PacketTileIncrementField;
import com.lothrazar.cyclicmagic.net.PacketTileRedstoneToggle;
import com.lothrazar.cyclicmagic.net.PacketTileSetField;
import com.lothrazar.cyclicmagic.net.PacketTileSizeToggle;
import com.lothrazar.cyclicmagic.net.PacketTileTextbox;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketRegistry {
  public static void register(SimpleNetworkWrapper network) {
    int packetID = 0;
    network.registerMessage(PacketOpenExtendedInventory.class, PacketOpenExtendedInventory.class, packetID++, Side.SERVER);
    network.registerMessage(PacketOpenNormalInventory.class, PacketOpenNormalInventory.class, packetID++, Side.SERVER);
    network.registerMessage(PacketSyncExtendedInventory.class, PacketSyncExtendedInventory.class, packetID++, Side.CLIENT);
    network.registerMessage(PacketSpellShiftLeft.class, PacketSpellShiftLeft.class, packetID++, Side.SERVER);
    network.registerMessage(PacketSpellShiftRight.class, PacketSpellShiftRight.class, packetID++, Side.SERVER);
    network.registerMessage(PacketParticleAtPosition.class, PacketParticleAtPosition.class, packetID++, Side.CLIENT);
    network.registerMessage(PacketSpellFromServer.class, PacketSpellFromServer.class, packetID++, Side.SERVER);
    network.registerMessage(PacketWandGui.class, PacketWandGui.class, packetID++, Side.SERVER);
    network.registerMessage(PacketMovePlayerColumn.class, PacketMovePlayerColumn.class, packetID++, Side.SERVER);
    network.registerMessage(PacketMovePlayerHotbar.class, PacketMovePlayerHotbar.class, packetID++, Side.SERVER);
    network.registerMessage(PacketWarpButton.class, PacketWarpButton.class, packetID++, Side.SERVER);
    network.registerMessage(PacketNewButton.class, PacketNewButton.class, packetID++, Side.SERVER);
    network.registerMessage(PacketDeleteWaypoint.class, PacketDeleteWaypoint.class, packetID++, Side.SERVER);
    network.registerMessage(PacketDepositPlayerToNearby.class, PacketDepositPlayerToNearby.class, packetID++, Side.SERVER);
    network.registerMessage(PacketDepositContainerToPlayer.class, PacketDepositContainerToPlayer.class, packetID++, Side.SERVER);
    network.registerMessage(PacketQuickStack.class, PacketQuickStack.class, packetID++, Side.SERVER);
    network.registerMessage(PacketRestockContainerToPlayer.class, PacketRestockContainerToPlayer.class, packetID++, Side.SERVER);
    network.registerMessage(PacketOpenFakeWorkbench.class, PacketOpenFakeWorkbench.class, packetID++, Side.SERVER);
    network.registerMessage(PacketSpellBuildSize.class, PacketSpellBuildSize.class, packetID++, Side.SERVER);
    network.registerMessage(PacketSyncPlayerData.class, PacketSyncPlayerData.class, packetID++, Side.CLIENT);
    packetID++;//removed packets from a refactor . KEEP THESE lines so packet ids dont mismatch
    packetID++;
    network.registerMessage(PacketSyncPlayerHealth.class, PacketSyncPlayerHealth.class, packetID++, Side.CLIENT);
    network.registerMessage(PacketTilePassword.class, PacketTilePassword.class, packetID++, Side.SERVER);
    network.registerMessage(PacketMoveBlock.class, PacketMoveBlock.class, packetID++, Side.SERVER);
    packetID++;
    network.registerMessage(PacketSwapBlock.class, PacketSwapBlock.class, packetID++, Side.SERVER);
    network.registerMessage(PacketRandomize.class, PacketRandomize.class, packetID++, Side.SERVER);
    network.registerMessage(PacketChestSack.class, PacketChestSack.class, packetID++, Side.SERVER);
    network.registerMessage(PacketTileRedstoneToggle.class, PacketTileRedstoneToggle.class, packetID++, Side.SERVER);
    network.registerMessage(PacketTileSizeToggle.class, PacketTileSizeToggle.class, packetID++, Side.SERVER);
    network.registerMessage(PacketTileIncrementField.class, PacketTileIncrementField.class, packetID++, Side.SERVER);
    packetID++;
    network.registerMessage(PacketTilePatternSwap.class, PacketTilePatternSwap.class, packetID++, Side.SERVER);
    packetID++;
    network.registerMessage(PacketTileVector.class, PacketTileVector.class, packetID++, Side.SERVER);
    network.registerMessage(PacketPlayerFalldamage.class, PacketPlayerFalldamage.class, packetID++, Side.SERVER);
    network.registerMessage(PacketSyncPlayerFlying.class, PacketSyncPlayerFlying.class, packetID++, Side.CLIENT);
    network.registerMessage(PacketSyncVillagerToClient.class, PacketSyncVillagerToClient.class, packetID++, Side.CLIENT);
    network.registerMessage(PacketSyncVillagerToServer.class, PacketSyncVillagerToServer.class, packetID++, Side.SERVER);
    network.registerMessage(PacketVillagerTrade.class, PacketVillagerTrade.class, packetID++, Side.SERVER);
    network.registerMessage(PacketSleepClient.class, PacketSleepClient.class, packetID++, Side.CLIENT);
    packetID++;
    network.registerMessage(PacketItemToggle.class, PacketItemToggle.class, packetID++, Side.SERVER);
    network.registerMessage(PacketTilePylon.class, PacketTilePylon.class, packetID++, Side.SERVER);
    network.registerMessage(PacketSound.class, PacketSound.class, packetID++, Side.CLIENT);
    network.registerMessage(PacketFluidSync.class, PacketFluidSync.class, packetID++, Side.CLIENT);
    network.registerMessage(PacketSwapPlayerHotbar.class, PacketSwapPlayerHotbar.class, packetID++, Side.SERVER);
    //    network.registerMessage(PacketTileFacingToggle.class, PacketTileFacingToggle.class, packetID++, Side.SERVER);
    network.registerMessage(PacketTileSetField.class, PacketTileSetField.class, packetID++, Side.SERVER);
    network.registerMessage(PacketTileFuelDisplay.class, PacketTileFuelDisplay.class, packetID++, Side.SERVER);
    network.registerMessage(PacketGuiShortOverride.class, PacketGuiShortOverride.class, packetID++, Side.CLIENT);
    network.registerMessage(PacketTileTextbox.class, PacketTileTextbox.class, packetID++, Side.SERVER);
    network.registerMessage(PacketScythe.class, PacketScythe.class, packetID++, Side.SERVER);
  }
}
