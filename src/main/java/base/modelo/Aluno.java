package base.modelo;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import ac.modelo.Pessoa;
import cope.modelo.enums.Parecer;

/**
 * Entity implementation class for Entity: Aluno
 *
 */
@Entity
@Table(name = "tab_aluno")
@PrimaryKeyJoinColumn(name = "id_pessoa")
public class Aluno extends Pessoa implements Serializable {

	public Aluno() {
		super();
	}

	private static final long serialVersionUID = 1L;

	private String situacao;
	// 1 para sim 2 para n�o

	private Double totalHorasCadastradasG1;
	private Double totalHorasComputadasG1;

	private Double totalHorasComputadasG2;
	private Double totalHorasCadastradasG2;

	private Double totalHorasComputadasG3;
	private Double totalHorasCadastradasG3;

	private String nomeResponsavel;

	private String telefoneResponsavel;
 
	
	// private boolean liberado;
	
	// modificado dia 13/12/2024
	private String caminhoImagem;
	
	// modificado dia 23/11/2024
	@Column(name = "status_carteirinha")
	private  Parecer statusCarteirinha;
	private Integer qtdRespostas;

	@Column(name = "permite_cadastro_certificado")
	private Integer permiteCadastroCertificado=1;
	
	
	public String getCaminhoImagem() {
		return caminhoImagem;
	}

	public void setCaminhoImagem(String caminhoImagem) {
		this.caminhoImagem = caminhoImagem;
	}
	
	public Parecer getStatusCarteirinha() {
		return statusCarteirinha;
	}

	public void setStatusCarteirinha(Parecer statusCarteirinha) {
		this.statusCarteirinha = statusCarteirinha;
	}

	public Integer getQtdRespostas() {
		return qtdRespostas;
	}

	public Double getTotalHorasCadastradasG1() {
		return totalHorasCadastradasG1;
	}

	public void setTotalHorasCadastradasG1(Double totalHorasCadastradasG1) {
		this.totalHorasCadastradasG1 = totalHorasCadastradasG1;
	}

	public Double getTotalHorasComputadasG1() {
		return totalHorasComputadasG1;
	}

	public void setTotalHorasComputadasG1(Double totalHorasComputadasG1) {
		this.totalHorasComputadasG1 = totalHorasComputadasG1;
	}

	public Double getTotalHorasComputadasG2() {
		return total/HorasComputadasG2;
	}

	public void setTotalHorasComputadasG2(Double totalHorasComputadasG2) {
		this.totalHorasComputadasG2 = totalHorasComputadasG2;
	}

	public Double getTotalHorasCadastradasG2() {
		return totalHorasCadastradasG2;
	}

	public void setTotalHorasCadastradasG2(Double totalHorasCadastradasG2) {
		this.totalHorasCadastradasG2 = totalHorasCadastradasG2;
	}

	public Double getTotalHorasComputadasG3() {
		return totalHorasComputadasG3;
	}

	public void setTotalHorasComputadasG3(Double totalHorasComputadasG3) {
		this.totalHorasComputadasG3 = totalHorasComputadasG3;
	}

	public Double getTotalHorasCadastradasG3() {
		return totalHorasCadastradasG3;
	}

	public void setTotalHorasCadastradasG3(Double totalHorasCadastradasG3) {
		this.totalHorasCadastradasG3 = totalHorasCadastradasG3;
	}

	public void setQtdRespostas(Integer qtdRespostas) {
		this.qtdRespostas = qtdRespostas;
	}

	public String getNomeResponsavel() {
		return nomeResponsavel;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}

	public String getTelefoneResponsavel() {
		return telefoneResponsavel;
	}

	public void setTelefoneResponsavel(String telefoneResponsavel) {
		this.telefoneResponsavel = telefoneResponsavel;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Integer getPermiteCadastroCertificado() {
		return permiteCadastroCertificado;
	}

	public void setPermiteCadastroCertificado(Integer permiteCadastroCertificado) {
		this.permiteCadastroCertificado = permiteCadastroCertificado;
	}

	@Override
	public int hashCode() {
		return Objects.hash(nomeResponsavel, permiteCadastroCertificado, qtdRespostas, situacao, telefoneResponsavel,
				totalHorasCadastradasG1, totalHorasCadastradasG2, totalHorasCadastradasG3, totalHorasComputadasG1,
				totalHorasComputadasG2, totalHorasComputadasG3);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Aluno other = (Aluno) obj;
		return Objects.equals(nomeResponsavel, other.nomeResponsavel)
				&& Objects.equals(permiteCadastroCertificado, other.permiteCadastroCertificado)
				&& Objects.equals(qtdRespostas, other.qtdRespostas) && Objects.equals(situacao, other.situacao)
				&& Objects.equals(telefoneResponsavel, other.telefoneResponsavel)
				&& Objects.equals(totalHorasCadastradasG1, other.totalHorasCadastradasG1)
				&& Objects.equals(totalHorasCadastradasG2, other.totalHorasCadastradasG2)
				&& Objects.equals(totalHorasCadastradasG3, other.totalHorasCadastradasG3)
				&& Objects.equals(totalHorasComputadasG1, other.totalHorasComputadasG1)
				&& Objects.equals(totalHorasComputadasG2, other.totalHorasComputadasG2)
				&& Objects.equals(totalHorasComputadasG3, other.totalHorasComputadasG3);
	}
	
	
}
