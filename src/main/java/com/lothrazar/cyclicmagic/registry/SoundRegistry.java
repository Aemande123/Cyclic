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
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SoundRegistry {
  public static ArrayList<SoundEvent> sounds = new ArrayList<SoundEvent>();
  public static SoundEvent crackle;
  public static SoundEvent basey;
  public static SoundEvent bip;
  public static SoundEvent buzzp;
  public static SoundEvent bwewe;
  public static SoundEvent bwoaaap;
  public static SoundEvent byeaa;
  public static SoundEvent dcoin;
  public static SoundEvent fill;
  public static SoundEvent pew;
  public static SoundEvent pow;
  public static SoundEvent thunk;
  public static SoundEvent warp;
  public static SoundEvent spikes_in;
  public static SoundEvent spikes_out;
  public static SoundEvent dungeonfinder;
  public static SoundEvent firelaunch;
  public static SoundEvent goodlaunch;
  public static SoundEvent laserbeanpew;
  public static SoundEvent powerupscales;
  public static SoundEvent spikemaybe;
  public static SoundEvent bass_echo;
  public static SoundEvent coin;
  public static SoundEvent crack;
  public static SoundEvent explosm;
  public static SoundEvent guitar;
  public static SoundEvent hovering;
  public static SoundEvent magic_missile;
  public static SoundEvent metal_pitch;
  public static SoundEvent pew_long;
  public static SoundEvent pschew_fire;
  public static void register() {
    basey = registerSound("basey");//used by storage bag deposit
    bip = registerSound("bip");//cyclic wand GUI rotation
    buzzp = registerSound("buzzp");//carbon paper & invo food & heart-eat-fail
    bwewe = registerSound("bwewe"); //invo food success
    bwoaaap = registerSound("bwoaaap");//vector plate & launch enchant
    byeaa = registerSound("byeaa");//magic net on release&spawn
    crackle = registerSound("crackle");//scaffolding
    dcoin = registerSound("dcoin");//tool toggles: auto torch, piston, randomize, exchange scepters
    fill = registerSound("fill");//heart food success
    pew = registerSound("pew");//magic net on catch (previously was base spell thrown)
    // pow = registerSound("pow");//UNUSED
    thunk = registerSound("thunk");//ChestSack 
    warp = registerSound("warp");//Ender Wings & Book 
    spikes_out = registerSound("spikes_out");//spikes
    spikes_in = registerSound("spikes_in");//spikes
    dungeonfinder = registerSound("dungeonfinder");// wandspawner
    firelaunch = registerSound("firelaunch");//used by wandblaze
    explosm = registerSound("explosm");//entity blaze fire
    goodlaunch = registerSound("goodlaunch");//used by wandice
    laserbeanpew = registerSound("laserbeanpew");// wandlightning
    powerupscales = registerSound("powerupscales");//wandhypno
    magic_missile = registerSound("magic_missile");//wand missile
    //metal_pitch = registerSound("metal_pitch");//UNUSED
    pschew_fire = registerSound("pschew_fire");//waterremoval
    bass_echo = registerSound("bass_echo");//UNUSED
    // coin = registerSound("coin");//UNUSED
    crack = registerSound("crack");//Uncrafter
    //guitar = registerSound("guitar");//UNUSED
    //  hovering = registerSound("hovering");//UNUSED
    //pew_long = registerSound("pew_long");//UNUSED
    // spikemaybe = registerSound("spikemaybe");//UNUSED
  }
  private static SoundEvent registerSound(String name) {
    //thanks for the help: https://github.com/Choonster/TestMod3/tree/162914a163c7fcb6bdd992917fcbc699584e40de/src/main/java/com/choonster/testmod3
    // and http://www.minecraftforge.net/forum/index.php?topic=38076.0
    final ResourceLocation res = new ResourceLocation(Const.MODID, name);
    SoundEvent sound = new SoundEvent(res);
    sound.setRegistryName(res);
    sounds.add(sound);
    return sound;
  }
  @SubscribeEvent
  public static void onRegistryEvent(RegistryEvent.Register<SoundEvent> event) {
    for (SoundEvent b : sounds) {
      event.getRegistry().register(b);
    }
  }
}
