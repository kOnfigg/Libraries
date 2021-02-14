package net.hodyus.libsplugin.effect;

import java.lang.reflect.*;

import net.hodyus.LibsPlugin;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.inventory.meta.*;

public class FireworkEffectPlayer {
    private Method world_getHandle;
    private Method nms_world_broadcastEntityEffect;
    private Method firework_getHandle;
    
    public void playFirework(World world, Location loc, FireworkEffect fe) throws Exception {
        Firework fw = (Firework)world.spawn(loc, Firework.class);
        Object nms_world = null;
        Object nms_firework = null;
        if (this.world_getHandle == null) {
            this.world_getHandle = getMethod(world.getClass(), "getHandle");
            this.firework_getHandle = getMethod(fw.getClass(), "getHandle");
        }
        nms_world = this.world_getHandle.invoke(world, (Object[])null);
        nms_firework = this.firework_getHandle.invoke(fw, (Object[])null);
        if (this.nms_world_broadcastEntityEffect == null) {
            this.nms_world_broadcastEntityEffect = getMethod(nms_world.getClass(), "broadcastEntityEffect");
        }
        FireworkMeta data = fw.getFireworkMeta();
        data.clearEffects();
        data.setPower(1);
        data.addEffect(fe);
        fw.setFireworkMeta(data);
        this.nms_world_broadcastEntityEffect.invoke(nms_world, nms_firework, 17);
        fw.eject();
        Bukkit.getScheduler().scheduleAsyncDelayedTask(LibsPlugin.getInstance(), () -> fw.detonate(), 2L);
    }
    
    private static Method getMethod(Class<?> cl, String method) {
        Method[] methods;
        for (int length = (methods = cl.getMethods()).length, i = 0; i < length; ++i) {
            Method m = methods[i];
            if (m.getName().equals(method)) {
                return m;
            }
        }
        return null;
    }
}
