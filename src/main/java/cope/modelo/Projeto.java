package cope.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cope.modelo.enums.StatusSubmissao;

@Entity
@Table(name = "tab_projeto")
public class Projeto implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_projeto")
	private Long id;
	private String nome;
	@Column(name = "tipoprojeto")
	private String tipoProjeto;
	@Column(name = "numeroprocesso")
	private String numeroProcesso;
	@Column(name = "isprograma")
	private String isPrograma;
	@Column(name = "isfluxcocontinuo")
	private boolean isFluxoContinuo;
	@Column(name = "datainicio")
	private Date dataInicio;
	@Column(name = "datatermino")
	private Date dataTermino;
	@Enumerated(EnumType.STRING)
	@Column(name = "situacao")
	private StatusSubmissao situacao;
	private Date proximoRelatorio;
	@Enumerated(EnumType.STRING)
	@Column(name = "situacaoProjeto")
	private StatusSubmissao situacaoProjeto;
	
	@ManyToOne
	private Projeto programa;
	private boolean bloqueiaAlterar;
	
	@ManyToOne
	private Programas programaCiencia;
	
	
	
	public StatusSubmissao getSituacaoProjeto() {
		return situacaoProjeto;
	}

	public void setSituacaoProjeto(StatusSubmissao situacaoProjeto) {
		this.situacaoProjeto = situacaoProjeto;
	}

	private boolean ciencia;
	
	private boolean projetoAntigo;
	
	private Date dataAprovacao;
	

	public boolean isBloqueiaAlterar() {
		return bloqueiaAlterar;
	}

	public void setBloqueiaAlterar(boolean bloqueiaAlterar) {
		this.bloqueiaAlterar = bloqueiaAlterar;
	}

	public Programas getProgramaCiencia() {
		return programaCiencia;
	}

	public void setProgramaCiencia(Programas programaCiencia) {
		this.programaCiencia = programaCiencia;
	}

	public boolean isCiencia() {
		return ciencia;
	}

	public void setCiencia(boolean ciencia) {
		this.ciencia = ciencia;
	}

	public boolean isProjetoAntigo() {
		return projetoAntigo;
	}

	public void setProjetoAntigo(boolean projetoAntigo) {
		this.projetoAntigo = projetoAntigo;
	}

	public Date getDataAprovacao() {
		return dataAprovacao;
	}

	public void setDataAprovacao(Date dataAprovacao) {
		this.dataAprovacao = dataAprovacao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipoProjeto() {
		return tipoProjeto;
	}

	public void setTipoProjeto(String tipoProjeto) {
		this.tipoProjeto = tipoProjeto;
	}

  

	public String getIsPrograma() {
		return isPrograma;
	}

	public void setIsPrograma(String isPrograma) {
		this.isPrograma = isPrograma;
	}

	public String getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public boolean isFluxoContinuo() {
		return isFluxoContinuo;
	}

	public void setFluxoContinuo(boolean isFluxoContinuo) {
		this.isFluxoContinuo = isFluxoContinuo;
	}

	public StatusSubmissao getSituacao() {
		return situacao;
	}

	public void setSituacao(StatusSubmissao situacao) {
		this.situacao = situacao;
	}

	public Projeto getPrograma() {
		return programa;
	}

	public void setPrograma(Projeto programa) {
		this.programa = programa;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataTermino() {
		return dataTermino;
	}

	public void setDataTermino(Date dataTermino) {
		this.dataTermino = dataTermino;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getProximoRelatorio() {
		return proximoRelatorio;
	}

	public void setProximoRelatorio(Date proximoRelatorio) {
		this.proximoRelatorio = proximoRelatorio;
	}

}
