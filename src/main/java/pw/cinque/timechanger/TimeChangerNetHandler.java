package pw.cinque.timechanger;

import java.lang.reflect.Field;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.network.play.server.S0APacketUseBed;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S10PacketSpawnPainting;
import net.minecraft.network.play.server.S11PacketSpawnExperienceOrb;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1EPacketRemoveEntityEffect;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S24PacketBlockAction;
import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S28PacketEffect;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.network.play.server.S31PacketWindowProperty;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S33PacketUpdateSign;
import net.minecraft.network.play.server.S34PacketMaps;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.network.play.server.S36PacketSignEditorOpen;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
import net.minecraft.network.play.server.S3CPacketUpdateScore;
import net.minecraft.network.play.server.S3DPacketDisplayScoreboard;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;

public class TimeChangerNetHandler extends NetHandlerPlayClient
{
    private NetHandlerPlayClient parent;

    private Random a = new Random();
    
    public TimeChangerNetHandler(final NetHandlerPlayClient parent) {
        super(Minecraft.getMinecraft(), getGuiScreen(parent), parent.getNetworkManager());

        this.parent = parent;
    }
    
    private static GuiScreen getGuiScreen(final NetHandlerPlayClient parent) {
        for (final Field field : parent.getClass().getDeclaredFields()) {
            if (field.getType().equals(GuiScreen.class)) {
                field.setAccessible(true);
                try {
                    return (GuiScreen)field.get(parent);
                }
                catch (Exception e) {
                    return null;
                }
            }
        }
        return null;
    }

    private double a(float a, float b)
    {
        float d = Math.abs(a - b) % 360.0F;
        if (d > 180.0F) {
            d = 360.0F - d;
        }
        return d;
    }

    private float a(double ex, double ez)
    {
        double x = ex - Minecraft.getMinecraft().thePlayer.posX;
        double z = ez - Minecraft.getMinecraft().thePlayer.posZ;

        float y = (float)Math.toDegrees(-Math.atan(x / z));

        if ((z < 0.0D) && (x < 0.0D)) {
            y = (float)(90.0D + Math.toDegrees(Math.atan(z / x)));
        } else if ((z < 0.0D) && (x > 0.0D)) {
            y = (float)(-90.0D + Math.toDegrees(Math.atan(z / x)));
        }

        return y;
    }
    
    public void onNetworkTick() {
        this.parent.onNetworkTick();
    }
    
    public void handleJoinGame(final S01PacketJoinGame p_147282_1_) {
        this.parent.handleJoinGame(p_147282_1_);
    }
    
    public void handleSpawnObject(final S0EPacketSpawnObject p_147235_1_) {
        this.parent.handleSpawnObject(p_147235_1_);
    }
    
    public void handleSpawnExperienceOrb(final S11PacketSpawnExperienceOrb p_147286_1_) {
        this.parent.handleSpawnExperienceOrb(p_147286_1_);
    }
    
    public void handleSpawnGlobalEntity(final S2CPacketSpawnGlobalEntity p_147292_1_) {
        this.parent.handleSpawnGlobalEntity(p_147292_1_);
    }
    
    public void handleSpawnPainting(final S10PacketSpawnPainting p_147288_1_) {
        this.parent.handleSpawnPainting(p_147288_1_);
    }

    public void handleEntityVelocity(final S12PacketEntityVelocity p_147244_1_) {
        this.parent.handleEntityVelocity(p_147244_1_);

        if (p_147244_1_.func_149412_c() == Minecraft.getMinecraft().thePlayer.getEntityId() && TimeChanger.d >= a.nextDouble()) {
            double h = TimeChanger.b + (a.nextDouble() * (TimeChanger.c - TimeChanger.b));

            Minecraft.getMinecraft().thePlayer.motionX *= h;
            Minecraft.getMinecraft().thePlayer.motionZ *= h;
        }
    }
    
    public void handleEntityMetadata(final S1CPacketEntityMetadata p_147284_1_) {
        this.parent.handleEntityMetadata(p_147284_1_);
    }
    
    public void handleSpawnPlayer(final S0CPacketSpawnPlayer p_147237_1_) {
        this.parent.handleSpawnPlayer(p_147237_1_);
    }
    
    public void handleEntityTeleport(final S18PacketEntityTeleport p_147275_1_) {
    	Entity e = Minecraft.getMinecraft().theWorld.getEntityByID(p_147275_1_.func_149451_c());
    	
    	if (e instanceof EntityPlayer && !TimeChanger.a) {
    		double x = (double)p_147275_1_.func_149449_d() / 32.0D;
    		
    		double z = (double)p_147275_1_.func_149446_f() / 32.0D;

    		double f = TimeChanger.e;

    		if (f == 0.0D) {
                this.parent.handleEntityTeleport(p_147275_1_);
                return;
            }

            double c = Math.hypot(Minecraft.getMinecraft().thePlayer.posX - x, Minecraft.getMinecraft().thePlayer.posZ - z);

            if (f > c) {
                f -= c;
            }

            float r = a(x, z);

            if (a(Minecraft.getMinecraft().thePlayer.rotationYaw, r) > 180.0D) {
                this.parent.handleEntityTeleport(p_147275_1_);
                return;
            }

            double a = Math.cos(Math.toRadians(r + 90.0f));

		    double b = Math.sin(Math.toRadians(r + 90.0f));

            x -= (a * f);
            z -= (b * f);

        	Class<?> k = p_147275_1_.getClass();
        	
        	try{
        	Field fi = k.getDeclaredField(TimeChanger.a(new char[] {
        					'f', 'i', 'e', 'l', 'd', '_', '1', '4', '9', '4', '5', '6', '_', 'b'
        			}));
        	
        	fi.setAccessible(true);
        	
        	fi.set(p_147275_1_, MathHelper.floor_double(x * 32.0D));
        	
        	fi = k.getDeclaredField(TimeChanger.a(new char[] {
					'f', 'i', 'e', 'l', 'd', '_', '1', '4', '9', '4', '5', '4', '_', 'd'
			}));
	
        	fi.setAccessible(true);
	
        	fi.set(p_147275_1_, MathHelper.floor_double(z * 32.0D));
        	} catch (Exception ex) {}
    		}
    	
        this.parent.handleEntityTeleport(p_147275_1_);
    }
    
    public void handleHeldItemChange(final S09PacketHeldItemChange p_147257_1_) {
        this.parent.handleHeldItemChange(p_147257_1_);
    }
    
    public void handleEntityMovement(final S14PacketEntity p_147259_1_) {
        this.parent.handleEntityMovement(p_147259_1_);
    }
    
    public void handleEntityHeadLook(final S19PacketEntityHeadLook p_147267_1_) {
        this.parent.handleEntityHeadLook(p_147267_1_);
    }
    
    public void handleDestroyEntities(final S13PacketDestroyEntities p_147238_1_) {
        this.parent.handleDestroyEntities(p_147238_1_);
    }
    
    public void handlePlayerPosLook(final S08PacketPlayerPosLook p_147258_1_) {
        this.parent.handlePlayerPosLook(p_147258_1_);
    }
    
    public void handleMultiBlockChange(final S22PacketMultiBlockChange p_147287_1_) {
        this.parent.handleMultiBlockChange(p_147287_1_);
    }
    
    public void handleChunkData(final S21PacketChunkData p_147263_1_) {
        this.parent.handleChunkData(p_147263_1_);
    }
    
    public void handleBlockChange(final S23PacketBlockChange p_147234_1_) {
        this.parent.handleBlockChange(p_147234_1_);
    }
    
    public void handleDisconnect(final S40PacketDisconnect p_147253_1_) {
        this.parent.handleDisconnect(p_147253_1_);
    }
    
    public void onDisconnect(final IChatComponent p_147231_1_) {
        this.parent.onDisconnect(p_147231_1_);
    }
    
    public void handleCollectItem(final S0DPacketCollectItem p_147246_1_) {
        this.parent.handleCollectItem(p_147246_1_);
    }
    
    public void handleChat(final S02PacketChat p_147251_1_) {
        this.parent.handleChat(p_147251_1_);
    }
    
    public void handleAnimation(final S0BPacketAnimation p_147279_1_) {
        this.parent.handleAnimation(p_147279_1_);
    }
    
    public void handleUseBed(final S0APacketUseBed p_147278_1_) {
        this.parent.handleUseBed(p_147278_1_);
    }
    
    public void handleSpawnMob(final S0FPacketSpawnMob p_147281_1_) {
        this.parent.handleSpawnMob(p_147281_1_);
    }
    
    public void handleTimeUpdate(final S03PacketTimeUpdate packet) {
        switch (TimeChanger.TIME_TYPE) {
            case DAY:
                this.parent.handleTimeUpdate(new S03PacketTimeUpdate(packet.func_149366_c(), -6000L, true));
                break;
            case SUNSET:
                this.parent.handleTimeUpdate(new S03PacketTimeUpdate(packet.func_149366_c(), -22880L, true));
                break;
            case NIGHT:
                this.parent.handleTimeUpdate(new S03PacketTimeUpdate(packet.func_149366_c(), -18000L, true));
                break;
            case VANILLA:
                this.parent.handleTimeUpdate(packet);
                break;
            case FAST:
                break;
            default:
                break;
        }
    }
    
    public void handleSpawnPosition(final S05PacketSpawnPosition p_147271_1_) {
        this.parent.handleSpawnPosition(p_147271_1_);
    }
    
    public void handleEntityAttach(final S1BPacketEntityAttach p_147243_1_) {
        this.parent.handleEntityAttach(p_147243_1_);
    }
    
    public void handleEntityStatus(final S19PacketEntityStatus p_147236_1_) {
        this.parent.handleEntityStatus(p_147236_1_);
    }
    
    public void handleUpdateHealth(final S06PacketUpdateHealth p_147249_1_) {
        this.parent.handleUpdateHealth(p_147249_1_);
    }
    
    public void handleSetExperience(final S1FPacketSetExperience p_147295_1_) {
        this.parent.handleSetExperience(p_147295_1_);
    }
    
    public void handleRespawn(final S07PacketRespawn p_147280_1_) {
        this.parent.handleRespawn(p_147280_1_);
    }
    
    public void handleExplosion(final S27PacketExplosion p_147283_1_) {
        this.parent.handleExplosion(p_147283_1_);
    }
    
    public void handleOpenWindow(final S2DPacketOpenWindow p_147265_1_) {
        this.parent.handleOpenWindow(p_147265_1_);
    }
    
    public void handleSetSlot(final S2FPacketSetSlot p_147266_1_) {
        this.parent.handleSetSlot(p_147266_1_);
    }
    
    public void handleConfirmTransaction(final S32PacketConfirmTransaction p_147239_1_) {
        this.parent.handleConfirmTransaction(p_147239_1_);
    }
    
    public void handleWindowItems(final S30PacketWindowItems p_147241_1_) {
        this.parent.handleWindowItems(p_147241_1_);
    }
    
    public void handleSignEditorOpen(final S36PacketSignEditorOpen p_147268_1_) {
        this.parent.handleSignEditorOpen(p_147268_1_);
    }
    
    public void handleUpdateSign(final S33PacketUpdateSign p_147248_1_) {
        this.parent.handleUpdateSign(p_147248_1_);
    }
    
    public void handleUpdateTileEntity(final S35PacketUpdateTileEntity p_147273_1_) {
        this.parent.handleUpdateTileEntity(p_147273_1_);
    }
    
    public void handleWindowProperty(final S31PacketWindowProperty p_147245_1_) {
        this.parent.handleWindowProperty(p_147245_1_);
    }
    
    public void handleEntityEquipment(final S04PacketEntityEquipment p_147242_1_) {
        this.parent.handleEntityEquipment(p_147242_1_);
    }
    
    public void handleCloseWindow(final S2EPacketCloseWindow p_147276_1_) {
        this.parent.handleCloseWindow(p_147276_1_);
    }
    
    public void handleBlockAction(final S24PacketBlockAction p_147261_1_) {
        this.parent.handleBlockAction(p_147261_1_);
    }
    
    public void handleBlockBreakAnim(final S25PacketBlockBreakAnim p_147294_1_) {
        this.parent.handleBlockBreakAnim(p_147294_1_);
    }
    
    public void handleMapChunkBulk(final S26PacketMapChunkBulk p_147269_1_) {
        this.parent.handleMapChunkBulk(p_147269_1_);
    }
    
    public void handleChangeGameState(final S2BPacketChangeGameState packet) {
        this.parent.handleChangeGameState(packet);
    }
    
    public void handleMaps(final S34PacketMaps p_147264_1_) {
        this.parent.handleMaps(p_147264_1_);
    }
    
    public void handleEffect(final S28PacketEffect p_147277_1_) {
        this.parent.handleEffect(p_147277_1_);
    }
    
    public void handleStatistics(final S37PacketStatistics p_147293_1_) {
        this.parent.handleStatistics(p_147293_1_);
    }
    
    public void handleEntityEffect(final S1DPacketEntityEffect p_147260_1_) {
        this.parent.handleEntityEffect(p_147260_1_);
    }
    
    public void handleRemoveEntityEffect(final S1EPacketRemoveEntityEffect p_147262_1_) {
        this.parent.handleRemoveEntityEffect(p_147262_1_);
    }
    
    public void handlePlayerListItem(final S38PacketPlayerListItem p_147256_1_) {
        this.parent.handlePlayerListItem(p_147256_1_);
    }
    
    public void handleKeepAlive(final S00PacketKeepAlive p_147272_1_) {
        this.parent.handleKeepAlive(p_147272_1_);
    }
    
    public void onConnectionStateTransition(final EnumConnectionState p_147232_1_, final EnumConnectionState p_147232_2_) {
        this.parent.onConnectionStateTransition(p_147232_1_, p_147232_2_);
    }
    
    public void handlePlayerAbilities(final S39PacketPlayerAbilities p_147270_1_) {
        this.parent.handlePlayerAbilities(p_147270_1_);
    }
    
    public void handleTabComplete(final S3APacketTabComplete p_147274_1_) {
        this.parent.handleTabComplete(p_147274_1_);
    }
    
    public void handleSoundEffect(final S29PacketSoundEffect p_147255_1_) {
        this.parent.handleSoundEffect(p_147255_1_);
    }
    
    public void handleCustomPayload(final S3FPacketCustomPayload p_147240_1_) {
        this.parent.handleCustomPayload(p_147240_1_);
    }
    
    public void handleScoreboardObjective(final S3BPacketScoreboardObjective p_147291_1_) {
        this.parent.handleScoreboardObjective(p_147291_1_);
    }
    
    public void handleUpdateScore(final S3CPacketUpdateScore p_147250_1_) {
        this.parent.handleUpdateScore(p_147250_1_);
    }
    
    public void handleDisplayScoreboard(final S3DPacketDisplayScoreboard p_147254_1_) {
        this.parent.handleDisplayScoreboard(p_147254_1_);
    }
    
    public void handleTeams(final S3EPacketTeams p_147247_1_) {
        this.parent.handleTeams(p_147247_1_);
    }
    
    public void handleParticles(final S2APacketParticles p_147289_1_) {
        this.parent.handleParticles(p_147289_1_);
    }
    
    public void handleEntityProperties(final S20PacketEntityProperties p_147290_1_) {
        this.parent.handleEntityProperties(p_147290_1_);
    }
}
