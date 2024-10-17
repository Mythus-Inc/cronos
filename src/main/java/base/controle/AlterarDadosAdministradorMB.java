package base.controle;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import util.CriptografiaSenha;
import util.ExibirMensagem;
import util.Mensagem;
import util.ValidacoesGerirUsuarios;
import ac.controle.UsuarioSessaoMB;
import base.modelo.Servidor;
import base.service.ServidorService;

@ViewScoped
@Named("alterarDadosAdministradorMB")
public class AlterarDadosAdministradorMB {

	private Servidor administrador;
	private String senha;
	private String senha2;
	
	@Inject
	private ValidacoesGerirUsuarios validacoesGerirUsuarios;

	@Inject
	private UsuarioSessaoMB usuarioSessao;
	
	@Inject
	private ServidorService servidorService;

	@PostConstruct
	public void inicializar() {
		administrador = new Servidor();
		senha = "";
		senha2 = "";
		preencherAdministrador();
	}
	public void alterarAdministrador() {
		
		System.out.println("senha sss =" +senha);
		System.out.println("senha sss 2 =" +senha2);
		
		try {
			
			Servidor s =  (Servidor) usuarioSessao.recuperarServidor();
			System.out.println("servidor buscado a senha == "+s.getSenha());
			System.out.println("senha digitada = " +senha);
			
			
			
			if ((validacoesGerirUsuarios.buscarUsuarios(administrador))
					&& (validacoesGerirUsuarios.buscarUsuarioAlterar(administrador))) {
				ExibirMensagem.exibirMensagem(Mensagem.USUARIO);
			} else {
				
				if (senha != "") {
					administrador.setSenha(senha);
					administrador.setSenha(CriptografiaSenha.criptografar(administrador.getSenha()));
				}
				
				System.out.println("senha nova que o adm digitou "+administrador.getSenha());

				servidorService.inserirAlterar(administrador);
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				preencherAdministrador();
			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			System.err.println("Erro alterarAdministrador");
			e.printStackTrace();
		}
	}
	public void preencherAdministrador() {
		try {
			administrador = (Servidor) usuarioSessao.recuperarServidor();
		
		} catch (Exception e) {
			System.err.println("preencherAdministrador");
			e.printStackTrace();
		}
	}
	public Servidor getAdministrador() {
		return administrador;
	}

	public void setAdministrador(Servidor administrador) {
		this.administrador = administrador;
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
