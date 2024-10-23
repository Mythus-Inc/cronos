package ac.controle;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
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
@Named("alterarDadosProfessorMB")
public class AlterarDadosProfessorMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Servidor professor;
	
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
		professor = new Servidor();
	
		senha = "";
		senha2 = "";
		preencherProfessor();
	}

	public void alterarProfessor() {
		try {
			if ((validacoesGerirUsuarios.buscarUsuarios(professor))
					&& (validacoesGerirUsuarios.buscarUsuarioAlterar(professor))) {
				ExibirMensagem.exibirMensagem(Mensagem.USUARIO);
			} else {
				if (senha != "") {
					professor.setSenha(senha);
					professor.setSenha(CriptografiaSenha.criptografar(professor.getSenha()));
				}
				servidorService.inserirAlterar(professor);
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				preencherProfessor();
			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			System.err.println("Erro alterarProfessor");
			e.printStackTrace();
		}
	}

	public void preencherProfessor() {
		try {
			professor = (Servidor) usuarioSessao.recuperarProfessor();
		} catch (Exception e) {
			System.err.println("Erro preencherProfessor");
			e.printStackTrace();
		}
	}



	public Servidor getProfessor() {
		return professor;
	}

	public void setProfessor(Servidor professor) {
		this.professor = professor;
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
