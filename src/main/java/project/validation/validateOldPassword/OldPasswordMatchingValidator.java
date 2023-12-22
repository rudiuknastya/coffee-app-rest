package project.validation.validateOldPassword;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.entity.User;
import project.repository.UserRepository;

public class OldPasswordMatchingValidator implements ConstraintValidator<OldPasswordMatching,Object> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public OldPasswordMatchingValidator(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private String id;
    private String oldPassword;
    @Override
    public boolean isValid(Object s, ConstraintValidatorContext constraintValidatorContext) {
        Object old = new BeanWrapperImpl(s).getPropertyValue(oldPassword);
        if(old == null || old.equals("")){
            return true;
        }
        Object idValue = new BeanWrapperImpl(s).getPropertyValue(id);

        User user = userRepository.findById((Long)idValue).orElseThrow(()-> new EntityNotFoundException("User was not found by id "+idValue));
        return passwordEncoder.matches(old.toString(),user.getPassword());
    }

    @Override
    public void initialize(OldPasswordMatching matching) {
        this.id = matching.id();
        this.oldPassword = matching.oldPassword();
    }
}
