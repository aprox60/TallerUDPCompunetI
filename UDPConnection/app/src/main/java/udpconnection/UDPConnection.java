package udpconnection;

import java.net.*;

public class UDPConnection extends Thread {

    private static UDPConnection instance;
    private DatagramSocket socket;

    private UDPConnection() {}

    public static UDPConnection getInstance() {
        if (instance == null) {
            instance = new UDPConnection();
        }
        return instance;
    }

    public void setPort(int port) {
        try {
            socket = new DatagramSocket(port);
            System.out.println("[INFO] Socket abierto en puerto: " + port);
        } catch (SocketException e) {
            System.err.println("[ERROR] No se pudo abrir el socket: " + e.getMessage());
        }
    }

    public void close() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
            System.out.println("[INFO] Socket cerrado.");
        }
    }

    @Override
    public void run() {
        System.out.println("[INFO] Escuchando mensajes...");
        byte[] buffer = new byte[1024];

        while (!socket.isClosed()) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String mensaje = new String(packet.getData(), 0, packet.getLength());
                String origen = packet.getAddress().getHostAddress();
                int puertoOrigen = packet.getPort();

                System.out.println("[RECIBIDO] De " + origen + ":" + puertoOrigen + " -> " + mensaje);

            } catch (Exception e) {
                if (!socket.isClosed()) {
                    System.err.println("[ERROR] Al recibir: " + e.getMessage());
                }
            }
        }
    }

    public void sendDatagram(String msj, String ipDest, int portDest) {
        new Thread(() -> {
            try {
                InetAddress address = InetAddress.getByName(ipDest);
                byte[] data = msj.getBytes();
                DatagramPacket packet = new DatagramPacket(data, data.length, address, portDest);
                socket.send(packet);
                System.out.println("[ENVIADO] A " + ipDest + ":" + portDest + " -> " + msj);
            } catch (Exception e) {
                System.err.println("[ERROR] Al enviar: " + e.getMessage());
            }
        }).start();
    }
}