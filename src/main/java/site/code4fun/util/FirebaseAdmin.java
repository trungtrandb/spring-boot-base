package site.code4fun.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

@Service
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FirebaseAdmin {
	
	private static FirebaseMessaging firebaseMessage;
	static {
		try {
			Resource resource = new ClassPathResource("serviceAccountKey.json");
			InputStream serviceAccount = resource.getInputStream();
			FirebaseOptions options;
			options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.build();
			FirebaseApp app = FirebaseApp.initializeApp(options);
			firebaseMessage = FirebaseMessaging.getInstance(app);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void pushNotification(String title, String content, String token) throws InterruptedException, ExecutionException {
        firebaseMessage.sendAsync(Message.builder()
                .setNotification( Notification.builder()
						.setTitle(title)
						.setBody(content)
						.build())
                .setToken(token)
                .build()).get();
    }
}
