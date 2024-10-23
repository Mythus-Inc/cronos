package inventario.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "tab_tombamento") 
public class Tombamento implements Serializable {

	 
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private EstadoConservacao estado;
	
	@Column(nullable=false)
    private boolean status;
	
	@ManyToOne
	private Localidades local;

	@ManyToOne
	private Equipamento equipamento;
	
	private String codigo;
	 
    private String descricao;
 

    private String cor;

    private String material;


    private String especificacao;

    private String dimensoes;

    private String marca;

    private String numeroSerie;
	
	
    
	public String getDescricao() {
		return descricao;
	}



	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}



	public String getCor() {
		return cor;
	}



	public void setCor(String cor) {
		this.cor = cor;
	}



	public String getMaterial() {
		return material;
	}



	public void setMaterial(String material) {
		this.material = material;
	}



	public String getEspecificacao() {
		return especificacao;
	}



	public void setEspecificacao(String especificacao) {
		this.especificacao = especificacao;
	}



	public String getDimensoes() {
		return dimensoes;
	}



	public void setDimensoes(String dimensoes) {
		this.dimensoes = dimensoes;
	}



	public String getMarca() {
		return marca;
	}



	public void setMarca(String marca) {
		this.marca = marca;
	}



	public String getNumeroSerie() {
		return numeroSerie;
	}



	public void setNumeroSerie(String numeroSerie) {
		this.numeroSerie = numeroSerie;
	}



	public Long getId() {
		return id;
	}

	
	
	public String getCodigo() {
		return codigo;
	}



	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}



	public void setId(Long id) {
		this.id = id;
	}

	public EstadoConservacao getEstado() {
		return estado;
	}

	public void setEstado(EstadoConservacao estado) {
		this.estado = estado;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Localidades getLocal() {
		return local;
	}

	public void setLocal(Localidades local) {
		this.local = local;
	}

	public Equipamento getEquipamento() {
		return equipamento;
	}

	public void setEquipamento(Equipamento equipamento) {
		this.equipamento = equipamento;
	}
	
 

}
