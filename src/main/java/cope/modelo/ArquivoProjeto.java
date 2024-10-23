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
import javax.persistence.Transient;

import cope.modelo.enums.StatusSubmissao;
import cope.modelo.enums.SubTipoArquivoProjeto;
import cope.modelo.enums.TipoArquivoProjeto;

@Entity
@Table(name = "tab_arquivoprojeto")
public class ArquivoProjeto implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_arquivoprojeto")
	private Long id;
	@Column(name = "localarquivo")
	private String localArquivo;
	private String descricao;
	private String observacoes;
	@Column(name = "dataenvio")
	private Date dataEnvio;
	private boolean controleRemover;
	private Integer versao;
	@Enumerated(EnumType.STRING)
	private TipoArquivoProjeto tipo;
	@Enumerated(EnumType.STRING)
	private SubTipoArquivoProjeto subTipo; // necessario apenas ce for um RELATORIO_PROJETO
	@Enumerated(EnumType.STRING)
	private StatusSubmissao situacao;
	@ManyToOne	
	private Projeto projeto;
	@ManyToOne
	private Avaliacao avaliacao;
	@Transient
	private String nomeArquivoTmp;
	
	private boolean relatorioFinal;
	
 
	
	private boolean status;
	

	public ArquivoProjeto(Projeto projeto, String localArquivo, Date dataEnvio, Integer versao, String nomeArquivoTmp, TipoArquivoProjeto tipo, StatusSubmissao situacao) {
		super();
		this.projeto = projeto;
		this.localArquivo = localArquivo;
		this.dataEnvio = dataEnvio;
		this.versao = versao;
		this.nomeArquivoTmp = nomeArquivoTmp;
		this.tipo = tipo;
		this.situacao = situacao;
	}

	public ArquivoProjeto() {
		super();
	}
	
	
	public boolean isStatus() {
		return status;
	}

 
	public boolean isRelatorioFinal() {
		return relatorioFinal;
	}

	public void setRelatorioFinal(boolean relatorioFinal) {
		this.relatorioFinal = relatorioFinal;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean isControleRemover() {
		return controleRemover;
	}

	public void setControleRemover(boolean controleRemover) {
		this.controleRemover = controleRemover;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	public Avaliacao getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(Avaliacao avaliacao) {
		this.avaliacao = avaliacao;
	}

	public String getLocalArquivo() {
		return localArquivo;
	}

	public void setLocalArquivo(String localArquivo) {
		this.localArquivo = localArquivo;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public Date getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(Date dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public TipoArquivoProjeto getTipo() {
		return tipo;
	}

	public void setTipo(TipoArquivoProjeto tipo) {
		this.tipo = tipo;
	}

	public SubTipoArquivoProjeto getSubTipo() {
		return subTipo;
	}

	public void setSubTipo(SubTipoArquivoProjeto subTipo) {
		this.subTipo = subTipo;
	}

	public String getNomeArquivoTmp() {
		return nomeArquivoTmp;
	}

	public void setNomeArquivoTmp(String nomeArquivoTmp) {
		this.nomeArquivoTmp = nomeArquivoTmp;
	}

	public StatusSubmissao getSituacao() {
		return situacao;
	}

	public void setSituacao(StatusSubmissao situacao) {
		this.situacao = situacao;
	}

}
