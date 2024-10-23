package protocolo.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ac.modelo.Pessoa;
import base.modelo.Servidor;

@Entity
@Table(name = "tab_protocolo")
@Inheritance(strategy = InheritanceType.JOINED)
public class Protocolo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	 
	@Temporal(TemporalType.DATE)
	private Date data;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable=false)
	private Requerimento requerimento;

	@Column(nullable=false)
    private String descricaoRequerente;
 
	@Temporal(TemporalType.DATE) 
    private Date dataResultado;

	@Column(length=20, nullable=false)
    private boolean status;

    private String observacoesAnexo;

	@Column(length=30, nullable=false)
    private String situacao;

    private String parecerFinal;
    
    private String dataFinalizacao;

	 
	private String origem;
	
	@ManyToOne(fetch = FetchType.EAGER) 
	private Pessoa pessoa;
	
	@ManyToOne(fetch = FetchType.EAGER) 
	private Pessoa responsavelFinaliza;
	
	
	@ManyToOne(fetch = FetchType.EAGER) 
	private Servidor servidorAtendente;
	
	
	@Column(length=150)
	private String nome; 
	 
	@Column(length=25)
	private String rg;
	
	@Column(length=25)
	private String cpf;
	
	@Column(length=100)
	private String email;
	
	@Column(length=25)
	private String telefone;
	
	@Column(length=25) 
	private String telefone2;
	
	
	public Servidor getServidorAtendente() {
		return servidorAtendente;
	}

	public void setServidorAtendente(Servidor servidorAtendente) {
		this.servidorAtendente = servidorAtendente;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public Pessoa getResponsavelFinaliza() {
		return responsavelFinaliza;
	}

	public void setResponsavelFinaliza(Pessoa responsavelFinaliza) {
		this.responsavelFinaliza = responsavelFinaliza;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getDescricaoRequerente() {
		return descricaoRequerente;
	}

	public void setDescricaoRequerente(String descricaoRequerente) {
		this.descricaoRequerente = descricaoRequerente;
	}

	public Date getDataResultado() {
		return dataResultado;
	}

	public void setDataResultado(Date dataResultado) {
		this.dataResultado = dataResultado;
	}



	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getObservacoesAnexo() {
		return observacoesAnexo;
	}

	public void setObservacoesAnexo(String observacoesAnexo) {
		this.observacoesAnexo = observacoesAnexo;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getParecerFinal() {
		return parecerFinal;
	}

	public void setParecerFinal(String parecerFinal) {
		this.parecerFinal = parecerFinal;
	}

	public String getDataFinalizacao() {
		return dataFinalizacao;
	}

	public void setDataFinalizacao(String dataFinalizacao) {
		this.dataFinalizacao = dataFinalizacao;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

 

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getTelefone2() {
		return telefone2;
	}

	public void setTelefone2(String telefone2) {
		this.telefone2 = telefone2;
	}


}
