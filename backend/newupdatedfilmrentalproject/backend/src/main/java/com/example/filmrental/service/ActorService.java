package com.example.filmrental.service;

import java.time.Instant;
import java.util.Collection;
import java.util.*;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.filmrental.dto.ActorDto;
import com.example.filmrental.exception.InvalidInputException;
import com.example.filmrental.exception.ResourceNotFoundException;
import com.example.filmrental.model.Actor;
import com.example.filmrental.model.Film;
import com.example.filmrental.model.FilmActor;
import com.example.filmrental.repository.ActorRepository;
import com.example.filmrental.repository.FilmActorRepository;
import com.example.filmrental.repository.FilmRepository;
 

 
@Service
public class ActorService {
 
    private final ActorRepository actorRepository;
    private final FilmActorRepository filmActorRepository;
    private final FilmRepository filmRepository;
    private final ModelMapper modelMapper;
 
    public ActorService(ActorRepository actorRepository,
                        FilmActorRepository filmActorRepository,
                        FilmRepository filmRepository,
                        ModelMapper modelMapper) {
        this.actorRepository = actorRepository;
        this.filmActorRepository = filmActorRepository;
        this.filmRepository = filmRepository;
        this.modelMapper = modelMapper;
    }
   
   
     
     
     
    	public List<ActorDto> getActorsByLastName(String lastName) throws InvalidInputException, ResourceNotFoundException {
    		if (lastName == null) {
    			throw new InvalidInputException("Last name cannot be null or empty");
    		}
    		List<Actor> actors = actorRepository.findByLastName(lastName);
    		if (actors.isEmpty()) {
    			throw new ResourceNotFoundException("No actors found with last name: " + lastName);
    		}
    		return actors.stream()
    				.map(this::convertToDTO)
    				.collect(Collectors.toList());
    	}
     
    	public List<ActorDto> getActorsByFirstName(String firstName) throws InvalidInputException, ResourceNotFoundException {
    		if (firstName == null) {
    			throw new InvalidInputException("First name cannot be null or empty");
    		}
    		List<Actor> actors = actorRepository.findByFirstName(firstName);
    		if (actors.isEmpty()) {
    			throw new ResourceNotFoundException("No actors found with first name: " + firstName);
    		}
    		return actors.stream()
    				.map(this::convertToDTO)
    				.collect(Collectors.toList());
    	}
     
    	public void updateLastName(Integer id, String lastName) throws ResourceNotFoundException, InvalidInputException {
    		if (lastName == null || lastName.isEmpty()) {
    			throw new InvalidInputException("Last name cannot be null or empty");
    		}
    		Actor actor = actorRepository.findById(id)
    				.orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + id));
    		actor.setLastName(lastName);
    		actorRepository.save(actor);
    	}
     
    	public void updateFirstName(Integer id, String firstName) throws ResourceNotFoundException, InvalidInputException {
    		if (firstName == null || firstName.isEmpty()) {
    			throw new InvalidInputException("First name cannot be null or empty");
    		}
    		Actor actor = actorRepository.findById(id)
    				.orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + id));
    		actor.setFirstName(firstName);
    		actorRepository.save(actor);
    	}
     
    	public List<ActorDto> getTopTenActorsByFilmCount() throws ResourceNotFoundException {
    		List<Actor> actors = actorRepository.findAll();
    		if (actors.isEmpty()) {
    			throw new ResourceNotFoundException("No actors found");
    		}
    		return actors.stream().limit(10)
    				.map(this::convertToDTO).collect(Collectors.toList());
    	}
     
    	public String addActor(ActorDto actorDTO) throws InvalidInputException {
    		if (actorDTO.getFirstName() == null || actorDTO.getFirstName().isEmpty() ||
    				actorDTO.getLastName() == null || actorDTO.getLastName().isEmpty()) {
    			throw new InvalidInputException("First name and Last name cannot be null or empty");
    		}
    		Actor actor = convertToEntity(actorDTO);
    		actorRepository.save(actor);
    		return "Record Created Successfully";
    	}
     
     
    	private ActorDto convertToDTO(Actor actor) {
    		return new ActorDto(actor.getId(), actor.getFirstName(), actor.getLastName());
    	}
     
    	private Actor convertToEntity(ActorDto actorDTO) {
    		Actor actor = new Actor();
    		actor.setId(actorDTO.getId());
    		actor.setFirstName(actorDTO.getFirstName());
    		actor.setLastName(actorDTO.getLastName());
    		return actor;
    	}
     
    	
    	public void assignFilmToActor(Integer actorId, Collection<Integer> filmIds) throws ResourceNotFoundException {
    		// TODO Auto-generated method stub
     
    		Actor actor = actorRepository.findById(actorId)
    				.orElseThrow(() -> new ResourceNotFoundException("Actor with ID " + actorId + " not found"));
     
     
    		for(Integer filmId : filmIds) {
    			Film film = filmRepository.findById(filmId)
    					.orElseThrow(()-> new ResourceNotFoundException("Film with Id "+filmId+" not found")) ;
     
    			if (!filmActorRepository.existsByFilmAndActor(film, actor)) {
    				FilmActor filmActor = new FilmActor();
    				filmActor.setFilm(film);
    				filmActor.setActor(actor);
    				filmActor.setLastUpdate(Instant.now());
     
    				filmActorRepository.save(filmActor);
     
    			}
     
     
     
    		}
     
     
    	}
     
    	
    	public List<Film> getFilmsByActorId(Integer id) throws ResourceNotFoundException {
    		// TODO Auto-generated method stub
     
    		List<FilmActor> filmActor_list = filmActorRepository.findAll();
     
    		List<Film> film_list = new ArrayList<>();
     
    		for(FilmActor filmActor : filmActor_list) {
    			if(filmActor.getActor().getId()==id) {
    				film_list.add(filmActor.getFilm());
     
    			}
    		}
    		
    		if(film_list.isEmpty()) {
    			throw new ResourceNotFoundException("No films found for the given actor");
    		}
    		return film_list;
    	}
    
     
     
     
     
     
     
     
     
     
     
  
//    public List<ActorDto> getActorsByLastName(String lastName)
//            throws InvalidInputException, ResourceNotFoundException {
// 
//        if (lastName == null || lastName.isBlank()) {
//            throw new InvalidInputException("Last name cannot be null or empty");
//        }
// 
//        List<Actor> actors = actorRepository.findByLastName(lastName);
//        if (actors.isEmpty()) {
//            throw new ResourceNotFoundException("No actors found with last name: " + lastName);
//        }
// 
//        return mapActorListToDto(actors);
//    }
// 
//      public List<ActorDto> getActorsByFirstName(String firstName)
//            throws InvalidInputException, ResourceNotFoundException {
// 
//        if (firstName == null || firstName.isBlank()) {
//            throw new InvalidInputException("First name cannot be null or empty");
//        }
// 
//        List<Actor> actors = actorRepository.findByFirstName(firstName);
//        if (actors.isEmpty()) {
//            throw new ResourceNotFoundException("No actors found with first name: " + firstName);
//        }
// 
//        return mapActorListToDto(actors);
//    }
// 
//   public void updateLastName(Long id, String lastName)
//            throws ResourceNotFoundException, InvalidInputException {
// 
//        if (lastName == null || lastName.isBlank()) {
//            throw new InvalidInputException("Last name cannot be null or empty");
//        }
// 
//        Actor actor = actorRepository.findById(id)
//            .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + id));
// 
//        actor.setLastName(lastName);
//        actorRepository.save(actor);
//    }
// 
//   public void updateFirstName(Long id, String firstName)
//            throws ResourceNotFoundException, InvalidInputException {
// 
//        if (firstName == null || firstName.isBlank()) {
//            throw new InvalidInputException("First name cannot be null or empty");
//        }
// 
//        Actor actor = actorRepository.findById(id)
//            .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + id));
// 
//        actor.setFirstName(firstName);
//        actorRepository.save(actor);
//    }
// 
//   public List<ActorDto> getTopTenActorsByFilmCount()
//            throws ResourceNotFoundException {
// 
//        // Assumes ActorRepository has a query ordering by SIZE(a.filmActors) or COUNT in FilmActor
//        List<Actor> actors = actorRepository.findTopActors(PageRequest.of(0, 10));
//        if (actors.isEmpty()) {
//            throw new ResourceNotFoundException("No actors found");
//        }
// 
//        return mapActorListToDto(actors);
//    }
// 
//   
//    public String addActor(ActorDto dto) throws InvalidInputException {
// 
//        if (dto == null ||
//            dto.getFirstName() == null || dto.getFirstName().isBlank() ||
//            dto.getLastName() == null || dto.getLastName().isBlank()) {
//            throw new InvalidInputException("First name and Last name cannot be null or empty");
//        }
// 
//        Actor actor = modelMapper.map(dto, Actor.class);
//        actorRepository.save(actor);
//        return "Record Created Successfully";
//    }
// 
//   public void assignFilmToActor(Long actorId, Collection<Long> filmIds)
//            throws ResourceNotFoundException, InvalidInputException {
// 
//        if (actorId == null) {
//            throw new InvalidInputException("Actor id cannot be null");
//        }
//        if (filmIds == null || filmIds.isEmpty()) {
//            throw new InvalidInputException("Film ids cannot be null or empty");
//        }
// 
//        Actor actor = actorRepository.findById(actorId)
//            .orElseThrow(() -> new ResourceNotFoundException("Actor with ID " + actorId + " not found"));
// 
//        List<Film> films = filmRepository.findAllById(filmIds);
//        if (films.isEmpty()) {
//            throw new ResourceNotFoundException("No films found for given ids");
//        }
// 
//        for (Film film : films) {
//            boolean exists = filmActorRepository.existsByFilmAndActor(film, actor);
//            if (!exists) {
//                FilmActor fa = new FilmActor();
//                fa.setFilm(film);
//                fa.setActor(actor);
//                fa.setLastUpdate(Instant.now());
//                filmActorRepository.save(fa);
//            }
//        }
//    }
//   
// 
//    
//    public List<Film> getFilmsByActorId(Long actorId)
//            throws ResourceNotFoundException {
// 
//        List<FilmActor> allLinks = filmActorRepository.findById(actorId)
// 
//        List<Film> films = new ArrayList<>();
//        for (FilmActor fa : allLinks) {
//            if (fa.getActor() != null && fa.getActor().getId() != null && fa.getActor().getId().equals(actorId)) {
//                films.add(fa.getFilm());
//            }
//        }
// 
//        if (films.isEmpty()) {
//            throw new ResourceNotFoundException("No films found for the given actor");
//        }
//        return films;
//    }
// 
//    public List<ActorDto> findAllActors() throws ResourceNotFoundException {
//        List<Actor> actors = actorRepository.findAll();
//        if (actors.isEmpty()) {
//            throw new ResourceNotFoundException("No actors found");
//        }
// 
//        return actors.stream()
//                     .map(actor -> modelMapper.map(actor, ActorDto.class))
//                     .collect(Collectors.toList());
//    }
//    private List<ActorDto> mapActorListToDto(List<Actor> actors) {
//        return modelMapper.map(actors, new TypeToken<List<ActorDto>>() {}.getType());
//        // Alternative:
//        // return actors.stream().map(a -> modelMapper.map(a, ActorDto.class)).collect(Collectors.toList());
//    }
//
//public Actor getActoById(Long id) {
//        // If PK is 'actorId'
//        return actorRepository.findById(id).orElse(null);
//
//    
//}
}

 