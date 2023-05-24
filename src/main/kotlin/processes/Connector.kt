package processes

import BlockGSON
import java.io.IOException
import java.io.PrintWriter
import java.net.Socket

object Connector {
    fun Process.sendInfoToAllProcesses(msg: Information) {
        for (port in getGroupPorts(this.port)) {
            try {
                val groupSocket = this.groupSockets.getOrPut(port) {
                    Socket(getGroupNodes()[getNumberNode(port) - 1], port)
                }
                val pw = PrintWriter(groupSocket.getOutputStream(), true)
                pw.println(BlockGSON.gson.toJson(msg))
            } catch (e: IOException) {
                println("Unable to connect to group on port $port")
            }
        }
    }
}