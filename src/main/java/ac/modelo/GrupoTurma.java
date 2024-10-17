package ac.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import base.modelo.Matriz;
import base.modelo.Turma;

/**
 * Entity implementation class for Entity: GrupoTurma
 *
 */
@Entity
@Table(name = "tab_grupo_turma")
public class GrupoTurma implements Serializable {

	public GrupoTurma() {
		super();
	}

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_grupo_turma")
	private Long id;

	@NotNull(message = "O campo mínimo de horas não pode ser nulo")
	@Column(name = "minimo_horas", nullable = false)
	private Double minimoHoras;

	@NotNull(message = "O campo máximo de horas não pode ser nulo")
	@Column(name = "maximo_horas", nullable = false)
	private Double maximoHoras;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false, name = "data_cadastro")
	private Date dataCadastro;

	@Column(nullable = false)
	private Boolean status;

	@NotNull(message = "O campo grupo não pode ser nulo")
	@JoinColumn(name = "id_grupo", nullable = false)
	@ManyToOne
	private Grupo grupo;

	@NotNull(message = "O campo turma não pode ser nulo")
	@JoinColumn(name = "id_matriz", nullable = false)
	@ManyToOne
	private Matriz matriz;

	public Long getId() {
		return id;
	}

	public Double getMinimoHoras() {
		return minimoHoras;
	}

	public void setMinimoHoras(Double minimoHoras) {
		this.minimoHoras = minimoHoras;
	}

	public Double getMaximoHoras() {
		return maximoHoras;
	}

	public void setMaximoHoras(Double maximoHoras) {
		this.maximoHoras = maximoHoras;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public Matriz getMatriz() {
		return matriz;
	}

	public void setMatriz(Matriz matriz) {
		this.matriz = matriz;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
 

}
