package inventario.modelo;

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
import cope.modelo.Avaliacao;

@Entity
@Table(name = "tab_historicolocais") 
public class HistoricoLocais implements Serializable {

	 
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	  
	private String descricao; 
	

	@Column(nullable=false)
    private boolean status;
	
	@Temporal(TemporalType.DATE)
	private Date dataHistoricos;
	
	@ManyToOne
	private Localidades local;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}
	
	

	public Localidades getLocal() {
		return local;
	}

	public void setLocal(Localidades local) {
		this.local = local;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Date getDataHistoricos() {
		return dataHistoricos;
	}

	public void setDataHistoricos(Date dataHistoricos) {
		this.dataHistoricos = dataHistoricos;
	}
	

 


}
