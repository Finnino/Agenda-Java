package agenda;

import java.io.*;
import java.util.ArrayList;

public class ArquivoContato {
    private static final String NOME_ARQUIVO = "contatos.txt";

    // Grava a lista completa no arquivo
    public static void salvarContatos(ArrayList<Contato> contatos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(NOME_ARQUIVO))) {
            for (Contato c : contatos) {
                bw.write(c.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    // Lê o arquivo e reconstrói a lista de objetos
    public static ArrayList<Contato> carregarContatos() {
        ArrayList<Contato> contatos = new ArrayList<>();
        File arquivo = new File(NOME_ARQUIVO);

        if (!arquivo.exists()) return contatos; // Se não existe, retorna lista vazia

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length == 5) {
                    String nome = dados[0];
                    String tel = dados[1];
                    String email = dados[2];
                    String end = dados[3];
                    String cat = dados[4];

                    if (cat.equalsIgnoreCase("Pessoal")) {
                        contatos.add(new ContatoPessoal(nome, tel, email, end));
                    } else {
                        contatos.add(new ContatoProfissional(nome, tel, email, end));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo: " + e.getMessage());
        }
        return contatos;
    }
}