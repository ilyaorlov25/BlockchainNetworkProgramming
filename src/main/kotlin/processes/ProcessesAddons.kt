package processes

const val PORT_1 = 8001
const val PORT_2 = 8002
const val PORT_3 = 8003
const val LOCALHOST = "localhost"

fun getNumberNode(port: Int): Int {
    return port % 1000
}

fun getGroupPorts(port: Int): List<Int> =
    when (port) {
        PORT_1 -> {
            listOf(PORT_2, PORT_3)
        }
        PORT_2 -> {
            listOf(PORT_1, PORT_3)
        }
        else -> {
            listOf(PORT_1, PORT_2)
        }
    }

fun checkHighLevelOfProcess(process: Process):Boolean {
    return process.nodeNumber == getNumberNode(PORT_1)
}