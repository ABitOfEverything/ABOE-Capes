package me.glorantq.devcapes

import com.jadarstudios.developercapes.DevCapes
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.common.config.Property
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.relauncher.Side
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(modid = "aboe-devcapes", name = "Developer Capes", version = "1.0-SNAPSHOT", acceptedMinecraftVersions = "[1.10.2]", acceptableRemoteVersions = "*")
class CapesMod {
    private val logger: Logger = LogManager.getLogger("DeveloperCapes")

    private lateinit var configuration: Configuration

    @Mod.EventHandler
    fun onModPreInit(event: FMLPreInitializationEvent) {
        if(event.side == Side.SERVER) {
            logger.error("This is a client-only mod!")
            return
        }

        configuration = Configuration(event.suggestedConfigurationFile)
        configuration.load()

        val jsonUrl: String = getKeyOrSetDefault("url", "https://raw.githubusercontent.com/ABitOfEverything/ABitOfEverythingConfigs/master/capes.json", Property.Type.STRING)

        DevCapes.getInstance().registerConfig(jsonUrl)

        logger.info("Registered capes!")
    }

    @Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
    private fun <T> getKeyOrSetDefault(key: String, defaultValue: T, propertyType: Property.Type): T {
        configuration.load()
        val chatCategory = configuration.getCategory("capes")

        if (!chatCategory.containsKey(key)) {
            chatCategory[key] = Property(key, defaultValue.toString(), propertyType)
            configuration.save()

            return defaultValue
        }

        val property = chatCategory.get(key)
        return when (propertyType) {
            Property.Type.DOUBLE -> property.double
            Property.Type.STRING -> property.string
            Property.Type.BOOLEAN -> property.boolean
            Property.Type.INTEGER -> property.int
            else -> defaultValue
        } as T
    }
}