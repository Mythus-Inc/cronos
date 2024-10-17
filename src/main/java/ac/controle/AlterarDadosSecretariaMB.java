package ac.controle;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import util.CriptografiaSenha;
import util.ExibirMensagem;
import util.Mensagem;
import util.ValidacoesGerirUsuarios;

import base.modelo.Servidor;
import base.service.ServidorService;

@ViewScoped
@Named("alterarDadosSecretariaMB")
public class AlterarDadosSecretariaMB implements Serializable{

	
	private static final long serialVersionUID = 1L;

	private Servidor secretaria;

	private String senha;
	private String senha2;

	@Inject
	private ServidorService servidorService;
	
	@Inject
	private ValidacoesGerirUsuarios validacoesGerirUsuarios;
	
	@Inject
	private UsuarioSessaoMB usuarioSessao;
	
	@PostConstruct
	public void inicializar() {
		secretaria = new Servidor();

		senha = "";
		senha2 = "";
		preencherSecretaria();
	}

	public void alterarSecretaria() {
		
		try {
			if ((validacoesGerirUsuarios.buscarUsuarios(secretaria))
					&& (validacoesGerirUsuarios.buscarUsuarioAlterar(secretaria))) {
				ExibirMensagem.exibirMensagem(Mensagem.USUARIO);
			} else {
				if (senha != "") {
					secretaria.setSenha(senha);
					secretaria.setSenha(CriptografiaSenha.criptografar(secretaria.getSenha()));
				}
				servidorService.inserirAlterar(secretaria);
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				preencherSecretaria();

			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			System.err.println("Erro alterarSecretaria");
			e.printStackTrace();
		}
	}

	public void preencherSecretaria() {
		secretaria = (Servidor) usuarioSessao.recuperarSecretaria();
	}



	public Servidor getSecretaria() {
		return secretaria;
	}

	public void setSecretaria(Servidor secretaria) {
		this.secretaria = secretaria;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getSenha2() {
		return senha2;
	}

	public void setSenha2(String senha2) {
		this.senha2 = senha2;
	}
}
