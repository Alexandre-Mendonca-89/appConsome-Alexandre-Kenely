package consome.appConsome.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import consome.appConsome.model.AlunoModel;

@Controller
@RequestMapping("/aluno")
public class AlunoController {

	private static final String API_URL = "http://localhost:8081/aluno";

	@GetMapping("/cadastro")
	public ModelAndView cadastroDeAluno() {
		ModelAndView modelAndView = new ModelAndView("aluno/cadastrarAluno");
		AlunoModel aluno = new AlunoModel();
		modelAndView.addObject("aluno", aluno);
		return modelAndView;
	}

	@PostMapping("/insere")
	public String cadastroAluno(AlunoModel aluno) {
		try {
			URL url = new URL(API_URL + "/cadastro");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);

			String jsonInputString = "{\"nome\":\"" + aluno.getNome() + "\", \"idade\":" + aluno.getIdade()
					+ ", \"sexo\":\"" + aluno.getSexo() + "\", \"curso\":\"" + aluno.getCurso() + "\"}";

			try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
				outputStream.writeBytes(jsonInputString);
				outputStream.flush();
			}

			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_CREATED) {
				System.out.println("Aluno cadastrado com sucesso!");
			} else {
				System.out.println("Falha ao cadastrar aluno. Código de resposta: " + responseCode);
			}

			connection.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "redirect:/";
	}
	
	
	@GetMapping("/listaTodos")
	public ModelAndView listarAlunos() {
	    ModelAndView modelAndView = new ModelAndView("aluno/listarAlunos");
	    List<AlunoModel> alunos = obterAlunosDaAPI();
	    modelAndView.addObject("alunos", alunos);
	    return modelAndView;
	}

	private List<AlunoModel> obterAlunosDaAPI() {
	    List<AlunoModel> alunos = new ArrayList<>();

	    try {
	        URL url = new URL(API_URL + "/listaTodos");
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("GET");
	        connection.setRequestProperty("Accept", "application/json");

	        int responseCode = connection.getResponseCode();
	        if (responseCode == HttpURLConnection.HTTP_OK) {
	            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	            String inputLine;
	            StringBuilder response = new StringBuilder();
	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }
	            in.close();

	            ObjectMapper objectMapper = new ObjectMapper();
	            alunos = objectMapper.readValue(response.toString(), TypeFactory.defaultInstance().constructCollectionType(List.class, AlunoModel.class));
	        } else {
	            System.out.println("Falha ao obter a lista de alunos. Código de resposta: " + responseCode);
	        }

	        connection.disconnect();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return alunos;
	}
}