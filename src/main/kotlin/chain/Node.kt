package chain

import java.lang.StringBuilder
import kotlin.random.Random.Default.nextInt
import java.security.MessageDigest

class Node {
    val HASH_LENGTH = 256
    val NONCE_DEFAULT = 0L

    private var genBlock = Block(
        index=0,
        data="text",
        prev_hash="preioushash",
        hash="982d9e3eb996f559e633f4d194def3761d909f5a3b647d1a851fead67c32c9d1",
        nonce=NONCE_DEFAULT,
        actuality=false
    )

    private var lastBlock: Block = genBlock
    var stepBlock: Block? = null

    fun getLastBlock(): Block = lastBlock

    fun setLastBlock(block: Block) {
        lastBlock = block
        stepBlock = null
    }

    fun getGenBlock(): Block = genBlock

    fun setGenBlock(index: Long, data: String, prev_hash: String, curr_hash: String, nonce: Long, act: Boolean) {
        genBlock = Block(
            index=index,
            data=data,
            prev_hash = prev_hash,
            hash = curr_hash,
            nonce = nonce,
            actuality = act
        )
    }

    fun tryToCreateBlock(): Block? {
        lateinit var currentHash: String
        if (stepBlock == null) {
            val index = lastBlock.index + 1
            val data = getBlockData()
            val previousHash = lastBlock.hash
            val nonce = NONCE_DEFAULT
            currentHash = getHashBlock(ind=index, lastHash=previousHash, data=data, nonce=nonce)
            stepBlock = Block(
                index=index,
                data=data,
                prev_hash=previousHash,
                hash=currentHash,
                nonce=nonce
            )
        } else {
            stepBlock?.let {
                currentHash = getHashBlock(
                    ind=it.index, lastHash=it.prev_hash, data=it.data, nonce=it.nonce + 1
                )
                stepBlock = it.copy(hash=currentHash, nonce=it.nonce+1)
            }
        }
        return if (currentHash.validateHash()) {
            stepBlock?.also { lastBlock = it; stepBlock = null}
        } else {
            null
        }
    }

    fun isBlockValid(block: Block): Boolean =
        block.hash.validateHash() && block.index == lastBlock.index + 1

    private fun getHashBlock(ind: Long, lastHash: String, data: String, nonce: Long): String =
        (ind.toString() + lastHash + data + nonce.toString()).toSHA()

    private fun getBlockData(): String = generateRandomData()

    private fun generateRandomData() : String {
        val symbols: List<Char> = ('A'.. 'Z') + ('a'.. 'z') + ('0'..'9')
        val sb: StringBuilder = StringBuilder()
        for (i in 0 until HASH_LENGTH) {
            val randIndex = nextInt(symbols.size)
            sb.append(symbols[randIndex])
        }
        return sb.toString()
    }

    private fun String.validateHash(): Boolean = this.takeLast(4) == "0000"

    private fun String.toSHA(): String {
        val bytes: ByteArray = MessageDigest.getInstance("SHA-256").digest(this.toByteArray())
        return bytes.toHex()
    }

    private fun ByteArray.toHex(): String = joinToString("") {"02x".format(it)}
}