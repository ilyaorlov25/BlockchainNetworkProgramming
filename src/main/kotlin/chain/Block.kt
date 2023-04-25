package chain

import processes.Information

data class Block(
    val index: Long,
    val prev_hash: String,
    val hash: String,
    val data: String,
    val nonce: Long,
    val actuality: Boolean = false
): Information