package questionario.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ac.modelo.AlunoTurma;
import base.modelo.Aluno;
import base.modelo.Curso;
import base.modelo.Turma;

@Entity
@Table(name = "tab_email")
public class Email implements Serializable {

	public Email() {
		super();
	}

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_email")
	private Long id;

	private boolean enviado;
	
	private String dataEnvio;
	
	private boolean status;
	

	@ManyToOne
	private AlunoTurma alunoTurma;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	


	public boolean isEnviado() {
		return enviado;
	}
	

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public void setEnviado(boolean enviado) {
		this.enviado = enviado;
	}

	public String getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(String dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	public AlunoTurma getAlunoTurma() {
		return alunoTurma;
	}

	public void setAlunoTurma(AlunoTurma alunoTurma) {
		this.alunoTurma = alunoTurma;
	}



	 

	
	
	
	
	
	

}
