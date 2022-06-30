package site.code4fun.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import site.code4fun.constant.Status;
import site.code4fun.model.Category;
import site.code4fun.model.Privilege;
import site.code4fun.model.Role;
import site.code4fun.model.User;
import site.code4fun.repository.CategoryRepository;
import site.code4fun.repository.PrivilegeRepository;
import site.code4fun.repository.RoleRepository;
import site.code4fun.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


/*
 * Generate demo data
 */
@Component
public class DemoData {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PrivilegeRepository privilegeRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    @Autowired
    public DemoData(RoleRepository roleRepository,
                    UserRepository userRepository,
                    PrivilegeRepository privilegeRepository,
                    PasswordEncoder passwordEncoder, CategoryRepository categoryRepository){
        this.privilegeRepository = privilegeRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.categoryRepository = categoryRepository;
    }

    @PostConstruct
    private void postConstruct(){
        if ("create".equalsIgnoreCase(ddlAuto) || "create-drop".equalsIgnoreCase(ddlAuto)){
            List<Privilege> privileges = createPrivilege();
            List<Role> roles = createRole(privileges);
            createCategory();
            createUser(roles);
        }
    }

    private List<Role> createRole(List<Privilege> privileges){
        Role r = Role.builder().name("ROLE_ADMIN").privileges(privileges).build();
        Role r1 = Role.builder().name("ROLE_USER").build();

        privileges.forEach(privilege ->{
            if ("READ".equalsIgnoreCase(privilege.getName()))
                r1.setPrivileges(Collections.singletonList(privilege));
        });

        List<Role> lst = Arrays.asList(r, r1);
        return roleRepository.saveAllAndFlush(lst);
    }

    private List<Category> createCategory(){
        Category r = Category.builder().name("Category Name 1").status(Status.ACTIVE).build();
        Category r1 = Category.builder().name("Category Name 2").status(Status.DRAFT).build();

        List<Category> lst = Arrays.asList(r, r1);
        return categoryRepository.saveAllAndFlush(lst);
    }

    private List<Privilege> createPrivilege(){
        Privilege r = Privilege.builder().name("READ").build();
        Privilege r1 = Privilege.builder().name("WRITE").build();
        Privilege r2 = Privilege.builder().name("DELETE").build();
        List<Privilege> lst = Arrays.asList(r, r1, r2);
        return privilegeRepository.saveAllAndFlush(lst);
    }

    private void createUser(List<Role> roles){
        User r = User.builder()
                .username("trungtrandb@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .status(Status.ACTIVE)
                .fullName("TrungTQ")
                .roles(new HashSet<>(roles))
                .email("trungtrandb@gmail.com").build();

        List<User> lst = Collections.singletonList(r);
        userRepository.saveAllAndFlush(lst);
    }

}
