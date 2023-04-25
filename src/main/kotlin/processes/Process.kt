package processes

import BlockGSON
import chain.Block
import com.google.gson.JsonParseException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException

open class Process(val name: String, val nodeNumber: Int, val port: Int) {
    private lateinit var serverSocket: ServerSocket
    private lateinit var clientSocket: Socket

    private var serverThread: Thread? = null
    private var clientThread: Thread? = null

    private val listOfClientSockets: MutableList<Socket> = mutableListOf()
    private val listOfClientThreads: MutableList<Thread> = mutableListOf()

    val groupSockets = mutableMapOf<Int, Socket>()

    fun start() {
        serverSocket = ServerSocket(port)

        serverThread = Thread {
            while (!Thread.currentThread().isInterrupted) {
                try {
                    Thread.sleep(1000)

                    // make connection client with server
                    clientSocket = serverSocket.accept()
                    listOfClientSockets.add(clientSocket)

                    clientThread = Thread {
                        val inData = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
                        while (!Thread.currentThread().isInterrupted) {
                            try {
                                Thread.sleep(1000)
                                val str = inData.readLine()
                                try {
                                    val blockchainBlock = BlockGSON.gson.fromJson(str, Block::class.java) ?: break
                                    BlockGSON.block = blockchainBlock
                                    if (blockchainBlock.actuality) {
                                        println("$name block actualize: $blockchainBlock")
                                    } else {
                                        println("$name block receive: $blockchainBlock")
                                    }
                                } catch (e: JsonParseException) {
                                    println("$name error parse str: $str")
                                }
                            } catch (e: SocketException) {
                                Thread.currentThread().interrupt()
                                println("Error: Client socket closed")
                            } catch (e:InterruptedException) {
                                println("${Thread.currentThread().name} interrupted")
                            }
                        }
                    }
                    clientThread?.start()
                    listOfClientThreads.add(clientThread!!)

                } catch (e: SocketException) {
                    Thread.currentThread().interrupt()
                    println("Error: Client socket closed")
                } catch (e: InterruptedException) {
                    println("${Thread.currentThread().name} interrupted")
                }
            }
        }
        serverThread?.start()
    }

    fun stop() {
        listOfClientThreads.forEach { it.interrupt() }
        listOfClientThreads.clear()

        serverThread?.interrupt()

        listOfClientSockets.forEach { it.close() }
        serverSocket.close()
    }
}