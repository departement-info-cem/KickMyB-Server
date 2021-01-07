package org.kickmyb.server.exos;

import org.kickmyb.server.id.BadCredentials;
import org.kickmyb.server.tasks.Existing;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Ceci est un web service pour pratiquer les étudiants sur un exemple déployé
 * Pratiquer l'envoi et la réception de
 * - Valeur simple en envoi et retour
 * - Valeur complexe et JSON / GSON
 * - Liste de valeurs complexes
 * - Valeur d'erreur selon l'objet envoyé en requête
 */

@Controller
public class WebServiceExos {

	// Simple valeur en arrivée et sortie
	@PostMapping("/exos/flottant/double")
	public @ResponseBody Float addOne(@RequestBody Float valeur) throws Existing, BadCredentials {
		return 2 * valeur;
	}

	@GetMapping("/exos/long/double/{valeur}")
	public @ResponseBody Long addOne(@PathVariable long valeur) throws Existing, BadCredentials {
		return 2 * valeur;
	}

	@GetMapping(value = "/exos/truc/complexe", produces = "application/json")
	public @ResponseBody Truc getFor(@RequestParam("name") String name) throws Existing, BadCredentials {
		System.out.println("WS SOCIAL : get a truc for " + name);
		Truc t = new Truc();
		t.b = name;
		t.a = new Random().nextInt(10);
		for (long n = 0 ; n < t.a ; n++) {  t.c.add(n);  }
		return t;
	}

	@GetMapping(value = "/exos/long/list", produces = "application/json")
	public @ResponseBody List<Long> getListeLong() {
		System.out.println("WS SOCIAL : list of trucs" );
		List<Long> res = new ArrayList<>();
		for (int i = 0 ; i < 1000 ; i++) {
			Truc t = new Truc();
			t.b = "Pipo"+i;
			t.a = new Random().nextInt(10);
			res.add(i+1L);
		}
		return res;
	}

	@GetMapping(value = "/exos/truc/list")
	public @ResponseBody List<Truc> getListe() {
		System.out.println("WS SOCIAL : list of trucs" );
		List<Truc> res = new ArrayList<>();
		for (int i = 0 ; i < 1000 ; i++) {
			Truc t = new Truc();
			t.b = "Pipo"+i;
			t.a = new Random().nextInt(10);
			res.add(t);
		}
		return res;
	}

	// TODO error based on value sent
	@PostMapping("/exos/error/or/not/")
	public @ResponseBody String htmlIndex(@RequestBody Requete req) throws TropCourt {
		if (req.nom.length() < 5) throw new TropCourt();
		return "Yeah!!!";
	}

	@GetMapping("/exos/cookie/echo")
	public @ResponseBody String echoCookie(HttpSession session) throws Existing, BadCredentials {
		return session.getId();
	}

}
