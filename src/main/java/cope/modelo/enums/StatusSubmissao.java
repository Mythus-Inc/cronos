package cope.modelo.enums;

import cope.controle.ProjetoSecretariaMB;
import cope.modelo.Avaliacao;

public enum StatusSubmissao {
	AGUARDANDO_ACEITE(1, "Aguardando aceite dos participantes"), 
	AGUARDANDO_AVALIACAO(2, "Aguardando parecer do COPE"),
	PRONTO_PARA_SUBMISSAO(3, "Aguardando envio"),
	EM_AVALIACAO(4, "Projeto em Avaliação"),
	AGUARDANDO_VISTO(5,"Aguardando Visto"),
	APROVADO(5,"Aprovado"),
	FINALIZADO(7,"Finalizado"),
	APROVADORESSALVAS(6,"Aprovado Com Ressalvas"),
	EM_ANALISE(7, "Aguardando");

	private final int codigo;
	private final String descricao;

	StatusSubmissao(final int codigo, final String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	 
	public final int getCodigo() {
		return codigo;
	}

	public final String getDescricao() {
		return descricao;
	}

}
