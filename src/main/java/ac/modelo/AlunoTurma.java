package ac.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import base.modelo.Aluno;
import base.modelo.Turma;

/**
 * Entity implementation class for Entity: AlunoTurma
 *
 */
@Entity
@Table(name = "tab_aluno_turma")
public class AlunoTurma implements Serializable {

	public AlunoTurma() {
		super();
	}

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_aluno_turma")
	private Long id; 
	
	@NotNull(message = "O campo RA não pode ser nulo")
	@Column(nullable = false)
	private String ra="0";

	@NotNull(message = "O campo aluno não pode ser nulo")
	@JoinColumn(name = "id_pessoa", nullable = false)
	@ManyToOne
	private Aluno aluno; 

	@NotNull(message = "O campo turma não pode ser nulo")
	@JoinColumn(name = "id_turma", nullable = false)
	@ManyToOne
	private Turma turma;  
	
	@Column(name = "permite_cadastro_certificado")
	private Integer permiteCadastroCertificado=1;
	
	private boolean liberado=true;

	@Column(name = "data_mudanca", nullable = false)
	private Date dataMudanca;
	
	@Column(name = "momento_mudanca")
	private Date momentoMudanca; 
	
	private String justificativa;  
	
	private Integer situacao;
	
	private int controle=1;

	@Column(nullable = false)
	private Boolean status=true; 

	public Long getId() {
		return id;
	}

	public Aluno getAluno() {
		return aluno;
	}
	
	public boolean isLiberado() {
		return liberado;
	}

	public void setLiberado(boolean liberado) {
		this.liberado = liberado;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}
	

	public Integer getSituacao() {
		return situacao;
	}

	public void setSituacao(Integer situacao) {
		this.situacao = situacao;
	}

	public Turma getTurma() {
		return turma;
	}

	public String getRa() {
		return ra;
	}

	public void setRa(String ra) {
		this.ra = ra;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public Date getDataMudanca() {
		return dataMudanca;
	}

	public void setDataMudanca(Date dataMudanca) {
		this.dataMudanca = dataMudanca;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Date getMomentoMudanca() {
		return momentoMudanca;
	}

	public int getControle() {
		return controle;
	}

	public void setControle(int controle) {
		this.controle = controle;
	}

	public void setMomentoMudanca(Date momentoMudanca) {
		this.momentoMudanca = momentoMudanca;
	}

	public Integer getPermiteCadastroCertificado() {
		return permiteCadastroCertificado;
	}

	public void setPermiteCadastroCertificado(Integer permiteCadastroCertificado) {
		this.permiteCadastroCertificado = permiteCadastroCertificado;
	}
	
	

	@Override
	public int hashCode() {
		return Objects.hash(aluno, controle, dataMudanca, id, justificativa, liberado, momentoMudanca,
				permiteCadastroCertificado, ra, situacao, status, turma);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AlunoTurma other = (AlunoTurma) obj;
		return Objects.equals(aluno, other.aluno) && controle == other.controle
				&& Objects.equals(dataMudanca, other.dataMudanca) && Objects.equals(id, other.id)
				&& Objects.equals(justificativa, other.justificativa) && liberado == other.liberado
				&& Objects.equals(momentoMudanca, other.momentoMudanca)
				&& Objects.equals(permiteCadastroCertificado, other.permiteCadastroCertificado)
				&& Objects.equals(ra, other.ra) && Objects.equals(situacao, other.situacao)
				&& Objects.equals(status, other.status) && Objects.equals(turma, other.turma);
	}

	@Override
	public String toString() {
		if(aluno!=null && aluno.getNome()!=null && turma!=null )
		return aluno.getNome() + " / "+turma.getAbreviacaoTurma();
		return "";
	}
	
	

}
