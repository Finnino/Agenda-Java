package agenda;

public abstract class Contato {
    private String nome;
    private String telefone;
    private String email;
    private String endereco;

    public Contato(String nome, String telefone, String email, String endereco) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
    }

    // Encapsulamento: Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    // Método Abstrato (Força as filhas a dizerem sua categoria)
    public abstract String getCategoria();

    // Polimorfismo: Sobrescrita para formatar a linha do arquivo TXT
    @Override
    public String toString() {
        return nome + ";" + telefone + ";" + email + ";" + endereco + ";" + getCategoria();
    }
}