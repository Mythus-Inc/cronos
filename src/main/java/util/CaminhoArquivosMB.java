package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named("caminhoArquivosMB")
@RequestScoped
public class CaminhoArquivosMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private StreamedContent file;
	private String nome = "";

	public void setFile(StreamedContent file) {
		this.file = file;
	}
	
	
	public void informarNome(String nome) {
		System.out.println("AQUI NO "+nome);
		this.nome = nome;
	}

	public StreamedContent getFile() throws FileNotFoundException {
		nome="20211125220646479zNUFpY4qYRZUhJwKicnzmo";
		if (FacesContext.getCurrentInstance().getRenderResponse()) {
			return new DefaultStreamedContent();
		} else {
			if (!nome.equals("")) {
				System.out.println(nome);
				File diretorio = new File(CaminhoArquivos.caminhoCertificados());
				String caminhoWebInf = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/");
				// Paths.get(diretorio.getAbsolutePath() +"/" +nome + ".pdf").get
				InputStream stream = new FileInputStream(diretorio.getAbsolutePath() + "/" + nome + ".pdf"); // Caminho
																										// arquivo.
				file = new DefaultStreamedContent(stream, "application/pdf", nome+".pdf");

				return file;
			} else {
				return new DefaultStreamedContent();
			}
		}
	}

	public String getCertificados(String nome) {
		// System.out.println("aqui " + nome);
		if (nome != null && nome.length() > 0) {
			File diretorio = new File(CaminhoArquivos.caminhoCertificados());
			byte[] inFileBytes = null;
			try {
				inFileBytes = Files.readAllBytes(Paths.get(diretorio.getAbsolutePath() + "/" + nome + ".pdf"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// System.out.println("ELSE");
			byte[] encoded = java.util.Base64.getEncoder().encode(inFileBytes);
			return new String(encoded);
		} else {
			// System.out.println("NULO ");
			return null;
		}
	}

//	private String caminhoUmaPastaCertificado = "../certificadosteste/";
	private String caminhoUmaPastaCertificado = "../certificados/";

//	private String caminhoDuasPastaCertificado = " ../../certificadosteste/";
	private String caminhoDuasPastaCertificado = " ../../certificados/";

//	private String caminhoTresPastaCertificado = "../../../certificadosteste/";
	private String caminhoTresPastaCertificado = "../../../certificados/";

	// private String caminhoUmaPastaCertificado = "../protocolosteste/";
	private String caminhoUmaPastaProtocolo = "../protocolosteste/";
	private String caminhoDuasPastaProtocolo = " ../../protocolosteste/";
	private String caminhoTresPastaProtocolo = "../../../protocolosteste/";

	public String getCaminhoUmaPastaCertificado() {
		return caminhoUmaPastaCertificado;
	}

	public void setCaminhoUmaPastaCertificado(String caminhoUmaPastaCertificado) {
		this.caminhoUmaPastaCertificado = caminhoUmaPastaCertificado;
	}

	public String getCaminhoDuasPastaCertificado() {
		return caminhoDuasPastaCertificado;
	}

	public void setCaminhoDuasPastaCertificado(String caminhoDuasPastaCertificado) {
		this.caminhoDuasPastaCertificado = caminhoDuasPastaCertificado;
	}

	public String getCaminhoTresPastaCertificado() {
		return caminhoTresPastaCertificado;
	}

	public void setCaminhoTresPastaCertificado(String caminhoTresPastaCertificado) {
		this.caminhoTresPastaCertificado = caminhoTresPastaCertificado;
	}

	public String getCaminhoUmaPastaProtocolo() {
		return caminhoUmaPastaProtocolo;
	}

	public void setCaminhoUmaPastaProtocolo(String caminhoUmaPastaProtocolo) {
		this.caminhoUmaPastaProtocolo = caminhoUmaPastaProtocolo;
	}

	public String getCaminhoDuasPastaProtocolo() {
		return caminhoDuasPastaProtocolo;
	}

	public void setCaminhoDuasPastaProtocolo(String caminhoDuasPastaProtocolo) {
		this.caminhoDuasPastaProtocolo = caminhoDuasPastaProtocolo;
	}

	public String getCaminhoTresPastaProtocolo() {
		return caminhoTresPastaProtocolo;
	}

	public void setCaminhoTresPastaProtocolo(String caminhoTresPastaProtocolo) {
		this.caminhoTresPastaProtocolo = caminhoTresPastaProtocolo;
	}

}
