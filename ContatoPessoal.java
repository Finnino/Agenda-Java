package agenda;

public class ContatoPessoal extends Contato {
    public ContatoPessoal(String nome, String telefone, String email, String endereco) {
        super(nome, telefone, email, endereco);
    }

    @Override
    public String getCategoria() {
        return "Pessoal";
    }
}