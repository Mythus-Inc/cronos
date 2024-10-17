package inventario.modelo;

import java.io.Serializable;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import base.modelo.Servidor;

@Entity
@Table(name = "tab_localinventario")
public class LocalInventario implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(nullable=false)
	private boolean status;
	
	private String situacao;
	
	@ManyToOne
	private Localidades local;
	
	@ManyToOne
	private Servidor servidorConferencia;
	
	@ManyToOne
	private Inventario Inventario;
	
	 
	public Inventario getInventario() {
		return Inventario;
	}

	public void setInventario(Inventario inventario) {
		Inventario = inventario;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

 

	public Localidades getLocal() {
		return local;
	}

	public void setLocal(Localidades local) {
		this.local = local;
	}

	public Servidor getServidorConferencia() {
		return servidorConferencia;
	}

	public void setServidorConferencia(Servidor servidorConferencia) {
		this.servidorConferencia = servidorConferencia;
	}
	
	

}
