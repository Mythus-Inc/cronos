package base.modelo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AlunoDTO {
	
	private String nome;
	
	private String email;
	@JsonIgnore
	private String senha;
	@JsonIgnore
	private String ra;
	private List<AlunoTurmaDTO> alunoTurma=new ArrayList<AlunoTurmaDTO>();
	
	
	
	public String getRa() {
		return ra;
	}
	public void setRa(String ra) {
		this.ra = ra;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public List<AlunoTurmaDTO> getAlunoTurma() {
		return alunoTurma;
	}
	public void setAlunoTurma(List<AlunoTurmaDTO> alunoTurma) {
		this.alunoTurma = alunoTurma;
	}
	
	

}
