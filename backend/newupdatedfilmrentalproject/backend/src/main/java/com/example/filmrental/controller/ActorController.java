package com.example.filmrental.controller;
 
import java.util.Collection;
import java.util.List;
import java.util.Map;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.filmrental.dto.ActorDto;
import com.example.filmrental.exception.InvalidInputException;
import com.example.filmrental.exception.ResourceNotFoundException;
import com.example.filmrental.model.Actor;
import com.example.filmrental.model.Film;
import com.example.filmrental.service.ActorService;

import jakarta.validation.Valid;
 

@RestController

	@RequestMapping("/api/actors")

	public class ActorController {

		@Autowired

		private ActorService actorService;
	 
	 
		@GetMapping("/lastname/{ln}")

		public ResponseEntity<List<ActorDto>> getActorsByLastName(@PathVariable String ln) throws InvalidInputException, ResourceNotFoundException {

			List<ActorDto> actors = actorService.getActorsByLastName(ln);

			return new ResponseEntity<>(actors, HttpStatus.OK);

		}
	 
		@GetMapping("/firstname/{fn}")

		public ResponseEntity<List<ActorDto>> getActorsByFirstName(@PathVariable String fn) throws InvalidInputException, ResourceNotFoundException {

			List<ActorDto> actors = actorService.getActorsByFirstName(fn);

			return new ResponseEntity<>(actors, HttpStatus.OK);

		}
	 
//		@PutMapping("/update/lastname/{id}")
//
//		public ResponseEntity<Void> updateLastName(@PathVariable Long id, @RequestBody @Valid Map<String, String> requestBody) throws ResourceNotFoundException, InvalidInputException {
//
//			String lastName = requestBody.get("lastName");
//
//			actorService.updateLastName(id, lastName);
//
//			return new ResponseEntity<>(HttpStatus.OK);
//
//		}
		@PutMapping(value="/update/lastname/{id}" ,consumes = "text/plain")
		public ResponseEntity<Void> updateLastName(
		        @PathVariable Integer id,
		        @RequestBody String lastName
		) throws ResourceNotFoundException, InvalidInputException {

		    if (lastName == null || lastName.trim().isEmpty()) {
		        throw new InvalidInputException("lastName must not be null or empty");
		    }

		    actorService.updateLastName(id, lastName.trim());
		    return ResponseEntity.ok().build();
		}
	 
//		@PutMapping("/update/firstname/{id}")
//
//		public ResponseEntity<Void> updateFirstName(@PathVariable Long id, @RequestBody @Valid Map<String, String> requestBody) throws ResourceNotFoundException, InvalidInputException {
//
//			String firstName = requestBody.get("firstName");
//
//			actorService.updateFirstName(id, firstName);
//
//			return new ResponseEntity<>(HttpStatus.OK);
//
//		}


@PutMapping(value="/update/firstname/{id}" ,consumes ="text/plain" )
public ResponseEntity<Void> updateFirstName(
        @PathVariable Integer id,
        @RequestBody String firstName
) throws ResourceNotFoundException, InvalidInputException {

    if (firstName == null || firstName.trim().isEmpty()) {
        throw new InvalidInputException("firstName must not be null or empty");
    }

    actorService.updateFirstName(id, firstName.trim());
    return ResponseEntity.ok().build();
}

	 
		@GetMapping("/toptenbyfilmcount")

		public ResponseEntity<List<ActorDto>> getTopTenActorsByFilmCount() throws ResourceNotFoundException {

			List<ActorDto> actors = actorService.getTopTenActorsByFilmCount();

			return new ResponseEntity<>(actors, HttpStatus.OK);

		}
	 
		@PostMapping("/post")

		public ResponseEntity<String> addActor(@RequestBody ActorDto actorDTO) throws InvalidInputException {

			String response = actorService.addActor(actorDTO);

			return new ResponseEntity<>(response, HttpStatus.CREATED);

		}
	 
		@PutMapping("/{actorId}/film")

		public ResponseEntity<String> assignFilmToActor(@PathVariable Integer actorId, @RequestBody Collection<Integer> filmIds) throws ResourceNotFoundException {

			try {

				actorService.assignFilmToActor(actorId,filmIds);

				return ResponseEntity.ok("Films assigned to Actor successfully");

			} catch (ResourceNotFoundException e) {

				return ResponseEntity.badRequest().body(e.getMessage());

			}

		}


		@GetMapping("/{id}/films")
	 
	    public ResponseEntity<List<Film>> getFilmsByActorId(@PathVariable Integer id) throws ResourceNotFoundException {
	 
	        List<Film> films = actorService.getFilmsByActorId(id); // Adjust service method accordingly
	 
	        return ResponseEntity.ok(films);
	 
	    }

	 
	 
	 
	}



 
 
 