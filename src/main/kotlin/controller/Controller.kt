package controller

import BlockGSON
import chain.Block
import chain.Node
import processes.Connector.sendInfoToAllProcesses
import processes.Process
import processes.checkHighLevelOfProcess

object Controller {
    const val INTERVAL = 50

    fun isBlockReceived(block: Block?): Boolean = (block != null) && !block.actuality

    fun isBlockReceivedActual(block: Block?, process: Process): Boolean =
        block != null && !checkHighLevelOfProcess(process)

    fun isBlockBroadcastActual(node: Node, process: Process): Boolean {
        return BlockGSON.lastActualIndex != node.getLastBlock().index
                && node.getLastBlock().index % INTERVAL == 0L
                && checkHighLevelOfProcess(process)
    }

    fun blockReceivedActualController(block: Block, node: Node): Boolean {
        node.setLastBlock(block)
        return true
    }

    fun blockReceivedController(block: Block, node: Node): Boolean {
        if (node.isBlockValid(block)) {
            node.setLastBlock(block)
            return true
        }
        return false
    }

    fun blockActualToAllController(node: Node, process: Process): Boolean {
        val blockActual = node.getLastBlock().copy(actuality=true)
        BlockGSON.lastActualIndex = blockActual.index
        process.sendInfoToAllProcesses(blockActual)
        return true
    }

    fun stepBlockController(node: Node, process: Process): Boolean {
        val stepBlock: Block? = node.tryToCreateBlock()
        if (stepBlock != null) {
            process.sendInfoToAllProcesses(stepBlock)
            return true
        }
        return false
    }
}