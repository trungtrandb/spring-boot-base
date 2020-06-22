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
	FirebaseMessaging firebaseMessage;
	
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
	
	public String pushNotification(String token) throws FirebaseMessagingException {
        String response = null;
//        AndroidConfig androidConfig = getAndroidConfig("");
//        ApnsConfig apnsConfig = getApnsConfig("");
        try {
			response = firebaseMessage.sendAsync(Message.builder()
//					.setApnsConfig(apnsConfig).setAndroidConfig(androidConfig)
	                .setNotification( new Notification("Hello", "Hello from Spring boot"))
	                .setToken(token)
	                .build()).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
        return response;
    }
	
//	private AndroidConfig getAndroidConfig(String topic) {
//        return AndroidConfig.builder()
//                .setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(topic)
//                .setPriority(AndroidConfig.Priority.HIGH)
//                .setNotification(AndroidNotification.builder().setSound("default")
//                        .setColor("#FFFF00").setTag(topic).build()).build();
//    }
//
//    private ApnsConfig getApnsConfig(String topic) {
//        return ApnsConfig.builder()
//                .setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build();
//    }
}
