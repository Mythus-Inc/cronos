package ac.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import util.CriptografiaSenha;
import util.EnviarEmail;
import util.GeradorSenhas;
import ac.modelo.Pessoa;
import ac.services.PessoaService;
import dao.GenericDAO;

@ViewScoped
@Named("recuperSenhaLoginMB")
public class RecuperSenhaLoginMB implements Serializable{

	private static final long serialVersionUID = 1L;

	private String email;
	
	private List<Pessoa> lista;
	private String mensagem;
	
	@Inject
	private GenericDAO<Pessoa> daoPessoa;
	
	@Inject
	private PessoaService pessoaService;
	
	@PostConstruct
	public void inicializa() {
		lista = new ArrayList<>();
		mensagem = "";
		email = "";
	}
	public Boolean buscarEmail() {
		lista = daoPessoa.listaComStatus(Pessoa.class);
		return lista.stream().anyMatch(p -> p.getUsuario().equals(email));
	} 

	public void recuperarSenhaLogin() {
		String senha;
		String novaSenha;
		if (buscarEmail()) {
			senha = GeradorSenhas.gerarSenha();
			novaSenha = senha.charAt(0)+""+senha.charAt(1)+""+senha.charAt(3)+""+senha.charAt(5)+""+senha.charAt(6);
			pessoaService.updateSenha(CriptografiaSenha.criptografar(novaSenha.toLowerCase()), email);
			
			mensagem=novaSenha.toLowerCase();
			int enviarEmail=0;
					
			enviarEmail=1;
			
			if (enviarEmail==0 && EnviarEmail.enviarEmail(email, "Recuperação de Senha - Cronos",
					"Olá, sua nova senha do Cronos é: "+novaSenha.toLowerCase())) {
				mensagem = "E-mail enviado";
			} else {
				mensagem += "Erro ao enviar e-mail..";
			}
		} else {
			mensagem = "E-mail não encontrado";
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
}
