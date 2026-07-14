package agenda;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class TelaAgenda extends JFrame {
    private ArrayList<Contato> listaContatos;
    
    // Componentes da Interface
    private JTextField txtNome, txtTelefone, txtEmail, txtEndereco, txtBuscar;
    private JComboBox<String> cbCategoria;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JButton btnSalvar, btnExcluir, btnEditar, btnBuscar, btnLimpar;

    public TelaAgenda() {
        // Carrega os dados existentes do arquivo TXT ao iniciar
        listaContatos = ArquivoContato.carregarContatos();

        setTitle("Agenda de Contatos - POO");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- PAINEL DE FORMULÁRIO (ENTRADA DE DADOS) ---
        JPanel painelFormulario = new JPanel(new GridLayout(5, 2, 5, 5));
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painelFormulario.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        painelFormulario.add(txtNome);

        painelFormulario.add(new JLabel("Telefone:"));
        txtTelefone = new JTextField();
        painelFormulario.add(txtTelefone);

        painelFormulario.add(new JLabel("E-mail:"));
        txtEmail = new JTextField();
        painelFormulario.add(txtEmail);

        painelFormulario.add(new JLabel("Endereço:"));
        txtEndereco = new JTextField();
        painelFormulario.add(txtEndereco);

        painelFormulario.add(new JLabel("Categoria:"));
        cbCategoria = new JComboBox<>(new String[]{"Pessoal", "Profissional", "Acadêmico"});
        painelFormulario.add(cbCategoria);

        add(painelFormulario, BorderLayout.NORTH);

        // --- PAINEL CENTRAL (TABELA E BUSCA) ---
        JPanel painelCentral = new JPanel(new BorderLayout(5, 5));
        painelCentral.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        // Barra de busca
        JPanel painelBusca = new JPanel(new BorderLayout(5, 5));
        txtBuscar = new JTextField();
        btnBuscar = new JButton("Buscar por Nome");
        btnLimpar = new JButton("Limpar Filtro");
        painelBusca.add(txtBuscar, BorderLayout.CENTER);
        
        JPanel painelBotoesBusca = new JPanel(new GridLayout(1, 2, 5, 5));
        painelBotoesBusca.add(btnBuscar);
        painelBotoesBusca.add(btnLimpar);
        painelBusca.add(painelBotoesBusca, BorderLayout.EAST);
        
        painelCentral.add(painelBusca, BorderLayout.NORTH);

        // Tabela
        modeloTabela = new DefaultTableModel(new Object[]{"Nome", "Telefone", "E-mail", "Endereço", "Categoria"}, 0);
        tabela = new JTable(modeloTabela);
        painelCentral.add(new JScrollPane(tabela), BorderLayout.CENTER);

        add(painelCentral, BorderLayout.CENTER);

        // --- PAINEL DE BOTÕES DE AÇÃO ---
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnSalvar = new JButton("Cadastrar / Atualizar");
        btnEditar = new JButton("Preencher Campos");
        btnExcluir = new JButton("Excluir Selecionado");

        painelAcoes.add(btnSalvar);
        painelAcoes.add(btnEditar);
        painelAcoes.add(btnExcluir);
        add(painelAcoes, BorderLayout.SOUTH);

        // --- CONFIGURAÇÃO DOS EVENTOS (AÇÕES DOS BOTÕES) ---
        configurarEventos();
        
        // Exibe os contatos iniciais na tabela
        atualizarTabela(listaContatos);
    }

    private void atualizarTabela(ArrayList<Contato> lista) {
        modeloTabela.setRowCount(0); // Limpa a tabela
        for (Contato c : lista) {
            modeloTabela.addRow(new Object[]{c.getNome(), c.getTelefone(), c.getEmail(), c.getEndereco(), c.getCategoria()});
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        txtEndereco.setText("");
        cbCategoria.setSelectedIndex(0);
        tabela.clearSelection();
    }

    private void configurarEventos() {
        // CADASTRAR OU ATUALIZAR
        btnSalvar.addActionListener(e -> {
            String nome = txtNome.getText().trim();
            String tel = txtTelefone.getText().trim();
            String email = txtEmail.getText().trim();
            String end = txtEndereco.getText().trim();
            String cat = (String) cbCategoria.getSelectedItem();

            if (nome.isEmpty() || tel.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome e Telefone são obrigatórios!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verifica se é uma edição (se o contato já existia pelo nome)
            Contato existente = null;
            for (Contato c : listaContatos) {
                if (c.getNome().equalsIgnoreCase(nome)) {
                    existente = c;
                    break;
                }
            }

            if (existente != null) {
                // Atualiza dados
                existente.setTelefone(tel);
                existente.setEmail(email);
                existente.setEndereco(end);
                JOptionPane.showMessageDialog(this, "Contato atualizado com sucesso!");
            } else {
                // Cria novo contato aplicando polimorfismo/herança
                Contato novo;
                if (cat.equals("Pessoal")) {
                    novo = new ContatoPessoal(nome, tel, email, end);
                } else {
                    novo = new ContatoProfissional(nome, tel, email, end); // Atende Profissional e Acadêmico
                }
                listaContatos.add(novo);
                JOptionPane.showMessageDialog(this, "Contato cadastrado com sucesso!");
            }

            ArquivoContato.salvarContatos(listaContatos);
            atualizarTabela(listaContatos);
            limparCampos();
        });

        // PREENCHER CAMPOS PARA EDITAR
        btnEditar.addActionListener(e -> {
            int linhaSelecionada = tabela.getSelectedRow();
            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um contato na tabela primeiro!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            txtNome.setText((String) modeloTabela.getValueAt(linhaSelecionada, 0));
            txtTelefone.setText((String) modeloTabela.getValueAt(linhaSelecionada, 1));
            txtEmail.setText((String) modeloTabela.getValueAt(linhaSelecionada, 2));
            txtEndereco.setText((String) modeloTabela.getValueAt(linhaSelecionada, 3));
            cbCategoria.setSelectedItem(modeloTabela.getValueAt(linhaSelecionada, 4));
        });

        // EXCLUIR CONTATO
        btnExcluir.addActionListener(e -> {
            int linhaSelecionada = tabela.getSelectedRow();
            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um contato na tabela para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nomeExcluir = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
            
            int confirmacao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir " + nomeExcluir + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmacao == JOptionPane.YES_OPTION) {
                listaContatos.removeIf(c -> c.getNome().equalsIgnoreCase(nomeExcluir));
                ArquivoContato.salvarContatos(listaContatos);
                atualizarTabela(listaContatos);
                limparCampos();
                JOptionPane.showMessageDialog(this, "Contato excluído com sucesso.");
            }
        });

        // BUSCAR CONTATO
        btnBuscar.addActionListener(e -> {
            String termo = txtBuscar.getText().trim().toLowerCase();
            if (termo.isEmpty()) {
                atualizarTabela(listaContatos);
                return;
            }

            ArrayList<Contato> filtrados = new ArrayList<>();
            for (Contato c : listaContatos) {
                if (c.getNome().toLowerCase().contains(termo)) {
                    filtrados.add(c);
                }
            }
            atualizarTabela(filtrados);
        });

        // LIMPAR FILTRO DE BUSCA
        btnLimpar.addActionListener(e -> {
            txtBuscar.setText("");
            atualizarTabela(listaContatos);
        });
    }
}