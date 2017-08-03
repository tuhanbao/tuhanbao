package com.tuhanbao.study.mina;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.apache.mina.core.buffer.IoBuffer;

import com.tuhanbao.study.mina.http.HttpRequestDecoder;
import com.tuhanbao.study.mina.http.HttpRequestMessage;
import com.tuhanbao.study.mina.http.HttpServer;

public class Main {

    public static void main(String[] args) throws Exception {
        HttpServer.start();
        test();
        
        
    }

    private static void test() throws IOException, SocketException, ClosedChannelException {
        Selector selector = Selector.open();
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        ServerSocket socket = channel.socket();

        // Set the reuseAddress flag accordingly with the setting
        socket.setReuseAddress(false);

        socket.bind(new InetSocketAddress(8082), 50);
        // Configure the server socket,
        channel.register(selector, SelectionKey.OP_ACCEPT);
        
        while (true) {
            int i = selector.select();
            if (i > 0) {
                SocketChannel ch = channel.accept();
                System.out.println(parseData(ch));
            }
        }
    }
    
    private static String parseData(SocketChannel ch) throws IOException {
//        ch.g
        int bufferSize = 2048;
        IoBuffer buf = IoBuffer.allocate(bufferSize);
        
        try {
//            int readBytes = 0;
//            int ret;
            ch.read(buf.buf());
//            while ((ret = ch.read(buf.buf())) > 0) {
//                readBytes += ret;
//
//                if (!buf.hasRemaining()) {
//                    break;
//                }
//            }
        } 
        catch (Exception e) {
            return "";
        }
        finally {
        
            buf.flip();
        }
        HttpRequestMessage message = HttpRequestDecoder.decodeBody(buf);
        return message.getContext();
            
    }
}
