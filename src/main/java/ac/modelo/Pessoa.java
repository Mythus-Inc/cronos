package ac.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.validation.annotation.Validated;

/**
 * Entity implementation class for Entity: Pessoa
 *
 */
@Entity
@Table(name = "tab_pessoa")
@Inheritance(strategy = InheritanceType.JOINED)
public class Pessoa implements Serializable {

	public Pessoa() {
		super();
	}

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_pessoa")
	private Long id;

	@NotNull(message = "O campo nome não pode ser nulo")
	@Column(nullable = false)
	private String nome;

	// @Pattern(regexp = ".+@.+\\.[a-z]+", message = "E-mail inv�lido")
	// @NotNull(message = "O campo e-mail n�o pode ser nulo")
	// @Column(nullable = false)
	private String usuario;

	private String senha;

	private String perfilAluno;

	// perfil foi retirado

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date dataCadastro = new Date();

	@Column(nullable = false)
	private Boolean status=true;

	// @CPF(message="CPF inválido")
	@NotNull(message = "O campo cpf não pode ser nulo")
	@Column(nullable = false)
	private String cpf;

	private Date dataNascimento;

	private String sexo;

	private String nomeMae;

	private String nomePai;

	@NotNull(message = "O campo rg não pode ser nulo")
	@Column(nullable = false)
	private String rg="000";

	private String orgaoRg;

	private String celular;

	private String telefone;

	private String natalidade;

	public String getCpf() {
		if(cpf==null) {
			return "";
		}
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getNomeMae() {
		return nomeMae;
	}

	public void setNomeMae(String nomeMae) {
		this.nomeMae = nomeMae;
	}

	public String getNomePai() {
		return nomePai;
	}

	public void setNomePai(String nomePai) {
		this.nomePai = nomePai;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getOrgaoRg() {
		return orgaoRg;
	}

	public void setOrgaoRg(String orgaoRg) {
		this.orgaoRg = orgaoRg;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getNatalidade() {
		return natalidade;
	}

	public void setNatalidade(String natalidade) {
		this.natalidade = natalidade;
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUsuario() {
		if(usuario==null) {
			return "";
		}
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getPerfilAluno() {
		return perfilAluno;
	}

	public void setPerfilAluno(String perfilAluno) {
		this.perfilAluno = perfilAluno;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(celular, cpf, dataCadastro, dataNascimento, id, natalidade, nome, nomeMae, nomePai, orgaoRg,
				perfilAluno, rg, senha, sexo, status, telefone, usuario);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pessoa other = (Pessoa) obj;
		return Objects.equals(celular, other.celular) && Objects.equals(cpf, other.cpf)
				&& Objects.equals(dataCadastro, other.dataCadastro)
				&& Objects.equals(dataNascimento, other.dataNascimento) && Objects.equals(id, other.id)
				&& Objects.equals(natalidade, other.natalidade) && Objects.equals(nome, other.nome)
				&& Objects.equals(nomeMae, other.nomeMae) && Objects.equals(nomePai, other.nomePai)
				&& Objects.equals(orgaoRg, other.orgaoRg) && Objects.equals(perfilAluno, other.perfilAluno)
				&& Objects.equals(rg, other.rg) && Objects.equals(senha, other.senha)
				&& Objects.equals(sexo, other.sexo) && Objects.equals(status, other.status)
				&& Objects.equals(telefone, other.telefone) && Objects.equals(usuario, other.usuario);
	}

}
