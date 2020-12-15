package esiea.ds.sondage.controller;

import java.util.*;

import javax.validation.Valid;

import esiea.ds.sondage.message.request.SondageForm;
import esiea.ds.sondage.message.request.SignUpForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import esiea.ds.sondage.message.request.LoginForm;
import esiea.ds.sondage.message.request.SignUpForm;
import esiea.ds.sondage.message.response.JwtResponse;
import esiea.ds.sondage.message.response.ResponseMessage;
import esiea.ds.sondage.model.Role;
import esiea.ds.sondage.model.RoleName;
import esiea.ds.sondage.model.User;
import esiea.ds.sondage.repository.RoleRepository;
import esiea.ds.sondage.repository.UserRepository;
import esiea.ds.sondage.security.jwt.JwtProvider;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthRestAPIs {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
    UserRepository userRepository;

	@Autowired
    RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
    JwtProvider jwtProvider;

	@RequestMapping(value="/find")
	public List<User> listUsers() {
		return userRepository.findAll();
	}

	@RequestMapping(value="/delete/{id}",method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ADMIN')")
	public void delete(@PathVariable("id") Long id) {
		userRepository.deleteById(id);
	}

	@RequestMapping(value="/update/{id}",method = RequestMethod.PUT)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Object> updateUser(@RequestBody SignUpForm signUpRequest, @PathVariable long id) {
		System.out.println("*****ROLES: "+signUpRequest.getRole());
		Optional<User> userOptional = userRepository.findById(id);

		if (!userOptional.isPresent())
			return ResponseEntity.notFound().build();

		User user = new User(signUpRequest.getName(),signUpRequest.getUsername(),
				signUpRequest.getEmail(),signUpRequest.getPassword());

		user.setId(id);

		Set<Role> roles = new HashSet<>();
		signUpRequest.getRole().forEach(role -> {
			switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN).orElse(null);
					roles.add(adminRole);

					break;
				default:
					Role recRole = roleRepository.findByName(RoleName.ROLE_USER).orElse(null);
					roles.add(recRole);
			}
		});

		user.setRoles(roles);
		System.out.println("*****User: "+ user);
		userRepository.save(user);

		return ResponseEntity.noContent().build();
	}


	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtProvider.generateJwtToken(authentication);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return new ResponseEntity<>(new ResponseMessage("Fail -> Username is already taken!"),
					HttpStatus.BAD_REQUEST);
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return new ResponseEntity<>(new ResponseMessage("Fail -> Email is already in use!"),
					HttpStatus.BAD_REQUEST);
		}

		// Creating user's account
		User user = new User(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		strRoles.forEach(role -> {
			switch (role) {
			case "admin":
				Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
						.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
				roles.add(adminRole);

				break;
			default:
				Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
						.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
				roles.add(userRole);
			}
		});

		user.setRoles(roles);
		userRepository.save(user);

		return new ResponseEntity<>(new ResponseMessage("User registered successfully!"), HttpStatus.OK);
	}

	@PostMapping("/update/{id}")
	public ResponseEntity<?> updateUser(@Valid @RequestBody SignUpForm signUpRequest,@PathVariable("id") Long id) {

		// Creating user's account
		User user = userRepository.findById(id).orElse(null);
		user.setUsername(signUpRequest.getUsername());
		user.setName(signUpRequest.getName());
		user.setPassword(encoder.encode(signUpRequest.getPassword()));

		userRepository.save(user);

		return new ResponseEntity<>(new ResponseMessage("User updated successfully!"), HttpStatus.OK);
	}

	@RequestMapping(value="/profile/{username}", method = RequestMethod.GET)
	public User getUser(@PathVariable("username") String un) {
		return userRepository.findByUsername(un).orElse(null);
	}
}
