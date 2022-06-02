package site.code4fun.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class) // JUnit 4
public class UserRepositoryMockitoJUnitRunnerTest {

    @Mock
    UserRepository mockRepository;

    @Test
    public void givenCountMethodMocked_WhenCountInvoked_ThenMockValueReturned() {
        when(mockRepository.count()).thenReturn(123L);

        long userCount = mockRepository.count();

//        List<User> expect = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            expect.add(User.builder().id((long) i).userName("trungtq").build());
//        }
//
//        // 2. define behavior of Repository
//        Mockito.when(mockRepository.findAll()).thenReturn(expect);

        assertEquals(123L, userCount);
        verify(mockRepository).count();
    }

    @Test
    public void givenCountMethodMocked_WhenCountInvoked_ThenMockedValueReturned() {
        UserRepository localMockRepository = mock(UserRepository.class);
        when(localMockRepository.count()).thenReturn(111L);

        long userCount = localMockRepository.count();

        assertEquals(111L, userCount);
        verify(localMockRepository).count();
    }
}


