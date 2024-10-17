package ac.modelo;

import java.io.Serializable;
import java.util.Date;


import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class CertificadosSistemaCertificados implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long id;


	private String texto;
	private Integer numeroLivro;
	private Integer numeroFolha;
	private Double cargaHoraria;
	private Double porcentagemFrequencia;
	@Temporal(TemporalType.DATE)
	private Date dataEmissao = new Date();

	private PessoasSistemaCertificados pessoa;
	private Boolean status = true;

	private String textoInicial;

	private String textoFinal;
	private String tipo = "DECLARAÇÃO";
	private Boolean mostrarCpf;
	private String autenticacao;
	private String tituloEvento;
	private Long numeroRegistro;
	private Long loteCertificado;
	private Boolean impresso = false;
	private Boolean atividadesVerso = false;
	private String cadastradoPor;
	private int versao;
	private String informacoesAdicionais;
	private String atividadeComplementar;
	private Double quantidadeHoras;
	
	
	


	public Double getQuantidadeHoras() {
		return quantidadeHoras;
	}

	public void setQuantidadeHoras(Double quantidadeHoras) {
		this.quantidadeHoras = quantidadeHoras;
	}

	public String getAtividadeComplementar() {
		return atividadeComplementar;
	}

	public void setAtividadeComplementar(String atividadeComplementar) {
		this.atividadeComplementar = atividadeComplementar;
	}

	private PessoasSistemaCertificados responsavelCadastro;
	private String papelResponsavelCadastro = "";


	private PessoasSistemaCertificados responsavelAssinatura1;
	private String papelAssinatura1;


	private PessoasSistemaCertificados responsavelAssinatura2;
	private String papelAssinatura2;

	private PessoasSistemaCertificados responsavelAssinatura3;
	private String papelAssinatura3;
	private String codigoCertificadoProvisorio = "";
	
	public CertificadosSistemaCertificados() {
		super();
	}

	public String getCodigoCertificadoProvisorio() {
		return codigoCertificadoProvisorio;
	}

	public void setCodigoCertificadoProvisorio(String codigoCertificadoProvisorio) {
		this.codigoCertificadoProvisorio = codigoCertificadoProvisorio;
	}

	public String getInformacoesAdicionais() {
		return informacoesAdicionais;
	}

	public void setInformacoesAdicionais(String informacoesAdicionais) {
		this.informacoesAdicionais = informacoesAdicionais;
	}



	public String getPapelResponsavelCadastro() {
		if (papelResponsavelCadastro == null) {
			papelResponsavelCadastro = "";
		}
		return papelResponsavelCadastro;
	}

	public void setPapelResponsavelCadastro(String papelResponsavelCadastro) {
		this.papelResponsavelCadastro = papelResponsavelCadastro;
	}

	public int getVersao() {
		return versao;
	}

	public void setVersao(int versao) {
		this.versao = versao;
	}

	public PessoasSistemaCertificados getResponsavelAssinatura1() {
		return responsavelAssinatura1;
	}

	public void setResponsavelAssinatura1(PessoasSistemaCertificados responsavelAssinatura1) {
		this.responsavelAssinatura1 = responsavelAssinatura1;
	}

	public PessoasSistemaCertificados getResponsavelAssinatura2() {
		return responsavelAssinatura2;
	}

	public void setResponsavelAssinatura2(PessoasSistemaCertificados responsavelAssinatura2) {
		this.responsavelAssinatura2 = responsavelAssinatura2;
	}

	public PessoasSistemaCertificados getResponsavelAssinatura3() {
		return responsavelAssinatura3;
	}

	public void setResponsavelAssinatura3(PessoasSistemaCertificados responsavelAssinatura3) {
		this.responsavelAssinatura3 = responsavelAssinatura3;
	}

	public String getPapelAssinatura1() {
		return papelAssinatura1;
	}

	public void setPapelAssinatura1(String papelAssinatura1) {
		this.papelAssinatura1 = papelAssinatura1;
	}

	public String getPapelAssinatura2() {
		return papelAssinatura2;
	}

	public void setPapelAssinatura2(String papelAssinatura2) {
		this.papelAssinatura2 = papelAssinatura2;
	}

	public String getPapelAssinatura3() {
		return papelAssinatura3;
	}

	public void setPapelAssinatura3(String papelAssinatura3) {
		this.papelAssinatura3 = papelAssinatura3;
	}

	public PessoasSistemaCertificados getResponsavelCadastro() {
		return responsavelCadastro;
	}

	public void setResponsavelCadastro(PessoasSistemaCertificados responsavelCadastro) {
		this.responsavelCadastro = responsavelCadastro;
	}

	public String getCadastradoPor() {
		return cadastradoPor;
	}

	public void setCadastradoPor(String cadastradoPor) {
		this.cadastradoPor = cadastradoPor;
	}

	public Boolean getAtividadesVerso() {
		return atividadesVerso;
	}

	public void setAtividadesVerso(Boolean atividadesVerso) {
		this.atividadesVerso = atividadesVerso;
	}

	public Long getLoteCertificado() {
		return loteCertificado;
	}

	public void setLoteCertificado(Long loteCertificado) {
		this.loteCertificado = loteCertificado;
	}

	public Boolean getImpresso() {
		return impresso;
	}

	public void setImpresso(Boolean impresso) {
		this.impresso = impresso;
	}

	public String getTituloEvento() {
		return tituloEvento;
	}

	public void setTituloEvento(String tituloEvento) {
		this.tituloEvento = tituloEvento;
	}

	public Long getNumeroRegistro() {
		return numeroRegistro;
	}

	public void setNumeroRegistro(Long numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}

	public String getAutenticacao() {
		return autenticacao;
	}

	public void setAutenticacao(String autenticacao) {
		this.autenticacao = autenticacao;
	}

	public Boolean getMostrarCpf() {
		return mostrarCpf;
	}

	public void setMostrarCpf(Boolean mostrarCpf) {
		this.mostrarCpf = mostrarCpf;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTextoInicial() {
		if (textoInicial == null) {
			textoInicial = "O Instituto Federal do Paraná certifica que";
		}
		return textoInicial;
	}

	public void setTextoInicial(String textoInicial) {
		this.textoInicial = textoInicial;
	}

	public String getTextoFinal() {
		if (textoFinal == null) {
			textoFinal = "participou da ****, realizada no dia ** de **** de ****, perfazendo um total de ** (***) horas de atividades.";
		}
		return textoFinal;
	}

	public void setTextoFinal(String textoFinal) {
		this.textoFinal = textoFinal;
	}

	public Boolean getStatus() {
		if (status == null) {
			status = true;
		}
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Double getPorcentagemFrequencia() {
		return porcentagemFrequencia;
	}

	public void setPorcentagemFrequencia(Double porcentagemFrequencia) {
		this.porcentagemFrequencia = porcentagemFrequencia;
	}

	public PessoasSistemaCertificados getPessoa() {
		return pessoa;
	}

	public void setPessoa(PessoasSistemaCertificados pessoa) {
		this.pessoa = pessoa;
	}

	public String getTexto() {
		return this.texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNumeroLivro() {
		return numeroLivro;
	}

	public void setNumeroLivro(Integer numeroLivro) {
		this.numeroLivro = numeroLivro;
	}

	public Integer getNumeroFolha() {
		return numeroFolha;
	}

	public void setNumeroFolha(Integer numeroFolha) {
		this.numeroFolha = numeroFolha;
	}

	public Double getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(Double cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((atividadesVerso == null) ? 0 : atividadesVerso.hashCode());
		result = prime * result + ((autenticacao == null) ? 0 : autenticacao.hashCode());
		result = prime * result + ((cadastradoPor == null) ? 0 : cadastradoPor.hashCode());
		result = prime * result + ((cargaHoraria == null) ? 0 : cargaHoraria.hashCode());
		result = prime * result + ((codigoCertificadoProvisorio == null) ? 0 : codigoCertificadoProvisorio.hashCode());
		result = prime * result + ((dataEmissao == null) ? 0 : dataEmissao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((impresso == null) ? 0 : impresso.hashCode());
		result = prime * result + ((informacoesAdicionais == null) ? 0 : informacoesAdicionais.hashCode());
		result = prime * result + ((loteCertificado == null) ? 0 : loteCertificado.hashCode());
		result = prime * result + ((mostrarCpf == null) ? 0 : mostrarCpf.hashCode());
		result = prime * result + ((numeroFolha == null) ? 0 : numeroFolha.hashCode());
		result = prime * result + ((numeroLivro == null) ? 0 : numeroLivro.hashCode());
		result = prime * result + ((numeroRegistro == null) ? 0 : numeroRegistro.hashCode());
		result = prime * result + ((papelAssinatura1 == null) ? 0 : papelAssinatura1.hashCode());
		result = prime * result + ((papelAssinatura2 == null) ? 0 : papelAssinatura2.hashCode());
		result = prime * result + ((papelAssinatura3 == null) ? 0 : papelAssinatura3.hashCode());
		result = prime * result + ((papelResponsavelCadastro == null) ? 0 : papelResponsavelCadastro.hashCode());
		result = prime * result + ((pessoa == null) ? 0 : pessoa.hashCode());
		result = prime * result + ((porcentagemFrequencia == null) ? 0 : porcentagemFrequencia.hashCode());
		result = prime * result + ((responsavelAssinatura1 == null) ? 0 : responsavelAssinatura1.hashCode());
		result = prime * result + ((responsavelAssinatura2 == null) ? 0 : responsavelAssinatura2.hashCode());
		result = prime * result + ((responsavelAssinatura3 == null) ? 0 : responsavelAssinatura3.hashCode());
		result = prime * result + ((responsavelCadastro == null) ? 0 : responsavelCadastro.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((texto == null) ? 0 : texto.hashCode());
		result = prime * result + ((textoFinal == null) ? 0 : textoFinal.hashCode());
		result = prime * result + ((textoInicial == null) ? 0 : textoInicial.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		result = prime * result + ((tituloEvento == null) ? 0 : tituloEvento.hashCode());
		result = prime * result + versao;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CertificadosSistemaCertificados other = (CertificadosSistemaCertificados) obj;
		if (atividadesVerso == null) {
			if (other.atividadesVerso != null)
				return false;
		} else if (!atividadesVerso.equals(other.atividadesVerso))
			return false;
		if (autenticacao == null) {
			if (other.autenticacao != null)
				return false;
		} else if (!autenticacao.equals(other.autenticacao))
			return false;
		if (cadastradoPor == null) {
			if (other.cadastradoPor != null)
				return false;
		} else if (!cadastradoPor.equals(other.cadastradoPor))
			return false;
		if (cargaHoraria == null) {
			if (other.cargaHoraria != null)
				return false;
		} else if (!cargaHoraria.equals(other.cargaHoraria))
			return false;
		if (codigoCertificadoProvisorio == null) {
			if (other.codigoCertificadoProvisorio != null)
				return false;
		} else if (!codigoCertificadoProvisorio.equals(other.codigoCertificadoProvisorio))
			return false;
		if (dataEmissao == null) {
			if (other.dataEmissao != null)
				return false;
		} else if (!dataEmissao.equals(other.dataEmissao))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (impresso == null) {
			if (other.impresso != null)
				return false;
		} else if (!impresso.equals(other.impresso))
			return false;
		if (informacoesAdicionais == null) {
			if (other.informacoesAdicionais != null)
				return false;
		} else if (!informacoesAdicionais.equals(other.informacoesAdicionais))
			return false;
		if (loteCertificado == null) {
			if (other.loteCertificado != null)
				return false;
		} else if (!loteCertificado.equals(other.loteCertificado))
			return false;
		if (mostrarCpf == null) {
			if (other.mostrarCpf != null)
				return false;
		} else if (!mostrarCpf.equals(other.mostrarCpf))
			return false;
		if (numeroFolha == null) {
			if (other.numeroFolha != null)
				return false;
		} else if (!numeroFolha.equals(other.numeroFolha))
			return false;
		if (numeroLivro == null) {
			if (other.numeroLivro != null)
				return false;
		} else if (!numeroLivro.equals(other.numeroLivro))
			return false;
		if (numeroRegistro == null) {
			if (other.numeroRegistro != null)
				return false;
		} else if (!numeroRegistro.equals(other.numeroRegistro))
			return false;
		if (papelAssinatura1 == null) {
			if (other.papelAssinatura1 != null)
				return false;
		} else if (!papelAssinatura1.equals(other.papelAssinatura1))
			return false;
		if (papelAssinatura2 == null) {
			if (other.papelAssinatura2 != null)
				return false;
		} else if (!papelAssinatura2.equals(other.papelAssinatura2))
			return false;
		if (papelAssinatura3 == null) {
			if (other.papelAssinatura3 != null)
				return false;
		} else if (!papelAssinatura3.equals(other.papelAssinatura3))
			return false;
		if (papelResponsavelCadastro == null) {
			if (other.papelResponsavelCadastro != null)
				return false;
		} else if (!papelResponsavelCadastro.equals(other.papelResponsavelCadastro))
			return false;
		if (pessoa == null) {
			if (other.pessoa != null)
				return false;
		} else if (!pessoa.equals(other.pessoa))
			return false;
		if (porcentagemFrequencia == null) {
			if (other.porcentagemFrequencia != null)
				return false;
		} else if (!porcentagemFrequencia.equals(other.porcentagemFrequencia))
			return false;
		if (responsavelAssinatura1 == null) {
			if (other.responsavelAssinatura1 != null)
				return false;
		} else if (!responsavelAssinatura1.equals(other.responsavelAssinatura1))
			return false;
		if (responsavelAssinatura2 == null) {
			if (other.responsavelAssinatura2 != null)
				return false;
		} else if (!responsavelAssinatura2.equals(other.responsavelAssinatura2))
			return false;
		if (responsavelAssinatura3 == null) {
			if (other.responsavelAssinatura3 != null)
				return false;
		} else if (!responsavelAssinatura3.equals(other.responsavelAssinatura3))
			return false;
		if (responsavelCadastro == null) {
			if (other.responsavelCadastro != null)
				return false;
		} else if (!responsavelCadastro.equals(other.responsavelCadastro))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (texto == null) {
			if (other.texto != null)
				return false;
		} else if (!texto.equals(other.texto))
			return false;
		if (textoFinal == null) {
			if (other.textoFinal != null)
				return false;
		} else if (!textoFinal.equals(other.textoFinal))
			return false;
		if (textoInicial == null) {
			if (other.textoInicial != null)
				return false;
		} else if (!textoInicial.equals(other.textoInicial))
			return false;
		if (tipo == null) {
			if (other.tipo != null)
				return false;
		} else if (!tipo.equals(other.tipo))
			return false;
		if (tituloEvento == null) {
			if (other.tituloEvento != null)
				return false;
		} else if (!tituloEvento.equals(other.tituloEvento))
			return false;
		if (versao != other.versao)
			return false;
		return true;
	}

	

}
