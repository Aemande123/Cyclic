package com.lothrazar.cyclicmagic.component.cable;

import com.lothrazar.cyclicmagic.SimpleCable;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
/**
 * 
 * @author insomniaKitten
 *
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = Const.MODID, value = Side.CLIENT)
public final class CableModel {
 
}