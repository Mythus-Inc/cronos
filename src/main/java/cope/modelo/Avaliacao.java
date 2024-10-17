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

import base.modelo.Servidor;
import cope.controle.ProjetoSecretariaMB; 

@Entity
@Table(name = "tab_avaliacao")
public class Avaliacao implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_parecer")
	private Long id;
	@Column(name = "dataprazoavaliacao")
	private Date dataPrazoAvaliacao;
	@Column(name = "dataprazoreadequar")
	private Date dataPrazoReadequar;
	@Column(name = "autenticacaopresidente")
	private boolean autenticacaoPresidente;
	@ManyToOne
	private Servidor presidente;
	@ManyToOne
	private Servidor avaliador1;
	@ManyToOne
	private Servidor avaliador2;

	private String parecer1;

	private String parecer2;
	private String observacoes1;
	private String observacoes2;
	@Column(name = "dataparecer1")
	private Date dataParecer1;
	@Column(name = "dataparecer2")
	private Date dataParecer2;

	
	
	
	
	public String getParecer1() {
		return parecer1;
	}

	public void setParecer1(String parecer1) {
		this.parecer1 = parecer1;
	}

	public String getParecer2() {
		return parecer2;
	}

	public void setParecer2(String parecer2) {
		this.parecer2 = parecer2;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataPrazoAvaliacao() {
		return dataPrazoAvaliacao;
	}

	public void setDataPrazoAvaliacao(Date dataPrazoAvaliacao) {
		this.dataPrazoAvaliacao = dataPrazoAvaliacao;
	}

	public Date getDataPrazoReadequar() {
		return dataPrazoReadequar;
	}

	public void setDataPrazoReadequar(Date dataPrazoReadequar) {
		this.dataPrazoReadequar = dataPrazoReadequar;
	}

	public boolean isAutenticacaoPresidente() {
		return autenticacaoPresidente;
	}

	public void setAutenticacaoPresidente(boolean autenticacaoPresidente) {
		this.autenticacaoPresidente = autenticacaoPresidente;
	}

	public Servidor getPresidente() {
		return presidente;
	}

	public void setPresidente(Servidor presidente) {
		this.presidente = presidente;
	}

	public Servidor getAvaliador1() {
		return avaliador1;
	}

	public void setAvaliador1(Servidor avaliador1) {
		this.avaliador1 = avaliador1;
	}

	public Servidor getAvaliador2() {
		return avaliador2;
	}

	public void setAvaliador2(Servidor avaliador2) {
		this.avaliador2 = avaliador2;
	}

	public String getObservacoes1() {
		return observacoes1;
	}

	public void setObservacoes1(String observacoes1) {
		this.observacoes1 = observacoes1;
	}

	public String getObservacoes2() {
		return observacoes2;
	}

	public void setObservacoes2(String observacoes2) {
		this.observacoes2 = observacoes2;
	}

	public Date getDataParecer1() {
		return dataParecer1;
	}

	public void setDataParecer1(Date dataParecer1) {
		this.dataParecer1 = dataParecer1;
	}

	public Date getDataParecer2() {
		return dataParecer2;
	}

	public void setDataParecer2(Date dataParecer2) {
		this.dataParecer2 = dataParecer2;
	}

}
