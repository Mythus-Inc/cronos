package ac.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import base.modelo.Aluno;

/**
 * Entity implementation class for Entity: Certificado
 *
 */
@Entity
@Table(name = "tab_certificado")
public class Certificado implements Serializable {

	public Certificado() {
		super();
	}

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_certificado")
	private Long id;
	
	private String atividadeSugerida;

	//MUDAR NO BANCO O TIPO DA DESCRIÇÃO PARA LONGTEXT
	@NotNull(message = "O campo descrição não pode ser nulo")
	@Column(nullable = false, length=1000)
	private String descricao;

	private String codigoAutenticacaoSistemaCertificados;

	private String url;

	private String codigoAutenticacao;

	@NotNull(message = "O campo data de início não pode ser nulo")
	@Column(name = "data_inicio", nullable = false)
	private Date dataInicio;

	@NotNull(message = "O campo data finalizado não pode ser nulo")
	@Column(name = "data_finalizado", nullable = false)
	private Date dataFinalizado;

	@NotNull(message = "O campo instituição não pode ser nulo")
	@Column(nullable = false)
	private String instituicao;

	@Column(name = "quantidade_maxima_hora")
	private Double quantidadeMaximaHora;

	@Column(nullable = false)
	private Integer situacao;
	//0: cadastrado
	//1: autenticado
	//2: não autenticado
	//3: validado
	//4: invalidado

	@Column(name = "hora_computada")
	private Double horaComputada;

	private String justificativa;

	@Column(name = "justificativa_professor")
	private String justificativaProfessor;

	@Column(name = "caminho_certificado")
	private String caminhoCertificado;

	@Column(name = "data_cadastro", nullable = false)
	private Date dataCadastro;

	@Column(nullable = false)
	private Boolean status;

	@JoinColumn(name = "id_alunoTurma", nullable = false)
	@ManyToOne
	private AlunoTurma alunoTurma;

	@JoinColumn(name = "id_atividade_turma", nullable = false)
	@ManyToOne
	private AtividadeTurma atividadeTurma;

	@JoinColumn(name = "id_pessoa", nullable = false)
	@ManyToOne
	private Aluno aluno;

	@Column(name = "autenticado_secretaria")
	private String autenticadoSecretaria;

	@Column(name = "validado_professor")
	private String validadoProfessor;

	@Column(name = "motivo_alteracao")
	private String motivoAlteracao;
	
	private String nomeArquivoAnexado="";

	private Boolean atualizado;

	private boolean backup;

	private String certificadoInterno;
	private Double quantidadeHorasImportacao=0.;
	

	// Adicionado para permitir que a exibi��o do grafico se torne mais rapida
	@Column(name = "id_grupo_turma")
	private Long idGrupoTurma;

	public String getCodigoAutenticacaoSistemaCertificados() {
		if (codigoAutenticacaoSistemaCertificados == null) {
			codigoAutenticacaoSistemaCertificados = "";
		}
		return codigoAutenticacaoSistemaCertificados;
	}
	
	

	public Double getQuantidadeHorasImportacao() {
		if(quantidadeHorasImportacao==null){
			quantidadeHorasImportacao=0.;
		}
		return quantidadeHorasImportacao;
	}
	
	



	public String getNomeArquivoAnexado() {
		if(nomeArquivoAnexado==null) {
			nomeArquivoAnexado="";
		}
		return nomeArquivoAnexado;
	}



	public void setNomeArquivoAnexado(String nomeArquivoAnexado) {
		this.nomeArquivoAnexado = nomeArquivoAnexado;
	}



	public void setQuantidadeHorasImportacao(Double quantidadeHorasImportacao) {
		this.quantidadeHorasImportacao = quantidadeHorasImportacao;
	}



	public void setCodigoAutenticacaoSistemaCertificados(String codigoAutenticacaoSistemaCertificados) {
		this.codigoAutenticacaoSistemaCertificados = codigoAutenticacaoSistemaCertificados;
	}
	

	public String getAtividadeSugerida() {
		if(atividadeSugerida==null) {
			atividadeSugerida="";
		}
		return atividadeSugerida;
	}

	public void setAtividadeSugerida(String atividadeSugerida) {
		this.atividadeSugerida = atividadeSugerida;
	}

	public Long getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getCertificadoInterno() {
		return certificadoInterno;
	}

	public void setCertificadoInterno(String certificadoInterno) {
		this.certificadoInterno = certificadoInterno;
	}

	public boolean isBackup() {
		return backup;
	}

	public void setBackup(boolean backup) {
		this.backup = backup;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFinalizado() {
		return dataFinalizado;
	}

	public void setDataFinalizado(Date dataFinalizado) {
		this.dataFinalizado = dataFinalizado;
	}

	public String getInstituicao() {
		return instituicao;
	}

	public void setInstituicao(String instituicao) {
		this.instituicao = instituicao;
	}

	public Double getQuantidadeMaximaHora() {
		return quantidadeMaximaHora;
	}

	public void setQuantidadeMaximaHora(Double quantidadeMaximaHora) {
		this.quantidadeMaximaHora = quantidadeMaximaHora;
	}

	public Integer getSituacao() {
		if(situacao==null){
			situacao = 99;
		}
		return situacao;
	}

	public void setSituacao(Integer situacao) {
		this.situacao = situacao;
	}

	public Double getHoraComputada() {
		return horaComputada;
	}

	public void setHoraComputada(Double horaComputada) {
		this.horaComputada = horaComputada;
	}

	public String getJustificativa() {
		if(justificativa==null) {
			justificativa="";
		}
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public String getCaminhoCertificado() {
		if(caminhoCertificado==null) {
			caminhoCertificado="";
		}
		return caminhoCertificado;
	}

	public void setCaminhoCertificado(String caminhoCertificado) {
		this.caminhoCertificado = caminhoCertificado;
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

	public AlunoTurma getAlunoTurma() {
		return alunoTurma;
	}

	public void setAlunoTurma(AlunoTurma alunoTurma) {
		this.alunoTurma = alunoTurma;
	}

	public AtividadeTurma getAtividadeTurma() {
		return atividadeTurma;
	}

	public void setAtividadeTurma(AtividadeTurma atividadeTurma) {
		this.atividadeTurma = atividadeTurma;
	}

	public String getAutenticadoSecretaria() {
		return autenticadoSecretaria;
	}

	public void setAutenticadoSecretaria(String autenticadoSecretaria) {
		this.autenticadoSecretaria = autenticadoSecretaria;
	}

	public String getValidadoProfessor() {
		return validadoProfessor;
	}

	public void setValidadoProfessor(String validadoProfessor) {
		this.validadoProfessor = validadoProfessor;
	}

	public Long getIdGrupoTurma() {
		return idGrupoTurma;
	}

	public void setIdGrupoTurma(Long idGrupoTurma) {
		this.idGrupoTurma = idGrupoTurma;
	}

	public Boolean getAtualizado() {
		return atualizado;
	}

	public void setAtualizado(Boolean atualizado) {
		this.atualizado = atualizado;
	}

	public String getJustificativaProfessor() {
		if(justificativaProfessor==null) {
			justificativaProfessor="";
		}
		return justificativaProfessor;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public void setJustificativaProfessor(String justificativaProfessor) {
		this.justificativaProfessor = justificativaProfessor;
	}

	public String getMotivoAlteracao() {
		return motivoAlteracao;
	}

	public void setMotivoAlteracao(String motivoAlteracao) {
		this.motivoAlteracao = motivoAlteracao;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCodigoAutenticacao() {
		return codigoAutenticacao;
	}

	public void setCodigoAutenticacao(String codigoAutenticacao) {
		this.codigoAutenticacao = codigoAutenticacao;
	}



	@Override
	public String toString() {
		return "Certificado [id=" + id + ", atividadeSugerida=" + atividadeSugerida + ", descricao=" + descricao
				+ ", codigoAutenticacaoSistemaCertificados=" + codigoAutenticacaoSistemaCertificados + ", url=" + url
				+ ", codigoAutenticacao=" + codigoAutenticacao + ", dataInicio=" + dataInicio + ", dataFinalizado="
				+ dataFinalizado + ", instituicao=" + instituicao + ", quantidadeMaximaHora=" + quantidadeMaximaHora
				+ ", situacao=" + situacao + ", horaComputada=" + horaComputada + ", justificativa=" + justificativa
				+ ", justificativaProfessor=" + justificativaProfessor + ", caminhoCertificado=" + caminhoCertificado
				+ ", dataCadastro=" + dataCadastro + ", status=" + status + ", alunoTurma=" + alunoTurma
				+ ", atividadeTurma=" + atividadeTurma + ", aluno=" + aluno + ", autenticadoSecretaria="
				+ autenticadoSecretaria + ", validadoProfessor=" + validadoProfessor + ", motivoAlteracao="
				+ motivoAlteracao + ", nomeArquivoAnexado=" + nomeArquivoAnexado + ", atualizado=" + atualizado
				+ ", backup=" + backup + ", certificadoInterno=" + certificadoInterno + ", quantidadeHorasImportacao="
				+ quantidadeHorasImportacao + ", idGrupoTurma=" + idGrupoTurma + "]";
	}
	
	

}
