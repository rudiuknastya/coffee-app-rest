package project.validation.validateOldPassword;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import project.entity.User;
import project.repository.UserRepository;

public class OldPasswordMatchingValidator implements ConstraintValidator<OldPasswordMatching,Object> {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public OldPasswordMatchingValidator(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    private String id;
    private String oldPassword;
    @Override
    public boolean isValid(Object s, ConstraintValidatorContext constraintValidatorContext) {
        Object old = new BeanWrapperImpl(s).getPropertyValue(oldPassword);
        if(old == null){
            return true;
        }
        Object idValue = new BeanWrapperImpl(s).getPropertyValue(id);

        User user = userRepository.findById((Long)idValue).orElseThrow(EntityNotFoundException::new);
        return bCryptPasswordEncoder.matches(old.toString(),user.getPassword());
    }

    @Override
    public void initialize(OldPasswordMatching matching) {
        this.id = matching.id();
        this.oldPassword = matching.oldPassword();
    }
}
