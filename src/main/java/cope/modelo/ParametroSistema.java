package cope.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tab_parametrosistema")
public class ParametroSistema implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_parametrosistema")
	private Integer id;
	@Column(name = "chave")
	private String chave;
	@Column(name = "valor")
	private String valor;
	@Column(name = "descricao")
	private String descricao;

	public ParametroSistema() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getChave() {
		String retorno = null;

		if (chave != null)
			retorno = chave.toUpperCase().trim();
		return retorno;
	}

	public void setChave(String chave) {
		if (chave != null) {
			this.chave = chave.toUpperCase().trim();
		} else
			this.chave = null;

	}

	public String getValor() {
		String retorno = null;

		if (valor != null)
			retorno = valor.trim();

		return retorno;
	}

	public void setValor(String valor) {
		if (valor != null) {
			this.valor = valor.trim();
		} else
			this.valor = null;

	}

	public String getDescricao() {
		String retorno = null;

		if (descricao != null)
			retorno = descricao.toUpperCase().trim();
		return retorno;
	}

	public void setDescricao(String descricao) {
		if (descricao != null) {
			this.descricao = descricao.toUpperCase().trim();
		} else
			this.descricao = null;

	}

}
