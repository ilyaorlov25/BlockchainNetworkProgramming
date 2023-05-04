import BlockGSON.getBlock
import chain.Node
import processes.Process
import processes.getNumberNode
import controller.Controller.blockActualToAllController
import controller.Controller.blockReceivedActualController
import controller.Controller.blockReceivedController
import controller.Controller.isBlockBroadcastActual
import controller.Controller.isBlockReceived
import controller.Controller.isBlockReceivedActual
import controller.Controller.stepBlockController

fun main(args: Array<String>) {
    val port = args[0].toInt()
    val process = Process("Process on node ${getNumberNode(port)}", getNumberNode(port), port)
    process.start()

    val node = Node()
    while (true) {
        val block = BlockGSON.getBlock()
        if (isBlockReceived(block)) {
            blockReceivedController(block!!, node)
        } else if (isBlockReceivedActual(block, process)) {
            blockReceivedActualController(block!!, node)
        } else if (isBlockBroadcastActual(node, process)) {
            blockActualToAllController(node, process)
        } else {
            stepBlockController(node, process)
        }
    }
    process.stop()
}