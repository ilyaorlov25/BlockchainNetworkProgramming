package chain

import processes.Information

data class Block(
    val index: Long,
    val data: String,
    val prev_hash: String,
    val hash: String,
    val nonce: Long,
    val actuality: Boolean = false
): Information