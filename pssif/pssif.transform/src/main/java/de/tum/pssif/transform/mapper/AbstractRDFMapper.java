package de.tum.pssif.transform.mapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;

import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.transform.IoMapper;
import de.tum.pssif.transform.Mapper;
import de.tum.pssif.transform.ModelMapper;
import de.tum.pssif.transform.io.GraphRDFIOMapper;
import de.tum.pssif.transform.io.RDFGraphIOMapper;
import de.tum.pssif.transform.io.URIs;

public class AbstractRDFMapper implements Mapper {
	String lang;
	
	public final Model read(Metamodel metamodel, InputStream inputStream) {

		com.hp.hpl.jena.rdf.model.Model results = ModelFactory
				.createDefaultModel();
		
		try{
		
		InputStream is_base = new FileInputStream(System.getProperty("user.dir") 
				+ File.separator + "rdfmapper" + File.separator + "sfb768_base.ttl");
		results.read(new BufferedReader(new InputStreamReader(is_base)), 
				"http://www.sfb768.tum.de/voc/base/ns#", "TURTLE");
		
		InputStream is_pssif = new FileInputStream(System.getProperty("user.dir") 
				+ File.separator + "rdfmapper" + File.separator + "sfb768_pssif.ttl");
		results.read(new BufferedReader(new InputStreamReader(is_pssif)), 
				"http://www.sfb768.tum.de/voc/base/ns#", "TURTLE");

		results.setNsPrefix("graph",  URIs.namespace);

		results.read(inputStream,	null, lang);
		 
		InputStream is_rules = new FileInputStream(System.getProperty("user.dir") + 
				File.separator + "rdfmapper" + File.separator + "rules"+ File.separator + "input.rules");
		@SuppressWarnings("rawtypes")
		List rules = Rule
				.parseRules(Rule.rulesParserFromReader(new BufferedReader(new InputStreamReader(is_rules))));
		
		//@SuppressWarnings("rawtypes")
		/*
		List rules = Rule
				.parseRules(Rule.rulesParserFromReader(new BufferedReader(
						new InputStreamReader(
								RDFTTLMapper.class
										.getResourceAsStream("/rdfmapper/rules/input.rules")))));
										*/
		@SuppressWarnings("unchecked")
		GenericRuleReasoner  reasoner = new GenericRuleReasoner(rules);
		reasoner.setTransitiveClosureCaching(true);
		InfModel inf = ModelFactory.createInfModel(reasoner, results);
		RDFGraphIOMapper mapper = new RDFGraphIOMapper(inf, metamodel);
		inf.write(System.out);
		return mapper.getPssifModel();
		
		}catch(Exception e){
			//System.out.println(e.toString());
			//e.printStackTrace();
			System.out.println(e.getMessage());
			return null;}
	}

	
	public final void write(Metamodel metamodel, Model model,
			OutputStream outputStream) {
		GraphRDFIOMapper mapper = new GraphRDFIOMapper(model, metamodel);
		com.hp.hpl.jena.rdf.model.Model results = mapper.rdfmodel;
		results.setNsPrefix("graph",  URIs.baseUri);

		results.write(outputStream, lang,null);
	}

	protected Metamodel getView(Metamodel metamodel) {
		return null;
	}

	protected ModelMapper getModelMapper() {
		return null;
	}

	protected IoMapper getIoMapper() {
		return null;
	}
}
