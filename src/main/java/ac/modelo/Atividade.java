package ac.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Entity implementation class for Entity: Atividade
 *
 */
@Entity
@Table(name = "tab_atividade")
public class Atividade implements Serializable {

	public Atividade() {
		super();
	}

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_atividade")
	private Long id;

	@NotNull(message = "O campo descrição não pode ser nulo")
	@Column(nullable = false)
	private String descricao;

	@Column(nullable = false)
	private Boolean status;

	@NotNull(message = "O campo grupo não pode ser nulo")
	@JoinColumn(name = "id_grupo", nullable = false)
	@ManyToOne
	private Grupo grupo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false, name = "data_cadastro")
	private Date dataCadastro;

	public Long getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

}
