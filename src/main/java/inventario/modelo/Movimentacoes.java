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

@Entity
@Table(name = "tab_movimentacoes") 
public class Movimentacoes implements Serializable {

	 
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
    private String descricao;
    
    private boolean status;
    
    @ManyToOne
    private Servidor servidor;
    
    @ManyToOne
    private Localidades localOrigem;
    
 
    @ManyToOne
    private Localidades localDestino;
    
   

	public Localidades getLocalOrigem() {
		return localOrigem;
	}

	public void setLocalOrigem(Localidades localOrigem) {
		this.localOrigem = localOrigem;
	}
 
 
	public Localidades getLocalDestino() {
		return localDestino;
	}

	public void setLocalDestino(Localidades localDestino) {
		this.localDestino = localDestino;
	}

	public Long getId() {
		return id;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Servidor getServidor() {
		return servidor;
	}

	public void setServidor(Servidor servidor) {
		this.servidor = servidor;
	}
 
 

}
