package com.example.RideShare.controller;

import com.example.RideShare.controller.dto.DeleteUserConfirmationCredentials;
import com.example.RideShare.controller.dto.InformationSafeUserDto;
import com.example.RideShare.controller.dto.UserInformationChangeRequest;
import com.example.RideShare.controller.exceptions.EmailAlreadyTakenException;
import com.example.RideShare.controller.exceptions.UserInformationChangeException;
import com.example.RideShare.controller.exceptions.UserNotFoundException;
import com.example.RideShare.model.entity.User;
import com.example.RideShare.model.repository.TripRepository;
import com.example.RideShare.model.repository.UserRepository;
import com.example.RideShare.model.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserRepository repository;

    @Autowired
    private final TripRepository tripRepository;

    @Autowired
    private final VehicleRepository vehicleRepository;

    public UserController(UserRepository repository, TripRepository tripRepository, VehicleRepository vehicleRepository) {
        this.repository = repository;
        this.tripRepository = tripRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @GetMapping
    List<InformationSafeUserDto> retrieveAllUsers(){
        return repository.findAll()
                .stream()
                .map(user -> new InformationSafeUserDto(
                        user.getEmail(),
                        user.getPhoneNumber(),
                        user.getFirstName(),
                        user.getLastName()
                )).collect(Collectors.toList());
    }

    @GetMapping("/{email}")
    InformationSafeUserDto getByEmail(@PathVariable("email") String email){
        User user = repository.findById(email)
                .orElseThrow(
                        ()-> new UserNotFoundException(email)
                );
        return new InformationSafeUserDto(
                user.getEmail(),
                user.getPhoneNumber(),
                user.getFirstName(),
                user.getLastName()
        );
    }

    @PostMapping
    InformationSafeUserDto createUser(@RequestBody User newUser) throws EmailAlreadyTakenException {
        String email = newUser.getEmail();

        //possibly validate email later

        if (repository.existsById(email)){
            throw new EmailAlreadyTakenException(email);
        }

        User user = repository.save(newUser);

        return new InformationSafeUserDto(
                user.getEmail(),
                user.getPhoneNumber(),
                user.getFirstName(),
                user.getLastName()
        );
    }

    @PutMapping("/{email}")
    @PreAuthorize("(#email == authentication.principal) && " +
            "(#userInformationChangeRequest.email == authentication.principal)")
    InformationSafeUserDto updateUser(@RequestBody UserInformationChangeRequest userInformationChangeRequest,
                                      @PathVariable("email") String email){
        User userAfterUpdate = repository.findById(email)
                .map(user -> {
                    //validate user has entered their current password correctly
                    if (!user.getPassword().equals(userInformationChangeRequest.getCurrentPassword()))
                        throw new UserInformationChangeException(email);

                    user.setPassword(userInformationChangeRequest.getNewPassword());
                    user.setPhoneNumber(userInformationChangeRequest.getPhoneNumber());
                    user.setAddress(userInformationChangeRequest.getAddress());
                    user.setPostalCode(userInformationChangeRequest.getPostalCode());
                    user.setFirstName(userInformationChangeRequest.getFirstName());
                    user.setLastName(userInformationChangeRequest.getLastName());
                    return repository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException(email));

        return new InformationSafeUserDto(
                userAfterUpdate.getEmail(),
                userAfterUpdate.getPhoneNumber(),
                userInformationChangeRequest.getFirstName(),
                userInformationChangeRequest.getLastName()
        );
    }


    @DeleteMapping("/{email}")
    @Transactional
    @PreAuthorize("#email == authentication.principal")
    public void deleteUser(@RequestBody DeleteUserConfirmationCredentials deleteUserConfirmationCredentials,
                           @PathVariable("email") String email){
        //Find the user account to delete
        User userToDelete = repository.findById(email)
                .orElseThrow(()-> new UserNotFoundException(email));

        //Validate authority to write changes one more time
        if (!userToDelete.getPassword().equals(deleteUserConfirmationCredentials.getPassword()))
            throw new UserInformationChangeException(email);

        //delete all trips where the user is the driver
        tripRepository.deleteTripsByDriver(email);

        //delete all vehicles owned by this user
        vehicleRepository.deleteOwnerVehicles(email);

        //remove all instances where the user is a passenger for a trip
        repository.deleteById(email);
    }

//    @GetMapping("/search/{searchString}")
//    List<User> searchByName(@PathVariable("searchString") String searchString){
//        return repository.searchByName(searchString);
//    }
}
