package processes

const val PORT_1 = 8001
const val PORT_2 = 8002
const val PORT_3 = 8003
const val LOCALHOST = "localhost"
const val NODE_1 = "node1"
const val NODE_2 = "node2"
const val NODE_3 = "node3"

fun getNumberNode(port: Int): Int {
    return port % 1000
}

fun getGroupPorts(port: Int): List<Int> {
    return if (port == PORT_1) {
        listOf(PORT_2, PORT_3)
    } else if (port == PORT_2) {
        listOf(PORT_1, PORT_3)
    }
    else {
        listOf(PORT_1, PORT_2)
    }
}

fun getGroupNodes(): List<String> {
    return listOf(NODE_1, NODE_2, NODE_3)
}

fun checkHighLevelOfProcess(process: Process):Boolean {
    return process.nodeNumber == getNumberNode(PORT_1)
}