package agenda;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Executa a interface de forma segura para o Swing
        SwingUtilities.invokeLater(() -> {
            TelaAgenda tela = new TelaAgenda();
            tela.setVisible(true);
        });
    }
}