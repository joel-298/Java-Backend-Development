package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController; 


@RestController
public class Car {
    
    @Autowired 
    private Dog dog ; 

    @GetMapping("/ok")
    public String ok() {
        return dog.fun() ; 
    }
}

// @ REST CONTROLLER :
//                     - Also Creates beans like @Component ! 
//                     - Specialized Version of @Component : Will discuss it later ! 

// @ GET MAPPING : 
//                 - Helps in creating API Receiving routes 


// @ AUTOWIRED   : DEPENDENCY INJECTION : Whenever we are required to use another class
// in line number 12 : 
// We are asking for an object of the DOG class from spring, and it was only possible because Spring contained its object by using @Component ! 
// ADVANTAGE : for example we have 100's of class using Dogs's object so therefore 100 objects of Dogs will be made  , 
//             Therefore : We will use Autowired which will prevent the creating 100 of objects , And therefore IOC will put Dog class as a component in its bag and will use the object from there !


