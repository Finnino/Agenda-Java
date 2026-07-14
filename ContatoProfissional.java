package agenda;

public class ContatoProfissional extends Contato {
    public ContatoProfissional(String nome, String telefone, String email, String endereco) {
        super(nome, telefone, email, endereco);
    }

    @Override
    public String getCategoria() {
        return "Profissional";
    }
}