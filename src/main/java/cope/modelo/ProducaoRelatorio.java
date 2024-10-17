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

@Entity
@Table(name = "tab_producaoRelatorio")
public class ProducaoRelatorio implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_producaoRelatorio")
	private Long id; 
	private String descricao;  
	private boolean status;
	
	@ManyToOne
	private TiposProducao tiposProducao;
	
	@ManyToOne
	private ArquivoProjeto arquivoProjeto;
	
	public Long getId() {
		return id;
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
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public TiposProducao getTiposProducao() {
		return tiposProducao;
	}
	public void setTiposProducao(TiposProducao tiposProducao) {
		this.tiposProducao = tiposProducao;
	}
	public ArquivoProjeto getArquivoProjeto() {
		return arquivoProjeto;
	}
	public void setArquivoProjeto(ArquivoProjeto arquivoProjeto) {
		this.arquivoProjeto = arquivoProjeto;
	}
	
	
 
	
	
	
 
}
