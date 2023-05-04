package chain

import java.lang.StringBuilder
import kotlin.random.Random.Default.nextInt
import java.security.MessageDigest

class Node {
    private val HASH_LENGTH = 256
    private val NONCE_DEFAULT = 0L

    private var genBlock = Block(
        index=0,
        data="asba",
        prev_hash="last",
        hash="09a4b2272c88fa4fe0b6742f030f516430c16c672799162f1b425471ecdb996c",
        nonce=NONCE_DEFAULT,
        actuality=false
    )

    private var lastBlock: Block = genBlock
    var stepBlock: Block? = null

    fun getGenBlock(): Block {
        return genBlock
    }

    fun setGenBlock(index:Long, data: String, prevHash: String, curHash: String, nonce: Long, isAct: Boolean) {
        genBlock = Block(
            index=index,
            data=data,
            prev_hash =prevHash,
            hash=curHash,
            nonce=nonce,
            actuality=isAct
        )
    }

    fun getLastBlock(): Block { return lastBlock }

    fun tryToCreateBlock(): Block? {
        lateinit var currentHash: String
        if (stepBlock == null) {
            val index = lastBlock.index + 1
            val data = getBlockData()
            val previousHash = lastBlock.hash
            val nonce = NONCE_DEFAULT
            currentHash = getHashBlock(ind=index, lastHash=previousHash, data=data, nonce=nonce)
            stepBlock = Block(
                index=lastBlock.index + 1,
                data=getBlockData(),
                prev_hash = lastBlock.hash,
                hash=currentHash,
                nonce=NONCE_DEFAULT
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

    fun setLastBlock(block: Block) {
        lastBlock = block
        stepBlock = null
    }

    fun isBlockValid(block: Block): Boolean {
        return block.hash.validateHash() && block.index == lastBlock.index + 1
    }

    protected fun getHashBlock(ind: Long, lastHash: String, data: String, nonce: Long): String {
        return (ind.toString() + lastHash + data + nonce.toString()).toSHA()
    }

    protected fun getBlockData(): String {
        return generateRandomData()
    }

    private fun generateRandomData() : String {
        val symbols: List<Char> = ('A'.. 'Z') + ('a'.. 'z') + ('0'..'9')
        val sb: StringBuilder = StringBuilder()
        for (i in 0 until HASH_LENGTH) {
            val randIndex = nextInt(symbols.size)
            sb.append(symbols[randIndex])
        }
        return sb.toString()
    }

    private fun String.validateHash(): Boolean {
        return this.takeLast(4) == "0000"
    }

    private fun String.toSHA(): String {
        val bytes: ByteArray = MessageDigest.getInstance("SHA-256").digest(this.toByteArray())
        return bytes.toHex()
    }

    private fun ByteArray.toHex(): String {
        return joinToString("") {"%02x".format(it)}
    }
}