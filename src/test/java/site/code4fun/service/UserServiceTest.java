package site.code4fun.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import site.code4fun.entity.User;
import site.code4fun.repository.UserRepository;
import site.code4fun.service.impl.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository repository;

    @InjectMocks
    UserServiceImpl service;

    @Test
    void whenGetAll_shouldReturnList() {
        // 1. create mock data
        List<User> expect = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            expect.add(User.builder().id((long) i).username("trungtq").build());
        }

        // 2. define behavior of Repository
        when(repository.findAll()).thenReturn(expect);

        // 3. call service method
        List<User> actual = service.getAll();

        // 4. assert the result
        assertThat(actual).hasSameSizeAs(expect);

        // 4.1 ensure repository is called
        verify(repository).findAll();
    }
}
