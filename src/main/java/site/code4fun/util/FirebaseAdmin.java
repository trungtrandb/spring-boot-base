package site.code4fun.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

@Service
public class FirebaseAdmin {
	
	private static FirebaseMessaging firebaseMessage;
	
	public FirebaseAdmin() {
		try {
			Resource resource = new ClassPathResource("serviceAccountKey.json");
			InputStream serviceAccount = resource.getInputStream();
			FirebaseOptions options;
			options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.build();
			FirebaseApp app = FirebaseApp.initializeApp(options);
			firebaseMessage = FirebaseMessaging.getInstance(app);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String pushNotification(String title, String content, String token) throws FirebaseMessagingException, InterruptedException, ExecutionException {
        return firebaseMessage.sendAsync(Message.builder()
                .setNotification( new Notification(title, content))
                .setToken(token)
                .build()).get();
    }
	
	
}
