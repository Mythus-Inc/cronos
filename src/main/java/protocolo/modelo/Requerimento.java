package protocolo.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tab_requerimento")
public class Requerimento {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable=false, length=50)
	private String nome;
		
	private String documetosNecessarios;
	
	@Column(nullable=false)
	private int prazoTotalDias;
	
	private int prazoEncaminhamentoInicial;
	
	private int prazoParecerDias;
	
	private String formularioEspecifico;
	
	private String procedimentos;
	
	private String tramitesNecessarios;
	
	private boolean status;
	
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	private boolean cienciaEmailProfessores;
	
	private boolean cienciaEmailSecretaria;
	
	private boolean cienciaEmailCoordenador;
	
	private boolean cienciaEmailDirecaoEnsino;
	
	private boolean cienciaEmailcoordenacaoPedagogica;
	
	private boolean cienciaEmailRequerente;
	
	@Column(length=50)
	private String cienciaOutroEmail;
	
	private boolean autenticacaoNecessariaDocumentos;
	
	private boolean notificarSecretariaParecerFinal;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Setor setorDestino;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Setor setorEncerramento;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	

	public String getProcedimentos() {
		return procedimentos;
	}

	public void setProcedimentos(String procedimentos) {
		this.procedimentos = procedimentos;
	}

	public String getDocumetosNecessarios() {
		return documetosNecessarios;
	}

	public void setDocumetosNecessarios(String documetosNecessarios) {
		this.documetosNecessarios = documetosNecessarios;
	}

	public int getPrazoTotalDias() {
		return prazoTotalDias;
	}

	public void setPrazoTotalDias(int prazoTotalDias) {
		this.prazoTotalDias = prazoTotalDias;
	}

	public int getPrazoEncaminhamentoInicial() {
		return prazoEncaminhamentoInicial;
	}

	public void setPrazoEncaminhamentoInicial(int prazoEncaminhamentoInicial) {
		this.prazoEncaminhamentoInicial = prazoEncaminhamentoInicial;
	}

	public int getPrazoParecerDias() {
		return prazoParecerDias;
	}

	public void setPrazoParecerDias(int prazoParecerDias) {
		this.prazoParecerDias = prazoParecerDias;
	}

	public String getFormularioEspecifico() {
		return formularioEspecifico;
	}

	public void setFormularioEspecifico(String formularioEspecifico) {
		this.formularioEspecifico = formularioEspecifico;
	}

	public boolean isCienciaEmailProfessores() {
		return cienciaEmailProfessores;
	}

	public void setCienciaEmailProfessores(boolean cienciaEmailProfessores) {
		this.cienciaEmailProfessores = cienciaEmailProfessores;
	}

	public boolean isCienciaEmailSecretaria() {
		return cienciaEmailSecretaria;
	}

	public void setCienciaEmailSecretaria(boolean cienciaEmailSecretaria) {
		this.cienciaEmailSecretaria = cienciaEmailSecretaria;
	}

	public boolean isCienciaEmailCoordenador() {
		return cienciaEmailCoordenador;
	}

	public void setCienciaEmailCoordenador(boolean cienciaEmailCoordenador) {
		this.cienciaEmailCoordenador = cienciaEmailCoordenador;
	}

	public boolean isCienciaEmailDirecaoEnsino() {
		return cienciaEmailDirecaoEnsino;
	}

	public void setCienciaEmailDirecaoEnsino(boolean cienciaEmailDirecaoEnsino) {
		this.cienciaEmailDirecaoEnsino = cienciaEmailDirecaoEnsino;
	}

	public boolean isCienciaEmailcoordenacaoPedagogica() {
		return cienciaEmailcoordenacaoPedagogica;
	}

	public void setCienciaEmailcoordenacaoPedagogica(boolean cienciaEmailcoordenacaoPedagogica) {
		this.cienciaEmailcoordenacaoPedagogica = cienciaEmailcoordenacaoPedagogica;
	}

	public boolean isCienciaEmailRequerente() {
		return cienciaEmailRequerente;
	}

	public void setCienciaEmailRequerente(boolean cienciaEmailRequerente) {
		this.cienciaEmailRequerente = cienciaEmailRequerente;
	}

	public String getCienciaOutroEmail() {
		return cienciaOutroEmail;
	}

	public void setCienciaOutroEmail(String cienciaOutroEmail) {
		this.cienciaOutroEmail = cienciaOutroEmail;
	}

	public boolean isAutenticacaoNecessariaDocumentos() {
		return autenticacaoNecessariaDocumentos;
	}

	public void setAutenticacaoNecessariaDocumentos(boolean autenticacaoNecessariaDocumentos) {
		this.autenticacaoNecessariaDocumentos = autenticacaoNecessariaDocumentos;
	}

	public boolean isNotificarSecretariaParecerFinal() {
		return notificarSecretariaParecerFinal;
	}

	public void setNotificarSecretariaParecerFinal(boolean notificarSecretariaParecerFinal) {
		this.notificarSecretariaParecerFinal = notificarSecretariaParecerFinal;
	}

	public Setor getSetorDestino() {
		return setorDestino;
	}

	public void setSetorDestino(Setor setorDestino) {
		this.setorDestino = setorDestino;
	}

	public Setor getSetorEncerramento() {
		return setorEncerramento;
	}

	public void setSetorEncerramento(Setor setorEncerramento) {
		this.setorEncerramento = setorEncerramento;
	}

	public String getTramitesNecessarios() {
		return tramitesNecessarios;
	}

	public void setTramitesNecessarios(String tramitesNecessarios) {
		this.tramitesNecessarios = tramitesNecessarios;
	}
	
	
	
}
