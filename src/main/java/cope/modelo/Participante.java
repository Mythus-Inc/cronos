package cope.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ac.modelo.Pessoa;

@Entity
@Table(name = "tab_participante")
public class Participante implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_participante")
	private Long id;
	@ManyToOne
	private Pessoa pessoa;
	@ManyToOne
	private Projeto projeto;
	private String funcao;
	private Integer cargaHoraria;
	private String identificacao;
	private String campus;
	private boolean eventual;
	private boolean aceite;
	private boolean status;
	private String nomeCampus;
	
	private Date dataInicioBolsa;
	
	private Date dataFimBolsa;	
	private Date dataInicioAtividade;	
	private Date dataFimAtividade;
	
	@ManyToOne
	private Programas programa;
	
	

	public Participante() {
		super();
		status = true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	

	public String getNomeCampus() {
		return nomeCampus;
	}

	public void setNomeCampus(String nomeCampus) {
		this.nomeCampus = nomeCampus;
	}

	public Programas getPrograma() {
		return programa;
	}

	public void setPrograma(Programas programa) {
		this.programa = programa;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	public String getFuncao() {
		return funcao;
	}

	public void setFuncao(String funcao) {
		this.funcao = funcao;
	}

	public Integer getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(Integer cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	public String getIdentificacao() {
		return identificacao;
	}

	public void setIdentificacao(String identificacao) {
		this.identificacao = identificacao;
	}

	public String getCampus() {
		return campus;
	}

	public void setCampus(String campus) {
		this.campus = campus;
	}

	public boolean isAceite() {
		return aceite;
	}

	public void setAceite(boolean aceite) {
		this.aceite = aceite;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean isEventual() {
		return eventual;
	}

	public void setEventual(boolean eventual) {
		this.eventual = eventual;
	}

	public Date getDataInicioBolsa() {
		return dataInicioBolsa;
	}

	public void setDataInicioBolsa(Date dataInicioBolsa) {
		this.dataInicioBolsa = dataInicioBolsa;
	}

	public Date getDataFimBolsa() {
		return dataFimBolsa;
	}

	public void setDataFimBolsa(Date dataFimBolsa) {
		this.dataFimBolsa = dataFimBolsa;
	}

	public Date getDataInicioAtividade() {
		return dataInicioAtividade;
	}

	public void setDataInicioAtividade(Date dataInicioAtividade) {
		this.dataInicioAtividade = dataInicioAtividade;
	}

	public Date getDataFimAtividade() {
		return dataFimAtividade;
	}

	public void setDataFimAtividade(Date dataFimAtividade) {
		this.dataFimAtividade = dataFimAtividade;
	}
	
	

}
