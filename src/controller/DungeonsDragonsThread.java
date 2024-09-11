package controller;
import java.util.concurrent.Semaphore;
import java.util.Random;

public class DungeonsDragonsThread extends Thread {
    public final int TID = (int) threadId();
    private static final int TOTAL_PORTAS = 4;
    private static int portaCorreta = new Random().nextInt(TOTAL_PORTAS);
    private static boolean[] portasDisponiveis = new boolean[TOTAL_PORTAS];
    private static Semaphore semaforoPortas = new Semaphore(1);
    private int velocidade;
    private int distance;

    private static boolean PegarPedraBrilhante = false;
    private static boolean PegarTocha = false;

    private static Semaphore semaforoTocha = new Semaphore(1);
    private static Semaphore semaforoPedraBrilhante = new Semaphore(1);
    private static Semaphore semaforoEntrarPorta = new Semaphore(1);
    private static Semaphore semaforoPedra = new Semaphore(1);
    private static Random rnd = new Random();

    static {
        for (int i = 0; i < TOTAL_PORTAS; i++) {
            portasDisponiveis[i] = true;
        }
    }

    public DungeonsDragonsThread() {
        this.velocidade = rnd.nextInt(3) + 2; // Atribui uma velocidade inicial entre 2 e 4
        this.distance = 0;
    }

    @Override
    public void run() {
        while (distance < 2000) {
            try {
                Thread.sleep(50); // Pausa a thread por 50 ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            distance += velocidade; 
            if (distance >= 500 && distance <= 600 && !PegarTocha) {
                try {
                    semaforoTocha.acquire();
                    if (!PegarTocha) {
                        PegarTocha = true;
                        velocidade += 2; 
                        System.out.println("Cavaleiro #" + TID + " pegou a tocha e agora sua velocidade eh " + velocidade + " m/50ms.");
                    }
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                } finally {
                    semaforoTocha.release();
                }
            }

            // Verifica se o cavaleiro está na região da pedra brilhante (1500m a 1600m)
            if (distance >= 1500 && distance <= 1600 && !PegarPedraBrilhante && !PegarTocha) {
                try {
                    semaforoPedra.acquire();
                    if (!PegarPedraBrilhante && !PegarTocha) {
                        PegarPedraBrilhante = true;
                        velocidade += 2; // Aumenta a velocidade
                        System.out.println("Cavaleiro #" + TID + " pegou a pedra brilhante e agora sua velocidade eh " + velocidade + " m/50ms.");
                    }
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                } finally {
                    semaforoPedra.release();
                }
            }

            
            System.out.println("Cavaleiro #" + TID + " percorreu " + distance + " metros.");

            
            if (distance >= 2000) {
                porta(); 
                return; 
            }
        }
    }

    private void porta() {
        try {
            semaforoPortas.acquire();
            System.out.println("Cavaleiro #" + TID + " chegou a uma porta.");

            int escolhaPorta = escolherPorta();
            if (escolhaPorta != -1) {
                System.out.println("Cavaleiro #" + TID + " escolheu a porta " + escolhaPorta);
                if (escolhaPorta == portaCorreta) {
                    System.out.println("Os deuses reservaram um destino seguro para o Cavaleiro #" + TID);
                } else {
                    System.out.println("Os dados do destino foram crueis com o Cavaleiro #" + TID);
                }
            } else {
                System.out.println("Todas as portas já foram escolhidas.");
            }
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        } finally {
            semaforoPortas.release();
        }
    }

    private int escolherPorta() {
        for (int i = 0; i < TOTAL_PORTAS; i++) {
            if (portasDisponiveis[i]) {
                portasDisponiveis[i] = false;
                return i;
            }
        }
        return -1; 
    }
}