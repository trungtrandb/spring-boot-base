package site.code4fun.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.code4fun.constant.AppConstants;
import site.code4fun.dto.AccessTokenResponseDTO;
import site.code4fun.dto.RoleDTO;
import site.code4fun.dto.request.SignInRequest;
import site.code4fun.model.Privilege;
import site.code4fun.model.Role;
import site.code4fun.model.User;
import site.code4fun.service.PrivilegeService;
import site.code4fun.service.RoleService;
import site.code4fun.util.JwtTokenUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final RoleService roleService;
    private final PrivilegeService privilegeService;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenUtil jwtTokenUtil,
                          RoleService roleService,
                          ModelMapper modelMapper,
                          PrivilegeService privilegeService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.privilegeService = privilegeService;
    }

    @Operation(summary = "Common login method use email and password, if want set token to header see @usingResponseEntityBuilderAndHttpHeaders")
    @PostMapping(value = "/login")
    public ResponseEntity<AccessTokenResponseDTO> createAuthenticationToken(@RequestBody SignInRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User userPrincipal = (User) authentication.getPrincipal();
        return ResponseEntity.ok(jwtTokenUtil.generateToken(userPrincipal));
    }

    @GetMapping(value = "/login-with-header")
    public ResponseEntity<AccessTokenResponseDTO> usingResponseEntityBuilderAndHttpHeaders(@RequestBody SignInRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User userPrincipal = (User) authentication.getPrincipal();
        AccessTokenResponseDTO responseDTO = jwtTokenUtil.generateToken(userPrincipal);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HttpHeaders.SET_COOKIE, AppConstants.TOKEN_COOKIE_NAME + "=" + responseDTO.getToken() + "; Path=/; Secure; HttpOnly; SameSite=Strict");

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(responseDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/roles")
    @ResponseStatus(HttpStatus.CREATED)
    public RoleDTO createRole(@RequestBody RoleDTO roleDTO) {
        Role role = convertToEntity(roleDTO);
        return convertToDto(roleService.create(role));
    }

    @DeleteMapping("/roles/{id}")
    public void deleteRole(@PathVariable long id) {
        roleService.delete(id);
    }

    @GetMapping("/privileges")
    public List<Privilege> getPrivileges() {
        return privilegeService.getAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/roles")
    public Page<Role> getAllRolePaging(@RequestParam(required = false, defaultValue = "0") int page,
                                       @RequestParam(required = false, defaultValue = "0") int size,
                                       @RequestParam(required = false) String sort,
                                       @RequestParam(required = false) String sortDir,
                                       @RequestParam(required = false, defaultValue = "") String q_like) {
        return roleService.getPaging(page, size, sortDir, sort, q_like);
    }

    @PostMapping(value = "/upload-image")
    public ResponseEntity<?> saveFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        ServletContext servletContext = request.getSession().getServletContext();
        String rootPath = servletContext.getRealPath("");
        String imagePath = "/resources/image/" + file.getOriginalFilename();
        Path path = Paths.get(rootPath + imagePath);

        try {
            Files.write(path, file.getBytes(), StandardOpenOption.CREATE);
            return ResponseEntity.created(new URI(imagePath)).build();
        } catch (IOException | URISyntaxException e) {
           e.printStackTrace();
           return ResponseEntity.internalServerError().build();
        }
    }

    private RoleDTO convertToDto(Role source) {
        RoleDTO des = modelMapper.map(source, RoleDTO.class);
        if (isNotEmpty(source.getPrivileges())){
            des.setPrivilegeIds(source.getPrivileges().stream().map(Privilege::getId).collect(Collectors.toList()));
        }
        return des;
    }

    private Role convertToEntity(RoleDTO postDto) {
        Role r = modelMapper.map(postDto, Role.class);
        if (isNotEmpty(postDto.getPrivilegeIds())){
            Collection<Privilege> privileges = postDto.getPrivilegeIds().stream().map(Privilege::new).collect(Collectors.toCollection(HashSet::new));
            r.setPrivileges(privileges);
        }
        return r;
    }
}
