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
@Table(name = "tab_equipamentoinventario") 
public class EquipamentoInventario implements Serializable {

	 
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	 
	@Temporal(TemporalType.DATE)
	private Date dataConferencia;
	
	
	@Temporal(TemporalType.DATE)
	private Date dataRemovido;
	    
    private boolean status; 
	private String estado;
    private String trocarEtiqueta;
    private String dataConferenciaFormatada;
    private boolean pertenceLocal;
    private boolean naoTombado;
    private int quantidade;
    private boolean bensEmDuplicidade;
    private String observacoes;
		
    @ManyToOne
	private LocalInventario localInventario;
	
	@ManyToOne
	private Tombamento tombamento;
    
    public int getQuantidade() {
		return quantidade;
	}
	public Date getDataRemovido() {
		return dataRemovido;
	}
	public void setDataRemovido(Date dataRemovido) {
		this.dataRemovido = dataRemovido;
	}
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	public Long getId() {
		return id;
	}
	public boolean isBensEmDuplicidade() {
		return bensEmDuplicidade;
	}
	public void setBensEmDuplicidade(boolean bensEmDuplicidade) {
		this.bensEmDuplicidade = bensEmDuplicidade;
	}
	public boolean isNaoTombado() {
		return naoTombado;
	}
	public String getObservacoes() {
		return observacoes;
	}
	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}
	public void setNaoTombado(boolean naoTombado) {
		this.naoTombado = naoTombado;
	}
	public String getDataConferenciaFormatada() {
		return dataConferenciaFormatada;
	}
	public void setDataConferenciaFormatada(String dataConferenciaFormatada) {
		this.dataConferenciaFormatada = dataConferenciaFormatada;
	}
	public Date getDataConferencia() {
		return dataConferencia;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public void setDataConferencia(Date dataConferencia) {
		this.dataConferencia = dataConferencia;
	}
	public String getTrocarEtiqueta() {
		return trocarEtiqueta;
	}
	public void setTrocarEtiqueta(String trocarEtiqueta) {
		this.trocarEtiqueta = trocarEtiqueta;
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
	public boolean isPertenceLocal() {
		return pertenceLocal;
	}
	public void setPertenceLocal(boolean pertenceLocal) {
		this.pertenceLocal = pertenceLocal;
	}
	public LocalInventario getLocalInventario() {
		return localInventario;
	}
	public void setLocalInventario(LocalInventario localInventario) {
		this.localInventario = localInventario;
	}
	public Tombamento getTombamento() {
		return tombamento;
	}
	public void setTombamento(Tombamento tombamento) {
		this.tombamento = tombamento;
	}
	 
}
