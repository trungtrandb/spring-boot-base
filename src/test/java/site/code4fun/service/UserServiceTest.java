package site.code4fun.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import site.code4fun.model.User;
import site.code4fun.repository.UserRepository;
import site.code4fun.service.impl.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // JUnit 5
class UserServiceTest {

    @Mock
    UserRepository repository;

    @InjectMocks
    UserServiceImpl service;

    User user;

    @Test
    void whenGetAll_shouldReturnList() {
        // 1. create mock data
        List<User> expect = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            expect.add(User.builder().id((long) i).userName("trungtq").build());
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

    @Test
    void givenValidUser_whenSaveUser_thenSucceed() {
        // Given
        user = User.builder().userName("Jery").password("123456").build();
        when(repository.save(any(User.class))).then(new Answer<User>() {
            Long sequence = 1L;

            @Override
            public User answer(InvocationOnMock invocation) {
                User user = invocation.getArgument(0);
                user.setId(sequence++);
                return user;
            }
        });

        // When
        User insertedUser = service.create(user);
        // Then
        verify(repository).save(user);
        assertNotNull(insertedUser.getId());
    }
}
