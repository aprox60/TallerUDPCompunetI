package udpconnection;

import java.util.Scanner;
import udpconnection.UDPConnection;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingresa el puerto local de escucha: ");
        int puertoLocal = scanner.nextInt();
        scanner.nextLine();

        UDPConnection conn = UDPConnection.getInstance();
        conn.setPort(puertoLocal);
        conn.start();

        System.out.println("Listo. Escribe mensajes con formato: IP:PUERTO:MENSAJE");
        System.out.println("Ejemplo: 192.168.1.5:9090:Hola compañero");
        System.out.println("Escribe 'salir' para terminar.");

        while (true) {
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("salir")) {
                conn.close();
                break;
            }

            String[] partes = input.split(":", 3);
            if (partes.length < 3) {
                System.out.println("[ERROR] Formato inválido. Usa IP:PUERTO:MENSAJE");
                continue;
            }

            String ip = partes[0];
            int puerto = Integer.parseInt(partes[1]);
            String mensaje = partes[2];

            conn.sendDatagram(mensaje, ip, puerto);
        }

        scanner.close();
        System.out.println("Conexión cerrada.");
    }
}