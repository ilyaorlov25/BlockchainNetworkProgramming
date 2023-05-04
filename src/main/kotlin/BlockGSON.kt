import chain.Block
import com.google.gson.Gson

object BlockGSON {
    var block: Block? = null
    var lastActualIndex = 0L
    val gson = Gson()

    fun BlockGSON.getBlock(): Block? {
        return this.block.also { this.block = null }
    }
}