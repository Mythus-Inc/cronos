package protocolo.modelo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ac.modelo.Pessoa;

@Entity
@Table(name = "tad_encaminhamento")
public class Encaminhamento {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id; 

	@Temporal(TemporalType.DATE)
	private Date dataEncaminhamento; 
	
	@Temporal(TemporalType.DATE)
	private Date dataParecer;
	
	private String descricao;
	
	private String descricaoRemetente;
	 
	
	@Column(nullable=false)
	private int prazoParecerDias;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable=false)
	private Protocolo protocolo;
	
	@ManyToOne(fetch = FetchType.EAGER) 
	private Pessoa pessoa ;
	
	@ManyToOne(fetch = FetchType.EAGER) 
	private Pessoa pessoaRemetente ;
	
	
	public String getDescricaoRemetente() {
		return descricaoRemetente;
	}

	public void setDescricaoRemetente(String descricaoRemetente) {
		this.descricaoRemetente = descricaoRemetente;
	}

	public Pessoa getPessoaRemetente() {
		return pessoaRemetente;
	}

	public void setPessoaRemetente(Pessoa pessoaRemetente) {
		this.pessoaRemetente = pessoaRemetente;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataEncaminhamento() {
		return dataEncaminhamento;
	}

	public void setDataEncaminhamento(Date dataEncaminhamento) {
		this.dataEncaminhamento = dataEncaminhamento;
	}

	public Date getDataParecer() {
		return dataParecer;
	}

	public void setDataParecer(Date dataParecer) {
		this.dataParecer = dataParecer;
	}

 

	public int getPrazoParecerDias() {
		return prazoParecerDias;
	}

	public void setPrazoParecerDias(int prazoParecerDias) {
		this.prazoParecerDias = prazoParecerDias;
	}

	public Protocolo getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(Protocolo protocolo) {
		this.protocolo = protocolo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
 
	
	
}
