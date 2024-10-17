package questionario.controle;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.PieChartModel;

import ac.controle.UsuarioSessaoMB;
import ac.modelo.Pessoa;
import ac.services.AlunoService;
import base.modelo.Aluno;
import base.modelo.Curso;
import dao.GenericDAO;
import questionario.modelo.Email;
import questionario.modelo.Formulario;
import questionario.service.EmailService;
import questionario.service.FormularioService;
import util.EnviarEmail;
import util.ExibirMensagem;
import util.Mensagem;

@ViewScoped
@Named("RespostasMB")
public class RespostasMB implements Serializable {

	private static final long serialVersionUID = 1L;


	private List<Formulario> listFormulario;
	private Formulario formulario;


	@Inject
	private GenericDAO<Formulario> daoFormulario;

	
	@PostConstruct
	public void inicializar() {
	
		formulario = new Formulario();
		listFormulario = new ArrayList<>();
		preencherEmail();
	}

	public void preencherEmail() {
		listFormulario = daoFormulario.listaComStatus(Formulario.class);
	}
	
	public void carregarFormulario(Formulario formulario){
		this.formulario = formulario;
		
		System.out.println("formuilaro "+this.formulario.getIadc2());
		
	}

	public List<Formulario> getListFormulario() {
		return listFormulario;
	}

	public void setListFormulario(List<Formulario> listFormulario) {
		this.listFormulario = listFormulario;
	}

	public Formulario getFormulario() {
		return formulario;
	}

	public void setFormulario(Formulario formulario) {
		this.formulario = formulario;
	}
	
	
	
	
	

}
